package org.abel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {
        Connection connection = Connexion.connect();
        try {
            assert connection != null;
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM region");
            while (rs.next()){
                System.out.println("\tid : " +rs.getInt(1));
                System.out.println("\tNom : " +rs.getString(2));
                System.out.println("===============================================");
            }
            connection.close();
        }catch (SQLException s){
            s.printStackTrace();
        }
    }
}