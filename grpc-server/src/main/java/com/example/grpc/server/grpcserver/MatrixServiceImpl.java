package com.example.grpc.server.grpcserver;


import java.util.Arrays;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
@GrpcService
public class MatrixServiceImpl extends MatrixServiceGrpc.MatrixServiceImplBase
{
	public int[][] convertMatrix(String file) {
		//https://stackoverflow.com/questions/14512251/convert-string-into-2d-int-array
		String[] row = file.split("\n");
		String[] column = row[0].split(" ");
		// temporary new array to return and pass onto mult func
		int[][] array = new int[row.length][row.length];
		for(int i = 0; i < row.length; i++){
			//split into columns for each row
			String[] cells = row[i].split(" ");
			for(int j = 0; j < cells.length; j++){
				//convert string to int
				array[i][j] = Integer.parseInt(cells[j].replace("\r", ""));
			}
		}
    	return array;
	}
	public String convert2String(int[][] file){ // [[1,2], [3,4]]
		String concat = "";
		for (int column = 0; column < file.length; column++) {	// 1 2
			for (int row = 0; row < file[column].length; row++) { // 3 4
				if (row != file[column].length-1) {
					concat += String.valueOf(file[column][row]) + " ";
				} else {
					concat += String.valueOf(file[column][row]);
				}
			}
			if (column != file.length-1) {
				concat += "\n";
			}
		}
		return concat;
	}
	@Override
	public void addBlock(MatrixRequest request, StreamObserver<MatrixReply> reply)
	{

		System.out.println("Request received from client:\n" + request);
		int[][] matrix1 = convertMatrix(request.getMatrix1());
		int[][] matrix2 = convertMatrix(request.getMatrix2());
		int[][] matrix3 = new int[matrix1.length][matrix2.length];
		//https://www.javatpoint.com/java-program-to-add-two-matrices
		for(int i = 0; i < matrix1.length; i++){  
			for(int j = 0; j < matrix1.length; j++){  
				matrix3[i][j] = matrix1[i][j]+ matrix2[i][j]; 
			}  
		} 
		String stringMatrix = convert2String(matrix3);

        MatrixReply response = MatrixReply.newBuilder().setMatrix3(stringMatrix).build();
	
		// MatrixReply response = MatrixReply.newBuilder().setMatrix(final).build();
		reply.onNext(response);
		reply.onCompleted();
	}
	@Override
    	public void multiplyBlock(MatrixRequest request, StreamObserver<MatrixReply> reply)
    	{
        System.out.println("Request received from client:\n" + request);
		int[][] matrix1 = convertMatrix(request.getMatrix1());
		int[][] matrix2 = convertMatrix(request.getMatrix2());
		int[][] matrix3 = new int[matrix1.length][matrix2.length];
		//https://stackoverflow.com/a/67279954
		for(int i = 0; i < matrix1.length/2; i++){
			for(int j = 0; j < matrix1.length/2; j++){
				for(int k = 0; k < matrix1.length/2; k++){
					matrix3[i][j] += matrix1[i][k] * matrix2[k][j];
				}

			}
		}
		String stringMatrix = convert2String(matrix3);
        MatrixReply response = MatrixReply.newBuilder().setMatrix3(stringMatrix).build();
        reply.onNext(response);
        reply.onCompleted();

		
    }
}
