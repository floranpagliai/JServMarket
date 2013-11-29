package com.jserveur;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.util.Vector;

public class Jserveur {

    private Vector  clients_ = new Vector();
    private int     nbClients_ = 0;

    public static void main(String args[]) {
        Jserveur serveur = new Jserveur();
        try {
            Integer port = 4242;

            //new Commandes(blablaServ); // lance le thread de gestion des commandes

            ServerSocket ss = new ServerSocket(port.intValue());
            while (true) {
                new Jclient(ss.accept(), serveur);
            }
        }
        catch (Exception e) { }
    }

    synchronized public int addClient(PrintWriter out) {
        nbClients_++; // un client en plus ! ouaaaih
        clients_.addElement(out); // on ajoute le nouveau flux de sortie au tableau
        return clients_.size()-1; // on retourne le numéro du client ajouté (size-1)
    }

    synchronized public void delClient(int i) {
        nbClients_--;
        if (clients_.elementAt(i) != null) {
            clients_.removeElementAt(i);
        }
    }
}
