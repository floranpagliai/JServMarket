package com.jservmarket.dao;

import com.jservmarket.model.CartModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CartDAO extends ADAO<CartModel> {
    @Override
    public CartModel create(CartModel obj) {
        try {
            PreparedStatement prepare = this.connect
                    .prepareStatement(
                            "INSERT INTO cart (userid, productid, quantity) VALUES(?, ?, ?)"
                    );
            prepare.setInt(1, obj.getUserId());
            prepare.setInt(2, obj.getProductId());
            prepare.setInt(3, obj.getQuantity());
            prepare.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return obj;
    }

    @Override
    public CartModel find(int id) {
        CartModel obj = new CartModel();
        try {
            ResultSet result = this.connect
                    .createStatement(
                            ResultSet.TYPE_SCROLL_INSENSITIVE,
                            ResultSet.CONCUR_UPDATABLE
                    ).executeQuery(
                            "SELECT * FROM cart INNER JOIN products ON cart.productid = products.id WHERE cart.id = '" + id + "'"
                    );
            if (result.first())
                obj = new CartModel(
                        id,
                        result.getInt("userid"),
                        result.getInt("productid"),
                        result.getInt("quantity"),
                        result.getString("designation"),
                        result.getFloat("price")
                );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return obj;
    }

    @Override
    public CartModel findByKey(String key, String keyEntry) {
        CartModel obj = new CartModel();
        try {
            ResultSet result = this.connect
                    .createStatement(
                            ResultSet.TYPE_SCROLL_INSENSITIVE,
                            ResultSet.CONCUR_UPDATABLE
                    ).executeQuery(
                            "SELECT * FROM cart INNER JOIN products ON cart.productid = products.id " +
                                    "WHERE cart." + key + " = '" + keyEntry + "'"
                    );
            if (result.first())
                obj = new CartModel(
                        result.getInt("id"),
                        result.getInt("userid"),
                        result.getInt("productid"),
                        result.getInt("quantity"),
                        result.getString("designation"),
                        result.getInt("price")
                );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return obj;
    }

    @Override
    public CartModel update(CartModel obj) {
        try {
            PreparedStatement prepare = this.connect
                    .prepareStatement(
                            "UPDATE cart SET quantity=? WHERE id=?"
                    );
            prepare.setInt(1, obj.getQuantity());
            prepare.setInt(2, obj.getId());
            prepare.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return obj;
    }

    @Override
    public void delete(CartModel obj) {
        try {
            PreparedStatement prepare = this.connect
                    .prepareStatement(
                            "DELETE FROM cart WHERE id=?"
                    );
            prepare.setInt(1, obj.getId());
            prepare.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int countRow() {
        try {
            ResultSet result = this.connect
                    .createStatement(
                            ResultSet.TYPE_SCROLL_INSENSITIVE,
                            ResultSet.CONCUR_UPDATABLE
                    ).executeQuery(
                            "SELECT * FROM cart"
                    );
            result.last();
            return result.getInt("id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int nbRow() {
        try {
            ResultSet result = this.connect
                    .createStatement(
                            ResultSet.TYPE_SCROLL_INSENSITIVE,
                            ResultSet.CONCUR_UPDATABLE
                    ).executeQuery(
                            "SELECT * FROM cart"
                    );
            result.last();
            return result.getRow();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
