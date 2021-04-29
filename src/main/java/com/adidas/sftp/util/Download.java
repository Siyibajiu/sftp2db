package com.adidas.sftp.util;

import com.adidas.sftp.config.SftpConfig;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import java.io.IOException;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;
import org.springframework.integration.sftp.session.SftpSession;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.time.Duration;

@Component
public class Download {

    private static SftpConfig sftpConfig;

    @Autowired
    public void setConfig(SftpConfig sftpConfig) {
        Download.sftpConfig = sftpConfig;
    }

    private DefaultSftpSessionFactory gimmeFactory() {
        DefaultSftpSessionFactory factory = new DefaultSftpSessionFactory();
        factory.setHost(sftpConfig.getIp());
        factory.setPort(22);
        Properties properties = new Properties();
        properties.setProperty("StrictHostKeyChecking", "false");
        factory.setSessionConfig(properties);
        factory.setAllowUnknownKeys(true);
        factory.setUser(sftpConfig.getUsername());
        factory.setChannelConnectTimeout(Duration.ofSeconds(1000));
        factory.setPassword(sftpConfig.getPassword());
        return factory;
    }
    public byte[] download(String remotePath, String filename) throws JSchException {
        SftpSession session = gimmeFactory().getSession();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            session.read(remotePath+"/"+filename, outputStream);
            return outputStream.toByteArray();

        } catch (IOException  e) {
            throw new RuntimeException(e);
        }
    }

    public ChannelSftp.LsEntry[] list(String remoteFilePath) throws JSchException {
        SftpSession session = gimmeFactory().getSession();
        try {
            ChannelSftp.LsEntry[] les = session.list(remoteFilePath);
            return les;

        } catch (IOException  e) {
            throw new RuntimeException(e);
        }
    }
}