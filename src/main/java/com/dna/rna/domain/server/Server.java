package com.dna.rna.domain.server;

import com.dna.rna.domain.ServerResource;
import com.dna.rna.domain.instance.Instance;
import com.dna.rna.dto.InstanceCreationDto;
import com.jcraft.jsch.*;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name= "server")
public class Server {

    public static final String SERVER_ID = "server_id";
    public static final int PORT_RANGE = 100;
    public static final int MIN_INTERNAL_PORT = 10000;

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = SERVER_ID)
    private Long serverId;

    @Column(name = "server_num")
    private int serverNum;

    @Column(name = "internal_ip")
    private String internalIP;

    @Column(name = "ssh_port")
    private int sshPort;

    @Column(name = "min_external_port")
    private int minExternalPort;

    @Column(name = "min_internal_port")
    private int minInternalPort;

    @Column(name = "excluded")
    private boolean excluded;

    @Embedded
    private ServerResource serverResource;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "server")
    private List<Instance> instanceList;

    private Server() {}

    public Server(int serverNum, String internalIP, int sshPort, int minExternalPort, ServerResource serverResource) {
        this.serverNum = serverNum;
        this.internalIP = internalIP;
        this.sshPort = sshPort;
        this.minExternalPort= minExternalPort;
        this.minInternalPort = MIN_INTERNAL_PORT;
        this.serverResource = serverResource;
        this.instanceList = new ArrayList<>();
        this.excluded = false;
    }

    public String startInstance(String containerHash) throws JSchException, IOException {
        JSch jsch = new JSch();
        Session session = jsch.getSession("4whomtbts", "210.94.223.123", getSshPort());
        session.setPassword("Hndp^(%#9!Q");
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect();  //연결

        Channel channel = session.openChannel("exec");
        ChannelExec channelExec = (ChannelExec) channel;
        channelExec.setPty(true);
        String commad = "sudo docker start " + containerHash;
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
                String result = outputBuffer.toString().replaceAll(lineSeparator, "");

                return result;
            }
        }
    }
}
