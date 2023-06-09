package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A DAO is a class that mediates the transformation of data between the format of objects in Java to rows in a
 * database. The methods here are mostly filled out, you will just need to add a SQL statement.
 *
 * We may assume that the database has already created a table named 'account'.
 * It contains similar values as the Account class:
 * account_id int primary key auto_increment,
    username varchar(255) unique,
    password varchar(255)
 */


public class AccountDAO {
     /**
     * Adds an account into the database which matches the values contained in the account object.
     * Uses the getters already written in the Account class to retrieve its values (getUsername(),
     * getPassword()). The account_id will be automatically generated by the SQL database, and JDBC will be able
     * to retrieve the generated ID automatically.
     *
     * Use of the sql String and set preparedStatement parameters.
     *
     */
    public Account insertAccount(Account account){
        Connection connection = ConnectionUtil.getConnection();
        try {
//          SQL logic here. You should only be inserting with the username and password column, so that the database may
//          automatically generate a primary key.
            String sql = "INSERT INTO account (username, password) VALUES (?,?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // preparedStatement's setString method here:
            String str1_username = account.getUsername();
            String str2_password = account.getPassword();

            preparedStatement.setString(1, str1_username);
            preparedStatement.setString(2, str2_password);

            preparedStatement.executeUpdate();
       
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
         
            if(pkeyResultSet.next()){
                int generated_account_id = (int) pkeyResultSet.getLong(1); // gets new account id
                account.setAccount_id(generated_account_id); // sets the id before the account is returned next
                return new Account(account.getAccount_id(), account.getUsername(), account.getPassword());
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * checks for an account in the database which matches the values contained in the account object.
     * Uses the getters already written in the Account class to retrieve its values (getUsername(),
     * getPassword()).
     *
     * Use of the sql String and set preparedStatement parameters.
     *
     */
    public Account checkAccount(Account account){
        Connection connection = ConnectionUtil.getConnection();
        try {
            // SQL logic here. gets account info where username and password match up in account table
            String sql = "SELECT * FROM account WHERE username = ? AND password = ?;";
            
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            // preparedStatement's setString method here.
            String str1_username = account.getUsername();
            String str2_password = account.getPassword();

            preparedStatement.setString(1, str1_username);
            preparedStatement.setString(2, str2_password);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                account = new Account(rs.getInt("account_id"), rs.getString("username"),
                        rs.getString("password")); // get account info and returns it next
                return account;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    
}