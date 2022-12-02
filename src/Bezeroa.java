import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Bezeroa {

    static Socket sock;

    public static void main(String[] args) throws IOException {

        sock = new Socket("localhost", 6666);

        Thread hilo = new Thread(new Runnable() {
            @Override
            public void run() {
                Scanner scanner = new Scanner(System.in);

                String mensaje;

                OutputStream outputStream;

                BufferedWriter writer;

                boolean vacio;

                while (true){
                    try {
                        mensaje = scanner.nextLine();
                        vacio = true;

                        outputStream = sock.getOutputStream();
                        writer = new BufferedWriter(new OutputStreamWriter(outputStream));

                        for(int i = 0; i < mensaje.length(); i++){
                            if(mensaje.charAt(i) != ' '){
                                vacio = false;
                            }
                        }

                        if (!vacio){
                            writer.write(mensaje);
                            writer.newLine();
                            writer.flush();
                        }

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        hilo.start();

        InputStream inputStream = sock.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String str;

        while ((str = reader.readLine()) != null){
            System.out.println(str);
        }

    }

}
