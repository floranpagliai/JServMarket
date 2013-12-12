package com.jservmarketmvc;

import com.jservmarketmvc.controler.Controler;
import com.jservmarketmvc.dao.DAOModels;
import com.jservmarketmvc.controler.Jclient;
import com.jservmarketmvc.view.Jview;
import com.sun.xml.internal.xsom.impl.scd.Iterators;

import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Vector;

/**
 * com.jservmarketmvc in JservMarket
 * Made by Floran Pagliai <floran.pagliai@gmail.com>
 * Started on 03/12/2013 at 23:01
 */

public class JservMarket {
    private Vector usersLogged = new Vector();
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
