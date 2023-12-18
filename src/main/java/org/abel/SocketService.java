package org.abel;

import java.net.DatagramSocket;
import java.net.SocketException;

public class SocketService {
    public static DatagramSocket createServerSocket(int port){
        DatagramSocket socket = null;
        try{
            socket= new DatagramSocket(3000);
        }catch (SocketException e){
            e.printStackTrace();
        }
        return socket;
    }
}
