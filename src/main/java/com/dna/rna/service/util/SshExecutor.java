package com.dna.rna.service.util;

import com.dna.rna.domain.ServerResource;
import com.dna.rna.domain.group.Group;
import com.dna.rna.domain.groupUser.GroupUser;
import com.dna.rna.domain.instance.Instance;
import com.dna.rna.domain.server.Server;
import com.dna.rna.domain.serverPort.ServerPort;
import com.dna.rna.domain.user.User;
import com.dna.rna.dto.InstanceCreationDto;
import com.jcraft.jsch.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@Component
public class SshExecutor {

    private static final Logger logger = LoggerFactory.getLogger(SshExecutor.class);

    @Value("${ssh.id}")
    private String sshId;

    @Value("${ssh.pwd}")
    private String sshPwd;

    @Value("${wan.ip}")
    private String wanIP;

    private String dcloudImage = "dguailab/ailab_base:2.1";

    private String buildGpuAllocOptionValue(List<Integer> gpus) {
        StringBuilder result = new StringBuilder();
        result.append("--gpus ");
        result.append("'\"device=");
        // 쉼표가 잘 못 찍히는 것을 막기 위한 조건
        for (int i = 0; i < gpus.size(); i++) {
            int curr = gpus.get(i);
            if (i != 0) {
              result.append(',');
            }
            result.append(curr);
        }

        result.append("\"'");
        return result.toString();
    }

    public String deleteInstance(Server server, Instance instance) throws JSchException, IOException {
        JSch jsch = new JSch();
        Session session = jsch.getSession(sshId, wanIP, server.getSshPort());
        session.setPassword(sshPwd);
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect();  //연결

        Channel channel = session.openChannel("exec");
        ChannelExec channelExec = (ChannelExec) channel;
        channelExec.setPty(true);
        String command = "sudo docker rm -f " + instance.getInstanceHash();
        channelExec.setCommand(command);
        System.out.println(command);
        logger.info("[{}] 서버에서 인스턴스 [{}] 삭제 명령 발행", server.getInternalIP(), instance.getInstanceHash());
        StringBuilder outputBuffer = new StringBuilder();
        InputStream in = channel.getInputStream();
        ((ChannelExec) channel).setErrStream(System.err);

        channel.connect();  //실행

        byte[] tmp = new byte[1024];
        while (true) {
            while (in.available() > 0) {
                int i = in.read(tmp, 0, 1024);
                outputBuffer.append(new String(tmp, 0, i));
                if (i < 0) break;
            }

            if (channel.isClosed()) {
                System.out.println(outputBuffer.toString());
                String status = outputBuffer.toString();
                if (channel.getExitStatus() != 0) {
                    logger.error("[{}] 서버의 인스턴스 [{}]를 삭제하는데 실패하였습니다 : existStatus = [{}], output = [{}]",
                            server.getInternalIP(), instance.getInstanceName(),
                            channel.getExitStatus(), outputBuffer.toString());
                }
                channel.disconnect();
                session.disconnect();
                return status;
            }
        }
    }

    public String fetchStatusOfInstance(Server server, String instanceContainerID) throws Exception {
        JSch jsch = new JSch();
        Session session = jsch.getSession(sshId, wanIP, server.getSshPort());
        session.setPassword(sshPwd);
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect();  //연결

        Channel channel = session.openChannel("exec");
        ChannelExec channelExec = (ChannelExec) channel;
        channelExec.setPty(true);
        String command = "sudo docker ps -af " +
                "'name="+ instanceContainerID +"' "+
                "--format \"table {{.Status}}\" | tail -n 1";
        channelExec.setCommand(command);
        System.out.println(command);
        //콜백을 받을 준비.
        StringBuilder outputBuffer = new StringBuilder();
        InputStream in = channel.getInputStream();
        ((ChannelExec) channel).setErrStream(System.err);

        channel.connect();  //실행

        byte[] tmp = new byte[1024];
        while (true) {
            while (in.available() > 0) {
                int i = in.read(tmp, 0, 1024);
                outputBuffer.append(new String(tmp, 0, i));
                if (i < 0) break;
            }

            if (channel.isClosed()) {
                System.out.println("결과");
                System.out.println(outputBuffer.toString());
                String status = outputBuffer.toString();
                System.out.println("에러 = " + channel.getExitStatus());
                channel.disconnect();
                session.disconnect();

                return status;
            }
        }
    }

