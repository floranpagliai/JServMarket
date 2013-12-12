package com.jservMarket;

import java.io.PrintWriter;
import java.sql.*;

/**
 * com.jservMarket in JservMarket
 * Made by Floran Pagliai <floran.pagliai@gmail.com>
 * Started on 29/11/2013 at 16:32
 */

public class Jcommand {
    private PrintWriter out_;
    private Jclient client_;


    Jcommand(PrintWriter out, Jclient client) {
        out_ = out;
        client_ = client;
    }

    protected String insertUser(String login, String pass) {
        String message = "";
        if (!client_.authentified_) {
            try {
                Statement state = JconnexionSQL.getInstance().createStatement();
                ResultSet result = state.executeQuery("SELECT login FROM users WHERE login='" + login + "'");
                result.last();
                if (result.getRow() == 0) {
                    state.executeUpdate("INSERT INTO users(login, password) VALUES('" + login + "', md5('" + pass + "'))");
                    message = "Nouvel utilisateur enregistré.";
                } else
                    message = "Un utilisateur utilise déjà ce pseudo.";
                state.close();
                result.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else
            message = "Vous êtes déjà connecté.";
        return message;
    }

    protected String authentificateUser(String login, String pass) {
        String message = "";
        if (!client_.authentified_) {
            try {
                Statement state = JconnexionSQL.getInstance().createStatement();
                ResultSet result = state.executeQuery("SELECT id, login FROM users WHERE login='" + login + "' AND password=md5('" + pass + "')");
                result.last();
                if (result.getRow() == 1) {
                    client_.authentified_ = true;
                    client_.clientId_ = result.getInt("id");
                    message = "Bonjour " + login + ".";
                } else
                    message = "Utilisateur inconu ou mot de passe erroné.";
                state.close();
                result.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else
            message = "Vous êtes déjà connecté.";
        return message;
    }

    protected void addToCart(String productId) {
        if (client_.authentified_) {
            try {
                Statement state = JconnexionSQL.getInstance().createStatement();
                ResultSet result = state.executeQuery("SELECT id FROM products WHERE id='" + productId + "' AND quantities > 0");
                result.last();
                if (result.getRow() == 1) {
                    result = state.executeQuery("SELECT id, quantity FROM cart WHERE userid='" + client_.clientId_ + "' AND productid='" + productId + "'");
                    result.last();
                    if (result.getRow() == 1) {
                        int id = result.getInt("id");
                        int qte = result.getInt("quantity") + 1;
                        state.executeUpdate("UPDATE cart SET quantity='" + qte + "' WHERE id='" + id + "'");
                    } else
                        state.executeUpdate("INSERT INTO cart(userid, productid, quantity) VALUES('" + client_.clientId_ + "', '" + productId + "', '" + 1 + "')");
                    out_.println("Le produit a été ajouté a votre panier.");
                } else
                    out_.println("Le produit n'existe pas ou n'est plus en stock.");
                state.close();
                result.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else
            out_.println("Veuillez vous authentifier.");
        out_.flush();
    }

    protected void getCartContent() {
        if (client_.authentified_) {
            try {
                Statement state = JconnexionSQL.getInstance().createStatement();
                ResultSet result = state.executeQuery("SELECT designation, quantity, price FROM cart INNER JOIN products ON cart.productid = products.id  WHERE userid='" + client_.clientId_ + "' ");
                result.last();
                if (result.getRow() >= 1) {
                    result.beforeFirst();
                    Integer sum = 0;
                    out_.println("------------------------------------------------------------------------------");
                    while (result.next()) {
                        String product = result.getString("designation");
                        Integer price = result.getInt("price");
                        Integer qte = result.getInt("quantity");
                        sum += price * qte;
                        out_.println(product + "  -  Quantité : " + qte + "  x  " + price + "€  = " + qte * price + " €");
                        out_.println("------------------------------------------------------------------------------");
                    }
                    out_.println("TOTAL : " + sum + " €");
                } else
                    out_.println("Le panier est vide.");
                state.close();
                result.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else
            out_.println("Veuillez vous authentifier.");
        out_.flush();

    }

    protected void pay() {
        if (client_.authentified_) {
            try {
                Statement state = JconnexionSQL.getInstance().createStatement();
                Statement state2 = JconnexionSQL.getInstance().createStatement();
                ResultSet result = state.executeQuery("SELECT productid, quantities, quantity, price FROM cart INNER JOIN products ON cart.productid = products.id WHERE userid='" + client_.clientId_ + "' ");
                result.last();
                if (result.getRow() >= 1) {
                    result.beforeFirst();
                    Integer sum = 0;
                    while (result.next()) {
                        Integer productID = result.getInt("productid");
                        Integer price = result.getInt("price");
                        Integer cartQTE = result.getInt("quantity");
                        Integer productQTE = result.getInt("quantities");
                        state2.executeUpdate("UPDATE products SET quantities='" + (productQTE - cartQTE) + "' WHERE id='" + productID + "'");
                        sum += price * cartQTE;
                    }
                    state.executeUpdate("DELETE FROM cart WHERE userid='" + client_.clientId_ + "' ");
                    out_.println("Merci de votre achat : " + sum + " € payé.");

                } else
                    out_.println("Le panier est vide.");
                state.close();
                result.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else
            out_.println("Veuillez vous authentifier.");
        out_.flush();
    }

    protected void showTable(String query) {
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