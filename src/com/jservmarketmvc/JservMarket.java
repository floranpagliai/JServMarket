package com.jservmarketmvc;

import com.jservmarketmvc.controler.Controler;
import com.jservmarketmvc.dao.DAOModels;
import com.jservmarketmvc.controler.Jclient;
import com.jservmarketmvc.view.Jview;

import java.net.ServerSocket;

/**
 * com.jservmarketmvc in JservMarket
 * Made by Floran Pagliai <floran.pagliai@gmail.com>
 * Started on 03/12/2013 at 23:01
 */

public class JservMarket {
    public static void main(String args[]) {
        JservMarket serveur = new JservMarket();
        DAOModels daoModels = new DAOModels();
        try {
            Integer port = 4243;
            ServerSocket ss = new ServerSocket(port.intValue());
            Jview jview = new Jview(daoModels);
            Controler controler =  new Controler(daoModels, jview);
            jview.printUsers.addActionListener(controler);
            jview.printCategories.addActionListener(controler);
            jview.printProducts.addActionListener(controler);
            while (true) {
                new Jclient(ss.accept(), daoModels);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
