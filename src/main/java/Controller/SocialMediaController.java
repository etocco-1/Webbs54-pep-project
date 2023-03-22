package Controller;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;


/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */

 /**
 *
 *  The included endpoints:
 *
 *  POST localhost:8080/register : process new User registrations
 * 
 *  POST localhost:8080/login : process User logins
 * 
 *  POST localhost:8080/messages : process the creation of new messages
 * 
 *  GET localhost:8080/messages : retrieve all messages
 * 
 *  GET localhost:8080/messages/{message_id} : retrieve a message by its ID
 * 
 *  DELETE localhost:8080/messages/{message_id} : delete a message identified by a message ID
 *  
 *  PATCH localhost:8080/messages/{message_id} : update a message text identified by a message ID
 * 
 *  GET localhost:8080/accounts/{account_id}/messages : retrieve all messages written by a particular user
 */
public class SocialMediaController {
    AccountService accountservice;
    MessageService messageservice;
    
        public SocialMediaController(){
            this.accountservice = new AccountService();
            this.messageservice = new MessageService();
        }
    /**
     * Method defines the structure of the Javalin SocialMedia API. Javalin methods will use handler methods
     * to manipulate the Context object, which is a special object provided by Javalin which contains information about
     * HTTP requests and can generate responses.
     */
    /**
     * below are the endpoints in the startAPI() method. 
     * The test suite receives the Javalin object (app) from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::postUserRegistrations); // process new User registrations
        app.post("/login", this::postUserLogins); // process User logins
        app.post("/messages", this::postNewMessages); // process the creation of new messages
        app.get("/messages", this::getAllMessages); // retrieve all messages
        app.get("/messages/{message_id}", this::getMessageById); // retrieve a message by its ID
        app.delete("/messages/{message_id}", this::deleteMessagebyId); // delete a message identified by a message ID
        app.patch("/messages/{message_id}", this::updateMessagebyId); // update a message text identified by a message ID
        app.get("/accounts/{account_id}/messages", this::getAllMessagesbyAccountId); // retrieve all messages written by a particular user
        // app.start(8080); app is started in the test files.

        return app;
    }

    /* 
     * Handler to post a new user.
     * The Jackson ObjectMapper will automatically convert the JSON of the POST request into a Account object.
     * If accountservice returns a null account, the username entry that is blank, a password less than 4 charachters,
     * or and account with a username that already exists. (meaning posting a account post/test was unsuccessful, the API will return a 400
     * message (client error).
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin. It will
     *            be available to this method automatically thanks to the app.post method.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void postUserRegistrations(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
    
        // Check if the username is blank, returns 400 status code
        if (account.getUsername().trim().isEmpty()) {
            ctx.status(400);
            ctx.result("Username cannot be blank");
            return;
        }

        // Check if the password is too short, returns 400 status code
        if (account.getPassword().length() < 4) {
            System.out.println("Password too short!");
            ctx.status(400);
            return;
        }

        // Check if the account post was succesful, if not it returns 400 status code
        // if succesful (else), returns the added account in a json string
        Account addedAccount = accountservice.addAccount(account);
        if (addedAccount == null) {
            ctx.status(400);
        } else {
            ctx.json(mapper.writeValueAsString(addedAccount));
        }

    }

    /* 
     * Handler to proccess user login.
     * The Jackson ObjectMapper will automatically convert the JSON of the POST request into a Account object.
     * If accountservice returns a null account, (the user login/test was unsuccessful, the API will return a 401
     * message (client error).
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin. It will
     *            be available to this method automatically thanks to the app.post method.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void postUserLogins(Context ctx) throws JsonProcessingException
{
    ObjectMapper mapper = new ObjectMapper();
    Account account = mapper.readValue(ctx.body(), Account.class);

    // if login failed status code 401 is returned. else returns the user account in a json string
    Account userAccount = accountservice.logintoAccount(account);
    if (userAccount == null) {
        ctx.status(401); // Return 401 if login fails
    } else {
        ctx.json(mapper.writeValueAsString(userAccount));
    }
}

    /**
     * Handler to post a new message.
     * The Jackson ObjectMapper will automatically convert the JSON of the POST request into a Message object.
     * If messageservice returns a null message (meaning posting a Message was unsuccessful), the API will return a 400
     * message (client error).
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin. It will
     *            be available to this method automatically thanks to the app.post method.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void postNewMessages(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);

        // Check if the username is blank and message greater than 255 charachters
        if ((message.getMessage_text().trim().isEmpty() || message.getMessage_text().length() > 255)) {
            ctx.status(400);
            ctx.result("Message cannot be blank or less than 255 characters");
            return;
        }

        // if message post failed status code 400 is returned. else returns the added message in a json string
        Message addedMessage = messageservice.addMessage(message);
        if(addedMessage==null){
            ctx.status(400);
        }else{
            ctx.json(mapper.writeValueAsString(addedMessage));
        }
    }

    /**
     * Handler to retrieve all messages.
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin. It will
     *            be available to this method automatically thanks to the app.put method.
     */
    private void getAllMessages(Context ctx){
        ctx.json(messageservice.getAllMessages()); // returns all messages in json using messageservice method
    }

