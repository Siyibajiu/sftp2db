package com.adidas.sftp.service.impl;

import com.adidas.sftp.config.BeanConfig;
import com.adidas.sftp.dto.MemberCrm;
import com.adidas.sftp.service.SftpService;
import com.adidas.sftp.util.Download;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.jeasy.batch.core.job.Job;
import org.jeasy.batch.core.job.JobBuilder;
import org.jeasy.batch.core.job.JobExecutor;
import org.jeasy.batch.core.job.JobReport;
import org.jeasy.batch.core.reader.IterableRecordReader;
import org.jeasy.batch.jdbc.BeanPropertiesPreparedStatementProvider;
import org.jeasy.batch.jdbc.JdbcRecordWriter;
import org.jeasy.batch.jdbc.PreparedStatementProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.UUID;

@Service
public class SftpServiceImpl implements SftpService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SftpServiceImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public boolean downloadFile(String remotePath) throws JSchException, IOException{
        ApplicationContext ctx = new AnnotationConfigApplicationContext(BeanConfig.class);
        DataSource dataSource = ctx.getBean(DataSource.class);
        JobExecutor jobExecutor = ctx.getBean(JobExecutor.class);
        Download download =new Download();
        ChannelSftp.LsEntry[] list = download.list(remotePath);
        for(ChannelSftp.LsEntry l : list){

            Long timestamp = System.currentTimeMillis();
            LOGGER.info("This file: {} start at ===> {}",remotePath+"/"+l.getFilename(), timestamp);
            byte[] content =  download.download(remotePath, l.getFilename());
            // way1 直接使用了opencsv 的实体映射以及解析处理，简化代码量
            ByteArrayInputStream byteArrayInputStream= new ByteArrayInputStream(content);

            // way2
            CSVReader reader = new CSVReaderBuilder(new InputStreamReader(byteArrayInputStream,"utf-8")).build();
            ArrayList<MemberCrm> memberList = new ArrayList<>();
            //  使用upsert 模式插入数据，实际处理，我们可以添加定时任务，同时最好暴露rest api，方便在处理异常的时候进行手工处理，同时推荐添加easy-batch 的监听以及监控（集成prometheus 也很不错，方便了解实际情况）
            memberList.clear();

            // 可以提前估计文件行数，在行数内循环，注意内存溢出
            while (true){
                String[] rowData = reader.readNext();
                if(ObjectUtils.isEmpty(rowData)){
                    break;
                }
                MemberCrm memberCrm = new MemberCrm();
                memberCrm.setUserId(UUID.randomUUID().toString());
                memberCrm.setUsername(rowData[0]);
                memberCrm.setPhonenumber(rowData[1]);
                memberList.add(memberCrm);
            }

            // for scenario insert
            String query = "insert into demo_table(id, username, phone_number) VALUES(?,?,?)";
            String[] fields = {"userId", "username", "phonenumber"};

            PreparedStatementProvider psp = new BeanPropertiesPreparedStatementProvider(MemberCrm.class, fields);
            Job job = new JobBuilder()
                    .batchSize(5000)
                    .reader(new IterableRecordReader(memberList))
                    .errorThreshold(100)
                    .writer(new JdbcRecordWriter(dataSource, query, psp))
                    .build();
            JobReport jobReport = jobExecutor.execute(job);
            LOGGER.info("This job info :{}", jobReport);
            LOGGER.info("This file over at ===> {}",timestamp);
        }

        return true;
    }
}