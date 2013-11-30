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
    private int clientId_;
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
            addToCart(tokens[1]);
        } else if (tokens[0].equalsIgnoreCase("getcartcontent") && authentified_) {
            getCartContent();
        } else if (tokens[0].equalsIgnoreCase("pay")) {

        } else if (tokens[0].equalsIgnoreCase("logout")) {
            authentified_ = false;
            out_.println("Aurevoir.");
        } else {
            out_.println("Cette commande n'existe pas ou est mal formé.");
            //out_.println("Veuillez vous authentifier.");

        }
        out_.flush();
    }

    private void insertUser(String login, String pass) {
        if (!authentified_) {
            try {
                Statement state = JconnexionSQL.getInstance().createStatement();
                ResultSet result = state.executeQuery("SELECT login FROM users WHERE login='" + login + "'");
                result.last();
                if (result.getRow() == 0) {
                    state.executeUpdate("INSERT INTO users(login, password) VALUES('" + login + "', md5('" + pass + "'))");
                    out_.println("Nouvel utilisateur enregistré.");
                } else
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

    private void authentificateUser(String login, String pass) {
        if (!authentified_) {
            try {
                Statement state = JconnexionSQL.getInstance().createStatement();
                ResultSet result = state.executeQuery("SELECT id, login FROM users WHERE login='" + login + "' AND password=md5('" + pass + "')");
                result.last();
                if (result.getRow() == 1) {
                    authentified_ = true;
                    clientId_ = result.getInt("id");
                    out_.println("Bonjour " + login + ".");
                } else
                    out_.println("Utilisateur inconu ou mot de passe erroné.");
                state.close();
                result.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else
            out_.println("Vous êtes déjà connecté.");
        out_.flush();
    }

    private void addToCart(String productId) {
        if (authentified_) {
            try {
                Statement state = JconnexionSQL.getInstance().createStatement();
                ResultSet result = state.executeQuery("SELECT id FROM products WHERE id='" + productId + "' AND quantities != 0");
                result.last();
                if (result.getRow() == 1) {
                    result = state.executeQuery("SELECT id, quantity FROM cart WHERE userid='" + clientId_ + "' AND productid='" + productId + "'");
                    result.last();
                    if (result.getRow() == 1) {
                        int id = result.getInt("id");
                        int qte = result.getInt("quantity") + 1;
                        state.executeUpdate("UPDATE cart SET quantity='" + qte + "' WHERE id='" + id + "'");
                    } else
                        state.executeUpdate("INSERT INTO cart(userid, productid, quantity) VALUES('" + clientId_ + "', '" + productId + "', '" + 1 + "')");
                    out_.println("Le produit a été ajouté a votre panier.");
                } else
                    out_.println("Le produit n'existe pas.");
                state.close();
                result.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else
            out_.println("Veuillez vous authentifier.");
        out_.flush();
    }

    private void getCartContent() {
        if (authentified_) {
            try {
                Statement state = JconnexionSQL.getInstance().createStatement();
                ResultSet result = state.executeQuery("SELECT designation, quantity, price FROM cart INNER JOIN products ON cart.productid = products.id  WHERE userid='" + clientId_ + "' ");
                result.last();
                Integer row;
                if ((row = result.getRow()) >= 1) {
                    result.beforeFirst();
                    Integer sum = 0;
                    while (result.next()) {
                        out_.println("------------------------------------------------------------------------------");
                        for (int i = 1 ; i < row ; i++) {
                            String product = result.getString("designation");
                            Integer price = result.getInt("price");
                            Integer qte = result.getInt("quantity");
                            sum += price * qte;
                            out_.println(product + "  -  Quantité : " + qte + "  x  "+ price + "€  = " + qte * price);
                        }
                        out_.println("------------------------------------------------------------------------------");
                    }
                    out_.println("TOTAL : " + sum + " €");
                } else {
                    out_.println("Le panier est vide.");
                    state.close();
                    result.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else
            out_.println("Veuillez vous authentifier.");
        out_.flush();

    }

    private void test(String query) {
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
