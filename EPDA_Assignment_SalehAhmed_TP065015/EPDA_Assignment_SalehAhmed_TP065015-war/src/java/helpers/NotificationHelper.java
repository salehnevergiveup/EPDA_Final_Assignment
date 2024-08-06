/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

/**
 *
 * @author saleh
 */

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class NotificationHelper {

        private static void clear(HttpServletRequest request) {  
            HttpSession session = request.getSession();
            session.removeAttribute("Validation_Error");
            session.removeAttribute("Error_Message");
            session.removeAttribute("Success_Message");
        }

    public static String displayNotifications(HttpServletRequest request) {
        HttpSession session = request.getSession();

        String validation = (String) HttpHelper.getSession(request, "Validation_Error");
        String errorMesage = (String) HttpHelper.getSession(request, "Error_Message");
        String successfull =  (String) HttpHelper.getSession(request, "Success_Message");
        String notifications = "";  

        if (validation != null && !validation.isEmpty()) {
            notifications =
                new ContentCreator()
                    .notificationPopup("Validation Error", validation, "","red")
                    .getContent();
            clear(request);
        }

        if (errorMesage != null && !errorMesage.isEmpty()) {
            notifications =
                new ContentCreator()
                    .notificationPopup("Error Message", errorMesage, "", "red")
                    .getContent();
            clear(request);
        }

        if (successfull != null && !successfull.isEmpty()) {
            notifications =
                new ContentCreator()
                    .notificationPopup("Success", successfull, "", "green")
                    .getContent();
            clear(request);
        }

        return  notifications;
    }
}