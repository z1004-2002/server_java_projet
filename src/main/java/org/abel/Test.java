package org.abel;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

public class Test {
    public static void main(String[] args) throws UnknownHostException {
        InetAddress ad2 = InetAddress.getByName("localhost");
        byte[] a = {10,55,41, (byte)174};
        ad2 = InetAddress.getByAddress(a);
        System.out.println(ad2);
    }
}
