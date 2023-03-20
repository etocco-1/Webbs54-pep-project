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
public class SocialMediaController {
    AccountService accountservice;
    MessageService messageservice;
    
        public SocialMediaController(){
            this.accountservice = new AccountService();
            this.messageservice = new MessageService();
        }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
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

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    // private void exampleHandler(Context context) {
    //     context.json("sample text");
    // }

    

    private void postUserRegistrations(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
    
        // Check if the username is blank
        if (account.getUsername().trim().isEmpty()) {
            ctx.status(400);
            ctx.result("Username cannot be blank");
            return;
        }

        // Check if the password is too short
        if (account.getPassword().length() < 4) {
            System.out.println("Password too short!");
            ctx.status(400);
            return;
        }

        Account addedAccount = accountservice.addAccount(account);
        if (addedAccount == null) {
            ctx.status(400);
        } else {
            ctx.json(mapper.writeValueAsString(addedAccount));
        }

        // // Check if the username is blank
        // if (account.getPassword().trim().isEmpty()) {
        //     ctx.status(401);
        //     ctx.result("Passwornd is Empty!");
        //     return;
        // }
    
        // Account addedAccount = accountservice.addAccount(account);
        // if (addedAccount == null) {
        //     ctx.status(200);
        // } else {
        //     ctx.json(mapper.writeValueAsString(addedAccount));
        // }
    }
    
    

   
    
    
    
    

    private void postUserLogins(Context ctx) throws JsonProcessingException
{
    ObjectMapper mapper = new ObjectMapper();
    Account account = mapper.readValue(ctx.body(), Account.class);

    // Add authentication logic here
    if (account.getUsername().trim().isBlank()) {
        ctx.status(401);
        return;
    }

    Account userAccount = accountservice.logintoAccount(account);
    if (userAccount == null) {
        ctx.status(401); // Return 401 if login fails
    } else {
        ctx.json(mapper.writeValueAsString(userAccount));
    }
}


    private void postNewMessages(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);

        // Check if the username is blank
        if (message.getMessage_text().trim().isEmpty()) {
            ctx.status(400);
            ctx.result("Message cannot be blank");
            return;
        }


        Message addedMessage = messageservice.addMessage(message);
        if(addedMessage==null){
            ctx.status(400);
        }else{
            ctx.json(mapper.writeValueAsString(addedMessage));
        }
    }

    /**
     * Handler to retrieve all messages. There is no need to change anything in this method.
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin. It will
     *            be available to this method automatically thanks to the app.put method.
     */
    private void getAllMessages(Context ctx){
        ctx.json(messageservice.getAllMessages());
    }

    // private void getMessageById(Context ctx){
    //     // ctx.json(messageservice.retrieveMessage(ctx.pathParam("message_id")));
    //     ctx.json(messageservice.retrieveMessage());
    // }

    // private void getMessageById(Context ctx) throws JsonProcessingException {
    //     ObjectMapper mapper = new ObjectMapper();
    //     Message message = mapper.readValue(ctx.body(), Message.class);
    //     Message message_by_id = messageservice.retrieveMessage(message);
    //     if(message_by_id!=null){
    //         ctx.json(mapper.writeValueAsString(message_by_id));
    //     }else{
    //         ctx.status(400);
    //     }
    // }

    private void getMessageById(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message getMessagebyId = messageservice.retrieveMessage(message_id);
        System.out.println(getMessagebyId);
        if(getMessagebyId == null){
            ctx.status(400);
        }else{
            ctx.json(mapper.writeValueAsString(getMessagebyId));
        }
    }


    private void deleteMessagebyId(Context ctx) throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message delete_message_by_id = messageservice.deleteMessage(message_id);
        if(delete_message_by_id!=null){
            ctx.json(mapper.writeValueAsString(delete_message_by_id));
        }
        else
        {
            ctx.status(200);
        }
        
    }

    // private void deleteMessagebyId(Context ctx) throws JsonProcessingException {
    //     ObjectMapper mapper = new ObjectMapper();
    //     int message_id = Integer.parseInt(ctx.pathParam("message_id"));
    //     Message delete_message_by_id = messageservice.deleteMessage(message_id);
    //     if(delete_message_by_id != null) {
    //         ctx.json(mapper.writeValueAsString(delete_message_by_id));
    //     } else {
    //         ctx.status(200);
    //     }
    // }
    

    private void updateMessagebyId(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        String message_text = ctx.pathParam("message_text");
        Message updatedMessage = messageservice.updateMessage(message_id, message_text);
        System.out.println(updatedMessage);
        if(updatedMessage == null){
            ctx.status(400);
        }else{
            ctx.json(mapper.writeValueAsString(updatedMessage));
        }

    }
    
    private void getAllMessagesbyAccountId(Context ctx) throws JsonProcessingException
    {
        int user_id = Integer.parseInt(ctx.pathParam("account_id"));
        ctx.json(messageservice.getAllMessagesbyAccount(user_id));
    }


}