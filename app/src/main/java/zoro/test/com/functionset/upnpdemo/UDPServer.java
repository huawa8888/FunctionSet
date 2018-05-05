package zoro.test.com.functionset.upnpdemo;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * @Author : Zoro.
 * @Date : 2018/3/8.
 * @Describe :
 */

public class UDPServer implements Runnable {
    private static final int PORT = 6000;
    private byte[] msg = new byte[2048];
    private boolean life = true;

    public UDPServer() {
    }

    public boolean isLife() {
        return life;
    }

    public void setLife(boolean life) {
        this.life = life;
    }

    @Override
    public void run() {
        DatagramSocket dSocket = null;
        DatagramPacket dPacket = new DatagramPacket(msg, msg.length);
        try {
            dSocket = new DatagramSocket(PORT);
            while (life) {
                try {
                    dSocket.receive(dPacket);
                    Log.d("tian msg sever received",
                            new String(dPacket.getData(), dPacket.getOffset(),
                                    dPacket.getLength())
                                    + "dPacket.getLength()="
                                    + dPacket.getLength());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    private void receiveFromUdpZjy() throws IOException, InterruptedException {
        //接收数据
        DatagramPacket packet_Receive;
//        receive = new MulticastSocket(9999);
//        address_Receive = InetAddress.getByName("224.0.0.1");
//        receive.joinGroup(address_Receive);
        byte[] rev = new byte[512];
        packet_Receive = new DatagramPacket(rev, rev.length);
        for (int i = 0; i < 15; i++) {
            Thread.sleep(2000);
//            receive.receive(packet_Receive);
            JSONObject jsonObject1 = JSONObject.parseObject(new String(packet_Receive.getData(), 0, packet_Receive.getLength()));
            String type = jsonObject1.getString("type");
            String mac = jsonObject1.getString("mac");
            String uuid = jsonObject1.getString("uuid");
            Message message = Message.obtain();
            Bundle bundle = new Bundle();
            bundle.putString("type", type);
            bundle.putString("mac", mac);
            bundle.putString("uuid", uuid);
            message.setData(bundle);
        }
    }

    private void sendToUdpZjy() throws IOException, InterruptedException {
//        send = new MulticastSocket(8888);
//        address_Send = InetAddress.getByName("224.0.0.1");
//        send.joinGroup(address_Send);
        DatagramPacket packet_Send;
        //发送数据包
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", "send");
        jsonObject.put("service", "deviceSearch");
        byte[] buf = jsonObject.toJSONString().getBytes();
//        packet_Send = new DatagramPacket(buf, buf.length, address_Send, 8888);
        for (int i = 0; i < 15; i++) {
            Thread.sleep(2000);
//            send.send(packet_Send);
        }
    }
}
