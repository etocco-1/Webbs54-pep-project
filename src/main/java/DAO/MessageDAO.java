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

   // post a message

   public Message postMessage(Message message){
      Connection connection = ConnectionUtil.getConnection();
      try {
          //Write SQL logic here. When inserting, you only need to define the 
          String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) values (?, ?, ?);" ;
          PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

          //write preparedStatement's setString and setInt methods here.
          
          int posted_by = message.getPosted_by();
          String message_text = message.getMessage_text();
          long time_posted = message.getTime_posted_epoch();
          
          preparedStatement.setInt(1,posted_by);
          preparedStatement.setString(2,message_text);
          preparedStatement.setLong(3,time_posted);


          preparedStatement.executeUpdate();
          ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
          if(pkeyResultSet.next()){
            int generated_message_id = (int) pkeyResultSet.getLong(1);
            message.setMessage_id(generated_message_id);
            //System.out.println(new Message(message.getMessage_id(), message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch()));
            return new Message(message.getMessage_id(), message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
          }
      }catch(SQLException e){
          System.out.println(e.getMessage());
      }
      return null;
  }

  /**
     * TODO: Retrieve all messages from the message table.
     *
     * You only need to change the sql String and set preparedStatement parameters.
     *
     * @return all messages.
     */
    public List<Message> getAllMessages(){
      Connection connection = ConnectionUtil.getConnection();
      List<Message> messages = new ArrayList<>();
      try {
          //Write SQL logic here
          String sql = "SELECT * FROM message;";

          PreparedStatement preparedStatement = connection.prepareStatement(sql);
          ResultSet rs = preparedStatement.executeQuery();
          while(rs.next()){
              Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"),
                      rs.getString("message_text"), rs.getLong("time_posted_epoch"));
              messages.add(message);
          }
      }catch(SQLException e){
          System.out.println(e.getMessage());
      }
      return messages;
  }

  /**
     * TODO: Retrieve a specific message using its message ID.
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
     * @param id a message ID.
     */
   public Message getMessageById(int id){
      Connection connection = ConnectionUtil.getConnection();
      try {
          //Write SQL logic here
          String sql = "SELECT * FROM message WHERE message_id = ?;";
          
          PreparedStatement preparedStatement = connection.prepareStatement(sql);


          //write preparedStatement's setString and setInt methods here.
          preparedStatement.setInt(1,id);

          ResultSet rs = preparedStatement.executeQuery();
          while(rs.next()){
              Message message_by_id = new Message(rs.getInt("message_id"), rs.getInt("posted_by"),
                      rs.getString("message_text"), rs.getLong("time_posted_epoch"));
              return message_by_id;
          }
      }catch(SQLException e){
          System.out.println(e.getMessage());
      }
      return null;
  }

  public Message deleteMessageById(Message message) throws Exception
{
    Connection connection = ConnectionUtil.getConnection();
    String sql = "DELETE FROM message WHERE message_id=?";
    try
    {
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        int id = message.getMessage_id();
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
        return new Message(message.getMessage_id(), message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
    }
    catch (RuntimeException | SQLException runtimeException) 
    {
        System.err.println("MessageDAO::deleteObject, RuntimeException occurred, message follows.");
        System.err.println(runtimeException);
        throw runtimeException;
    }
}


//   public Message deleteMessageById(Message message){
//     Connection connection = ConnectionUtil.getConnection();
//     try {
//         //Write SQL logic here. When inserting, you only need to define the 
//         String sql = "DELETE FROM message WHERE message_id = ?;" ;
//         PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

//         //write preparedStatement's setString and setInt methods here.
//         int id = message.getMessage_id();
//         preparedStatement.setInt(1, id);

//         preparedStatement.executeUpdate();
//         ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
//         if(pkeyResultSet.next()){
//             int message_id_to_delete = (int) pkeyResultSet.getInt(1);
//             message.setMessage_id(message_id_to_delete);
//           //System.out.println(new Message(message.getMessage_id(), message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch()));
//           return new Message(message.getMessage_id(), message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
//         }
//     }catch(SQLException e){
//         System.out.println(e.getMessage());
//     }
//     return null;
// }

// public Message deleteMessageById(int id){
//     Connection connection = ConnectionUtil.getConnection();
//     try {
//         String sql = "DELETE FROM message WHERE message_id = ?;";
        
//         PreparedStatement preparedStatement = connection.prepareStatement(sql);
//         preparedStatement.setInt(1, id);

//         ResultSet rs = preparedStatement.executeQuery();
//         while(rs.next()){
//             Message message_by_id = new Message(rs.getInt("message_id"), rs.getInt("posted_by"),
//                                     rs.getString("message_text"), rs.getLong("time_posted_epoch"));
//             return message_by_id;
//         }
//     } catch(SQLException e){
//         System.out.println(e.getMessage());
//     }
//     return null;
// }


public void updateMessage(int id, String message) {
    Connection connection = ConnectionUtil.getConnection();
    try {
        // Write SQL logic here
        String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, message);
        preparedStatement.setInt(2, id);
        preparedStatement.executeUpdate();
    } catch (SQLException e) {
        System.out.println(e.getMessage());
    }
}




