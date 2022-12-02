import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Server {

    static ServerSocket SS;
    static List<Socket> sockets = new ArrayList<>();
    static int numCli = 0;
    static ReentrantLock lock = new ReentrantLock();


    public static void main (String[] args) throws IOException {

        SS = new ServerSocket(6666);


        while (true){
            Socket sCliente = SS.accept();

            lock.lock();

            sockets.add(sCliente);

            lock.unlock();

            Thread thread = new Thread(new Ovillo(sCliente, numCli));
            numCli++;
            thread.start();

        }


    }

}
