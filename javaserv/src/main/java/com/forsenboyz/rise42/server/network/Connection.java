package com.forsenboyz.rise42.server.network;

import com.sun.istack.internal.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

public class Connection {

    private final int NUMBER;

    private Server server;

    private Socket socket;

    private OutputStreamWriter outputWriter;
    private InputStream inputStream;

    Connection(int number, Socket socket, Server server) {
        this.NUMBER = number;
        this.server = server;
        this.socket = socket;

        try {
            this.outputWriter = new OutputStreamWriter(socket.getOutputStream());
            this.inputStream = socket.getInputStream();
            System.out.println("connection "+NUMBER+" streams opened");

            startInputThread(socket);
            System.out.println("connection "+NUMBER+" input thread started");

            sendInitialMessage();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        System.out.println("Connection "+NUMBER+" created");
    }

    void sendMessage(String outcomeMessage){
        try{
            System.out.println("connection: "+NUMBER+" sending: "+outcomeMessage);
            outputWriter.write(outcomeMessage);
            outputWriter.flush();
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    private void startInputThread(Socket socket){
        Thread thread = new Thread(
                () -> {
                    while(socket.isConnected()){
                        String msg = readMessage();

                        if(msg != null){
                            server.processMessage(msg, this.NUMBER);
                        } else System.exit(0);

                    }
                }
        );
        //thread.setDaemon(true);
        thread.start();
    }

    @Nullable
    private String readMessage() {
        try {
            byte[] buffer = new byte[255];
            if (inputStream.read(buffer) > 0) {
                ArrayList<Character> list = new ArrayList<>();
                for (byte b : buffer) {
                    //System.out.println(b);
                    if (b == 0) {
                        break;
                    }
                    list.add((char) b);
                }
                return list.toString().replaceAll("[,\\s\\[\\]]", "");
            }
            return null;
        } catch (IOException e) {
            System.out.println("Server explosion");
            System.exit(0);
            return null;
        }
    }

    private void sendInitialMessage() throws IOException {
        outputWriter.write("{\"init\":"+NUMBER+"}");
        outputWriter.flush();
        System.out.println("connection "+NUMBER+" preset send");
    }

    //TODO: isn't correct
    boolean isConnected(){
        return this.socket.isConnected();
    }
}
