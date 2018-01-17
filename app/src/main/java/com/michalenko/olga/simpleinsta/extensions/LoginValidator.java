package com.michalenko.olga.simpleinsta.extensions;

import com.michalenko.olga.simpleinsta.model.AlertMessage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mac-242 on 1/17/18.
 */

public class LoginValidator {
    private String password;
    private String email;

    public LoginValidator(String email, String password){
        this.email = email;
        this.password = password;
    }

    public AlertMessage validateForm(){
        AlertMessage message = new AlertMessage();
        if(password.isEmpty() || password.length()<=5){
            message.setAlert(true);
            message.setMessage("Не верный формат пароля!");
        }
        if(!isEmailValid(email)){
            message.setAlert(true);
            message.setMessage("Не верный формат email!");
        }
        return message;
    }

    private static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}