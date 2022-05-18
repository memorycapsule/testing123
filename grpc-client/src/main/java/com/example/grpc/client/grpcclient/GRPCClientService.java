package com.example.grpc.client.grpcclient;

import com.example.grpc.server.grpcserver.PingRequest;
import com.example.grpc.server.grpcserver.PongResponse;
import com.example.grpc.server.grpcserver.PingPongServiceGrpc;
import com.example.grpc.server.grpcserver.MatrixRequest;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.text.StringContent;
import java.util.List;
import com.example.grpc.server.grpcserver.MatrixReply;
import com.example.grpc.server.grpcserver.MatrixServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;
import java.util.concurrent.CompletableFuture;

@Service
public class GRPCClientService {
	//Since I am using threads instead of multiple small servers we use the ports for channel builder
	public void checkMatrix(String file) throws Exception{
		//I split by newline for every row
		String[] rows = file.split("\n");
		//I then check the length of any row and check the sizes
		String[] columns = rows[1].split(" ");
		//Check matrix size errors
		if(rows.length % 2 != 0 || columns.length % 2 != 0){
			throw new Exception("Matrix size is not to the power of 2");
		} 
		if(rows.length <= 0 || columns.length <= 0){
			throw new Exception("Matrix needs to be greater than length 0");
		}
		if(rows.length != columns.length) {
			throw new Exception("Matrix does not have the same amount of rows and columns");
		} 
	}

	//This function converts from string to a 2d int array as suggested in the coursework.
	public int[][] convertMatrix(String file) throws Exception{
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

	//This function does the reverse of ConvertMatrix
	public String convert2String(int[][] file){ // [[1,2], [3,4]]
		//just the reverse of convertmatrix, but i add onto a string instead
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
	public String finaloutput(int[][] file){ // [[1,2], [3,4]]
		//same as the one above, but new line for every new row
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
				concat += "<br>";
			}
		}
		return concat;
	}
	//The following functions are from the BlockMult.java file from Lab 1
	public static int MAX = 4; 
    
    static int[][] multiplyBlock(int A[][], int B[][])
    {
    	int C[][]= new int[MAX][MAX];
    	C[0][0]=A[0][0]*B[0][0]+A[0][1]*B[1][0];
    	C[0][1]=A[0][0]*B[0][1]+A[0][1]*B[1][1];
    	C[1][0]=A[1][0]*B[0][0]+A[1][1]*B[1][0];
    	C[1][1]=A[1][0]*B[0][1]+A[1][1]*B[1][1];
    	return C;
    }
    static int[][] addBlock(int A[][], int B[][])
    {
    	int C[][]= new int[MAX][MAX];
    	for (int i=0;i<C.length;i++)
    	{
    		for (int j=0;j<C.length;j++)
    		{
    			C[i][j]=A[i][j]+B[i][j];
    		}
    	}
    	return C;
    }

	//from BlockMult.java
	public ArrayList<int[][]> divideIntoBlocks(int[][] A, int[][] B) {
		ArrayList<int[][]> blocks = new ArrayList<int[][]>();

		int MAX = A.length;
		int bSize = MAX/2;
	
    	int[][] A1 = new int[MAX][MAX];
    	int[][] A2 = new int[MAX][MAX];
    	int[][] A3 = new int[MAX][MAX];
    	int[][] B1 = new int[MAX][MAX];
    	int[][] B2 = new int[MAX][MAX];
    	int[][] B3 = new int[MAX][MAX];
    	int[][] C1 = new int[MAX][MAX];
    	int[][] C2 = new int[MAX][MAX];
    	int[][] C3 = new int[MAX][MAX];
    	int[][] D1 = new int[MAX][MAX];
    	int[][] D2 = new int[MAX][MAX];
    	int[][] D3 = new int[MAX][MAX];

		for (int i = 0; i < bSize; i++) { 
			for (int j = 0; j < bSize; j++) {
				A1[i][j]=A[i][j];
				A2[i][j]=B[i][j];
			}
		}

		for (int i = 0; i < bSize; i++) { 
			for (int j = bSize; j < MAX; j++) {
				B1[i][j-bSize]=A[i][j];
				B2[i][j-bSize]=B[i][j];
			}
		}

		for (int i = bSize; i < MAX; i++) { 
			for (int j = 0; j < bSize; j++) {
				C1[i-bSize][j]=A[i][j];
				C2[i-bSize][j]=B[i][j];
			}
		}

		for (int i = bSize; i < MAX; i++) { 
			for (int j = bSize; j < MAX; j++){
				D1[i-bSize][j-bSize]=A[i][j];
				D2[i-bSize][j-bSize]=B[i][j];
			}
		}

		blocks.add(A1);
		blocks.add(A2);
		
		blocks.add(B1);
		blocks.add(B2);

		blocks.add(C1);
		blocks.add(C2);
		
		blocks.add(D1);
		blocks.add(D2);
		return blocks;
	}
	
