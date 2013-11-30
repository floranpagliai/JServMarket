package com.jservMarket;

import java.sql.*;

/**
 * com.jservMarket in JservMarket
 * Made by Floran Pagliai <floran.pagliai@gmail.com>
 * Started on 29/11/2013 at 18:26
 */

public class JconnexionSQL {
    private String url_ = "jdbc:mysql://localhost:3306/jservmarket";
    private String user_ = "root";
    private String passwd_ = "root";
    private static Connection connect_;

    private JconnexionSQL(){
        try {
            connect_ = DriverManager.getConnection(url_, user_, passwd_);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getInstance(){
        if(connect_ == null){
            new JconnexionSQL();
        }
        return connect_;
    }
}
