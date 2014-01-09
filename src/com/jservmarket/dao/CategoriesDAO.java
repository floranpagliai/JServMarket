package com.jservmarket.dao;

import com.jservmarket.model.CategoriesModel;
import com.jservmarket.model.ProductsModel;
import com.jservmarket.model.UsersModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * com.jservmarket.dao in JservMarket
 * Made by Floran Pagliai <floran.pagliai@gmail.com>
 * Started on 06/12/2013 at 18:52
 */

public class CategoriesDAO extends ADAO<CategoriesModel> {
    @Override
    public CategoriesModel create(CategoriesModel obj) {
        try {
            PreparedStatement prepare = this.connect
                    .prepareStatement(
                            "INSERT INTO categories (label) " +
                                    "VALUES(?)"
                    );
            prepare.setString(1, obj.getLabel());
            prepare.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return obj;
    }

    @Override
    public CategoriesModel find(int id) {
        CategoriesModel obj = new CategoriesModel();
        try {
            ResultSet result = this.connect
                    .createStatement(
                            ResultSet.TYPE_SCROLL_INSENSITIVE,
                            ResultSet.CONCUR_UPDATABLE
                    ).executeQuery(
                            "SELECT * FROM categories WHERE id = '" + id + "'"
                    );
            if (result.first())
                obj = new CategoriesModel(
                        id,
                        result.getString("label")
                );

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return obj;
    }

    @Override
    public CategoriesModel findByKey(String key, String keyEntry) {
        return null;
    }

    @Override
    public CategoriesModel update(CategoriesModel obj) {
        try {
            PreparedStatement prepare = this.connect
                    .prepareStatement(
                            "UPDATE categories SET label=? WHERE id=?"
                    );
            prepare.setString(1, obj.getLabel());
            prepare.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return obj;
    }

    @Override
    public void delete(CategoriesModel obj) {
        try {
            PreparedStatement prepare = this.connect
                    .prepareStatement(
                            "DELETE FROM categories WHERE id=?"
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
                            "SELECT * FROM categories"
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
                            "SELECT * FROM categories"
                    );
            result.last();
            return result.getRow();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
