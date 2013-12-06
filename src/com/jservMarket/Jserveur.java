package com.jservMarket;

import java.net.ServerSocket;
import java.util.Vector;

/**
 * com.jservMarket in JservMarket
 * Made by Floran Pagliai <floran.pagliai@gmail.com>
 * Started on 29/11/2013 at 15:05
 */

public class Jserveur {

    private Vector<Jclient> clients_ = new Vector();
    protected int nbClients_ = 0;

    public static void main(String args[]) {
        Jserveur serveur = new Jserveur();
        new Jview(serveur);
        try {
            Integer port = 4243;
            ServerSocket ss = new ServerSocket(port.intValue());
            while (true) {
                new Jclient(ss.accept(), serveur);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    synchronized public int addClient(Jclient client) {
        nbClients_++;
        clients_.addElement(client);
        return clients_.size() - 1;
    }

    synchronized public void delClient(int i) {
        nbClients_--;
        if (clients_.elementAt(i) != null) {
            clients_.removeElementAt(i);
        }
    }

    public int getClients() {
        return nbClients_;
    }

    public int getClientsLogged() {
        int sum = 0;
        for (int i = 0 ; i < nbClients_ ; i++) {
            if (clients_.get(i).authentified_)
                sum++;
            }
        return sum;
    }
}
