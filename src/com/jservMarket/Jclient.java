package com.jservMarket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * com.jservMarket in JservMarket
 * Made by Floran Pagliai <floran.pagliai@gmail.com>
 * Started on 29/11/2013 at 15:39
 */

class Jclient implements Runnable {
    private Thread t_;
    private Socket socket_;
    private PrintWriter out_;
    private BufferedReader in_;
    private Jserveur serveur_;
    private Jcommand jcommand_;
    private int numClient_ = 0;
    protected int clientId_;
    protected boolean authentified_ = false;

    Jclient(Socket s, Jserveur serveur) {
        socket_ = s;
        serveur_ = serveur;
        try {
            out_ = new PrintWriter(socket_.getOutputStream());
            in_ = new BufferedReader(new InputStreamReader(socket_.getInputStream()));
            jcommand_ = new Jcommand(out_, this);
            numClient_ = serveur_.addClient(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        t_ = new Thread(this);
        t_.start();
    }

    public void run() {
        String message = "";
        String tokens[];
        System.out.println("Client " + numClient_ + ": connexion");
        try {
            char charCur[] = new char[1];
            out_.print(">");
            out_.flush();
            while (in_.read(charCur, 0, 1) != -1) {
                if (charCur[0] != '\u0000' && charCur[0] != '\n' && charCur[0] != '\r')
                    message += charCur[0];
                else if (!message.equalsIgnoreCase("")) {
                    tokens = message.split("[;]");
                    System.out.println("Client " + numClient_ + ": " + tokens[0]);
                    commands(tokens);
                    out_.print(">");
                    out_.flush();
                    message = "";
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                System.out.println("Client " + numClient_ + ": deconnexion");
                serveur_.delClient(numClient_);
                socket_.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void commands(String tokenCmds[]) {
        if (tokenCmds[0].equalsIgnoreCase("login") && tokenCmds.length == 3) {
            out_.println(jcommand_.authentificateUser(tokenCmds[1], tokenCmds[2]));
        } else if (tokenCmds[0].equalsIgnoreCase("register") && tokenCmds.length == 3) {
            out_.println(jcommand_.insertUser(tokenCmds[1], tokenCmds[2]));
        } else if (tokenCmds[0].equalsIgnoreCase("getproducts")) {
            jcommand_.showTable("SELECT * from products");
        } else if (tokenCmds[0].equalsIgnoreCase("getcategories")) {
            jcommand_.showTable("SELECT * from categories");
        } else if (tokenCmds[0].equalsIgnoreCase("addtocart") && tokenCmds.length == 2) {
            jcommand_.addToCart(tokenCmds[1]);
        } else if (tokenCmds[0].equalsIgnoreCase("getcartcontent") && authentified_) {
            jcommand_.getCartContent();
        } else if (tokenCmds[0].equalsIgnoreCase("pay")) {
            jcommand_.pay();
        } else if (tokenCmds[0].equalsIgnoreCase("logout")) {
            authentified_ = false;
            out_.println("Aurevoir.");
        } else
            out_.println("Cette commande n'existe pas ou est mal formé.");
        out_.flush();
    }
}
