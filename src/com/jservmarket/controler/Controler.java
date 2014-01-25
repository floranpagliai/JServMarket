package com.jservmarket.controler;

import com.jservmarket.JservMarket;
import com.jservmarket.dao.DAOModels;
import com.jservmarket.view.Jview;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controler implements ActionListener {
    private DAOModels daoModels_;
    private Jview jview_;
    private JservMarket server_;

    public Controler(DAOModels daoModels, Jview jview, JservMarket server) {
        this.daoModels_ = daoModels;
        this.jview_ = jview;
        this.server_ = server;
        jview_.usersConnected.setText("Client(s) connecté(s) : " + server_.getUsersConnected()
                + " ("+server_.usersLogged.size()+" authentifié(s))");
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == jview_.printUsers)
            jview_.printUsers(getUsers());
        else if (e.getSource() == jview_.printCategories)
            jview_.printCategories(getCategories());
        else if (e.getSource() == jview_.printProducts)
            jview_.printProducts(getProducts());
        jview_.usersConnected.setText("Client(s) connecté(s) : " + server_.getUsersConnected()
                + " ("+server_.usersLogged.size()+" authentifié(s))");
    }

    private Object[][] getUsers() {
        Object table[][] = new Object[daoModels_.getUsersDAO().nbRow()][3];
        int idMax = daoModels_.getUsersDAO().countRow();
        int i = 0;
        for (int id = 1 ; id <= idMax; id++) {
            if (!daoModels_.getUsersDAO().find(id).isNull()) {
                table[i][0] =  daoModels_.getUsersDAO().find(id).getId();
                table[i][1] = daoModels_.getUsersDAO().find(id).getLogin();
                if (this.server_.getUsersLogged(daoModels_.getUsersDAO().find(id).getId()))
                    table[i][2] = "Yes";
                else
                    table[i][2] = "No";
                i++;
            }
        }
        return table;
    }

    private Object[][] getCategories() {
        Object table[][] = new Object[daoModels_.getCategoriesDAO().nbRow()][2];
        int idMax = daoModels_.getCategoriesDAO().countRow();
        int i = 0;
        for (int id = 1 ; id <= idMax; id++) {
            if (daoModels_.getCategoriesDAO().find(id).getLabel() != "") {
                table[i][0] =  daoModels_.getCategoriesDAO().find(id).getId();
                table[i][1] = daoModels_.getCategoriesDAO().find(id).getLabel();
                i++;
            }
        }
        return table;
    }

    private Object[][] getProducts() {
        Object table[][] = new Object[daoModels_.getProductDAO().nbRow()][5];
        int idMax = daoModels_.getProductDAO().countRow();
        int i = 0;
        for (int id = 1 ; id <= idMax; id++) {
            if (daoModels_.getProductDAO().find(id).getDesignation() != "") {
                table[i][0] =  daoModels_.getProductDAO().find(id).getId();
                table[i][1] = daoModels_.getProductDAO().find(id).getDesignation();
                table[i][2] = daoModels_.getProductDAO().find(id).getDescription();
                table[i][3] = daoModels_.getProductDAO().find(id).getPrice() + " €";
                table[i][4] = daoModels_.getProductDAO().find(id).getQuantities();
                i++;
            }
        }
        return table;
    }
}
