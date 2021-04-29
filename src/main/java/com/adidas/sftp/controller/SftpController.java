package com.adidas.sftp.controller;

import com.adidas.sftp.service.SftpService;
import com.jcraft.jsch.JSchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/sftp")
public class SftpController {

    @Autowired
    private SftpService sftpService;

    @GetMapping(value = "/init")
    public ResponseEntity init(@RequestParam(name = "remotePath") String remotePath) throws IOException, JSchException {
        if(remotePath == null){
            return ResponseEntity.ok("init failed");
        }
        return ResponseEntity.ok(sftpService.downloadFile(remotePath));
    }
}
