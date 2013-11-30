package com.jservMarket;

import javax.swing.*;
import java.awt.*;

/**
 * com.jservMarket in JservMarket
 * Made by Floran Pagliai <floran.pagliai@gmail.com>
 * Started on 30/11/2013 at 15:59
 */

public class Jview extends JFrame implements Runnable {
    private Thread t_;
    private Jserveur serveur_;
    JPanel pan;

    Jview(Jserveur serveur) {
        serveur_ = serveur;
        this.setTitle("JservMarket");
        this.setSize(900, 600);
        this.setLocationRelativeTo(null);

        pan = new JPanel();
        pan.setBackground(Color.LIGHT_GRAY);
        this.setContentPane(pan);
        this.setVisible(true);
        t_ = new Thread(this);
        t_.start();
    }

    public void run() {
        JLabel nbClients = new JLabel();
        JLabel nbClientsLogged = new JLabel();
        pan.add(nbClients);
        pan.add(nbClientsLogged);
        this.setContentPane(pan);
        while(true) {
            nbClients.setText("Visiteur connectés : " + serveur_.getClients());
            nbClientsLogged.setText("Clients connectés : " + serveur_.getClientsLogged());
        }
    }

}
