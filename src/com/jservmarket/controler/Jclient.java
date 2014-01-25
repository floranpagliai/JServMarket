package com.jservmarket.controler;

import com.jservmarket.JservMarket;
import com.jservmarket.dao.DAOModels;
import com.jservmarket.model.CartModel;
import com.jservmarket.model.ProductsModel;
import com.jservmarket.model.UsersModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static com.jservmarket.MD5.md5;

public class Jclient implements Runnable {
    private Thread t_;
    private Socket socket_;
    private PrintWriter out_;
    private BufferedReader in_;
    private DAOModels daoModels_;
    private int userId_ = -1;
    private JservMarket server_;

    public Jclient(Socket s, DAOModels daoModels, JservMarket server) {
        this.socket_ = s;
        this.daoModels_ = daoModels;
        this.server_ = server;
        try {
            out_ = new PrintWriter(socket_.getOutputStream());
            in_ = new BufferedReader(new InputStreamReader(socket_.getInputStream()));
            this.server_.addUsersConnected();
        } catch (IOException e) {
            e.printStackTrace();
        }
        t_ = new Thread(this);
        t_.start();
    }

    public void run() {
        String message = "";
        String tokens[];
        try {
            char charCur[] = new char[1];
            //out_.print(">");
            //out_.flush();
            while (in_.read(charCur, 0, 1) != -1) {
                if (charCur[0] != '\u0000' && charCur[0] != '\n' && charCur[0] != '\r')
                    message += charCur[0];
                else if (!message.equalsIgnoreCase("")) {
                    tokens = message.split("[;]");
                    commands(tokens);
                    //out_.print(">");
                    out_.flush();
                    System.out.println(message);
                    message = "";
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                this.server_.delUsersConnected();
                this.server_.delUsersLogged(this.userId_);
                this.userId_ = -1;
                socket_.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void commands(String tokenCmds[]) {
        if (tokenCmds[0].equalsIgnoreCase("login") && tokenCmds.length == 3)
            out_.println(authenticateUser(tokenCmds[1], tokenCmds[2]));
        else if (tokenCmds[0].equalsIgnoreCase("register") && tokenCmds.length == 3)
            out_.println(registerUser(tokenCmds[1], tokenCmds[2]));
        else if (tokenCmds[0].equalsIgnoreCase("getproducts")) {
            for (int i = 1 ; i <= daoModels_.getProductDAO().countRow() ; i++) {
                if (daoModels_.getProductDAO().find(i).getId() != -1)
                    out_.print(daoModels_.getProductDAO().find(i).toString());
            }
            out_.print("\n");
        } else if (tokenCmds[0].equalsIgnoreCase("getcategories")) {
            for (int i = 1 ; i <= daoModels_.getCategoriesDAO().countRow() ; i++) {
                if (daoModels_.getCategoriesDAO().find(i).getId() != -1)
                    out_.println(daoModels_.getCategoriesDAO().find(i).toString());
            }
        } else if (tokenCmds[0].equalsIgnoreCase("addtocart") && tokenCmds.length == 2)
            out_.println(addToCart(tokenCmds[1]));
        else if (tokenCmds[0].equalsIgnoreCase("getcartcontent"))
            out_.println(getCartContent());
        else if (tokenCmds[0].equalsIgnoreCase("pay"))
            out_.println(pay());
        else if (tokenCmds[0].equalsIgnoreCase("logout")) {
            this.server_.delUsersLogged(this.userId_);
            this.userId_ = -1;
        } else
            out_.println("Cette commande n'existe pas ou est malformée.");
    }

    private String authenticateUser(String login, String password) {
        if (this.userId_ == -1) {
            if (daoModels_.getUsersDAO().findByKey("login", login).getPassword().equalsIgnoreCase(md5(password))) {
                if (!this.server_.getUsersLogged(daoModels_.getUsersDAO().findByKey("login", login).getId())) {
                    this.userId_ = daoModels_.getUsersDAO().findByKey("login", login).getId();
                    this.server_.addUsersLogged(this.userId_);
                    return "loginOk";
                } else
                    return "loginOtherLogged";
            } else
                return "loginError";
        } else
            return "loginLogged";
    }

    private String registerUser(String login, String password) {
        if (this.userId_ == -1) {
            if (!daoModels_.getUsersDAO().findByKey("login", login).getLogin().equalsIgnoreCase(login)) {
                this.daoModels_.getUsersDAO().create(new UsersModel(login, md5(password)));
                return "registerOk";
            } else
                return "registerError";
        } else
            return "loginLogged";
    }

    private String addToCart(String cmd) {
        CartModel cart;
        int idMax = daoModels_.getCartDAO().countRow();
        int id = -1;
        try {
            id = Integer.parseInt(cmd);
        } catch (NumberFormatException e) {
            return "Veuillez entrer un nombre.";
        }
        boolean updated = false;
        if (this.userId_ != -1) {
            if (daoModels_.getProductDAO().find(id).getId() != -1  && daoModels_.getProductDAO().find(id).getQuantities() > 0 ) {
                for (int i = 1 ; i <= idMax ; i++) {
                    if (daoModels_.getCartDAO().find(i).getUserId() == this.userId_ && daoModels_.getCartDAO().find(i).getProductId() == id) {
                        cart = daoModels_.getCartDAO().find(i);
                        cart.setQuantity(cart.getQuantity() + 1);
                        ProductsModel product = daoModels_.getProductDAO().find(id);
                        product.setQuantities(daoModels_.getProductDAO().find(id).getQuantities() - 1);
                        daoModels_.getProductDAO().update(product);
                        daoModels_.getCartDAO().update(cart);
                        updated = true;
                    }
                }
                if (!updated) {
                    ProductsModel product = daoModels_.getProductDAO().find(id);
                    product.setQuantities(daoModels_.getProductDAO().find(id).getQuantities() - 1);
                    daoModels_.getProductDAO().update(product);
                    daoModels_.getCartDAO().create(new CartModel(this.userId_, id, 1));
                }
                return "addtocartOk";
            } else
                return "addtocartError";
        } else
            return "Veuillez vous authentifier.";
    }

    private String getCartContent() {
        CartModel cart;
        String cartContent = "";
        int idMax = daoModels_.getCartDAO().countRow();
        float sum = 0;
        if (this.userId_ != -1) {
            for (int i = 1 ; i <= idMax ; i++) {
                if (daoModels_.getCartDAO().find(i).getUserId() == this.userId_) {
                    cart = daoModels_.getCartDAO().find(i);
                    cartContent += cart.toString();//Recupération de l'affichage du produit
                    sum += cart.getPrice() * cart.getQuantity();//Ajout au total du prix du produit x la qte dans le panier
                }
            }
            if (cartContent == "")
                return "cartEmpty";
            else
                return cartContent;
        } else
            return "Veuillez vous authentifier.";
    }

    private String pay() {
        CartModel cart;
        int idMax = daoModels_.getCartDAO().countRow();
        int sum = 0;
        if (this.userId_ != -1) {
            for (int i = 1 ; i <= idMax ; i++) {
                if (daoModels_.getCartDAO().find(i).getUserId() == this.userId_)
                    sum++;
            }
            if (sum == 0)
                return "cartEmpty";
            for (int i = 1 ; i <= idMax ; i++) {
                if (daoModels_.getCartDAO().find(i).getUserId() == this.userId_) {
                    cart = daoModels_.getCartDAO().find(i);
                    //ProductsModel product = daoModels_.getProductDAO().find(cart.getProductId());
                    //product.setQuantities(product.getQuantities() - cart.getQuantity());//Soustraction dans le stock de la qte du panier
                    //daoModels_.getProductDAO().update(product);//Mise a jour du produit dans la BD
                    daoModels_.getCartDAO().delete(cart);//Suppression du produit dans le panier
                }
            }
            return "Merci de votre achat.";
        } else
            return "Veuillez vous authentifier.";
    }

}
