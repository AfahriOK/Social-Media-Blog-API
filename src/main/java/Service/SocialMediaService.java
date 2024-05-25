package Service;

import java.util.List;
import DAO.SocialMediaDAO;
import Model.Account;
import Model.Message;

public class SocialMediaService {
    SocialMediaDAO socialMediaDAO;

    public SocialMediaService() {
        socialMediaDAO = new SocialMediaDAO();
    }

    public SocialMediaService(SocialMediaDAO socialMediaDAO) {
        this.socialMediaDAO = socialMediaDAO;
    }

    /**
     * Retrieves a list of all messages
     * @return a list of all messages even if empty
     */
    public List<Message> getAllMessages() {
        return socialMediaDAO.getAllMessages();
    }

    /**
     * Retrieves a message by id
     * @return a single message or null if not found
     */
    public Message getMessageById(int id) {
        return socialMediaDAO.getMessageById(id);
    }

    /**
     * Registers the user account
     * @param account an account to be added
     * @return the account that was added or null if unsuccessful
     */
    public Account registerAccount(Account account) {
        return socialMediaDAO.registerAccount(account);
    }

    /**
     * Authenticates the user account
     * @param account an account to be authenticated
     * @return the authenticated users' credentials or null if unsuccessful
     */
    public Account authenticateAccount(Account account) {
        return socialMediaDAO.authenticateAccount(account);
    }

    /**
     * Creates a message 
     * @param message a message to be added
     * @return the message that was added or null if unsuccessful
     */
    public Message createMessage(Message message) {
        return socialMediaDAO.createMessage(message);
    }
    
    /**
     * Deletes a message by id
     * @return the deleted message or null if not found
     */
    public Message deleteMessage(int id) {
        return socialMediaDAO.deleteMessage(id);
    }

    /**
     * Updates a message by id
     * @return the updated message or null if not found
     */
    public Message patchMessage(String text, int id) {
        return socialMediaDAO.patchMessage(text, id);
    }
    
    /**
     * Retrieves a user's messages
     * @return all of a users' messages
     */
    public List<Message> getMessagesByUser(int account_id) {
        return socialMediaDAO.getUserMessages(account_id);
    }
}
