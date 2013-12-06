package com.jservmarketmvc.dao;

import com.jservmarketmvc.model.*;

/**
 * com.jservmarketmvc.dao in JservMarket
 * Made by Floran Pagliai <floran.pagliai@gmail.com>
 * Started on 05/12/2013 at 14:26
 */

public class DAOModels {
    private ADAO<UsersModel> usersDAO = new UsersDAO();
    private ADAO<CartModel> cartDAO = new CartDAO();
    private ADAO<ProductsModel> productDAO = new ProductsDAO();
    private ADAO<CategoriesModel> categoriesDAO = new CategoriesDAO();

    public ADAO<UsersModel> getUsersDAO() {
        return usersDAO;
    }

    public ADAO<CartModel> getCartDAO() {
        return cartDAO;
    }

    public ADAO<ProductsModel> getProductDAO() { return productDAO; }

    public ADAO<CategoriesModel> getCategoriesDAO() { return categoriesDAO; }
}
