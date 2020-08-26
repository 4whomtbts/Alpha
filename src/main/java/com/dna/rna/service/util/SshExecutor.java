package com.dna.rna.service.util;

import com.dna.rna.domain.ServerResource;
import com.dna.rna.domain.instance.Instance;
import com.dna.rna.domain.server.Server;
import com.dna.rna.domain.serverPort.ServerPort;
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
        for (int i = 0; i < gpus.size(); i++) {
            int curr = gpus.get(i);
            if (curr == -1 || curr == 1) {
                if (i != 0) {
                    result.append(',');
                }
                result.append((i + 1));
            }
        }
        result.append("\"'");
        return result.toString();
    }

    public static String deleteInstance(Server server, Instance instance) throws Exception {
        JSch jsch = new JSch();
        Session session = jsch.getSession("4whomtbts", "210.94.223.123", server.getSshPort());
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

    public static String fetchStatusOfInstance(Server server, String instanceContainerID) throws Exception {
        JSch jsch = new JSch();
        Session session = jsch.getSession("4whomtbts", "210.94.223.123", server.getSshPort());
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
                //int colonIndex = status.indexOf(':');
                //int crIndex = status.indexOf('\r');
                //status = status.substring(colonIndex+2, crIndex-1);
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

    public InstanceCreationDto createNewInstance(int serverHostSshPort, List<ServerPort> selectedPortList, ServerResource serverResource) throws Exception {
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
        String dcloudImage = "dcloud:1.0";
        String commad = "sudo docker run -d " +
                buildGpuAllocOptionValue(serverResource) + " " +
                generatedDockerRunPortOption(selectedPortList) +
                "-it --runtime=nvidia " +
                "--cap-add=SYS_ADMIN " +
                "--shm-size=2g " +
                "--name " + generatedInstanceID + " " + dcloudImage;
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
                String lineSeparator = System.lineSeparator();
                String containerHash = outputBuffer.toString().replaceAll(lineSeparator, "");

                return new InstanceCreationDto(generatedInstanceID, containerHash);
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
    public String copyInitShellScriptToInstance(int serverHostSshPort, String instanceContainerId) throws JSchException, IOException {
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
        String dcloudImage = "dcloud:1.0";
        String commad = "sudo docker cp ~/dcloud/images/"+dcloudImage+"/init.sh "+instanceContainerId +":/";
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
                return outputBuffer.toString();
            }
        }
    }

    public String executeInstanceInit(int serverHostSshPort, String instanceContainerId) throws JSchException, IOException {
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
        String dcloudImage = "dcloud:1.0";
        String commad = "sudo docker exec -it "+instanceContainerId+" bash /init.sh hello 1234";
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
                return outputBuffer.toString();
            }
        }
    }
}
