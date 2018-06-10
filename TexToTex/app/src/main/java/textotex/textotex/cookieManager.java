package textotex.textotex;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class cookieManager {

    final Context mContext;
    final SharedPreferences mSharedPref;
    final SharedPreferences.Editor mEditor;
    final ProgressDialog pdLoading;

    cookieManager(Context context) {
        this.mContext = context;
        this.mSharedPref = this.mContext.getSharedPreferences(this.mContext.getString(R.string.preference_file), Context.MODE_PRIVATE);
        this.mEditor = this.mSharedPref.edit();
        this.pdLoading = new ProgressDialog(this.mContext);
    }

    public void checkSession() {

        //Resetting the isLogged boolean to check everything
        mEditor.putBoolean(mContext.getString(R.string.is_logged_key), false);
        mEditor.commit();

        //Getting the cookie
        final String cookie = this.mSharedPref.getString(this.mContext.getString(R.string.cookie_key), "null");

        //If no cookie, put isLogged/cookie to "null"/false and start Logging Activity
        if(cookie == null || cookie.compareTo("null") == 0) {
            mEditor.putString(mContext.getString(R.string.cookie_key), "null");
            mEditor.commit();

            this.callLogin();

            return;
        }

        //Getting the login, if no, same
        final String login = mSharedPref.getString(mContext.getString(R.string.login_key), "null");
        if(login.compareTo("null") == 0){
            mEditor.putString(mContext.getString(R.string.cookie_key), "null");
            mEditor.commit();

            this.callLogin();

            return;
        }

        //The request queue..
        final RequestQueue queue = Volley.newRequestQueue(mContext);

        queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                pdLoading.dismiss();
            }
        });

        //TODO: Put a loading screen to prevent user inputs

        //The request..
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, mContext.getString(R.string.url_base) + mContext.getString(R.string.url_check_cookie),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.contains("true")){
                            mEditor.putBoolean(mContext.getString(R.string.is_logged_key), true);
                            mEditor.commit();
                        }
                        else if(response.contains("false"))
                        {
                            if(response.contains("error: ")) {
                                String error_str = response.substring(response.indexOf("error: ") + "error: ".length());
                                Toast.makeText(mContext, error_str, Toast.LENGTH_LONG);
                            }
                            callLogin();
                        }
                        else
                        {
                            callLogin();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("login", login);
                params.put("cookie", cookie);

                return params;
            }
        };

        pdLoading.setMessage("\tLoading...");
        pdLoading.setCancelable(false);
        pdLoading.show();

        queue.add(stringRequest);
    }

    private void callLogin() {
        //TODO: Finish the calling activity
        ((AppCompatActivity)mContext).finish();

        //Creating the new intent
        Intent newIntent = new Intent(mContext, LoginActivity.class);

        //Putting info in the bundle
        newIntent.putExtra("login", mContext.getString(R.string.login_key));

        //Starting the new activity
        mContext.startActivity(newIntent);
    }


}
