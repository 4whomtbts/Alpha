package com.dna.rna.service.util;


import ch.qos.logback.core.net.server.ServerListener;
import com.dna.rna.domain.ServerResource;
import com.dna.rna.domain.server.Server;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.junit.Test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.dna.rna.service.util.InstanceResourceAllocator.MAX_GPU_NUM;
import static org.assertj.core.api.Assertions.assertThat;

public class ResourceAllocatorTest {

    /*
    @Test
    public void allocateGPU() {
        List<Server> serverList = new ArrayList();
        serverList.add(new Server(1, "192.168.1.11", 8081, new ServerResource()));
        InstanceResourceAllocator  instanceResourceAllocator = new InstanceResourceAllocator();
        List<Server> result = instanceResourceAllocator.allocateGPU(serverList, 3, true);
        List<Integer> resultGPU = result.get(0).getServerResource().getGpus();
        assertThat(resultGPU.get(0)).isEqualTo(ServerResource.EXCLUSIVELY_ALLOC);
        assertThat(resultGPU.get(1)).isEqualTo(ServerResource.EXCLUSIVELY_ALLOC);
        assertThat(resultGPU.get(2)).isEqualTo(ServerResource.EXCLUSIVELY_ALLOC);

        serverList = new ArrayList();
        serverList.add(new Server(1, "192.168.1.11", 8081, new ServerResource()));
        result = instanceResourceAllocator.allocateGPU(serverList, 3, false);
        resultGPU = result.get(0).getServerResource().getGpus();
        assertThat(resultGPU.get(0)).isEqualTo(ServerResource.ALLOC);
        assertThat(resultGPU.get(1)).isEqualTo(ServerResource.ALLOC);
        assertThat(resultGPU.get(2)).isEqualTo(ServerResource.ALLOC);

        serverList = new ArrayList();
        serverList.add(new Server(1, "192.168.1.11", 8081, new ServerResource()));
        result = instanceResourceAllocator.allocateGPU(serverList, 3, false);
        result = instanceResourceAllocator.allocateGPU(serverList, 3, true);
        resultGPU = result.get(0).getServerResource().getGpus();
        assertThat(resultGPU.get(0)).isEqualTo(ServerResource.ALLOC);
        assertThat(resultGPU.get(1)).isEqualTo(ServerResource.ALLOC);
        assertThat(resultGPU.get(2)).isEqualTo(ServerResource.ALLOC);
        assertThat(resultGPU.get(3)).isEqualTo(ServerResource.EXCLUSIVELY_ALLOC);
        assertThat(resultGPU.get(4)).isEqualTo(ServerResource.EXCLUSIVELY_ALLOC);
        assertThat(resultGPU.get(5)).isEqualTo(ServerResource.EXCLUSIVELY_ALLOC);
    }

    @Test
    public void allocateGPU_PASS_MoreThanActuallyHave_WHEN_NotExclusiveMode() {
        List<Server> serverList = new ArrayList();
        serverList.add(new Server(1, "192.168.1.11", 8081, new ServerResource()));
        InstanceResourceAllocator instanceResourceAllocator = new InstanceResourceAllocator();
        List<Server> result = instanceResourceAllocator.allocateGPU(serverList, 9, false);
        result = instanceResourceAllocator.allocateGPU(serverList, 1, false);
        List<Integer> resultGPU = result.get(0).getServerResource().getGpus();
        assertThat(resultGPU.get(0)).isEqualTo(ServerResource.ALLOC + 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void allocateGPU_FAIL_WHEN_RequestedMoreThanActuallyHaveForSingleInstance() {
        List<Server> serverList = new ArrayList();
        serverList.add(new Server(1, "192.168.1.11", 8081, new ServerResource()));
        InstanceResourceAllocator instanceResourceAllocator = new InstanceResourceAllocator();
        List<Server> result = instanceResourceAllocator.allocateGPU(serverList, 10, false);
        List<Integer> resultGPU = result.get(0).getServerResource().getGpus();
    }

    @Test(expected = IllegalArgumentException.class)
        public void allocateGPU_FAIL_WHEN_RequestedMoreThanActuallyHave() {
            List<Server> serverList = new ArrayList();
            serverList.add(new Server(1, "192.168.1.11", 8081, new ServerResource()));
            InstanceResourceAllocator instanceResourceAllocator = new InstanceResourceAllocator();
            List<Server> result = instanceResourceAllocator.allocateGPU(serverList, 10, false);
            List<Integer> resultGPU = result.get(0).getServerResource().getGpus();
    }

    @Test(expected = IllegalArgumentException.class)
    public void allocateGPU_FailWhenNumOfRequestGpuIsBiggerThanAvailable() {
        List<Server> serverList = new ArrayList();
        serverList.add(new Server(1, "192.168.1.11", 8081, new ServerResource()));
        InstanceResourceAllocator  instanceResourceAllocator = new InstanceResourceAllocator();
        List<Server> result = instanceResourceAllocator.allocateGPU(serverList, MAX_GPU_NUM, true);
        List<Integer> resultGPU = result.get(0).getServerResource().getGpus();
        for (int i=0; i < MAX_GPU_NUM; i++) {
            assertThat(resultGPU.get(i)).isEqualTo(ServerResource.EXCLUSIVELY_ALLOC);
        }
        instanceResourceAllocator.allocateGPU(serverList, 1, true);

    }

    @Test
    public void testGood() throws Exception {
        String serverHostSshPort = "8081";
        String sshPort = "7071";
        String generatedInstanceID = UUID.randomUUID().toString();
        JSch jsch = new JSch();
        Session session = jsch.getSession("4whomtbts", "210.94.223.123", Integer.parseInt(serverHostSshPort));
        session.setPassword("Hndp^(%#9!Q");
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect();  //연결

        Channel channel = session.openChannel("exec");  //채널접속
        ChannelExec channelExec = (ChannelExec) channel; //명령 전송 채널사용
        channelExec.setPty(true);
        channelExec.setCommand("echo 'Hndp^(%#9!Q' | " +
                "sudo -S docker run -d --gpus all " +
                "-p "+ sshPort + ":22 " +
                "-p 3389:3389 " +
                "-it --runtime=nvidia " +
                "--cap-add=SYS_ADMIN " +
                "--shm-size=2g " +
                "--name " + generatedInstanceID +" aitf:20200707"); //내가 실행시킬 명령어를 입력


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
                channel.disconnect();
                return;
            }
        }
    }
    */
}
