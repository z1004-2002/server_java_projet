package org.abel;

import java.sql.*;

public class Connexion {
    public static Connection connect(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Le pilote JDBC a été chargé avec succès.");
        } catch (ClassNotFoundException e) {
            System.err.println("Impossible de charger le pilote JDBC : " + e.getMessage());
        }
        try{
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/e_vote","admin_e_vote","admin123");
        }catch (SQLException s){
            s.printStackTrace();
        }
        return null;
    }
}