package com.jservmarket.dao;

import com.jservmarket.model.*;

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
