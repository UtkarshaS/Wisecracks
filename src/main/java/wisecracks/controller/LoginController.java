package main.java.wisecracks.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

class LoginController {
    public void login(HttpServletRequest request, HttpServletResponse response, String userId) {

        boolean userExists = //RDS Search query;
        if(!userExists)
        {
            //Create user entry in DB
            String firstName =; // retrive from Google - in Http request
            String lastName =; // retrive from Google - in Http request

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            //get current date time with Date()
            Date date = new Date();
            Date current = dateFormat.format(date);

            //Create a new user
            User(userId, firstName, lastName, current);
        }
    }
}