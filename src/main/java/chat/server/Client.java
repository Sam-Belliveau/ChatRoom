package chat.server;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.*;

public class Client {

    private Socket mConnection;
    private PrintStream mSender;
    private String mName;

    public Client(Socket client) throws IOException {
        mConnection = client;
        mSender = new PrintStream(mConnection.getOutputStream());
        mName = new String[]{
            "Myles",
            "Drake",
            "Hitler",
            "Stalin",
            "Jesus",
            "God",
            "Timmy",
            "Caleb",
            "PooPoo Head",
            "Ass"
        }[((int) (Math.random() * 10))];
    }

    public void send(String msg) {
        if(mSender != null) {
            mSender.println(msg);
            mSender.flush();
        }
    }
    
    public void close() {
        try {
            mConnection.close();
        } catch (IOException e) {
            
        }
        
        mSender = null;
    }

    public boolean isOpen() {
        return !mConnection.isClosed() && mSender != null;
    }

    @Override
    public String toString() {
        return mName;
    }


}