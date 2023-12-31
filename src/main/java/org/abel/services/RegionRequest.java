package org.abel.services;

import org.abel.Connexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class RegionRequest {
    public static void addRegion(String nom){
        Connection connection = Connexion.connect();
        try {
            assert connection != null;
            Statement stm = connection.createStatement();
            stm.executeUpdate("INSERT INTO region(nom) Values ('"+nom+"')");
            connection.close();
        }catch (SQLException s){
            s.printStackTrace();
        }
    }
    public static void deleteRegion(int id_region){
        Connection connection = Connexion.connect();
        try {
            assert connection != null;
            Statement stm = connection.createStatement();
            stm.executeUpdate("DELETE FROM region WHERE id_region = "+id_region);
            connection.close();
        }catch (SQLException s){
            s.printStackTrace();
        }
    }
    public static List<Map<String,Object>> getAllRegion(){
        Connection connection = Connexion.connect();
        try {
            assert connection != null;
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM region");

            List<Map<String,Object>> result = ServerService.resultSetToList(rs);
            connection.close();

            return result;
        }catch (SQLException s){
            s.printStackTrace();
        }
        return null;
    }
    public static void addRepresantant(int id_region,int id_parti, String nom_representant){
        Connection connection = Connexion.connect();
        try {
            assert connection != null;
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM represantant WHERE id_region="+id_region+" AND id_parti="+id_parti);
            int n=0;
            while (rs.next()) n++;
            if (n==0) {
                stm.executeUpdate("INSERT INTO represantant(id_region,id_parti,nom_representant) VALUES (" + id_region + ", " + id_parti + ", '" + nom_representant + "');");
            }else {
                stm.executeUpdate("UPDATE represantant SET nom_representant='" + nom_representant + "' WHERE id_region=" + id_region + " AND id_parti=" + id_parti);
            }
            connection.close();
        }catch (SQLException s){
            s.printStackTrace();
        }
    }
    public static void addElecteurs(){
        List<Map<String,Object>> regions = RegionRequest.getAllRegion();
        assert regions != null;
        Connection connection = Connexion.connect();
        try {
            assert connection != null;
            Statement stm = connection.createStatement();
            for (Map<String,Object> region:regions){
                int nombre = ServerService.RandomNumberGenerator(6000,10000);
                stm.executeUpdate("UPDATE region SET electeurs = "+nombre+" WHERE id_region = "+region.get("id_region"));
            }
            connection.close();
        }catch (SQLException s){
            s.printStackTrace();
        }
    }
    public static List<Map<String,Object>> selectVoteByRegion(Integer id_region){
        Connection connection = Connexion.connect();
        try {
            assert connection != null;
            PreparedStatement stm = connection.prepareStatement("SELECT * FROM represantant WHERE id_region=?");
            stm.setInt(1,id_region);
            ResultSet rs = stm.executeQuery();
            List<Map<String,Object>> result = ServerService.resultSetToList(rs);
            List<Map<String,Object>> data = new ArrayList<>();
            for (int i=0;i<result.size();i++){
                Map<String,Object> aux = result.get(i);
                Statement st = connection.createStatement();
                ResultSet r = st.executeQuery("SELECT nom FROM parti WHERE id_parti="+aux.get("id_parti"));
                String s = null;
                while (r.next()){
                    s = r.getString(1);
                }
                aux.put("nom_parti",s);
                result.set(i,aux);
            }
            connection.close();
            return result;
        }catch (SQLException s){
            s.printStackTrace();
        }
        return null;
    }
}
