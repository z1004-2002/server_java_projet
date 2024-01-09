package org.abel.services;

import org.abel.Connexion;

import java.io.*;
import java.sql.*;
import java.util.*;

public class ServerService {
    public static void dropDatabase(){
        Connection connection = Connexion.connect();
        try {
            assert connection != null;
            Statement stm = connection.createStatement();
            stm.executeUpdate("DELETE FROM represantant");
            stm.executeUpdate("DELETE FROM parti");
            stm.executeUpdate("DELETE FROM region");
            connection.close();
        }catch (SQLException s){
            s.printStackTrace();
        }
    }
    public static void makeVote()   {
        Connection connection = Connexion.connect();
        List<Map<String, Object>> regions =  RegionRequest.getAllRegion();
        assert regions != null;
        assert connection != null;
        for (Map<String, Object> region:regions){
            int electeurs = (Integer) region.get("electeurs");
            int votants;
            if (RandomNumberGenerator(0,1)==0){
                votants = RandomNumberGenerator((electeurs/4),electeurs);
            }else {
                votants = electeurs;
            }
            try {
                Statement stm = connection.createStatement();
                stm.executeUpdate("UPDATE region SET votants = "+votants+" WHERE id_region = "+region.get("id_region"));
            }catch (SQLException s){
                s.printStackTrace();
            }
            List<Map<String,Object>> partis = PartiRequest.getAllParti();
            assert partis != null;
            List<Integer> votes = generateRandomSum(partis.size(), votants);
            int x = RandomNumberGenerator(0,1);
            if (x == 0 && votants == electeurs){
                try {
                    Statement stm = connection.createStatement();
                    stm.executeUpdate("UPDATE represantant SET vote = 0 WHERE id_region = "+region.get("id_region"));
                    x = RandomNumberGenerator(1, partis.size());
                    stm.executeUpdate("UPDATE represantant SET vote = "+votants+" WHERE id_region = "+region.get("id_region")+" AND id_parti = "+partis.get(x-1).get("id_parti"));
                }catch (SQLException s){
                    s.printStackTrace();
                }
            }else {
                for(int i=0;i<partis.size();i++){
                    try {
                        Statement stm = connection.createStatement();
                        stm.executeUpdate("UPDATE represantant SET vote = "+votes.get(i)+" WHERE id_region = "+region.get("id_region")+" AND id_parti = "+partis.get(i).get("id_parti"));
                    }catch (SQLException s){
                        s.printStackTrace();
                    }
                }
            }
        }
        try {
            connection.close();
        }catch (SQLException s){
            s.printStackTrace();
        }
    }
    public static List<Map<String, Object>> resultSetToList(ResultSet resultSet) throws SQLException {

        List<Map<String, Object>> resultList = new ArrayList<>();

        ResultSetMetaData metaData = resultSet.getMetaData();

        int columnCount = metaData.getColumnCount();

        while (resultSet.next()) {
            Map<String, Object> row = new HashMap<>();

            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                Object value = resultSet.getObject(i);
                row.put(columnName, value);
            }
            resultList.add(row);
        }

        return resultList;
    }
    public static byte [] transformToByte(List<Map<String, Object>> result){
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(result);
            oos.flush();
            return bos.toByteArray();
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static byte [] transformToByte(Map<String, Object> result){
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(result);
            oos.flush();
            return bos.toByteArray();
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static int RandomNumberGenerator(int min,int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }

    public static List<Integer> generateRandomSum(Integer p, Integer n) {
        List<Integer> randomNumbers = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < p - 1; i++) {
            int remainingNumbers = p - i - 1;
            int maxRandomNumber = n - sumList(randomNumbers) - remainingNumbers;
            int randomNumber = random.nextInt(maxRandomNumber + 1);
            randomNumbers.add(randomNumber);
        }
        randomNumbers.add(n - sumList(randomNumbers));

        return randomNumbers;
    }

    private static int sumList(List<Integer> list) {
        int sum = 0;
        for (int num : list) {
            sum += num;
        }
        return sum;
    }

    public static Map<String,Object> transformToMap(byte [] tmp)  {
        try{
            ByteArrayInputStream bis = new ByteArrayInputStream(tmp);
            ObjectInputStream ois = new ObjectInputStream(bis);
            return  (Map<String, Object>) ois.readObject();
        }catch (IOException| ClassNotFoundException e){
            e.printStackTrace();
        }
        return null;
    }
}
