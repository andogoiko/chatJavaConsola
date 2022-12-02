import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ConcurrentModificationException;

public class Ovillo implements Runnable{
    Socket shocko;
    int numCli;
    boolean deleteOld = false;

    public Ovillo(Socket shocko, int numCli){
        this.shocko = shocko;
        this.numCli = numCli;
    }

    @Override
    public void run() {

        BufferedReader bR = null;
        try {
            bR = new BufferedReader(new InputStreamReader(shocko.getInputStream()));

            String queDice;

            while ((queDice = bR.readLine()) != null){


                System.out.printf("Lo que cuenta el cliente Nª%d: %s\n", numCli, queDice);

                String finalQueDice = queDice;

                Server.lock.lock();

                try{

                    Server.sockets.forEach(socket -> {
                        try {
                            if(socket.isConnected()){
                                BufferedWriter bufferedWriterSock = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                                bufferedWriterSock.write("Lo que me cuenta el cliente Nª" + numCli + ": " + finalQueDice + "\n");
                                bufferedWriterSock.newLine();
                                bufferedWriterSock.flush();
                            }

                        }catch(SocketException e) {

                            deleteOld = true;

                            System.out.println("casca");

                            e.printStackTrace();

                        } catch (IOException e) {

                            e.printStackTrace();

                        }
                    });

                    if(deleteOld){
                        deleteOld = false;
                        //Server.sockets.removeIf(s -> s == shocko);
                    }

                }catch (ConcurrentModificationException y){
                    y.printStackTrace();
                }

                Server.lock.unlock();
            }
        }catch(SocketException e) {

            Server.lock.lock();

            //Server.sockets.removeIf(s -> s == shocko);

            Server.lock.unlock();

        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}
