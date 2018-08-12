package com.example.hp.map_experiment;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/**Simple search function to make sure thet only selected shops are marked.
  Here is where we need to add database searching function and filter only those shops which have the required medicine**/
public class Searching {
    final List<HashMap<String, String>> new_list=new ArrayList<>();
    HashMap<String, String> googlePlace;
    public List<HashMap<String, String>> selected (List<HashMap<String, String>> nearbyPlacesList,String txt)
    {
        String medicine = txt.toString();
          for (int i = 0; i < nearbyPlacesList.size(); i++) {
            googlePlace = nearbyPlacesList.get(i);
            String placeName = googlePlace.get("place_name");
            String [] details = {placeName,medicine};
            Background bcd = new Background();
            bcd.execute(details);
          }

          return new_list;
    }

    class Background extends AsyncTask<String, String, String> {

        protected String doInBackground(String... params) {
            String medshop = (String) params[0];
            String medicine = (String) params[1];
            medshop=medshop.replaceAll("[ ]","%20");


            String result = "";
            String link = "https://novemberat13.000webhostapp.com/search3.php?medshop="+medshop+"&medicine="+medicine;
            HttpClient client=new DefaultHttpClient();
            HttpGet request = new HttpGet();
            try {
                request.setURI(new URI(link));
                HttpResponse response =client.execute(request);
                StringBuffer stringBuffer =new StringBuffer("");
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                String line ="";
                while ((line=reader.readLine())!=null){
                    stringBuffer.append(line);
                    break;
                }
                reader.close();
                result=stringBuffer.toString();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (result.equalsIgnoreCase("success"))
                new_list.add(googlePlace);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            //if (result.equalsIgnoreCase("success"))
              //  new_list.add(googlePlace);


        }
    }

}
