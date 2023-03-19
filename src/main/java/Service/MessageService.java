package Service;

import Model.Message;
import DAO.MessageDAO;
import java.util.Objects;

import java.util.List;

/**
 * The purpose of a Service class is to contain "business logic" that sits between the web layer (controller) and
 * persistence layer (DAO). That means that the Service class performs tasks that aren't done through the web or
 * SQL: programming tasks like checking that the input is valid, conducting additional security checks, or saving the
 * actions undertaken by the API to a logging file.
 *
 * It's perfectly normal to have Service methods that only contain a single line that calls a DAO method. An
 * application that follows best practices will often have unnecessary code, but this makes the code more
 * readable and maintainable in the long run!
 */

public class MessageService {
    private MessageDAO messageDAO;
    /**
     * no-args constructor for creating a new MessageService with a new MessageDAO.
     * 
     */
    public MessageService(){
        messageDAO = new MessageDAO();
    }
    /**
     * Constructor for a MessageService when a MessageDAO is provided.
     * This is used for when a mock MessageDAO that exhibits mock behavior is used in the test cases.
     * This would allow the testing of MessageService independently of MessageDAO.
     * There is no need to modify this constructor.
     * @param messageDAO
     */
    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }
    
}
