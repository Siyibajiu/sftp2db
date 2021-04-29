package com.adidas.sftp.service;

import com.jcraft.jsch.JSchException;

import java.io.IOException;

public interface SftpService {
    public boolean downloadFile(String remoteFileName) throws JSchException, IOException;

}
