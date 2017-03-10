package com.connectto.notification;

import com.connectto.general.exception.HttpConnectionDeniedException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by htdev01 on 11/6/15.
 */
public class SMSNotification {

    public static String sendSMS(String userName, String userSurname, String partitionName, String bulkUserName, String bulkPassword, String verification, String phone)
            throws HttpConnectionDeniedException {

        String message = String.format("Dear %s %s. You successfully registered on %s verification code is %s", userName, userSurname, partitionName, verification);
        StringBuilder responseBuilder = new StringBuilder();
        try {
            // Construct data
            String data = "";
            //http://developer.bulksms.com/eapi/submission/send_sms/
            //
            /*
             * Note the suggested encoding for certain parameters, notably
             * the username, password and especially the message.  ISO-8859-1
             * is essentially the character set that we use for message bodies,
             * with a few exceptions for e.g. Greek characters.  For a full list,
             * see:  http://developer.bulksms.com/eapi/submission/character-encoding/
             */
            //hollor hi1915a24
            data += "username=" + URLEncoder.encode(bulkUserName, "ISO-8859-1");
            data += "&password=" + URLEncoder.encode(bulkPassword, "ISO-8859-1");
            data += "&message=" + URLEncoder.encode(message, "ISO-8859-1");
            data += "&want_report=1";
            data += "&msisdn=" + phone;

            System.out.println("Data " + data);
            // Send data
            // Please see the FAQ regarding HTTPS (port 443) and HTTP (port 80/5567)
            //URL url = new URL("https://bulksms.vsms.net/eapi/submission/send_sms/2/2.0");
            URL url = new URL("http://usa.bulksms.com/eapi/submission/send_sms/2/2.0");

            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();

            // Get the response
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                // Print the response output...
                responseBuilder.append(line);
                System.out.println(line);
            }
            wr.close();
            rd.close();
        } catch (Exception e) {
            throw new HttpConnectionDeniedException(e);
        }
        return responseBuilder.toString();
    }

    public static String sendCustomSMS(String partitionName, String bulkUserName, String bulkPassword, String msg, String phone) throws HttpConnectionDeniedException {

        String message = String.format("%s. %s", partitionName, msg);
        StringBuilder responseBuilder = new StringBuilder();
        try {
            // Construct data
            String data = "";
            /*
             * Note the suggested encoding for certain parameters, notably
             * the username, password and especially the message.  ISO-8859-1
             * is essentially the character set that we use for message bodies,
             * with a few exceptions for e.g. Greek characters.  For a full list,
             * see:  http://developer.bulksms.com/eapi/submission/character-encoding/
             */
            data += "username=" + URLEncoder.encode(bulkUserName, "ISO-8859-1");
            data += "&password=" + URLEncoder.encode(bulkPassword, "ISO-8859-1");
            data += "&message=" + URLEncoder.encode(message, "ISO-8859-1");
            data += "&want_report=1";
            data += "&msisdn=" + phone;

            // Send data
            // Please see the FAQ regarding HTTPS (port 443) and HTTP (port 80/5567)
            URL url = new URL("http://usa.bulksms.com/eapi/submission/send_sms/2/2.0");
            //URL url = new URL("https://bulksms.vsms.net/eapi/submission/send_sms/2/2.0");

            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();

            // Get the response
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                // Print the response output...
                responseBuilder.append(line);
                System.out.println(line);
            }
            wr.close();
            rd.close();
        } catch (Exception e) {
            throw new HttpConnectionDeniedException(e);
        }
        return responseBuilder.toString();
    }

}
