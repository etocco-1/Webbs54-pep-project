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
     * TODO: Retrieve a specific account using its username and password.
     *
     * You only need to change the sql String and set preparedStatement parameters.
     *
     * Remember that the format of a select where statement written as a Java String looks something like this:
     * String sql = "select * from TableName where ColumnName = ?";
     * The question marks will be filled in by the preparedStatement setString, setInt, etc methods. they follow
     * this format, where the first argument identifies the question mark to be filled (left to right, starting
     * from zero) and the second argument identifies the value to be used:
     * preparedStatement.setInt(1,int1);
     *
     * @param str1 a username.
     * @param str2 a password.
     */
    public Account insertAccount(Account account){
        Connection connection = ConnectionUtil.getConnection();
        try {
//          Write SQL logic here. You should only be inserting with the username and password column, so that the database may
//          automatically generate a primary key.
            String sql = "INSERT INTO account (account_id, username, password) VALUES (?,?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            //write preparedStatement's setString method here.
            String str1_username = account.getUsername();
            String str2_password = account.getPassword();

            preparedStatement.setString(1, str1_username);
            preparedStatement.setString(2, str2_password);


            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if(pkeyResultSet.next()){
                int generated_account_id = (int) pkeyResultSet.getLong(1);
                account.setAccount_id(generated_account_id);
                return new Account(generated_account_id, str1_username, str2_password);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    
}