	/*The bottom part of multiplyMatrixBlock in BlockMult.java, 
		I use this to concatenate the blocks back together and
		return the result.
	*/
	public int[][] getBlocks(int[][] A3, int[][] B3,int[][] C3,int[][] D3){
    	int[][] res= new int[MAX][MAX];

		int bSize=2;
    	for (int i = 0; i < bSize; i++) 
        { 
            for (int j = 0; j < bSize; j++)
            {
                res[i][j]=A3[i][j];
            }
        }
    	for (int i = 0; i < bSize; i++) 
        { 
            for (int j = bSize; j < MAX; j++)
            {
                res[i][j]=B3[i][j-bSize];
            }
        }
    	for (int i = bSize; i < MAX; i++) 
        { 
            for (int j = 0; j < bSize; j++)
            {
                res[i][j]=C3[i-bSize][j];
            }
        } 
    	for (int i = bSize; i < MAX; i++) 
        { 
            for (int j = bSize; j < MAX; j++)
            {
                res[i][j]=D3[i-bSize][j-bSize];
            }
        } 
    	return res;
	}


	//Takes in both files and multiplies them
	public String multMatrix(String file1, String file2) throws Exception {
		checkMatrix(file1);
		checkMatrix(file2);
		int[][] intmatrix1 = convertMatrix(file1);
		int[][] intmatrix2 = convertMatrix(file2);

		ArrayList<int[][]> blocks = divideIntoBlocks(intmatrix1, intmatrix2);

		//Creating different channels and stubs for the blocks
		ManagedChannel channel1 = ManagedChannelBuilder.forAddress("localhost", 9092).usePlaintext().build();
		MatrixServiceGrpc.MatrixServiceBlockingStub stub1 = MatrixServiceGrpc.newBlockingStub(channel1);
		ManagedChannel channel2 = ManagedChannelBuilder.forAddress("localhost", 9093).usePlaintext().build();
		MatrixServiceGrpc.MatrixServiceBlockingStub stub2 = MatrixServiceGrpc.newBlockingStub(channel2);
		ManagedChannel channel3 = ManagedChannelBuilder.forAddress("localhost", 9094).usePlaintext().build();
		MatrixServiceGrpc.MatrixServiceBlockingStub stub3 = MatrixServiceGrpc.newBlockingStub(channel3);
		ManagedChannel channel4 = ManagedChannelBuilder.forAddress("localhost", 9095).usePlaintext().build();
		MatrixServiceGrpc.MatrixServiceBlockingStub stub4 = MatrixServiceGrpc.newBlockingStub(channel4);
		ManagedChannel channel5 = ManagedChannelBuilder.forAddress("localhost", 9096).usePlaintext().build();
		MatrixServiceGrpc.MatrixServiceBlockingStub stub5 = MatrixServiceGrpc.newBlockingStub(channel5);
		ManagedChannel channel6 = ManagedChannelBuilder.forAddress("localhost", 9097).usePlaintext().build();
		MatrixServiceGrpc.MatrixServiceBlockingStub stub6 = MatrixServiceGrpc.newBlockingStub(channel6);
		ManagedChannel channel7 = ManagedChannelBuilder.forAddress("localhost", 9098).usePlaintext().build();
		MatrixServiceGrpc.MatrixServiceBlockingStub stub7 = MatrixServiceGrpc.newBlockingStub(channel7);
		ManagedChannel channel8 = ManagedChannelBuilder.forAddress("localhost", 9099).usePlaintext().build();
		MatrixServiceGrpc.MatrixServiceBlockingStub stub8 = MatrixServiceGrpc.newBlockingStub(channel8);

	// 	A3=addBlock(multiplyBlock(A1,A2),multiplyBlock(B1,C2));
    // 	B3=addBlock(multiplyBlock(A1,B2),multiplyBlock(B1,D2));
    // 	C3=addBlock(multiplyBlock(C1,A2),multiplyBlock(D1,C2)); // [[1,2], [1,1]]
    // 	D3=addBlock(multiplyBlock(C1,B2),multiplyBlock(D1,D2));

	//https://www.baeldung.com/java-completablefuture
	//For async 
	long startTime = System.nanoTime();
	long deadline = 5000;
	CompletableFuture<MatrixReply> A1A2 = CompletableFuture.supplyAsync(() -> stub1.multiplyBlock(
		MatrixRequest.newBuilder().setMatrix1(convert2String(blocks.get(0))).setMatrix2(convert2String(blocks.get(1))).build()));

	//get footprint
	long endTime = System.nanoTime();
	long footprint= endTime-startTime;


	//twelve compatible futures
	// int numberServer=(footprint*12)/deadline;
	CompletableFuture<MatrixReply> B1C2 = CompletableFuture.supplyAsync(() -> stub2.multiplyBlock(
		MatrixRequest.newBuilder().setMatrix1(convert2String(blocks.get(2))).setMatrix2(convert2String(blocks.get(5))).build()));
	CompletableFuture<MatrixReply> A1B2 = CompletableFuture.supplyAsync(() -> stub3.multiplyBlock(
		MatrixRequest.newBuilder().setMatrix1(convert2String(blocks.get(0))).setMatrix2(convert2String(blocks.get(3))).build()));
	CompletableFuture<MatrixReply> B1D2 = CompletableFuture.supplyAsync(() -> stub4.multiplyBlock(
		MatrixRequest.newBuilder().setMatrix1(convert2String(blocks.get(2))).setMatrix2(convert2String(blocks.get(7))).build()));
	CompletableFuture<MatrixReply> C1A2 = CompletableFuture.supplyAsync(() -> stub5.multiplyBlock(
		MatrixRequest.newBuilder().setMatrix1(convert2String(blocks.get(4))).setMatrix2(convert2String(blocks.get(1))).build()));
	CompletableFuture<MatrixReply> D1C2 = CompletableFuture.supplyAsync(() -> stub6.multiplyBlock(
		MatrixRequest.newBuilder().setMatrix1(convert2String(blocks.get(6))).setMatrix2(convert2String(blocks.get(5))).build()));
	CompletableFuture<MatrixReply> C1B2 = CompletableFuture.supplyAsync(() -> stub7.multiplyBlock(
		MatrixRequest.newBuilder().setMatrix1(convert2String(blocks.get(4))).setMatrix2(convert2String(blocks.get(3))).build()));
	CompletableFuture<MatrixReply> D1D2 = CompletableFuture.supplyAsync(() -> stub8.multiplyBlock(
		MatrixRequest.newBuilder().setMatrix1(convert2String(blocks.get(6))).setMatrix2(convert2String(blocks.get(7))).build()));

		CompletableFuture<MatrixReply> A3 = CompletableFuture.supplyAsync(() -> {
			try {
				return stub1.addBlock(MatrixRequest.newBuilder().setMatrix1(A1A2.get().getMatrix3()).setMatrix2(B1C2.get().getMatrix3()).build());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		});

		CompletableFuture<MatrixReply> B3 = CompletableFuture.supplyAsync(() -> {
			try {
				return stub2.addBlock(MatrixRequest.newBuilder().setMatrix1(A1B2.get().getMatrix3()).setMatrix2(B1D2.get().getMatrix3()).build());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		});

		CompletableFuture<MatrixReply> C3 = CompletableFuture.supplyAsync(() ->{
			try {
				return stub3.addBlock(MatrixRequest.newBuilder().setMatrix1(C1A2.get().getMatrix3()).setMatrix2(D1C2.get().getMatrix3()).build());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		});
		CompletableFuture<MatrixReply> D3 = CompletableFuture.supplyAsync(() ->{
			try {
				return stub4.addBlock(MatrixRequest.newBuilder().setMatrix1(C1B2.get().getMatrix3()).setMatrix2(D1D2.get().getMatrix3()).build());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		});

		int[][] mergedBlocks = getBlocks(convertMatrix(A3.get().getMatrix3()),convertMatrix(B3.get().getMatrix3()),convertMatrix(C3.get().getMatrix3()),convertMatrix(D3.get().getMatrix3()));
		String stringBlock = finaloutput(mergedBlocks);
		return stringBlock;
	}
}
