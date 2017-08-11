package com.food.lmln.food.utils;
		import java.io.BufferedWriter;
		import java.io.IOException;
		import java.io.InputStream;
		import java.io.OutputStreamWriter;
		import java.io.PrintWriter;
		import java.net.Socket;
		import java.util.ArrayList;
		import java.util.List;
		import org.json.JSONObject;
		import android.app.Activity;

public class socket_client  extends Activity{
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

	public void again_connect(final String ip)
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

	/**
     * 判断连接
     * @return
     */
    public  int  sendTest(){
        int a=0;
        if(writer != null){
            a=1;
        }else{
            a=0;
        }
        return a;
    }


	/**
	 * 判断是否断开连接，断开返回true,没有返回false
	 * @param
	 * @return
	 */
	public Boolean isServerClose(){
		try{
			client.sendUrgentData(0xFF);//发送1个字节的紧急数据，默认情况下，服务器端没有开启紧急数据处理，不影响正常通信
			return false;
		}catch(Exception se){
			return true;
		}
	}

	/**
	 * 发送打印信息
	 * @param obj
	 */
	public void sendfood(JSONObject obj){
		try {
			writer.write(String.valueOf(obj));   // 下单
			writer.flush();  //强制发送			
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			System.out.println("主机信息为空，请补充后再试试");
		}
	}


	/**
	 * 发送数据，发送失败返回false,发送成功返回true
	 * @param
	 * @param
	 * @return
	 */
	public Boolean SendJson(JSONObject obj){
		try{
			PrintWriter out = new PrintWriter(client.getOutputStream(), true);
			out.println(obj);
			return true;
		}catch(Exception se){
			se.printStackTrace();
			return false;
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
