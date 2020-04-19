package com.example.chat.Utils;

import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

public class SendNotification {
    public SendNotification(String message, String heading, String notificationKey) throws JSONException {

        
        JSONObject notificationContent = new JSONObject("{ 'contents':{ 'en':'" + message + "'}," +
                "'include_player_ids':['"+notificationKey +"']," + "'headings':{'en': '"+ heading +"'}}");
        OneSignal.postNotification(notificationContent,null);
    }
}
