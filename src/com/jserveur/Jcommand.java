package com.jserveur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * com.jserveur in Jserveur
 * Made by Floran Pagliai <floran.pagliai@gmail.com>
 * Started on 29/11/2013 at 16:32
 */

public class Jcommand {
    private PrintWriter out_;
    BufferedReader in_;

    Jcommand(PrintWriter out) {
        out_ = out;
        in_ = new BufferedReader(new InputStreamReader(System.in));
    }

    public void exec(String command) {
                if (command.equalsIgnoreCase("login")) {
                    out_.println("veuillez vous identifier");
                    out_.flush();
                }
    }
}
