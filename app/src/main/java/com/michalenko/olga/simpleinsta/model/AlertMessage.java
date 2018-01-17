package com.michalenko.olga.simpleinsta.model;

/**
 * Created by mac-242 on 1/17/18.
 */

public class AlertMessage  {

    private String message;
    private Boolean isAlert;

    public AlertMessage(){
        message = "";
        isAlert = false;
    }

    public Boolean getAlert() {
        return isAlert;
    }

    public void setAlert(Boolean alert) {
        isAlert = alert;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}