    private String generatedDockerRunPortOption(List<ServerPort> selectedPortList) {
        StringBuilder result = new StringBuilder();
        for (ServerPort serverPort : selectedPortList) {
            result.append("-p ");
            result.append(serverPort.getFrom());
            result.append(':');
            result.append(serverPort.getTo());
            result.append(' ');
        }
        System.out.println(result.toString());
        return result.toString();
    }

    private String storageOptionBuilder(String storageServerDirectoryPath, String containerDirectoryPath) {
        return "--mount type=bind,source=\"" + storageServerDirectoryPath +
                "\",target=" + containerDirectoryPath + " ";
    }

    private String mappingGlobalStorage(Server server, String sudoerId) {
        return storageOptionBuilder(server.getSharedDirectoryPath() +
                                    "/global-share",
                                    "/home/irteam/dcloud-global-dir");
    }

    private String mappingGroupStorage(User user, String sudoerId) {
        StringBuilder result = new StringBuilder();
        for (int i=0; i < user.getGroupUserList().size(); i++) {
            GroupUser groupUser = user.getGroupUserList().get(i);
            Group group = groupUser.getGroup();
            result.append(storageOptionBuilder(group.getGroupShareDirName(),
                          "/home/irteam/"+group.getGroupName()+ "-dcloud-dir"));
        }
        return result.toString();
    }

    private String mappingUserStorage(Server server, User user, String sudoerId) {
        return storageOptionBuilder(server.getSharedDirectoryPath() +
                                    "/user-share/" +
                                    user.getLoginId(),
                                    "/home/"+sudoerId+"/"+user.getLoginId() + "-dcloud-dir");
    }

    private String mappingStoragesOption(Server selectedServer, User user, String sudoerId) {
        return mappingGlobalStorage(selectedServer, sudoerId) +
               mappingGroupStorage(user, sudoerId) +
               mappingUserStorage(selectedServer, user, sudoerId);

    }

    private SshResult<String> createNewUserShareDir(Server selectedServer, User user) throws JSchException, IOException {
        JSch jsch = new JSch();
        Session session = jsch.getSession(sshId, wanIP, selectedServer.getSshPort());
        session.setPassword(sshPwd);
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect();

        Channel channel = session.openChannel("exec");
        ChannelExec channelExec = (ChannelExec) channel;

        String command = "sudo mkdir " + selectedServer.getSharedDirectoryPath() +"/user-share/"+ user.getLoginId();
        channelExec.setCommand(command);
        System.out.println(command);

        //콜백을 받을 준비.
        StringBuilder outputBuffer = new StringBuilder();
        InputStream in = channel.getInputStream();
        ((ChannelExec) channel).setErrStream(System.err);

        channel.connect();  //실행

        byte[] tmp = new byte[1024];
        while (true) {
            while (in.available() > 0) {
                int i = in.read(tmp, 0, 1024);
                outputBuffer.append(new String(tmp, 0, i));
                if (i < 0) break;
            }
            if (channel.isClosed()) {
                logger.info("[{}] 서버에 커맨드 실행 : cmd = [{}], output = [{}]",
                        selectedServer.getInternalIP(), command, outputBuffer.toString());
                channel.disconnect();
                session.disconnect();

                SshResultError error = null;
                int exitStatus = channel.getExitStatus();
                logger.info("[{}] 서버에 인스턴스가 사용할 유저 전용 디렉터리 생성 결과 : exitStatus = [{}], result = [{}]",
                        selectedServer.getInternalIP(), exitStatus, outputBuffer.toString());
                // exitStatus 1은 디렉터리가 이미 있을 경우인데, 에러가 아닌것으로 봐도 무방
                if (exitStatus != 0 && exitStatus != 1) {
                    error = new SshResultError(outputBuffer.toString(), exitStatus);
                }

                return new SshResult<>(error, outputBuffer.toString());
            }
        }
    }

