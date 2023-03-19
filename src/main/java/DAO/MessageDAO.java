package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A DAO is a class that mediates the transformation of data between the format of objects in Java to rows in a
 * database. The methods here are mostly filled out, you will just need to add a SQL statement.
 *
 * We may assume that the database has already created a table named 'message'.
 * It contains similar values as the Message class:
 * message_id int primary key auto_increment,
    posted_by int,
    message_text varchar(255),
    time_posted_epoch bigint,
    //
    foreign key (posted_by) references  account(account_id)
 */

public class MessageDAO {
    
}