    /**
     * Handler to get message by the message_id.
     * If messageservice returns a null message (meaning no message was found), the API will return a 200
     * status (client error).
     *
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin. It will
     *            be available to this method automatically thanks to the app.put method.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void getMessageById(Context ctx) {
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message getMessageById = messageservice.retrieveMessage(message_id);
    
        if (getMessageById == null) {
            ctx.status(200); // Returns 200 if the message is not found
            ctx.result(""); // Returns an empty response as requested in the readme.md
        } else {
            ctx.json(getMessageById); // if succesful, message is returned in json format
        }
    }
 
    /**
     * Handler to delete message by the message_id.
     * If messageservice is null (meaning no message was found), the API will return a 200
     * status (client error).
     *
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin. It will
     *            be available to this method automatically thanks to the app.put method.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    public void deleteMessagebyId(Context ctx) {
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message deletedMessage = messageservice.deleteMessage(message_id);
    
        // if the intended message to be deleted exists, it would have been deleted, and then returned here.
        if (deletedMessage != null) {
            ctx.json(deletedMessage);
        } else {
            ctx.status(200); // Return 200 if the message is not found
            ctx.result(""); // Return an empty response body
        }
    }
    
    /**
     * Handler to update a message by the message_id.
     * If messageservice returns a null message (meaning no message was found), the API will return a 400
     * status (client error).
     * If the message text is empty or is longer than 255 characters, the API will return a 400 status.
     * 
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin. It will
     *            be available to this method automatically thanks to the app.put method.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void updateMessagebyId(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));

        // returns 400 status code if message text is empty and greater than 255 characters.
        if (message.getMessage_text().trim().isEmpty() || message.getMessage_text().length() > 255) {
            ctx.status(400);
            ctx.result("Message cannot be blank or longer than 255 characters");
            return;
        }

        // if message update is not succesful, status code 400 is returned. if succesful, the updated message is returned in json.
        Message updatedMessage = messageservice.updateMessage(message_id, message.getMessage_text());
        if (updatedMessage == null) {
            ctx.status(400);
        } else {
            ctx.json(mapper.writeValueAsString(updatedMessage));
        }
    }
    
    /**
     * Handler to get all messages by the account_id.
     * If messageservice returns a null message (meaning no messages were found), the API will return a 200
     * status (client error) by default.
     *
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin. It will
     *            be available to this method automatically thanks to the app.put method.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void getAllMessagesbyAccountId(Context ctx) throws JsonProcessingException
    {
        int user_id = Integer.parseInt(ctx.pathParam("account_id"));
        ctx.json(messageservice.getAllMessagesbyAccount(user_id));
    }


}