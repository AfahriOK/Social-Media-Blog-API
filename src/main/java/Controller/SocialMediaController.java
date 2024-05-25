package Controller;

import java.util.List;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import Model.Account;
import Model.Message;
import Service.SocialMediaService;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class SocialMediaController {

    SocialMediaService socialMediaService;

    public SocialMediaController() {
        socialMediaService = new SocialMediaService();
    }

    /**
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageHandler);
        app.get("/accounts/{account_id}/messages", this::getUserMessagesHandler);
        app.post("/register", this::postRegistrationHandler);
        app.post("/login", this::postAuthenticationHandler);
        app.post("/messages", this::postMessageHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.patch("/messages/{message_id}", this::patchMessageHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    /**
     * This is a messages handler for a get messages endpoint.
     * @param ctx The Javalin Context object manages information about both the HTTP request and response.
     */
    private void getAllMessagesHandler(Context ctx) {
        ctx.json(socialMediaService.getAllMessages());
    }

    /**
     * This is a get single message handler for a get message by id endpoint.
     * @param ctx The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException
     */
    private void getMessageHandler(Context ctx) throws JsonProcessingException {
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message retrivedMessage = socialMediaService.getMessageById(message_id);

        if(retrivedMessage == null) {
            ctx.res();
        } else {
            ctx.json(retrivedMessage);
        }
    }

    /**
     * This is an account registration handler for a post register endpoint.
     * @param ctx The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException
     */
    private void postRegistrationHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        Account account = om.readValue(ctx.body(), Account.class);
        if(account.getUsername().length() <= 0 || account.getPassword().length() < 4) {
            ctx.status(400);
        } else {
            Account registeredAccount = socialMediaService.registerAccount(account);
            if (registeredAccount == null) {
                ctx.status(400);
            } else {
                ctx.json(om.writeValueAsString(registeredAccount));
            }
        }
    }

    /**
     * This is an authentication handler for a post login endpoint.
     * @param ctx The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException
     */
    private void postAuthenticationHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        Account account = om.readValue(ctx.body(), Account.class);
        Account authenticatedAccount = socialMediaService.authenticateAccount(account);
        if (authenticatedAccount == null) {
            ctx.status(401);
        } else {
            ctx.json(om.writeValueAsString(authenticatedAccount));
        }
    }

    /**
     * This is a message handler for a post message endpoint.
     * @param ctx The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException
     */
    private void postMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        Message message = om.readValue(ctx.body(), Message.class);
        if (message.getMessage_text().length() <= 0){
            ctx.status(400);
        } else{
            Message createdMessage = socialMediaService.createMessage(message);
            if (createdMessage == null) {
                ctx.status(400);
            } else {
                ctx.json(om.writeValueAsString(createdMessage));
            }
        }
    }

    /**
     * This is a delete message handler for a delete message_id endpoint.
     * @param ctx The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException
     */
    private void deleteMessageHandler(Context ctx) throws JsonProcessingException {
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message deletedMessage = socialMediaService.deleteMessage(message_id);

        if(deletedMessage == null) {
            ctx.res();
        } else {
            ctx.json(deletedMessage);
        }
    }

    /**
     * This is a patch message handler for a patch message_id endpoint.
     * @param ctx The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException
     */
    private void patchMessageHandler(Context ctx) throws JsonProcessingException {
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message updateableMessage = socialMediaService.getMessageById(message_id);

        if(updateableMessage == null) {
            ctx.status(400);
        } else {
            ObjectMapper om = new ObjectMapper();
            Message message = om.readValue(ctx.body(), Message.class);

            if (message.message_text.length() <= 0) {
                ctx.status(400);
            } else {
                Message patchedMessage = socialMediaService.patchMessage(message.message_text, message_id);

                if(patchedMessage == null) {
                    ctx.status(400);
                } else {
                    ctx.json(patchedMessage);
                }
            }
        }     
    }

    /**
     * This is a get all user messages handler for a get message by account id endpoint.
     * @param ctx The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException
     */
    private void getUserMessagesHandler(Context ctx) throws JsonProcessingException {
        int account_id = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> retrivedMessages = socialMediaService.getMessagesByUser(account_id);

        ctx.json(retrivedMessages);
    }
}