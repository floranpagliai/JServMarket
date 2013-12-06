package com.jservmarketmvc.controler;

import com.jservmarketmvc.dao.ADAO;
import com.jservmarketmvc.model.UsersModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * com.jservmarketmvc.controler in JservMarket
 * Made by Floran Pagliai <floran.pagliai@gmail.com>
 * Started on 05/12/2013 at 03:02
 */

public class Controler implements ActionListener {
    private ADAO<UsersModel> usersDAO_;
    private Jclient client_;

    public Controler(ADAO<UsersModel> usersDAO_, Jclient client) {
        this.usersDAO_ = usersDAO_;
        this.client_= client;
    }

    public void actionPerformed(ActionEvent e) {

    }
}
