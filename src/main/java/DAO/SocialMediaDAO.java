package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Model.Account;
import Model.Message;
import Util.ConnectionUtil;

public class SocialMediaDAO {

    /**
     * Retrieves all messages in the database
     * @return all messages
     */
    public List<Message> getAllMessages(){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            //Retrieve all message records
            String sql = "SELECT * FROM message";

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
     * Retrieves a specific message by id
     * @param id the id of the requested message
     * @return the specified message or null if Id not found
     */
    public Message getMessageById(int id) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            //Retrieve message
            String sql = "SELECT * FROM message WHERE message_id = ?";
            
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            //Set the parameter
            preparedStatement.setInt(1, id);

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()){
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"),
                        rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                return message;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Inserts an account to the database
     * @param account an account to be added to the database
     * @return new account object with ID or null if error
     */
    public Account registerAccount(Account account) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            //SQL string to insert accounts
            String sql = "INSERT INTO account (username, password) VALUES (?, ?)" ;
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            //Set the parameters
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());

            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if(pkeyResultSet.next()){
                int generated_account_id = (int) pkeyResultSet.getLong(1);
                return new Account(generated_account_id, account.getUsername(), account.getPassword());
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Authenticates the account
     * @param account account information to be verified
     * @return account information if successful or null if not
     */
    public Account authenticateAccount(Account account) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            //SQL string to retrieve account
            String sql = "SELECT * FROM account WHERE username = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            //Set the parameters
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());

            ResultSet rs = preparedStatement.executeQuery();
        
            if(rs.next()){
                return new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Inserts the message to the database
     * @param message a message to be added to the database
     * @return new message object with ID or null if error
     */
    public Message createMessage(Message message) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            //SQL string to insert accounts
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)" ;
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            //Set the parameters
            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());

            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if(pkeyResultSet.next()){
                int generated_message_id = (int) pkeyResultSet.getLong(1);
                return new Message(generated_message_id, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Deletes a message from the database
     * @param id the id of the specific message to be deleted
     * @return deleted message information
     */
    public Message deleteMessage(int id) {
        Connection connection = ConnectionUtil.getConnection();
        Message message = getMessageById(id);
        try {
            if (message == null) {
                return null;
            } else {
                //delete message
                String sql = "DELETE * FROM message WHERE message_id = ?";
                
                PreparedStatement preparedStatement = connection.prepareStatement(sql);

                //Set the parameter
                preparedStatement.setInt(1, id);

                preparedStatement.executeUpdate();

                return message;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return message;
    }

    /**
     * Updates the specified message with the new text
     * @param text the message text to be updated
     * @param id the id of the message to be updated
     * @return the updated message or null if unsuccessful
     */
    public Message patchMessage(String text, int id) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            //patch message
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
                
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            //Set the parameters
            preparedStatement.setString(1, text);
            preparedStatement.setInt(2, id);

            preparedStatement.executeUpdate();

            Message message = getMessageById(id);
            return message;
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Retrieves all a users' messages
     * @return all messages for a specific user
     */
    public List<Message> getUserMessages(int account_id){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            //Retrieve all message records
            String sql = "SELECT * FROM message WHERE posted_by = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            //Set the parameter
            preparedStatement.setInt(1, account_id);

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
