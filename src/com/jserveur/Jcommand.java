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
        if (command.equalsIgnoreCase("login")) {
            authentificateUser("floran", "pass");
        } else if (command.equalsIgnoreCase("register")) {
            insertUser("test", "pass");
        } else if (command.equalsIgnoreCase("getproducts") && authentified_) {
            test("SELECT * from products");
        } else if (command.equalsIgnoreCase("getcategories") && authentified_) {
            test("SELECT * from categories");
        } else if (!authentified_) {
            out_.println("Veuillez vous authentifier.");
            out_.flush();
        } else {
            out_.println("Cette commande n'existe pas.");
            out_.flush();
        }
    }

    public void insertUser(String login, String pass) {
        try {
            Statement state = JconnexionSQL.getInstance().createStatement();
            ResultSet result = state.executeQuery("SELECT login FROM users WHERE login='" + login + "'");
            result.last();
            if (result.getRow() == 0)
                state.executeUpdate("INSERT INTO users(login, password) VALUES('" + login + "', md5('" + pass + "'))");
            else {
                out_.println("Un utilisateur utilise déjà ce pseudo.");
                out_.flush();
            }
            state.close();
            result.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void authentificateUser(String login, String pass) {
        try {
            Statement state = JconnexionSQL.getInstance().createStatement();
            ResultSet result = state.executeQuery("SELECT id, login FROM users WHERE login='" + login + "' AND password=md5('" + pass + "')");
            result.last();
            if (result.getRow() != 0) {
                authentified_ = true;
                while (result.next())
                    System.out.println(result.getInt("id"));
            }
            else {
                out_.println("Utilisateur inconu ou mot de passe erroné.");
                out_.flush();
            }
            state.close();
            result.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
