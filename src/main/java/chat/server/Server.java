package chat.server;

import java.io.IOException;
import java.io.PrintStream;
import java.net.*;
import java.util.LinkedList;

public class Server {

    private ServerSocket mServer;

    private ClientManager mUsers;

    private PrintStream mOutput;

    private Thread mAcceptor;

    public Server(int port, int maxUsers) throws IOException {
        mServer = new ServerSocket(port, maxUsers * 2);
        mUsers = new ClientManager(maxUsers);

        mOutput = System.out;

        mAcceptor = new Thread(() -> {

            while(this.isRunning()) {
                this.accept();
            }

            this.stop();
        });
        
        mAcceptor.setName("Acceptor Thread");
    }

    public Server(int port) throws IOException {
        this(port, Default.kMaxUsers);
    }

    public Server() throws IOException {
        this(Default.kPort);
    }

    public void start() {
        mAcceptor.start();
    }

    public void stop() {
        if(this.isRunning()) {
            try {
                mUsers.close();
                mServer.close();
            } catch (IOException e) {
                
            }
        }
    }

    public boolean isRunning() {
        return !mServer.isClosed();
    }

    public void brodcast(String msg) {
        mOutput.println("[SERVER BRODCAST] " + msg);
        mUsers.broadcast(msg);
    }

    private void accept() {
        try {
            Client client = new Client(mServer.accept());
            brodcast("New Connection from " + client + " (" + mUsers.getUsers() + " / ?)");
            mUsers.add(client);
        } catch(Exception e) {
            mOutput.println("Ooops! Shits fucked");
        }
    }

    public void run() {
        mUsers.clean();
    }

    public static void main(String[] args) throws Exception {

        Server server = new Server();

        server.start();

        while(server.isRunning()) {
            server.run();

            Thread.sleep(1000);
        }
        
    }
}
