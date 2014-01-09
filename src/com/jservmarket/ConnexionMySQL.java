package com.jservmarket;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * com.jservmarket in JservMarket
 * Made by Floran Pagliai <floran.pagliai@gmail.com>
 * Started on 03/12/2013 at 22:34
 */

public class ConnexionMySQL {
    private String url_ = "jdbc:mysql://localhost:3306/jservmarket";
    private String user_ = "root";
    private String passwd_ = "root";
    private static Connection connect_;

    private ConnexionMySQL(){
        try {
            connect_ = DriverManager.getConnection(url_, user_, passwd_);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getInstance(){
        if(connect_ == null){
            new ConnexionMySQL();
        }
        return connect_;
    }
}
