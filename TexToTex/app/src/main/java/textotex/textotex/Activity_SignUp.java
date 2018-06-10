package textotex.textotex;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

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
import java.util.concurrent.ExecutionException;

public class Activity_SignUp extends Activity
{
    public static final int READ_TIMEOUT=100;
    public static final int CONNECTION_TIMEOUT=100;
    private Button buttonSend;
    private EditText mail;
    private EditText passwd;
    private EditText passwdBis;
    private EditText userName;
    private CheckBox isAgree;
    public  String passwordS;
    public  String usernameS;
    public  String passwordBisS;
    public  String mailS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        isAgree  = findViewById(R.id.chkBox1);
        buttonSend = findViewById(R.id.btn_signUp);
        mail = findViewById(R.id.mail);
        passwd = findViewById(R.id.password);
        passwdBis = findViewById(R.id.bisPassword);
        userName = findViewById(R.id.username);

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                try {
                    sendInfoServer(arg0);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public boolean isFillallTextEdit ()     {
        usernameS = userName.getText().toString();
        passwordS = passwd.getText().toString();
        passwordBisS = passwdBis.getText().toString();
        mailS = mail.getText().toString();

        boolean isOk;
        if( passwordBisS == null || passwordBisS.isEmpty())
        {
            isOk = false;
            return isOk;
        }
        if( usernameS == null || usernameS.isEmpty())
        {
            isOk = false;
            return isOk;
        }
        if( passwordS == null || passwordS.isEmpty())
        {
            isOk = false;
            return isOk;
        }
        if( mailS == null || mailS.isEmpty())
        {
            isOk = false;
            isAgree.setText("Mail is empty or wrong !");
            return isOk;
        }
        if( !passwordBisS.equals(passwordS))
        {
            isOk = false;
            isAgree.setText("Wrong password ! Re-type again");
            //isAgree.setText(passwordBisS + " " + passwordS);
            passwdBis.setText("");
            passwd.setText("");
            return isOk;
        }
        isOk = true;
        return isOk;
    }

    public void sendInfoServer(View arg0) throws ExecutionException, InterruptedException {
        if(isAgree.isChecked())
        {
            if(isFillallTextEdit())
            {
                AsyncTask toto = new AsyncSendServer().execute();
                String result = (String) toto.get();
                isAgree.setText(result);
                if(result.compareTo("true") == 0)
                {
                    finish();
                    startActivity(new Intent(Activity_SignUp.this, LoginActivity.class));
                }
                else
                {
                    //isAgree.setText("This username already exist :'( ...");
                    isAgree.setTextColor(Color.YELLOW);
                }
            }
            else{
                isAgree.setTextColor(Color.RED);
            }

        }
        else
        {
            isAgree.setTextColor(Color.RED);
        }
        return;

    }

    private class AsyncSendServer extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(Activity_SignUp.this);
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
                url = new URL(getString(R.string.url_base) + getString(R.string.url_creatCompte));
                //url = new URL("http://10.105.249.0:8000/login.php");
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
                        .appendQueryParameter("username", usernameS)
                        .appendQueryParameter("password",passwordS)
                        .appendQueryParameter("mail",mailS);
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
                        result.append(line);
                    }
                    // Pass data to onPostExecute method
                    return(result.toString());

                }else{

                    return("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            } finally {
                conn.disconnect();
            }

        }
        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread
            pdLoading.dismiss();

        }
    }


}
