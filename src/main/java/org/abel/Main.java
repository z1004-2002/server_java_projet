package org.abel;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.sql.*;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        DatagramSocket socket = SocketService.createServerSocket(3000);
        while(true){
            try{
                byte []tmp = new byte[2048];
                DatagramPacket packet = new DatagramPacket(tmp, tmp.length);
                socket.receive(packet);
                MonThread thread = new MonThread(socket,packet);
                thread.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}