//    public List<Message> updateMessage(int id, String message){
//     Connection connection = ConnectionUtil.getConnection();
//     List<Message> messages = new ArrayList<>();
//     try {
//         //Write SQL logic here
//         String sql = "UPDATE message SET message_text =? WHERE message_id = ?;";
//         PreparedStatement preparedStatement = connection.prepareStatement(sql);

//         //int posted_by = message.getPosted_by();
//         //String message_text = message.getMessage_text();
//         //long time_posted = message.getTime_posted_epoch();


//         preparedStatement.setInt(2,id);
//         //preparedStatement.setInt(1,posted_by);
//         preparedStatement.setString(1,message);
//         //preparedStatement.setLong(3,time_posted);
//         //System.out.println(preparedStatement);

//         //PreparedStatement preparedStatement = connection.prepareStatement(sql);
//         ResultSet rs = preparedStatement.executeQuery();
//         while(rs.next()){
//             Message message1 = new Message(rs.getInt("message_id"), rs.getInt("posted_by"),
//                     rs.getString("message_text"), rs.getLong("time_posted_epoch"));
//             messages.add(message1);
//             //System.out.println(message1);
//         }
//     }catch(SQLException e){
//         System.out.println(e.getMessage());
//     }
//     return messages;
// }

//    public void updateMessage(int id, Message message){
//       Connection connection = ConnectionUtil.getConnection();
//       try {
//           //Write SQL logic here
          
//           String sql = "UPDATE message SET posted_by =?, message_text =?, time_posted_epoch =? WHERE message_id = ?;";
//           PreparedStatement preparedStatement = connection.prepareStatement(sql);

//           //write PreparedStatement setString and setInt methods here.

        //   int posted_by = message.getPosted_by();
        //   String message_text = message.getMessage_text();
        //   long time_posted = message.getTime_posted_epoch();


        //   preparedStatement.setInt(4,id);
        //   preparedStatement.setInt(1,posted_by);
        //   preparedStatement.setString(2,message_text);
        //   preparedStatement.setLong(3,time_posted);

//         //   ResultSet rs = preparedStatement.executeQuery();
//         //     while(rs.next()){
//         //         String generated_message = (String) rs.getString(1);
//         //         message.setMessage_text(generated_message);
//         //         message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"),
//         //                 rs.getLong("time_posted_epoch"));
//         //         return message;
//         //     }


//           preparedStatement.executeUpdate();
//       }catch(SQLException e){
//           System.out.println(e.getMessage());
//       }
//   }





  public List<Message> getAllMessagesbyUser(int id){
   Connection connection = ConnectionUtil.getConnection();
   List<Message> messages = new ArrayList<>();
   try {
       //Write SQL logic here
       String sql = "SELECT * FROM message WHERE posted_by =?;";

       PreparedStatement preparedStatement = connection.prepareStatement(sql);

       preparedStatement.setInt(1,id);

       ResultSet rs = preparedStatement.executeQuery();
       while(rs.next()){
           Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"),
                   rs.getString("message_text"), rs.getLong("time_posted_epoch"));
           messages.add(message);
       }
   }catch(SQLException e){
       System.out.println(e.getMessage());
   }
   return messages;
}


    
}
