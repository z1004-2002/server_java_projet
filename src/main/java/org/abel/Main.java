package org.abel;

import org.abel.services.SocketService;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Main {
    public static void main(String[] args) {
        DatagramSocket socket = SocketService.createServerSocket(3000);
        System.out.println("Votre serveur a démaré sans problème 🎉!");
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