    public SshResult<InstanceCreationDto> createNewInstance(Server selectedServer, User user, List<ServerPort> selectedPortList,
                                                            List<Integer> gpus, String containerId, String sudoerId) throws Exception {
        logger.info("[{}] 인스턴스용 컨테이너 생성 시작...", containerId);
        JSch jsch = new JSch();
        Session session = jsch.getSession(sshId, wanIP, selectedServer.getSshPort());
        session.setPassword(sshPwd);
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect();

        Channel channel = session.openChannel("exec");
        ChannelExec channelExec = (ChannelExec) channel;
        channelExec.setPty(true);
        SshResult<String> createShareDirResult = createNewUserShareDir(selectedServer, user);
        if (createShareDirResult.getError() != null) {
            return new SshResult<>(createShareDirResult.getError(), null);
        }

        String command =
                "sudo docker run -d " +
                buildGpuAllocOptionValue(gpus) + " " +
                generatedDockerRunPortOption(selectedPortList) +
                "-it --runtime=nvidia " +
                "--cap-add=SYS_ADMIN " +
                "--ipc=host " +
                mappingStoragesOption(selectedServer, user, sudoerId) +
                "--name " + containerId + " " + dcloudImage;
        channelExec.setCommand(command);
        logger.info("[{}] 컨테이너 생성 명령어 실행 : cmd = [{}]", containerId, command);

        StringBuilder outputBuffer = new StringBuilder();
        InputStream in = channel.getInputStream();
        ((ChannelExec) channel).setErrStream(System.err);

        channel.connect();

        byte[] tmp = new byte[65536];
        while (true) {
            while (in.available() > 0) {
                int i = in.read(tmp, 0, 65536);
                outputBuffer.append(new String(tmp, 0, i));
                if (i < 0) break;
            }
            if (channel.isClosed()) {
                System.out.println(outputBuffer.toString());
                channel.disconnect();
                String lineSeparator = System.lineSeparator();
                String containerHash = outputBuffer.toString().replaceAll(lineSeparator, "");
                SshResultError error = null;
                int exitStatus = channel.getExitStatus();
                logger.info("[{}] Docker container 생성결과 :\n cmd = [{}], exitStatus = [{}], result = [{}]",
                        containerId, command, exitStatus, outputBuffer.toString());
                if (exitStatus != 0) {
                    logger.error("[{}] Docker container 생성실패 :\n cmd = [{}], exitStatus = [{}], result = [{}]",
                            containerId, command, exitStatus, outputBuffer.toString());
                    error = new SshResultError(
                            String.format("[%s] 실행한 명령어 :\n [%s], output = [%S]", containerId, command, outputBuffer.toString()),
                            exitStatus);
                }
                channel.disconnect();
                session.disconnect();
                return new SshResult<>(
                        error,
                        new InstanceCreationDto(containerId, containerHash));
            }
        }
    }

