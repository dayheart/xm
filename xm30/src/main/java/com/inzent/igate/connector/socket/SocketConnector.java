package com.inzent.igate.connector.socket;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import com.dayheart.util.XLog;
import com.inzent.igate.adapter.AdapterParameter;
import com.inzent.igate.connector.IGateConnectorException;
import com.inzent.igate.connector.core.OutBoundConnector;
import com.inzent.igate.core.exception.IGateException;


public class SocketConnector extends OutBoundConnector {
	
	
	private String serverIP;
	private int port;
	
	private Socket socket;
	private OutputStream os = null;
	
	protected ThreadPoolExecutor executor;
	
	public SocketConnector() {
		executor = (ThreadPoolExecutor)Executors.newFixedThreadPool(1);		
				
	}

	@Override
	public void callService(AdapterParameter adapterParameter) throws IGateConnectorException {
		
		try {
			
			
			socket = new Socket();
			this.serverIP = (String)adapterParameter.get("serverIP");
			Integer portObj = (Integer)adapterParameter.get("port");
			this.port = portObj.intValue();
			socket.connect(new InetSocketAddress(serverIP, port));
			
			os = socket.getOutputStream();
			
			byte[] h_tcpgw ;
			int h_size = 2048;
			
			
			ByteBuffer sndBuf = ByteBuffer.allocate(Integer.BYTES);
			
			XLog.stdout(String.format("Integer.BYTES[%d]", Integer.BYTES));
			//sndBuf.clear();
			sndBuf.order(ByteOrder.LITTLE_ENDIAN);
			sndBuf.putInt(h_size);
			//sndBuf.put(new byte[4-h_tcpgw.length]);
			//sndBuf.order(ByteOrder.LITTLE_ENDIAN);
			
			h_tcpgw = sndBuf.array();
			
			XLog.stdout(String.format("SND_H_TCPGW[%s], len:%d", new String(h_tcpgw), h_tcpgw.length));
			os.write(h_tcpgw, 0, h_tcpgw.length);
			os.flush();
			
			
			/*
			byte [] header = new byte[4];
			int h_len = 2048;
			// Little Edian
			header[3] = (byte)(h_len >> 24);
			header[2] = (byte)(h_len >> 16);
			header[1] = (byte)(h_len >> 8);
			header[0] = (byte)(h_len);
			
			os.write(header, 0, header.length);
			os.flush();
			*/
			
			byte[] tlgrm = new byte[2048];
			byte white_space = (byte)32;
			Arrays.fill(tlgrm, white_space);
			
			byte[] sys_header = (byte[])adapterParameter.getRequestData();
			if(sys_header!=null)  {
				if(tlgrm.length > sys_header.length) {
					System.arraycopy(sys_header, 0, tlgrm, 0, sys_header.length);
				} else {
					System.arraycopy(sys_header, 0, tlgrm, 0, tlgrm.length);
				}
				XLog.stdout(String.format("SND_BYTES[%s], len:%d", new String(tlgrm), sys_header.length));
				os.write(tlgrm, 0, tlgrm.length);
				os.flush();
				
				TcpgwReceiver receiver = new TcpgwReceiver(socket);
				//executor.execute(receiver);
				
				//receiver.join();
				
				executor.submit(receiver);
			}
			
		//} catch (IOException ioe) {
			// TODO Auto-generated catch block
			//ioe.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			
			stop();
			
			throw new IGateConnectorException();
		}
		
		
		//this.stop();
	}
	
	
	private void stop() {
		try {
			if(socket!=null) {
				os.close();
				socket.close();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	class TcpgwReceiver extends Thread {
		
		private Socket socket;
		
		public TcpgwReceiver(Socket socket) {
			this.socket = socket;
		}
		
		public void run() {
			
			OutputStream os = null;
			InputStream is = null;
			BufferedInputStream bis = null;
			try {
				os = socket.getOutputStream();
				is = socket.getInputStream();
				bis = new BufferedInputStream(is);
				
				
				byte[] rcvBuf = new byte[1024];
				byte[] rcvMsg = new byte[8192];
				
				int read = -1;
				int j = 0;
				int totalBytes = 0;
				while(true) {
					read = bis.read(rcvBuf, 0, rcvBuf.length);
					System.out.printf("READ[%d]..:%d\n", j++, read);
					
					System.arraycopy(rcvBuf, 0, rcvMsg, totalBytes, read);
					totalBytes += read;
					
					if(read < rcvBuf.length) {
						break;
					}
					
				}
				
				XLog.stdout(String.format("RCV_BYTES [%s], len:%d\n", new String(rcvMsg), totalBytes));
				
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				
				try { os.close(); } catch (Exception e) {
					e.printStackTrace();
				}
				try { bis.close(); } catch (Exception e) {
					e.printStackTrace();
				}
				
				try { socket.close(); } catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}
	}
	
}
