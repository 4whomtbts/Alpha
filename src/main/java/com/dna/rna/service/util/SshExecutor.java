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

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

public class SshExecutor {

    private String buildGpuAllocOptionValue(ServerResource serverResource) {
        List<Integer> gpus = serverResource.getGpus();
        StringBuilder result = new StringBuilder();
        result.append("--gpus ");
        result.append("'\"device=");
        // 쉼표가 잘 못 찍히는 것을 막기 위한 조건
        int acc = 0;
        for (int i = 0; i < gpus.size(); i++) {
            int curr = gpus.get(i);
            if (curr == -1 || curr == 1) {
                if (i != 0 && acc != 0) {
                    result.append(',');
                }
                result.append(i);
                acc++;
            }
        }
        result.append("\"'");
        return result.toString();
    }

    public static String deleteInstance(Server server, Instance instance) throws JSchException, IOException {
        JSch jsch = new JSch();
        Session session = jsch.getSession("4whomtbts", server.getInternalIP(), server.getSshPort());
        session.setPassword("Hndp^(%#9!Q");
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect();  //연결

        Channel channel = session.openChannel("exec");  //채널접속
        ChannelExec channelExec = (ChannelExec) channel; //명령 전송 채널사용
        channelExec.setPty(true);
        String command = "sudo docker rm -f " + instance.getInstanceHash();
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
                System.out.println(outputBuffer.toString());
                String status = outputBuffer.toString();
                //int colonIndex = status.indexOf(':');
                //int crIndex = status.indexOf('\r');
                //status = status.substring(colonIndex+2, crIndex-1);
                System.out.println("에러 = " + channel.getExitStatus());
                channel.disconnect();
                return status;
            }
        }
    }

    public static String deleteContainer(Server server, String containerId) throws JSchException, IOException {
        JSch jsch = new JSch();
        Session session = jsch.getSession("4whomtbts", server.getInternalIP(), server.getSshPort());
        session.setPassword("Hndp^(%#9!Q");
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect();  //연결

        Channel channel = session.openChannel("exec");  //채널접속
        ChannelExec channelExec = (ChannelExec) channel; //명령 전송 채널사용
        channelExec.setPty(true);
        String command = "sudo docker rm -f " + containerId;
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
                System.out.println(outputBuffer.toString());
                String status = outputBuffer.toString();
                //int colonIndex = status.indexOf(':');
                //int crIndex = status.indexOf('\r');
                //status = status.substring(colonIndex+2, crIndex-1);
                System.out.println("에러 = " + channel.getExitStatus());
                channel.disconnect();
                return status;
            }
        }
    }

    public static String fetchStatusOfInstance(Server server, String instanceContainerID) throws Exception {
        JSch jsch = new JSch();
        Session session = jsch.getSession("4whomtbts", server.getInternalIP(), server.getSshPort());
        session.setPassword("Hndp^(%#9!Q");
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
                                    "/home/"+sudoerId+"/dcloud-global-dir");
    }

    private String mappingGroupStorage(User user, String sudoerId) {
        StringBuilder result = new StringBuilder();
        for (int i=0; i < user.getGroupUserList().size(); i++) {
            GroupUser groupUser = user.getGroupUserList().get(i);
            Group group = groupUser.getGroup();
            result.append(storageOptionBuilder(group.getGroupShareDirName(),
                          "/home/"+sudoerId+"/"+group.getGroupName()+ "-dcloud-dir"));
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

    public SshResult<String> createNewUserShareDir(Server selectedServer, User user) throws JSchException, IOException {
        JSch jsch = new JSch();
        Session session = jsch.getSession("4whomtbts", selectedServer.getInternalIP(), selectedServer.getSshPort());
        session.setPassword("Hndp^(%#9!Q");
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect();  //연결

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
                System.out.println("결과");
                System.out.println(outputBuffer.toString());
                System.out.println("에러 = " + channel.getExitStatus());
                channel.disconnect();
                String lineSeparator = System.lineSeparator();
                String containerHash = outputBuffer.toString().replaceAll(lineSeparator, "");

                SshResultError error = null;
                int exitStatus = channel.getExitStatus();
                if (exitStatus != 0 && exitStatus != 1) {
                    error = new SshResultError(outputBuffer.toString(), exitStatus);
                }

                return new SshResult<>(error, outputBuffer.toString());
            }
        }
    }
    public SshResult<InstanceCreationDto> createNewInstance(Server selectedServer, User user, List<ServerPort> selectedPortList,
                                                            ServerResource serverResource, String containerId, String sudoerId) throws Exception {
        JSch jsch = new JSch();
        Session session = jsch.getSession("4whomtbts", selectedServer.getInternalIP(), selectedServer.getSshPort());
        session.setPassword("Hndp^(%#9!Q");
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect();  //연결

        Channel channel = session.openChannel("exec");
        ChannelExec channelExec = (ChannelExec) channel;
        channelExec.setPty(true);
        String dcloudImage = "dcloud1.0";

        SshResult<String> createShareDirResult = createNewUserShareDir(selectedServer, user);
        if (createShareDirResult.getError() != null) {
            return new SshResult<>(createShareDirResult.getError(), null);
        }

        String command =
                "sudo docker run -d " +
                buildGpuAllocOptionValue(serverResource) + " " +
                generatedDockerRunPortOption(selectedPortList) +
                "-it --runtime=nvidia " +
                "--cap-add=SYS_ADMIN " +
                "--shm-size=2g " +
                mappingStoragesOption(selectedServer, user, sudoerId) +
                "--name " + containerId + " " + dcloudImage;
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
                System.out.println("에러 = " + channel.getExitStatus());
                channel.disconnect();
                String lineSeparator = System.lineSeparator();
                String containerHash = outputBuffer.toString().replaceAll(lineSeparator, "");

                SshResultError error = null;
                int exitStatus = channel.getExitStatus();
                if (exitStatus != 0) {
                    error = new SshResultError(outputBuffer.toString(), exitStatus);
                }

                return new SshResult<>(
                        error,
                        new InstanceCreationDto(containerId, containerHash));
            }
        }
    }
    /*
List<String> commands = new ArrayList<>();
        commands.add("sudo docker run -d " +
                buildGpuAllocOptionValue(serverResource) + " " +
                generatedDockerRunPortOption(selectedPortList) +
                "-it --runtime=nvidia " +
                "--cap-add=SYS_ADMIN " +
                "--shm-size=2g " +
                "--name " + generatedInstanceID + " " + dcloudImage);
        commands.add("sudo docker cp ~/dcloud/images/"+dcloudImage+"/init.sh "+dcloudImage+":/");
        commands.add("sudo docker exec -it "+generatedInstanceID+" bash /init.sh");
 */
    public static SshResult<String> copyFileToInstance(Server server, String instanceContainerId, String filePath) throws JSchException, IOException {
        JSch jsch = new JSch();
        Session session = jsch.getSession("4whomtbts", server.getInternalIP(), server.getSshPort());
        session.setPassword("Hndp^(%#9!Q");
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect();  //연결

        Channel channel = session.openChannel("exec");
        ChannelExec channelExec = (ChannelExec) channel;
        channelExec.setPty(true);
        String dcloudImage = "dcloud1.0";
        String commad = "sudo docker cp "+ filePath +" "+ instanceContainerId +":/";
        channelExec.setCommand(commad);
        System.out.println(commad);

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
                System.out.println("에러 = " + channel.getExitStatus());
                channel.disconnect();

                int exitStatus = channel.getExitStatus();
                SshResultError error = null;
                if (exitStatus != 0) error = new SshResultError(outputBuffer.toString(), exitStatus);
                return new SshResult<>(error, outputBuffer.toString());
            }
        }
    }

    public static SshResult<String> copyInitShellScriptToInstance(Server server, String instanceContainerHash) throws JSchException, IOException {
        String dcloudImage = "dcloud1.0";
        return copyFileToInstance(server, instanceContainerHash, "~/dcloud/images/"+dcloudImage+"/init.sh");
    }

    public static SshResult<String> copyRemoteAccessScriptToInstance(Server server, String instanceContainerHash) throws JSchException, IOException {
        String dcloudImage = "dcloud1.0";
        return copyFileToInstance(server, instanceContainerHash, "~/dcloud/images/"+dcloudImage+"/remote_access.sh");
    }

    public SshResult<String> executeRemoteAccessScript(int serverHostSshPort, String instanceContainerId, String sudoerId,
                                                 String sudoerPwd) throws JSchException, IOException {
        String generatedInstanceID = UUID.randomUUID().toString();
        JSch jsch = new JSch();
        Session session = jsch.getSession("4whomtbts", "210.94.223.123", serverHostSshPort);
        session.setPassword("Hndp^(%#9!Q");
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect();  //연결

        Channel channel = session.openChannel("exec");
        ChannelExec channelExec = (ChannelExec) channel;
        channelExec.setPty(true);
        String dcloudImage = "dcloud1.0";
        String command = "sudo docker exec -it "+instanceContainerId+" bash /remote-access.sh " + sudoerId + " " + sudoerPwd;
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
                System.out.println("에러 = " + channel.getExitStatus());
                channel.disconnect();

                int exitStatus = channel.getExitStatus();
                SshResultError error = null;
                if (exitStatus != 0) error = new SshResultError(outputBuffer.toString(), exitStatus);
                return new SshResult<>(error, outputBuffer.toString());
            }
        }
    }

    public SshResult<String> executeInstanceInit(Server server, String instanceContainerId, String sudoerId,
                                                 String sudoerPwd) throws JSchException, IOException {
        String generatedInstanceID = UUID.randomUUID().toString();
        JSch jsch = new JSch();
        Session session = jsch.getSession("4whomtbts", server.getInternalIP(), server.getSshPort());
        session.setPassword("Hndp^(%#9!Q");
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect();  //연결

        Channel channel = session.openChannel("exec");
        ChannelExec channelExec = (ChannelExec) channel;
        channelExec.setPty(true);
        String dcloudImage = "dcloud1.0";
        String command = "sudo docker exec -it "+instanceContainerId+" bash /init.sh " + sudoerId + " " + sudoerPwd;
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
                System.out.println("에러 = " + channel.getExitStatus());
                channel.disconnect();

                int exitStatus = channel.getExitStatus();
                SshResultError error = null;
                if (exitStatus != 0) error = new SshResultError(outputBuffer.toString(), exitStatus);
                return new SshResult<>(error, outputBuffer.toString());
            }
        }
    }
}
