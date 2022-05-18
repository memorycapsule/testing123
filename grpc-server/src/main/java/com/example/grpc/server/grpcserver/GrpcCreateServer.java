package com.example.grpc.server.grpcserver;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import java.io.IOException;


//Thread based approach, create different servers based on number of threads.
public class GrpcCreateServer extends Thread {
	private final int port;
    private final int thread;
	private final Server server;

	public GrpcCreateServer(int port, int thread){
		this.port = port;
		this.thread = thread;
		this.server = ServerBuilder.forPort(this.port).addService(new MatrixServiceImpl()).build();
		}

	public void run(){
		try{
			server.start();
			System.out.println("Server deployed on port" + server.getPort());
			server.awaitTermination();
		}catch(Exception e){
			e.printStackTrace();
		}

	}
}
