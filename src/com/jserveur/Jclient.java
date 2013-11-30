package com.jserveur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * com.jserveur in Jserveur
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

    Jclient(Socket s, Jserveur serveur) {
        socket_ = s;
        serveur_ = serveur;
        try {
            out_ = new PrintWriter(socket_.getOutputStream());
            in_ = new BufferedReader(new InputStreamReader(socket_.getInputStream()));
            jcommand_ = new Jcommand(out_);
            numClient_ = serveur_.addClient(out_);
        } catch (IOException e) {
            e.printStackTrace();
        }
        t_ = new Thread(this);
        t_.start();
    }

    public void run() {
        String message = "";

        System.out.println("Un nouveau client s'est connecte, no " + numClient_);
        try {
            char charCur[] = new char[1];
            while (in_.read(charCur, 0, 1) != -1) {
                if (charCur[0] != '\u0000' && charCur[0] != '\n' && charCur[0] != '\r')
                    message += charCur[0];
                else if (!message.equalsIgnoreCase("")) {
                    System.out.println(message);
                    jcommand_.exec(message);
                    message = "";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                System.out.println("Le client no " + numClient_ + " s'est deconnecte");
                serveur_.delClient(numClient_);
                socket_.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
