package com.dna.rna.service.util;

import com.dna.rna.domain.ServerResource;
import com.dna.rna.dto.InstanceCreationDto;
import com.jcraft.jsch.*;

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

    public InstanceCreationDto createNewInstance(int serverHostSshPort, int sshPort, ServerResource serverResource) throws Exception {
        String generatedInstanceID = UUID.randomUUID().toString();
        JSch jsch = new JSch();
        Session session = jsch.getSession("4whomtbts", "210.94.223.123", serverHostSshPort);
        session.setPassword("Hndp^(%#9!Q");
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect();  //연결

        Channel channel = session.openChannel("exec");  //채널접속
        ChannelExec channelExec = (ChannelExec) channel; //명령 전송 채널사용
        channelExec.setPty(true);
        channelExec.setCommand("echo 'Hndp^(%#9!Q' | " +
                "sudo -S docker run -d " +
                buildGpuAllocOptionValue(serverResource) + " " +
                "-p " + sshPort + ":22 " +
                "-p 3389:3389 " +
                "-it --runtime=nvidia " +
                "--cap-add=SYS_ADMIN " +
                "--shm-size=2g " +
                "--name " + generatedInstanceID + " aitf:20200707"); //내가 실행시킬 명령어를 입력


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
                String containerHash = outputBuffer.toString();
                int colonIndex = containerHash.indexOf(':');
                int crIndex = containerHash.indexOf('\r');
                containerHash = containerHash.substring(colonIndex+2, crIndex-1);

                channel.disconnect();
                return new InstanceCreationDto(generatedInstanceID, containerHash);
            }
        }
    }
}
