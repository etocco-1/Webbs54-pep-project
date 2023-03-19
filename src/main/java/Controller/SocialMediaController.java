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

        // Check if the username is blank
        if (account.getPassword().trim().isEmpty()) {
            ctx.status(401);
            ctx.result("Passwornd is Empty!");
            return;
        }
    
        Account addedAccount = accountservice.addAccount(account);
        if (addedAccount == null) {
            ctx.status(200);
        } else {
            ctx.json(mapper.writeValueAsString(addedAccount));
        }
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


    private void postNewMessages(Context ctx)
    {
        
    }

    private void getAllMessages(Context ctx)
    {
        
    }

    private void getMessageById(Context ctx)
    {
        
    }

    private void deleteMessagebyId(Context ctx)
    {
        
    }

    private void updateMessagebyId(Context ctx)
    {
        
    }

    private void getAllMessagesbyAccountId(Context ctx)
    {
        
    }


}