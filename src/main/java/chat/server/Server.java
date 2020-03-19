package chat.server;

import java.io.IOException;
import java.io.PrintStream;
import java.net.*;
import java.util.LinkedList;

public class Server {

    private final int mMaxUsers;

    private ServerSocket mServer;
    private LinkedList<Client> mUsers;

    private PrintStream mOutput;

    private Thread mAcceptor;

    public Server(int port, int maxUsers) throws IOException {
        mMaxUsers = maxUsers;
        mServer = new ServerSocket(port, mMaxUsers * 2);
        mUsers = new LinkedList<Client>();

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
                while(!mUsers.isEmpty()) {
                    Client user = mUsers.pop();
                    user.close();
                }

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
        for(Client user : mUsers) {
            user.send(msg);
        }
    }

    private void accept() {
        try {
            Client client = new Client(mServer.accept());

            int current = mUsers.size();
    
            if(current < mMaxUsers) {
                brodcast("New Connection from " + client);
                mUsers.push(client);
            } else {
                client.send("Chat Room is Full (" + current + " / " + mMaxUsers + ")!");
                client.close();
            }
        } catch(Exception e) {
            mOutput.println("Ooops! Shits fucked");
        }
    }

    public void run() {
        brodcast("hello bitch \n");
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
