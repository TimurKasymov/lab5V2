package src;

import java.util.LinkedList;
import java.util.List;

public class MessageHandler {
    private LinkedList<String> loggs;
    /** displays the message to user */

    public void displayToUser(String message){
        System.out.println(message);
    }
    /** logs the message to log collection */
    public void log(String message){
        loggs.add(message);
    }

    /** gets the logs collection */
    public List<String> getLogs(){
        return loggs;
    }
}