    public SshResult<String> copyFileToInstance(int serverHostSshPort, String instanceContainerId, String filePath) throws JSchException, IOException {
        JSch jsch = new JSch();
        Session session = jsch.getSession(sshId, wanIP, serverHostSshPort);
        session.setPassword(sshPwd);
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect();  //연결

        Channel channel = session.openChannel("exec");
        ChannelExec channelExec = (ChannelExec) channel;
        channelExec.setPty(true);
        // 콜론 주의
        String command = "sudo docker cp "+ filePath +" "+ instanceContainerId +":/";
        channelExec.setCommand(command);
        System.out.println(command);

        StringBuilder outputBuffer = new StringBuilder();
        InputStream in = channel.getInputStream();
        ((ChannelExec) channel).setErrStream(System.err);

        channel.connect();

        byte[] tmp = new byte[1024];
        while (true) {
            while (in.available() > 0) {
                int i = in.read(tmp, 0, 1024);
                outputBuffer.append(new String(tmp, 0, i));
                if (i < 0) break;
            }
            if (channel.isClosed()) {
                System.out.println("결과");
                System.out.println(outputBuffer.toString());
                System.out.println("에러 = " + channel.getExitStatus());
                channel.disconnect();
                session.disconnect();

                int exitStatus = channel.getExitStatus();
                SshResultError error = null;
                if (exitStatus != 0) error = new SshResultError(outputBuffer.toString(), exitStatus);
                return new SshResult<>(error, outputBuffer.toString());
            }
        }
    }

    public SshResult<String> copyInitShellScriptToInstance(int serverHostSshPort, String instanceContainerHash) throws JSchException, IOException {
        // 콜론주의(명령어 입력때 파일명에 콜론 들어가면 제대로 인식이 안 되어서 뺌
        return copyFileToInstance(serverHostSshPort, instanceContainerHash, "~/dcloud/images/"+dcloudImage+"/init.sh");
    }

    public SshResult<String> copyRemoteAccessScriptToInstance(int serverHostSshPort, String instanceContainerHash) throws JSchException, IOException {
        // 콜론주의(명령어 입력때 파일명에 콜론 들어가면 제대로 인식이 안 되어서 뺌
        String dcloudImage = "dcloud1.0";
        return copyFileToInstance(serverHostSshPort, instanceContainerHash, "~/dcloud/images/"+dcloudImage+"/remote_access.sh");
    }

    public SshResult<String> executeRemoteAccessScript(int serverHostSshPort, String instanceContainerId, String sudoerId,
                                                 String sudoerPwd) throws JSchException, IOException {
        String generatedInstanceID = UUID.randomUUID().toString();
        JSch jsch = new JSch();
        Session session = jsch.getSession(sshId, wanIP, serverHostSshPort);
        session.setPassword(sshPwd);
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect();  //연결

        Channel channel = session.openChannel("exec");
        ChannelExec channelExec = (ChannelExec) channel;
        channelExec.setPty(true);
        String dcloudImage = "dcloud:1.0";
        String command = "sudo docker exec -it "+instanceContainerId+" bash /remote-access.sh " + sudoerId + " " + sudoerPwd;
        channelExec.setCommand(command);
        System.out.println(command);

        StringBuilder outputBuffer = new StringBuilder();
        InputStream in = channel.getInputStream();
        ((ChannelExec) channel).setErrStream(System.err);

        channel.connect();

        byte[] tmp = new byte[1024];
        while (true) {
            while (in.available() > 0) {
                int i = in.read(tmp, 0, 1024);
                outputBuffer.append(new String(tmp, 0, i));
                if (i < 0) break;
            }
            if (channel.isClosed()) {
                System.out.println("결과");
                System.out.println(outputBuffer.toString());
                System.out.println("에러 = " + channel.getExitStatus());
                channel.disconnect();

                int exitStatus = channel.getExitStatus();
                SshResultError error = null;
                if (exitStatus != 0) error = new SshResultError(outputBuffer.toString(), exitStatus);
                return new SshResult<>(error, outputBuffer.toString());
            }
        }
    }

