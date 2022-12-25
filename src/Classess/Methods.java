package Classess;

import android.os.Build;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 5/31/2017.
 */
public class Methods {

    public static String httpGet(String url) {

        try {
            HttpClient client  = new DefaultHttpClient();
            HttpGet request = new HttpGet(url);
            request.setHeader("Content-type", "application/json");

            HttpResponse response = client.execute(request);

            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        }//end of try
        catch (Exception ex)
        {
            return "Error: " + ex.getMessage();
        }//end of catch
    }

    public static void disableConnectionReuseIfNecessary() {
        // Work around pre-Froyo bugs in HTTP connection reuse.
        if (Integer.parseInt(Build.VERSION.SDK) < Build.VERSION_CODES.FROYO) {
            System.setProperty("http.keepAlive", "false");
        }
    }

    public static String httpPUT(String url, String data)
    {
        try {
            HttpClient client = new DefaultHttpClient();

            HttpPut put = new HttpPut(url);

            put.setHeader("Content-type", "application/json");
            StringEntity postingString = new StringEntity(data, HTTP.UTF_8);//convert your pojo to   json
            put.setEntity(postingString);

            HttpResponse response = client.execute(put);

            String c = response.getStatusLine().getStatusCode() + "";
            System.out.println("Response Code : " + c);

            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            //return result.toString();
            return c;
        }//end of try
        catch (Exception ex)
        {
            return "Error: " + ex.getMessage();
        }//end of catch

    }

    public static class ServerResponse {
        public String ResponseCode;
        public String Content;
    }

    public static ServerResponse httpPUTServerError(String url, String data)
    {
        try {
            HttpClient client = new DefaultHttpClient();

            HttpPut put = new HttpPut(url);

            put.setHeader("Content-type", "application/json");
            StringEntity postingString = new StringEntity(data, HTTP.UTF_8);//convert your pojo to   json
            put.setEntity(postingString);

            HttpResponse response = client.execute(put);

            StringBuilder result = new StringBuilder();
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(response.getEntity().getContent()),65728);
            String line = null;

            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            String c = response.getStatusLine().getStatusCode() + "";
            System.out.println("Response Code : " + c);



            ServerResponse serverResponse=new ServerResponse();
            serverResponse.ResponseCode="Response Code : " + c;
            if(result!=null)
              serverResponse.Content=result.toString();


            return serverResponse;
        }//end of try
        catch (Exception ex)
        {
            ServerResponse serverResponse=new ServerResponse();
            serverResponse.ResponseCode="Error: " + ex.getMessage();
            serverResponse.Content="";
            return serverResponse;
        }//end of catch

    }


}
