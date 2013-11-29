package com.jserveur;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.util.Vector;

/**
 * com.jserveur in Jserveur
 * Made by Floran Pagliai <floran.pagliai@gmail.com>
 * Started on 29/11/2013 at 15:05
 */

public class Jserveur {

    private Vector clients_ = new Vector();
    private int nbClients_ = 0;

    public static void main(String args[]) {
        Jserveur serveur = new Jserveur();
        try {
            Integer port = 4242;
            ServerSocket ss = new ServerSocket(port.intValue());
            while (true) {
                new Jclient(ss.accept(), serveur);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    synchronized public int addClient(PrintWriter out) {
        nbClients_++;
        clients_.addElement(out);
        return clients_.size() - 1;
    }

    synchronized public void delClient(int i) {
        nbClients_--;
        if (clients_.elementAt(i) != null) {
            clients_.removeElementAt(i);
        }
    }
}
