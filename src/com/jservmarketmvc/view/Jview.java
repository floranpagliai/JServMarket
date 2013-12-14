package com.jservmarketmvc.view;

import com.jservmarketmvc.dao.DAOModels;

import javax.swing.*;
import javax.swing.table.TableColumn;
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
    public JLabel usersConnected = new JLabel("");
    JScrollPane scrollPan = new JScrollPane(new JTable());
    JPanel btns = new JPanel();
    JPanel head = new JPanel();
    public JTextArea console = new JTextArea();

    public Jview(DAOModels daoModels) {
        this.daoModels_ = daoModels;

        this.setTitle("JservMarket");
        this.setSize(700, 450);
        this.setLocationRelativeTo(null);

        this.getContentPane().setBackground(Color.LIGHT_GRAY);

        this.setLayout(new BorderLayout());
        head.setLayout(new BorderLayout());

        btns.add(printUsers, BorderLayout.WEST);
        btns.add(printCategories, BorderLayout.CENTER);
        btns.add(printProducts, BorderLayout.EAST);

        usersConnected.setHorizontalAlignment(SwingConstants.CENTER);

        head.add(usersConnected, BorderLayout.NORTH);
        head.add(btns, BorderLayout.CENTER);
        this.getContentPane().add(head, BorderLayout.NORTH);
;
        this.getContentPane().add(scrollPan, BorderLayout.CENTER);
        this.setVisible(true);
    }

    public void update(Observable o, Object arg) {
    }

    public void printUsers(Object[][] table) {
        String  title[] = {"Id", "Login", "Connecté ?"};
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
