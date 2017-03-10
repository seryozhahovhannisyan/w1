package com.connectto.general.model;

import com.connectto.general.model.lcp.ResponseStatus;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Serozh
 * Date: 05.05.13
 * Time: 19:02
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement
public class ResponseDto implements Serializable {
    private ResponseStatus status;
    private List<String> messages;

    private Map<String,Object> response;

    public void addResponse(String key, Object value) {
        if(response == null || response.size() == 0 ){
            response = new HashMap<String, Object>();
        }
        response.put(key,value);
    }

    public void addMessage(String message) {
        if (messages == null) {
            messages = new ArrayList<String>();
        }
        messages.add(message);
    }

    public void cleanMessages() {
        if (messages != null) {
            messages.clear();
        }
        status = null;
    }



    public ResponseStatus getStatus() {
        return status;
    }

    public void setStatus(ResponseStatus status) {
        this.status = status;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    public Map<String, Object> getResponse() {
        return response;
    }

    public void setResponse(Map<String, Object> response) {
        this.response = response;
    }
}
