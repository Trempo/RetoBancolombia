package net;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Clase para implementar el servicio HTTP que desencripta un código de un cliente
 */
public class decryptAPI {
    public static String decrypt(String crypt){
        //Se crea la conexión como una petición GET al URL dado, concatenando el código a desencriptar.
        HttpURLConnection connection;
        try {
            //Create connection
            URL url = new URL("https://test.evalartapp.com/extapiquest/code_decrypt/" + crypt);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            //Stream del resultado
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));// or StringBuffer if Java version 5+
            String inputLine;
            String response ="";
            while ((inputLine = rd.readLine()) != null)
                response+=inputLine;
            rd.close();
            return response.split("\"")[1];
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return "";
    }
}
