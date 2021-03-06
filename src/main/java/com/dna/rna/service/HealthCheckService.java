package com.dna.rna.service;

import com.dna.rna.domain.server.Server;
import com.dna.rna.domain.server.ServerRepository;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

@Component
@RequiredArgsConstructor
public class HealthCheckService {

    private static final Logger logger = LoggerFactory.getLogger(HealthCheckService.class);

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

    private final List<String> receivers = Arrays.asList("4whomtbts@gmail.com", "ailabsysmanager.dgu@gmail.com", "5656jieun@dgu.ac.kr");

    private final ServerRepository serverRepository;

    @Scheduled(fixedDelayString = "${healthcheck.interval.ms}")
    public void healthCheck() {

        Properties props = new Properties();
        props.put("mail.smtp.user", mailUser);
        props.put("mail.smtp.password", mailPwd);
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "25");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable","true");
        props.put("mail.smtp.EnableSSL.enable","true");
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.port", "465");
        props.setProperty("mail.smtp.socketFactory.port", "465");

        List<Server> servers = serverRepository.findAll();
        servers.parallelStream().forEach(server -> {
            JSch jsch = new JSch();
            com.jcraft.jsch.Session session;
            try {
                session = jsch.getSession(sshId, wanIP, server.getSshPort());
                session.setPassword(sshPwd);
                java.util.Properties config = new java.util.Properties();
                config.put("StrictHostKeyChecking", "no");
                session.setConfig(config);
                session.connect(10000);  //??????
                session.disconnect();
            } catch (JSchException e) {
                logger.error("[{}] ????????? Healthcheck??? ????????????????????? : exception - {}, stackTrace - {}",
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

                        message.setSubject(String.format("[????????? ?????????????????????] %s ?????? ???????????? ??????", server.getInternalIP()));
                        String mailContent = String.format("[%s] ????????? SSH ?????? Healthcheck??? ????????????????????? : \n" +
                                "?????? ????????? ?????? ?????? SSH??? ???????????????. \n" +
                                "exception : %s", server.getInternalIP(), ExceptionUtils.getMessage(e));
                        message.setText(mailContent);
                        Transport.send(message);
                    } catch (MessagingException emailException) {
                        logger.error("Healthcheck failure email ????????? ????????????????????? : {}", ExceptionUtils.getMessage(emailException));
                    }
                });

            }
        });
    }

}
