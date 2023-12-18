package org.abel;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.List;
import java.util.Map;

public class MonThread extends Thread{
    DatagramSocket socket;
    DatagramPacket packet;

    public MonThread(DatagramSocket socket, DatagramPacket packet) {
        this.socket = socket;
        this.packet = packet;
    }
    @Override
    public void run() {
        Map<String, Object> request = null;

        request = ServerService.transformToMap(packet.getData());
        assert request != null;

        if (request.get("name_request").equals("get_all_regions")){
            byte [] data = ServerService.transformToByte(RegionRequest.getAllRegion());
            packet.setData(data);
            packet.setLength(data.length);
            try {
                socket.send(packet);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else if (request.get("name_request").equals("get_all_partis")) {
            byte [] data = ServerService.transformToByte(PartiRequest.getAllParti());
            packet.setData(data);
            packet.setLength(data.length);
            try {
                socket.send(packet);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else if (request.get("name_request").equals("drop_data_base")) {
            ServerService.dropDatabase();
        }
        else if (request.get("name_request").equals("add_region")) {
            RegionRequest.addRegion((String) request.get("to_add"));
        }
        else if (request.get("name_request").equals("add_parti")) {
            PartiRequest.addParti((String) request.get("to_add"));
        }
        else if (request.get("name_request").equals("delete_parti")) {
            PartiRequest.deleteParti((Integer) request.get("to_delete"));
        }
        else if (request.get("name_request").equals("delete_region")) {
            RegionRequest.deleteRegion((Integer) request.get("to_delete"));
        }
        else if (request.get("name_request").equals("add_reprerentant")) {
            RegionRequest.addRepresantant(
                    (Integer) request.get("id_region"),
                    (Integer) request.get("id_parti"),
                    (String) request.get("represantant")
            );
        }
        else if (request.get("name_request").equals("add_votant")){
            RegionRequest.addElecteurs();
        }
        else if (request.get("name_request").equals("make_vote")){
            ServerService.makeVote();
        }
        else if (request.get("name_request").equals("resultat_region")){
            List<Map<String,Object>> result = RegionRequest.selectVoteByRegion((Integer) request.get("id_region"));
            byte [] data = ServerService.transformToByte(result);
            packet.setData(data);
            packet.setLength(data.length);
            try {
                socket.send(packet);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println(request.get("name_request"));
    }
}
