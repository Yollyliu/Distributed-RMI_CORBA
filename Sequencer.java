import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;





public class Sequencer {
	
	
	
	
	public static  int SeqNumber=1;
//	private ArrayList<String> HostAddr = new ArrayList<>();     //store hostAddress
//	public static  HashMap<Integer, String> requestMap = new HashMap<>();
	
	public static RequestBuffer requestBuffer;            //put request to request buffer
	public static Queue<Request> requestQue=new LinkedList<>();
	
	 public static void getRequest(FE fe) {
			
	    	Request request=new  Request();
//	    	request.ipAdress=fe.ipAdress;         //assume that work
	    	request.ipAdress="";
	    	request.portNumber=fe.portNumber;
	    	request.SeqNumber=SeqNumber;
	    	request.message=fe.message;
	    	requestQue.add(request);
	    	requestBuffer.PutRequest(SeqNumber, request);
	    	request.getClass();
	    	SeqNumber++;
	    	
		}
	
   
	public static void main(String[] args) throws Exception {
		Lock lock = new ReentrantLock();	
		
		
		int outerPort = 1314;
		DatagramSocket socket = new DatagramSocket(outerPort);
		System.out.println("Sequencer is running!");
		System.out.println();
		
		while(true){
			UDPConnection udp = new UDPConnection();
			DatagramPacket packet = udp.ReceivePacket(socket);
			
			if(packet == null)
				continue;
			
			String receivedata = new String(packet.getData(), 0, packet.getLength());
			FE afe=new FE();
			afe.message=receivedata;
			afe.ipAdress=packet.getAddress();
			afe.portNumber=packet.getPort();
			getRequest(afe);
			sendRM(requestQue);
			
			
			
			
		}
	}
			

	
	public static void sendRM(Queue<Request> requestQue)  throws SocketException, InterruptedException {
		
		Request request1=new Request();
		request1.ipAdress="132.155.99.06";
		request1.portNumber=1556;
		request1.message="hello,world";
		request1.SeqNumber=5;
		requestQue.add(request1);
		
		
		Lock lock = new ReentrantLock();	{
		
		
		DatagramSocket aSocket = null;
		
		try {
			aSocket = new DatagramSocket();
			Request request=requestQue.poll();
			if(request!=null) {
				
			String message=String.valueOf(request.ipAdress)+"_"+String.valueOf(request.portNumber)+"_"
					+ String.valueOf(request.SeqNumber)+"_"+request.message;
					               
			byte[] m = message.getBytes();
			

			
			InetAddress aHost = InetAddress.getByName("230.1.1.5");

			DatagramPacket datagramPacket = new DatagramPacket(m, m.length, aHost, 1314);
			
			for(int i=0;i<3;i++) {
				aSocket.send(datagramPacket);
				System.out.println("sending "+i+" ");
				System.out.println(m);
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	}
	
	
	public static void resendRM(int strnumb)  throws SocketException, InterruptedException {
		
	    DatagramSocket aSocket = null;
		
		try {
			aSocket = new DatagramSocket();
			Request request=new Request();
		    request= requestBuffer.GetRequest(strnumb);
			if(request!=null) {
				
			String message=String.valueOf(request.ipAdress)+"_"+String.valueOf(request.portNumber)+"_"
					+ String.valueOf(request.SeqNumber)+"_"+request.message;
					               
			byte[] m = message.getBytes();
			

			
			InetAddress aHost = InetAddress.getByName("230.1.1.5");

			DatagramPacket datagramPacket = new DatagramPacket(m, m.length, aHost, 1314);
			
			
				aSocket.send(datagramPacket);
				
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	
	

}

class Request{
	String ipAdress;
	int portNumber;
	int SeqNumber;
	String message;


}
class FE{
	InetAddress ipAdress;
	int portNumber;
	String message;
}


