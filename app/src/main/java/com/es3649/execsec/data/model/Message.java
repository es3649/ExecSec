package com.es3649.execsec.data.model;

import com.es3649.execsec.nlp.NLPIntent;

import java.util.Date;
import java.util.List;

/**
 * Defines a message in a conversation
 *
 * Created by es3649 on 4/24/19.
 */

public class Message {
    private boolean fromMe;
    private String messageContent;
    private Date time;
    private List<NLPIntent> inferredIntents;
    private String suggestedResponse;

    public Message(String messageContent, List<NLPIntent> inferredIntents) {
        setMessageContent(messageContent);
        setInferredIntents(inferredIntents);
    }

    public void setFromMe(boolean fromMe) {this.fromMe = fromMe;}
    public void setInferredIntents(List<NLPIntent> inferredIntents) {this.inferredIntents = inferredIntents;}
    public void setTime(Date time) {this.time = time;}
    public void setMessageContent(String messageContent) {this.messageContent = messageContent;}
    public void setSuggestedResponse(String suggestedResponse) {this.suggestedResponse = suggestedResponse;}

    public boolean getFromMe() {return this.fromMe;}
    public List<NLPIntent> getIntents() {return inferredIntents;}
    public Date getTime() {return time;}
    public String getMessageContent() {return messageContent;}
    public String getSuggestedResponse() {return suggestedResponse;}
}
