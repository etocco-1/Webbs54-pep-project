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

    public Message addMessage(Message message) {
        return messageDAO.postMessage(message);
    }

    /**
     * TODO: Use the MessageDAO to retrieve a List containing all messages.
     * You could use the messageDAO.getAllMessages method.
     *
     * @return all flights in the database.
     */
    public List<Message> getAllMessages() {
        List<Message> all_messages = messageDAO.getAllMessages();
        return all_messages;
    }

    public Message retrieveMessage(int message_id){
        return messageDAO.getMessageById(message_id);
    }

    public Message deleteMessage(int message_id){
        return messageDAO.deleteMessageById(message_id);
    }
    
    /**
    * TODO: Use the FlightDAO to update an existing flight from the database.
     * You should first check that the flight ID already exists. To do this, you could use an if statement that checks
     * if flightDAO.getFlightById returns null for the flight's ID, as this would indicate that the flight id does not
     * exist.
     *
     * @param flight_id the ID of the flight to be modified.
     * @param flight an object containing all data that should replace the values contained by the existing flight_id.
     *         the flight object does not contain a flight ID.
     * @return the newly updated flight if the update operation was successful. Return null if the update operation was
     *         unsuccessful. We do this to inform our application about successful/unsuccessful operations. (eg, the
     *         user should have some insight if they attempted to edit a nonexistent flight.)
     */
    public Message updateMessage(int message_id, Message message){
        if (Objects.isNull(messageDAO.getMessageById(message_id)))
        {
            return null;
        }
        messageDAO.updateMessage(message_id, message);
        return messageDAO.getMessageById(message_id);
    }

    public List<Message> getAllMessagesbyAccount(int user_id) {
        List<Message> all_messages = messageDAO.getAllMessagesbyUser(user_id);
        return all_messages;
    }
}
