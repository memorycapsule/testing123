package com.example.grpc.server.grpcserver;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import java.util.concurrent.ExecutorService;
import java.io.IOException;

import java.util.concurrent.Executors;
@SpringBootApplication
public class GrpcServerApplication extends SpringBootServletInitializer {
	private static int[] ports = {9092, 9093, 9094, 9095, 9096, 9097, 9098, 9099};
	private static int[] threads = {1, 2, 3, 4, 5, 6, 7, 8};

	public static void main(String[] args) throws Exception{
        SpringApplication.run(GrpcServerApplication.class, args);
		for (int i = 0; i < 8; i++) {
			int port = ports[i];
			try{
				new GrpcCreateServer(port, i).start();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
};
