package com.dna.rna.service;

import com.dna.rna.domain.server.Server;
import com.dna.rna.domain.server.ServerRepository;
import com.jcraft.jsch.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

@Component
@RequiredArgsConstructor
public class HealthCheckService {

    private static final Logger logger = LoggerFactory.getLogger(HealthCheckService.class);

    private static final int TEN_MINUTE = 10 * 60 * 1000;

    @Value("${ssh.id}")
    private String sshId;

    @Value("${ssh.pwd}")
    private String sshPwd;

    @Value("${wan.ip}")
    private String wanIP;

    @Value("${mail.user}")
    private String mailUser;

    @Value("${mail.pwd}")
    private String mailPwd;

    private final String host = "smtp.gmail.com";

    private final List<String> receivers = Arrays.asList("4whomtbts@gmail.com");

    private final ServerRepository serverRepository;

    @Scheduled(fixedDelay = TEN_MINUTE)
    public void healthCheck() {

        logger.info("Healthcheck 가동 ...");
        Properties props = new Properties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", 587);
        props.put("mail.smtp.auth", "true");

        List<Server> servers = serverRepository.findAll();
        servers.parallelStream().forEach(server -> {
            JSch jsch = new JSch();
            com.jcraft.jsch.Session session = null;
            try {
                session = jsch.getSession(sshId, wanIP, server.getSshPort());
                session.setPassword(sshPwd);
                java.util.Properties config = new java.util.Properties();
                config.put("StrictHostKeyChecking", "no");
                session.setConfig(config);
                session.connect(10000);  //연결
                session.disconnect();
                logger.info("{} 서버의 HealthCheck 성공!", server.getInternalIP());
            } catch (JSchException e) {
                logger.error("[{}] 서버의 Healthcheck에 실패하였습니다 : exception - {}, stackTrace - {}",
                        server.getInternalIP(), ExceptionUtils.getMessage(e), ExceptionUtils.getStackTrace(e));
                receivers.parallelStream().forEach(receiver -> {
                    javax.mail.Session mailSession = javax.mail.Session.getDefaultInstance(props, new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(mailUser, mailPwd);
                        }
                    });

                    try {
                        MimeMessage message = new MimeMessage(mailSession);
                        message.setFrom(new InternetAddress(mailUser));
                        message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiver));

                        message.setSubject(String.format("[%s] 서버 장애발생 알림", server.getInternalIP()));
                        String mailContent = String.format("[%s] 서버의 SSH 기반 Healthcheck에 실패하였습니다 : \n" +
                                "exception : %s", server.getInternalIP(), ExceptionUtils.getMessage(e));
                        message.setText(mailContent);
                        Transport.send(message);
                    } catch (MessagingException emailException) {
                        logger.error("Healthcheck failure email 전송에 실패하였습니다 : %s", ExceptionUtils.getMessage(emailException));
                    }
                });

            }
        });
    }

}
