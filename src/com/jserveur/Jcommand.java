package com.jserveur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.*;

/**
 * com.jserveur in Jserveur
 * Made by Floran Pagliai <floran.pagliai@gmail.com>
 * Started on 29/11/2013 at 16:32
 */

public class Jcommand {
    private PrintWriter out_;
    private BufferedReader in_;
    private int idClient_;
    private boolean authentified_ = false;

    Jcommand(PrintWriter out) {
        out_ = out;
        in_ = new BufferedReader(new InputStreamReader(System.in));
    }

    public void exec(String command) {
        String tokens[] = command.split("[;]");
        if (tokens[0].equalsIgnoreCase("login") && tokens.length == 3) {
            authentificateUser(tokens[1], tokens[2]);
        } else if (tokens[0].equalsIgnoreCase("register") && tokens.length == 3) {
            insertUser(tokens[1], tokens[2]);
        } else if (tokens[0].equalsIgnoreCase("getproducts")) {
            test("SELECT * from products");
        } else if (tokens[0].equalsIgnoreCase("getcategories")) {
            test("SELECT * from categories");
        } else if (tokens[0].equalsIgnoreCase("addtocart") && tokens.length == 2) {

        } else if (tokens[0].equalsIgnoreCase("getcartcontent") && authentified_) {
            test("SELECT * from cart where userid='" + idClient_ + "'");
        } else if (tokens[0].equalsIgnoreCase("pay")) {

        } else {
            out_.println("Cette commande n'existe pas ou est mal formé.");
            //out_.println("Veuillez vous authentifier.");

        }
        out_.flush();
    }

    public void insertUser(String login, String pass) {
        if (!authentified_) {
            try {
                Statement state = JconnexionSQL.getInstance().createStatement();
                ResultSet result = state.executeQuery("SELECT login FROM users WHERE login='" + login + "'");
                result.last();
                if (result.getRow() == 0) {
                    state.executeUpdate("INSERT INTO users(login, password) VALUES('" + login + "', md5('" + pass + "'))");
                    out_.println("Nouvel utilisateur enregistré.");
                }
                else
                    out_.println("Un utilisateur utilise déjà ce pseudo.");
                state.close();
                result.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else
            out_.println("Vous êtes déjà connecté.");
        out_.flush();
    }

    public void authentificateUser(String login, String pass) {
        if (!authentified_) {
            try {
                Statement state = JconnexionSQL.getInstance().createStatement();
                ResultSet result = state.executeQuery("SELECT id, login FROM users WHERE login='" + login + "' AND password=md5('" + pass + "')");
                result.last();
                if (result.getRow() == 1) {
                    authentified_ = true;
                    idClient_ = result.getInt("id");
                    out_.println("Bonjour " + login + ".");
                } else {
                    out_.println("Utilisateur inconu ou mot de passe erroné.");
                    out_.flush();
                }
                state.close();
                result.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else
            out_.println("Vous êtes déjà connecté.");
        out_.flush();
    }

    public void test(String query) {
        try {
            Statement state = JconnexionSQL.getInstance().createStatement();

            //L'objet ResultSet contient le résultat de la requête SQL
            ResultSet result = state.executeQuery(query);
            //On récupère les MetaData
            ResultSetMetaData resultMeta = result.getMetaData();

            out_.println("**********************************");
            //On affiche le nom des colonnes
            for (int i = 1 ; i <= resultMeta.getColumnCount() ; i++)
                out_.print(resultMeta.getColumnName(i).toUpperCase() + " | ");
            out_.println("\n**********************************");

            while (result.next()) {
                for (int i = 1 ; i <= resultMeta.getColumnCount() ; i++) {
                    if (result.getObject(i) != null)
                        out_.print(result.getObject(i).toString() + " | ");
                }
                out_.println("\n---------------------------------");
            }
            out_.flush();
            result.close();
            state.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
