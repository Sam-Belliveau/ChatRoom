package chat.server;

import java.util.LinkedList;

public class ClientManager {

    private final int mMaxUsers;
    private LinkedList<Client> mUsers;

    public ClientManager(int maxUsers) {
        mMaxUsers = maxUsers;

        mUsers = new LinkedList<Client>();
    }

    public int getUsers() {
        return mUsers.size();
    }

    public void add(Client client) {
        if(this.getUsers() < mMaxUsers) {
            mUsers.push(client);
            brodcast("New Connection from " + client + " (" + mUsers.getUsers() + " / ?)");
        } else {
            client.send("Chat Room is Full (" + this.getUsers() + " / " + mMaxUsers + ")!");
            client.close();
        }
    } 

    public void clean() {
        for(Client user : mUsers) {
            if(!user.isOpen()) {
                user.close();
                mUsers.remove(user);
            }
        }
    }

    public void broadcast(String msg) {
        for(Client user : mUsers) {
            user.send(msg);
        }
    }

    public void close() {
        while(!mUsers.isEmpty()) {
            Client user = mUsers.pop();
            user.send("Pools Closed!");
            user.close();
        }
    }

}