    public SshResult<String> executeInstanceInit(int serverHostSshPort, String instanceContainerId, String sudoerId,
                                                 String sudoerPwd) throws JSchException, IOException {
        String generatedInstanceID = UUID.randomUUID().toString();
        JSch jsch = new JSch();
        Session session = jsch.getSession(sshId, wanIP, serverHostSshPort);
        session.setPassword(sshPwd);
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect();

        Channel channel = session.openChannel("exec");
        ChannelExec channelExec = (ChannelExec) channel;
        channelExec.setPty(true);
        String dcloudImage = "dcloud:1.0";
        String command = "sudo docker exec -it "+instanceContainerId+" bash /init.sh " + sudoerId + " " + sudoerPwd;
        channelExec.setCommand(command);
        System.out.println(command);

        StringBuilder outputBuffer = new StringBuilder();
        InputStream in = channel.getInputStream();
        ((ChannelExec) channel).setErrStream(System.err);

        channel.connect();

        byte[] tmp = new byte[65536];
        while (true) {
            while (in.available() > 0) {
                int i = in.read(tmp, 0, 65536);
                outputBuffer.append(new String(tmp, 0, i));
                if (i < 0) break;
            }
            if (channel.isClosed()) {
                System.out.println("결과");
                System.out.println(outputBuffer.toString());
                System.out.println("에러 = " + channel.getExitStatus());
                channel.disconnect();
                session.disconnect();
                int exitStatus = channel.getExitStatus();
                SshResultError error = null;
                if (exitStatus != 0) error = new SshResultError(outputBuffer.toString(), exitStatus);
                return new SshResult<>(error, outputBuffer.toString());
            }
        }
    }

    public SshResult<String> startInstance(String containerHash, int sshPort) throws JSchException, IOException {
        JSch jsch = new JSch();
        Session session = jsch.getSession(sshId, wanIP, sshPort);
        session.setPassword(sshPwd);
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect();

        Channel channel = session.openChannel("exec");
        ChannelExec channelExec = (ChannelExec) channel;
        channelExec.setPty(true);
        String command = "sudo docker start " + containerHash;
        channelExec.setCommand(command);
        System.out.println(command);

        StringBuilder outputBuffer = new StringBuilder();
        InputStream in = channel.getInputStream();
        ((ChannelExec) channel).setErrStream(System.err);

        channel.connect();

        byte[] tmp = new byte[1024];
        while (true) {
            while (in.available() > 0) {
                int i = in.read(tmp, 0, 1024);
                outputBuffer.append(new String(tmp, 0, i));
                if (i < 0) break;
            }
            if (channel.isClosed()) {
                System.out.println(outputBuffer.toString());
                logger.info("startInstance exit status:" + channel.getExitStatus());
                channel.disconnect();
                session.disconnect();
                String lineSeparator = System.lineSeparator();
                String result = outputBuffer.toString().replaceAll(lineSeparator, "");
                return new SshResult<>(null, result);
            }
        }
    }

    // ssh 랑 xrdp 를 재시작 해주는 스크립트
    public String restartRemoteAccessServices(String containerHash, int sshPort) throws JSchException, IOException {
        JSch jsch = new JSch();
        Session session = jsch.getSession(sshId, wanIP, sshPort);
        session.setPassword(sshPwd);
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect();

        Channel channel = session.openChannel("exec");
        ChannelExec channelExec = (ChannelExec) channel;
        channelExec.setPty(true);
        String command = "sudo docker exec -it " + containerHash + " " + "/remote_access.sh";
        channelExec.setCommand(command);
        System.out.println(command);

        StringBuilder outputBuffer = new StringBuilder();
        InputStream in = channel.getInputStream();
        ((ChannelExec) channel).setErrStream(System.err);

        channel.connect();

        byte[] tmp = new byte[1024];
        while (true) {
            while (in.available() > 0) {
                int i = in.read(tmp, 0, 1024);
                outputBuffer.append(new String(tmp, 0, i));
                if (i < 0) break;
            }
            if (channel.isClosed()) {
                System.out.println("결과");
                System.out.println(outputBuffer.toString());
                System.out.println("에러 = " + channel.getExitStatus());
                channel.disconnect();
                String lineSeparator = System.lineSeparator();
                String result = outputBuffer.toString().replaceAll(lineSeparator, "");

                return result;
            }
        }
    }
}
