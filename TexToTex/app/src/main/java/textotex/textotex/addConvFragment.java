package textotex.textotex;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class addConvFragment extends Fragment
{
    private EditText mUsernameView;

    public addConvFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle onSavedInstanceState){
        View rootView = inflater.inflate(R.layout.add_conv_fragment, container, false);

        mUsernameView = rootView.findViewById(R.id.add_conv_username);

        final Button mainButton = (Button) rootView.findViewById(R.id.bt_add_conv);
        mainButton.setOnClickListener(new View.OnClickListener() {
            final EditText mTargetUsername = (EditText) getActivity().findViewById(R.id.add_conv_username);

            @Override
            public void onClick(View v) {
                attemptAddConv();
            }
        });

        return rootView;
    }

    private void attemptAddConv() {

        final SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE);

        final String mUsername = sharedPref.getString(getString(R.string.login_key), "null");
        final String mCookie = sharedPref.getString(getString(R.string.cookie_key), "null");

        final String mTargetUsername = ((EditText) getActivity().findViewById(R.id.add_conv_username)).getText().toString();

        final RequestQueue mQueue = Volley.newRequestQueue(getContext());

        mQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
            }
        });

        //The request..
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getString(R.string.url_base) + getString(R.string.url_start_conv),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.contains("true")){
                            //Call fab's conv list with the id of the conv or smth

                            //Intent convActivity = new Intent(getContext(), chatRoom().class);
                            //convActivity.putExtra("idConversation", );

                            //startActivity(convÀctivity);
                            Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();

                        }
                        else if (response.contains("false")) {
                            //Put invalid username error prompt
                            String line = "";
                            String error_txt = "";

                            error_txt = response.substring(response.indexOf("false") + 6);

                            Toast.makeText(getActivity(), error_txt, Toast.LENGTH_LONG).show();

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //put connection problem or smth
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("login", mUsername);
                params.put("cookie", mCookie);
                params.put("target", mTargetUsername);
                return params;
            }

        };
        mQueue.add(stringRequest);
    }


}
