package com.example.grpc.client.grpcclient;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.PostMapping;
import java.nio.charset.StandardCharsets;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
@RestController
public class PingPongEndpoint {    

	GRPCClientService grpcClientService;    
	@Autowired
	public PingPongEndpoint(GRPCClientService grpcClientService) {
		this.grpcClientService = grpcClientService;
	}    
	/* https://spring.io/guides/gs/uploading-files/
		following the guide to create a file uploading functionality
		https://stackoverflow.com/questions/38700790/how-to-return-a-html-page-from-a-restful-controller-in-spring-boot
		for returning a html page
	*/
	@GetMapping("/mult")
	public ModelAndView index () {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("uploader.html");
		return modelAndView;
	}

	@PostMapping("/mult")
	public String mult(@RequestParam("file1") MultipartFile file1,@RequestParam("file2") MultipartFile file2) {
		// errors without try/except for some reason?
		try {
			String matrix1 = new String(file1.getBytes(), StandardCharsets.UTF_8);
			String matrix2 = new String(file2.getBytes(), StandardCharsets.UTF_8);

			return grpcClientService.multMatrix(matrix1,matrix2);
	    } catch(Exception e) {
	    	e.printStackTrace();
	    	return e.getLocalizedMessage();
		}
	}
}