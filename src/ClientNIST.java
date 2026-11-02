import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;

public class ClientNIST {
    static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.out.println("Need 2 arguments: serverIP and serverPort");
            return;
        }
        byte[] buffer = new byte[4];
        InetAddress serverIP = InetAddress.getByName(args[0]);
        int serverPort = Integer.parseInt(args[1]);
        DatagramPacket requestPacket = new DatagramPacket(buffer, buffer.length, InetAddress.getByAddress(serverIP.getAddress()), serverPort);
        DatagramSocket clientSocket = new DatagramSocket();
        clientSocket.send(requestPacket);

        DatagramPacket serverReply = new DatagramPacket(new byte[1024], 1024);
        clientSocket.receive(serverReply);
        byte[] payload = Arrays.copyOf(serverReply.getData(), serverReply.getLength());

        ByteBuffer byteBuffer = ByteBuffer.wrap(payload);
        int value =  byteBuffer.getInt();

        long almostDate = Integer.toUnsignedLong(value);

        ZonedDateTime epoch = ZonedDateTime.of(1900, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC"));

        epoch = epoch.plusSeconds(almostDate);
        ZonedDateTime updatedEpoch = epoch.withZoneSameInstant(ZoneId.of("America/New_York"));
        System.out.println(updatedEpoch);

        clientSocket.close();
    }
}


// byte[4] -> 32-but int -> unsigned long -> Date & Time
//ByteBuffer.wrap()