import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

public class ClientNIST {
    static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.out.println("Need 2 arguments: serverIP and serverPort");
            return;
        }
        byte[] buffer = new byte[1024];
        InetAddress serverIP = InetAddress.getByName(args[0]);
        int serverPort = Integer.parseInt(args[1]);
        DatagramPacket requestPacket = new DatagramPacket(buffer, buffer.length, InetAddress.getByAddress(serverIP.getAddress()), serverPort);
        DatagramSocket clientSocket = new DatagramSocket();
        clientSocket.send(requestPacket);

        DatagramPacket serverReply = new DatagramPacket(new byte[1024], 1024);
        clientSocket.receive(serverReply);
        byte[] payload = Arrays.copyOf(serverReply.getData(), serverReply.getLength());

        String replyMessage = new String(payload);
        System.out.println("Server reply: " + replyMessage);
        clientSocket.close();
    }
}
