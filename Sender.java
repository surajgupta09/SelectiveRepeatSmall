import java.io.*;
import java.net.*;
class Receive extends Thread{
    public void run(){
        DatagramSocket socket = null;
        DatagramPacket inPacket = null;
        byte[] inBuf;

        try{
            socket = new DatagramSocket(9999);
                while(true){
                    inBuf = new byte[2];
                    inPacket = new DatagramPacket(inBuf, inBuf.length);
                    socket.receive(inPacket);
                    
                    int nack = (int)inPacket.getData()[0];
                    int pack = (int)inPacket.getData()[1];
                    if(nack == 1){
                        System.out.println("reTransmitting packetNO"+pack);
                       try{

                        Send s1 = new Send(8888,pack-1,pack);
                        s1.start();
                       }catch(Exception e){
                           e.printStackTrace();
                       }
                    }
                }
        }catch(Exception e){
            e.printStackTrace();
        }


    }
}

class Send extends Thread{
    Send(int port,int start,int limit){
        this.port = port;
        this.limit=limit;
        this.start = start;
    }

    String [] frame = {"hello","world","bye","country"};
    int port;
    int limit;
    int start;
    public void run(){
        DatagramSocket socket = null;
        DatagramPacket outPacket = null;
        byte[] outBuf;  
            

        try{
            InetAddress sourceAddress = InetAddress.getLocalHost();
            socket = new DatagramSocket();
            while(true){            
                for(int i=start;i<limit;i++){
                    System.out.println("Sending Frame"+(i+1));
                    outBuf = new byte[1024];
                    outBuf=frame[i].getBytes();
                    outPacket = new DatagramPacket(outBuf,outBuf.length,sourceAddress,port);
                    socket.send(outPacket);
                }
                Thread.sleep(2000);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

class Sender {
    public static void main(String[] args) {
        try{Send s = new Send(8888,0,4);
        Receive r = new Receive();
        Thread.currentThread.sleep(5000);
        s.start();
        //s.join();
        r.start();
        //r.join();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
