package com.simple.netty.prod.server;

import javax.swing.Spring;
import com.simple.netty.prod.server.acceptor.DefaultCommonSrvAcceptor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringServerApplication implements ApplicationRunner {
    public static void main(String[] args) {
        SpringApplication.run(SpringServerApplication.class);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        new DefaultCommonSrvAcceptor(20011, null).start();
    }
}
