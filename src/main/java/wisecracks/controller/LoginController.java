package main.java.wisecracks.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

class LoginController {
    public void login(HttpServletRequest request, HttpServletResponse response, int userId) {

        boolean userExists = //RDS Search query;
        if(!userExists)
        {
            //Create user entry in DB
            String firstName =; // retrive from Google
            String lastName =; // retrive from Google

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            //get current date time with Date()
            Date date = new Date();
            Date current = dateFormat.format(date);

            User(userId, firstName, lastName, );
        }
    }
}