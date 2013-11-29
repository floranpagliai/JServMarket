package com.jserveur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;


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
            test();
        }
    }

    public void test() {
        try {
        Statement state = JconnexionSQL.getInstance().createStatement();


        //L'objet ResultSet contient le résultat de la requête SQL
        ResultSet result = state.executeQuery("SELECT * FROM users");
        //On récupère les MetaData
        ResultSetMetaData resultMeta = result.getMetaData();

        System.out.println("\n**********************************");
        //On affiche le nom des colonnes
        for (int i = 1 ; i <= resultMeta.getColumnCount() ; i++)
            System.out.print("\t" + resultMeta.getColumnName(i).toUpperCase() + "\t *");

        System.out.println("\n**********************************");

        while (result.next()) {
            for (int i = 1 ; i <= resultMeta.getColumnCount() ; i++)
                System.out.print("\t" + result.getObject(i).toString() + "\t |");

            System.out.println("\n---------------------------------");

        }

        result.close();
        state.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
