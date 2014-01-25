package com.jservmarket;

import com.jservmarket.controler.Controler;
import com.jservmarket.dao.DAOModels;
import com.jservmarket.controler.Jclient;
import com.jservmarket.view.Jview;

import java.net.ServerSocket;
import java.util.Vector;

public class JservMarket {
    public Vector usersLogged = new Vector();
    private Integer usersConnected = 0;
    public static void main(String args[]) {
        JservMarket serveur = new JservMarket();

        DAOModels daoModels = new DAOModels();
        try {
            Integer port = 4242;
            ServerSocket ss = new ServerSocket(port.intValue());
            Jview jview = new Jview(daoModels);
            Controler controler =  new Controler(daoModels, jview, serveur);
            jview.printUsers.addActionListener(controler);
            jview.printCategories.addActionListener(controler);
            jview.printProducts.addActionListener(controler);
            while (true) {
                new Jclient(ss.accept(), daoModels, serveur);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addUsersConnected() {
        this.usersConnected++;
    }

    public void delUsersConnected() {
        this.usersConnected--;
    }

    public String getUsersConnected() {
        return this.usersConnected.toString();
    }

    public void addUsersLogged(int id) {
         this.usersLogged.addElement(id);
    }

    public void delUsersLogged(int id) {
        for (int i = 0; i <= this.usersLogged.size() ; i++) {
            if (id != -1 && this.usersLogged.elementAt(i).equals(id))
                this.usersLogged.removeElementAt(i);
        }
    }

    public boolean getUsersLogged(int id) {
        return  this.usersLogged.contains(id);
    }
}
