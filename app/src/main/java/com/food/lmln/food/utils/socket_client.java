package com.food.lmln.food.utils;

import android.app.Activity;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class socket_client  extends Activity {
    Socket client;
	private BufferedWriter writer = null;

	private String foodTY ;
	private String foodName ;
	private String foodPrice ;
	private int bufferLength;
	public static boolean i = false;
	public static boolean j = false;
	public void runclient(final String ip){
		Thread thr  =new Thread(){
			@Override
			public void run() {	
				try {
					client = new Socket(ip,30000);
					//	client = new Socket("192.168.1.107",30000);
					//InputStream dataStream = client.getInputStream();
					if(writer==null){
						writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream(),"UTF8"));
					}
					else{writer = null;}
					ReceiveDate();
					again_connect(ip);
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
		};
		thr.setDaemon(true);
		thr.start();
		
	}

	private void again_connect(final String ip) 
	{	while (true) {
		Thread thr  =new Thread(){
			@Override
			public void run() {	
				try {	
					client.sendUrgentData(0xFF); 					
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					try {
						client.close();
						Thread thr1  =new Thread(){
							@Override
							public void run() {	
								try {
									client = new Socket(ip,30000);
									//client = new Socket("192.168.0.199",30000);
									//InputStream dataStream = client.getInputStream();									
									writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream(),"UTF8"));					 
								} catch (IOException e) {
									// TODO 自动生成的 catch 块
									e.printStackTrace();
								} 
							}
						};
						thr1.start();
					} catch (IOException e1) {
						// TODO 自动生成的 catch 块
						e1.printStackTrace();
					}
					System.out.println(e+":要关掉了阿 ！");  
				}   	
			}
		};
		thr.start();
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}	
	}		
}

	public void send(String desk_num,String people_num){

		try {
			writer.write("8906063210##"+desk_num+"#"+people_num+"\n");   //开台
			writer.flush();  //强制发送

		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	public void sendfood(JSONObject obj){
		try {
			writer.write(String.valueOf(obj));   // 下单
			writer.flush();  //强制发送			
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			System.out.println("主机信息为空，请补充后再试试");
		}
	}

	private void ReceiveDate()
	{
		byte[] buffer = new byte[1024];	
		String receiveMsg = null;

		while (true)
		{

			InputStream dataStream;
			try {
				dataStream = client.getInputStream();				
				//得到数据长度

				bufferLength = dataStream.read(buffer);
				if (bufferLength == -1) break;
				if(buffer[0]==1){
					receiveMsg = new String(buffer, 1,buffer.length-1,"UTF8");
					String receiveMsg2 = receiveMsg.replace("\0", "");   //去掉接收到的字符后面的null
					String[] strArray = receiveMsg2.split("%");			//用%作为分隔符

					this.foodTY = strArray[0];
					this.foodName = strArray[1];
					this.foodPrice= strArray[2];
					i =true;
					System.out.println(foodName);					
				}
				if(buffer[0]==2){
					receiveMsg = new String(buffer, 1,buffer.length-1,"UTF8");
					String receiveMsg2 = receiveMsg.replace("\0", "");   //去掉接收
					j =true;
				}
				//	else{System.out.println("fialdata");}
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
			}


			//	foods(foodTY,foodName,foodPrice);  //写入SQL food 表
		}

	}

	public List<String> foods(){

		List<String> list =new ArrayList<String>();
		if(foodTY!=null&&foodName!=null&&foodPrice!=null){
			list.add(foodTY);
			list.add(foodName);
			list.add(foodPrice);
			return list;
		}
		return null;
	}

}