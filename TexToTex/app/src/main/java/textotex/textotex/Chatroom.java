package textotex.textotex;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Chatroom extends AppCompatActivity {
    private ChatViewMessage chatArrayAdapter;
    private ListView listView;
    private EditText chatText;
    private Button buttonSend;
    private boolean side = false;
    private cookieManager cm;
    private int conversationID = -1;
    public int userID = 0;
    public  JSONArray jsonArray ;
    public String chatName;
    private String cookie;
    public String urlDisplay;
    public  TextView txt;
    public String messageNow;
    public static List<List<String>> reponses ;
    public  ArrayList<String> reponsesBis;
    public static final int READ_TIMEOUT=15000;
    public static final int CONNECTION_TIMEOUT=10000;

    @Override
    public void onNewIntent(Intent newIntent) {
        this.setIntent(newIntent);
        }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.chatroom_main);

        cm = new cookieManager(this);

        cm.checkSession();

        buttonSend =  findViewById(R.id.send);

        listView =  findViewById(R.id.msgview);

        conversationID = getIntent().getIntExtra("conversationID", -1);
        chatName = getIntent().getStringExtra("conversationName");

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE);

        userID = sharedPref.getInt(getString(R.string.user_id_key), -1);
        cookie = sharedPref.getString(getString(R.string.cookie_key), "null");

        chatArrayAdapter = new ChatViewMessage(getApplicationContext(), R.layout.right);
        listView.setAdapter(chatArrayAdapter);
        chatText = findViewById(R.id.msg);

        try {
            getUserInfo ();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        chatText.setOnKeyListener(
                new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                {
                    try
                    {
                        return sendChatMessage();
                    }
                    catch (ExecutionException e)
                    {
                        e.printStackTrace();
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                try {
                    sendChatMessage();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setAdapter(chatArrayAdapter);

        //to scroll the list view to bottom on data change
        chatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged()
            {
                super.onChanged();
                listView.setSelection(chatArrayAdapter.getCount() - 1);
            }
        });
    }

    private void getUserInfo () throws ExecutionException, InterruptedException, JSONException  {
        AsyncTask toto = new getUsers().execute();
        String result = (String) toto.get();
        try {
            jsonArray = new JSONArray(result.trim());

            JSONObject ouimaisnon = null;

            reponsesBis = new ArrayList<String>(result.length());
            reponses = new ArrayList<List<String>>();

            for (int i = 0; i < jsonArray.length(); i++)
            {
                try {
                    reponses.add(i,new ArrayList<String>(1));
                    ouimaisnon = jsonArray.getJSONObject(i);

                    if(userID == Integer.valueOf(ouimaisnon.getString("idUser")))
                    {
                        String name = ouimaisnon.getString("lastName") + " " + ouimaisnon.getString("firstName");
                        boolean a = sendMessageSide(true, ouimaisnon.getString("content"),ouimaisnon.getString("date"),name);
                    }
                    else {
                        String name = ouimaisnon.getString("lastName") + " " + ouimaisnon.getString("firstName");
                        boolean a = sendMessageSide(false, ouimaisnon.getString("content"), ouimaisnon.getString("date"), name);
                    }

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (jsonArray != null)
        {
        }
    }

    private class getUsers extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(Chatroom.this);
        HttpURLConnection conn;
        URL url = null;
        @Override
        protected void onPreExecute() {
        super.onPreExecute();
        //this method will be running on UI thread
        pdLoading.setMessage("\tLoading...");
        pdLoading.setCancelable(false);
        pdLoading.show();
        }
        @Override
        protected String doInBackground(String... params) {
        try {
            // Enter URL address where your php file resides
            url = new URL( getString(R.string.url_base)+ getString(R.string.url_display_text));

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "exception";
        }
        try
        {
            // Setup HttpURLConnection class to send and receive data from php and mysql
            conn = (HttpURLConnection)url.openConnection();
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setRequestMethod("POST");
            // setDoInput and setDoOutput method depict handling of both send and receive
            conn.setDoInput(true);
            conn.setDoOutput(true);
            // Append parameters to URL
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("cookie", cookie)
                    .appendQueryParameter("conversationID", Integer.toString(conversationID))
                    .appendQueryParameter("userID", Integer.toString(userID));

            String query = builder.build().getEncodedQuery();

            // Open connection for sending data
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            conn.connect();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return "exception";
        }
        try {
            int response_code = conn.getResponseCode();

            // Check if successful connection made
            if (response_code == HttpURLConnection.HTTP_OK) {

                // Read data sent from server
                InputStream input = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder result = new StringBuilder();
                //String result;
                String line;

                while ((line = reader.readLine()) != null) {
                    if(line.compareTo("true") == 0)
                        continue;
                    else if(line.compareTo("false") == 0)
                    {
                        //TODO: Parse and print the error.
                        continue;
                    }
                    else
                        result.append(line + "\n");
                }
                // Pass data to onPostExecute method
                return(result.toString());

            }else{

                return("unsuccessful");
            }

        } catch (IOException e) {
            e.printStackTrace();
            return "exception";
        }
        finally
        {
            conn.disconnect();
        }
        }
        @Override
        protected void onPostExecute(String result)
        {
            //this method will be running on UI thread
            pdLoading.dismiss();
        }

    }

    private class SendMessageServer extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(Chatroom.this);
        HttpURLConnection conn;
        URL url = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                // Enter URL address where your php file resides
                url = new URL( getString(R.string.url_base)+ getString(R.string.url_add_message));

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
            try
            {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");
                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);
                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("cookie", cookie)
                        .appendQueryParameter("userID", String.valueOf(userID))
                        .appendQueryParameter("content",messageNow)
                        .appendQueryParameter("conversationID", String.valueOf(conversationID));
                String query = builder.build().getEncodedQuery();

                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return "exception";
            }
            try {
                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    //String result;
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line + "\n");
                    }
                    // Pass data to onPostExecute method
                    return(result.toString());

                }else{

                    return("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            }
            finally
            {
                conn.disconnect();
            }
        }
        @Override
        protected void onPostExecute(String result)
        {
            //this method will be running on UI thread
            //Dismiss the loading popup
            pdLoading.dismiss();

            //Parsing the result string
            if(result.contains("true"))
            {
                Toast.makeText(Chatroom.this, "Sent", Toast.LENGTH_LONG);
            }
            else if(result.contains("false"))
            {
                if(result.contains("error: "))
                {
                    String error_txt = result.substring(result.indexOf("error: ") + "error: ".length());

                    Toast.makeText(Chatroom.this, error_txt, Toast.LENGTH_LONG);

                    if(error_txt.contains("cookie"))
                    {
                        Chatroom.this.cm.checkSession();
                    }
                }
                else
                {
                    Toast.makeText(Chatroom.this, "Internal error..", Toast.LENGTH_LONG);
                }
            }

        }

    }

    private void setInfo()
    {
        for (int i = 0; i < reponses.size() ; i++)
        {
            if(userID == Integer.parseInt(reponses.get(i).get(0)))
            {
                //boolean a = sendMessageSide(true, reponses.get(i).get(3));
            }
            else
            {
                //boolean a = sendMessageSide(false, reponses.get(i).get(3));
            }
        }
    }
    private boolean sendChatMessage() throws ExecutionException, InterruptedException {
        side = true;
        messageNow = chatText.getText().toString();
        AsyncTask toto = new  SendMessageServer().execute();
        /*String result = (String) toto.get();
        sendMessageSide(false,result);*/
      //  chatArrayAdapter.add(new ManagerChat(side, messageNow));
        Date date = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss");
        chatArrayAdapter.add(new ManagerChat(side, messageNow, ft.format(date),"Me"));
        chatText.setText("");
        side = !side;
        return true;
    }

    private boolean sendMessageSide(boolean pside, String message , String date , String user)
    {
        chatArrayAdapter.add(new ManagerChat(pside, message,date,user));
        chatText.setText("");
        pside = !pside;
        return pside;
    }

    private class AsyncDeplayText extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(Chatroom.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }
        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides

                url = new URL(urlDisplay);

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
            try
            {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("Message", params[0])
                        .appendQueryParameter("userSource", params[1])
                        .appendQueryParameter("UserDestination", params[2])
                        .appendQueryParameter("Conversation", params[3]);

                String query = builder.build().getEncodedQuery();

                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return "exception";
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    //String result;

                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line + "\n");
                    }
                    jsonArray = new JSONArray(result.toString().trim());
                    // Pass data to onPostExecute method
                    return(jsonArray.toString());

                }else{

                    return("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            } catch (JSONException e) {
                e.printStackTrace();
                return "exception";
            }
            finally {
                conn.disconnect();
            }

        }
        @Override
        protected void onPostExecute(String result)
        {

            //this method will be running on UI thread

            pdLoading.dismiss();
            JSONObject ouimaisnon = null;
            String[] test = new String[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++)
            {
                try {
                        ouimaisnon = jsonArray.getJSONObject(i);
                        //initChatRoom(ouimaisnon.getString("message"), 1);
                        txt.setText(ouimaisnon.getString("idUser"));

                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    private class Async_Add_Get_On_Server  extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(Chatroom.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }
        @Override
        protected String doInBackground(String... params)
        {
            try
            {
                // Enter URL address where your php file resides
                String urlDisplay = getResources().getString(R.string.url_display_text);
                url = new URL(urlDisplay);

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("message", params[0])
                        .appendQueryParameter("isAdd", params[1]);
                String query = builder.build().getEncodedQuery();

                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return "exception";
            }
            try
            {
                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    //String result;

                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line + "\n");
                    }
                    jsonArray = new JSONArray(result.toString().trim());
                    // Pass data to onPostExecute method
                    return(jsonArray.toString());

                }else{

                    return("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            } catch (JSONException e) {
                e.printStackTrace();
                return "exception";
            }
            finally {
                conn.disconnect();
            }
        }
        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread

            pdLoading.dismiss();
            JSONObject ouimaisnon = null;
            String[] test = new String[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++)
            {
                try {
                    ouimaisnon = jsonArray.getJSONObject(i);
                    //initChatRoom(ouimaisnon.getString("message"), 1);
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}


