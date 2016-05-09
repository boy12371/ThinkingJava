package com.thinking.socket;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TestClient {
	public static void main(String[] args) throws Exception{
		Socket so = new Socket("127.0.0.1",7456);
		InputStream is = so.getInputStream();
		OutputStream os = so.getOutputStream();
		BufferedReader br = //�ͻ���server��������
		new BufferedReader(new InputStreamReader(is));
		PrintStream ps = new PrintStream(os);
		InputStreamReader isr = 
			new InputStreamReader(System.in);
		BufferedReader key = //�Ӽ��̵�������
			new BufferedReader(isr);
		while(true){
		    String temp = key.readLine();
		    ps.println(temp);//��Ϊserver�õ���readline,������println
		    System.out.println(br.readLine());
		    if(temp.equals("bye")){
		    	Thread.sleep(1000);//Ϊ�˰�bye���õķ���
		    	break;
		    }
		}
		key.close();//java ����.����
		ps.close();
		br.close();
		so.close();
	}

}