package com.jservmarketmvc.view;

import com.jservmarketmvc.dao.DAOModels;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * com.jservmarketmvc.view in JservMarket
 * Made by Floran Pagliai <floran.pagliai@gmail.com>
 * Started on 10/12/2013 at 02:32
 */

public class Jview extends JFrame implements Observer {
    private DAOModels daoModels_;
    public JButton printUsers = new JButton("Utillisateurs");
    public JButton printProducts = new JButton("Produits");
    public JButton printCategories = new JButton("Categories");
    JScrollPane scrollPan = new JScrollPane(new JTable());
    JPanel btns = new JPanel();
    public JTextArea console= new JTextArea();

    public Jview(DAOModels daoModels) {
        this.daoModels_ = daoModels;


        this.setTitle("JservMarket");
        this.setSize(900, 600);
        this.setLocationRelativeTo(null);

        this.getContentPane().setBackground(Color.LIGHT_GRAY);

        this.setLayout(new BorderLayout());
        btns.add(printUsers, BorderLayout.WEST);
        btns.add(printCategories, BorderLayout.CENTER);
        btns.add(printProducts, BorderLayout.EAST);
        this.getContentPane().add(btns, BorderLayout.NORTH);

        this.getContentPane().add(scrollPan, BorderLayout.CENTER);
        this.getContentPane().add(console, BorderLayout.SOUTH);
        this.setVisible(true);
    }

    public void update(Observable o, Object arg) {
    }

    public void printUsers(Object[][] table) {
        String  title[] = {"Id", "Login"};
        this.remove(scrollPan);
        scrollPan = new JScrollPane(new JTable(table, title));
        this.getContentPane().add(scrollPan);
        this.validate();

    }

    public void printCategories(Object[][] table) {
        String  title[] = {"Id", "Catégorie"};
        this.remove(scrollPan);
        scrollPan = new JScrollPane(new JTable(table, title));
        this.getContentPane().add(scrollPan);
        this.validate();
    }

    public void printProducts(Object[][] table) {
        String  title[] = {"Id", "Désignation", "Description", "Prix", "Stock"};
        this.remove(scrollPan);
        scrollPan = new JScrollPane(new JTable(table, title));
        this.getContentPane().add(scrollPan);
        this.validate();
    }
}
