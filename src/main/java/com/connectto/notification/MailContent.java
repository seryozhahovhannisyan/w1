package com.connectto.notification;

import javax.mail.Message;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : Serozh
 *         Date: 24.08.13
 *         Time: 1:55
 */
public class MailContent {

    private String subject;
    private String content;

    private Message.RecipientType recipientType;
    private String[] emailsTo;
    private String[] emailsCC;
    private String[] emailsBCC;
    private List<File> fileAttachments;

    private Map<String, String> dataSources;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Message.RecipientType getRecipientType() {
        return recipientType;
    }

    public void setRecipientType(Message.RecipientType recipientType) {
        this.recipientType = recipientType;
    }

    public void setRecipientTypeTo() {
        this.recipientType = Message.RecipientType.TO;
    }

    public String[] getEmailsTo() {
        return emailsTo;
    }

    public void setEmailsTo(String[] emailsTo) {
        this.emailsTo = emailsTo;
    }

    public String[] getEmailsCC() {
        return emailsCC;
    }

    public void setEmailsCC(String[] emailsCC) {
        this.emailsCC = emailsCC;
    }

    public String[] getEmailsBCC() {
        return emailsBCC;
    }

    public void setEmailsBCC(String[] emailsBCC) {
        this.emailsBCC = emailsBCC;
    }

    public List<File> getFileAttachments() {
        return fileAttachments;
    }

    public void setFileAttachments(List<File> fileAttachments) {
        this.fileAttachments = fileAttachments;
    }

    public Map<String, String> getDataSources() {
        return dataSources;
    }

    public void setDataSources(Map<String, String> dataSources) {
        this.dataSources = dataSources;
    }

    public void addDataSource(String data , String source) {
        if(dataSources == null){
            dataSources = new HashMap<String, String>();
        }
        dataSources.put(data, source);
    }
}