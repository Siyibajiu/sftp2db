package com.adidas.sftp;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

class SftpApplicationTests {

    @Test
    void contextLoads() {

        String dateString = "2019-09-19";

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date date = null;
        try {
            date = simpleDateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long ts = date.getTime();
        System.out.println(ts);
    }

}
