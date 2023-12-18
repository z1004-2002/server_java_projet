package org.abel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

public class PartiRequest {
    public static void addParti(String nom){
        Connection connection = Connexion.connect();
        try {
            assert connection != null;
            Statement stm = connection.createStatement();
            stm.executeUpdate("INSERT INTO parti(nom) Values ('"+nom+"')");

            connection.close();
        }catch (SQLException s){
            s.printStackTrace();
        }
    }
    public static void deleteParti(int id_parti){
        Connection connection = Connexion.connect();
        try {
            assert connection != null;
            Statement stm = connection.createStatement();
            stm.executeUpdate("DELETE FROM parti WHERE parti.id_parti = "+id_parti);
            connection.close();
        }catch (SQLException s){
            s.printStackTrace();
        }
    }
    public static List<Map<String,Object>> getAllParti(){
        Connection connection = Connexion.connect();
        try {
            assert connection != null;
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM parti");

            List<Map<String,Object>> result = ServerService.resultSetToList(rs);
            connection.close();

            return result;
        }catch (SQLException s){
            s.printStackTrace();
        }
        return null;
    }
}
