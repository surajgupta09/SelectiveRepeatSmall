import java.io.*;
import java.net.*;

class SendR extends Thread{
    static int flag = 2;
    public void run(){
        DatagramSocket socket = null;
        DatagramPacket outPacket = null;
        byte[] outBuf;
        

        try{
            InetAddress address = InetAddress.getLocalHost();
            socket = new DatagramSocket();
            
            while(true){
                for(int i=0;i<100;i++){
                    if(i==flag){
                        System.out.println("Sending nack packNO "+(i%4));
                        outBuf = new byte[]{1,(byte)(i%4)};
                        outPacket = new DatagramPacket(outBuf,outBuf.length,address,9999);
                        socket.send(outPacket);
                       
                        flag = (i+1)%4;
                        Thread.sleep(2000);
                    }
                    
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

class ReceiveR extends Thread{

    public static StringBuilder data(byte[] a) 
    { 
        if (a == null) 
            return null; 
        StringBuilder ret = new StringBuilder(); 
        int i = 0; 
        while (a[i] != 0) 
        { 
            ret.append((char) a[i]); 
            i++; 
        } 
        return ret; 
    } 

    String [] frame = new String[4];

    public void run(){
        DatagramSocket socket = null;
        DatagramPacket inPacket = null;
        byte[] inBuf;

        try{
            socket = new DatagramSocket(8888); 
            while(true){   
            for(int i=0;i<frame.length;i++){
                
                inBuf = new byte[1024];
                inPacket = new DatagramPacket(inBuf,0,inBuf.length);
                socket.receive(inPacket);
                System.out.println("receiving frameNo"+(i));
                System.out.println("recived data is : "+data(inBuf));
            }
        }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

class Receiver {
    public static void main(String[] args) {
        try{
        SendR s = new SendR();
        ReceiveR r = new ReceiveR();
        r.start();
        r.join();
         s.start();

        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
}
