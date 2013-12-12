package com.jservmarketmvc.dao;

import com.jservmarketmvc.model.ProductsModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * com.jservmarketmvc.dao in JservMarket
 * Made by Floran Pagliai <floran.pagliai@gmail.com>
 * Started on 06/12/2013 at 01:50
 */

public class ProductsDAO extends ADAO<ProductsModel> {
    @Override
    public ProductsModel create(ProductsModel obj) {
        try {
            PreparedStatement prepare = this.connect
                    .prepareStatement(
                            "INSERT INTO products (designation, description, category, price, quantities) " +
                                    "VALUES(?, ?, ?, ?, ?)"
                    );
            prepare.setString(1, obj.getDesignation());
            prepare.setString(2, obj.getDescription());
            prepare.setInt(3, obj.getCategory());
            prepare.setFloat(4, obj.getPrice());
            prepare.setInt(5, obj.getQuantities());

            prepare.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return obj;
    }

    @Override
    public ProductsModel find(int id) {
        ProductsModel obj = new ProductsModel();
        try {
            ResultSet result = this.connect
                    .createStatement(
                            ResultSet.TYPE_SCROLL_INSENSITIVE,
                            ResultSet.CONCUR_UPDATABLE
                    ).executeQuery(
                            "SELECT * FROM products WHERE id = '" + id + "'"
                    );
            if (result.first())
                obj = new ProductsModel(
                        id,
                        result.getString("designation"),
                        result.getString("description"),
                        result.getInt("category"),
                        result.getFloat("price"),
                        result.getInt("quantities")
                );

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return obj;
    }

    @Override
    public ProductsModel findByKey(String key, String keyEntry) {
        return null;
    }

    @Override
    public ProductsModel update(ProductsModel obj) {
        try {
            PreparedStatement prepare = this.connect
                    .prepareStatement(
                            "UPDATE products SET quantities=? WHERE id=?"
                    );
            prepare.setInt(1, obj.getQuantities());
            prepare.setInt(2, obj.getId());
            prepare.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return obj;
    }

    @Override
    public void delete(ProductsModel obj) {
        try {
            PreparedStatement prepare = this.connect
                    .prepareStatement(
                            "DELETE FROM products WHERE id=?"
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
                            "SELECT * FROM products"
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
                            "SELECT * FROM products"
                    );
            result.last();
            return result.getRow();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
