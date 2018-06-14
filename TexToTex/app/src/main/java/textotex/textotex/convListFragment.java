package textotex.textotex;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class convListFragment extends ListFragment {

    List<convListData> listData;
    ListView mListView;
    Dialog myDialog;
    public Dialog myDialogBis;
    public TextView txtclose;
    public Button btnFollow;
    public Button search ;
    public int  userID ;
    public TextView nbConversation;
    public TextView nbFollowers;
    public TextView nbMessage;
    public TextView displayUserName;
    public EditText inputSearch;
    public String mCookie;
    public String mUsernameReach;
    public String mUsername;
    public SharedPreferences sharedPref;
    public boolean isNameExiste;
    public Dialog myDialogError;
    public boolean isAddNewFriend;

    public convListFragment(int userID, String cookie)
    {
        this.userID = userID;
        this.mCookie = cookie;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle onSavedInstanceState) {
        View v = inflater.inflate(R.layout.conv_list_fragment, container, false);

        listData = new ArrayList<>();
        mListView = (ListView)v.findViewById(android.R.id.list);
        myDialog = new Dialog(getContext());
        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);

        sharedPref = getActivity().getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE);

        userID = sharedPref.getInt(getString(R.string.user_id_key), -1);
        mCookie = sharedPref.getString(getString(R.string.cookie_key), "null");
        mUsername = sharedPref.getString(getString(R.string.login_key), "null");

        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                    myDialog.setContentView(R.layout.search_pop_up);
                    txtclose =(TextView) myDialog.findViewById(R.id.txtclose);
                     txtclose.setText("X");
                    search  = (Button) myDialog.findViewById(R.id.btnsearch);
                inputSearch = myDialog.findViewById(R.id.serachEdit);
                    txtclose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            myDialog.dismiss();
                        }
                    });

                search.setOnClickListener( new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v) {
                        mUsernameReach = inputSearch.getText().toString();
                        myDialog.dismiss();
                        myDialogBis = new Dialog(getContext());
                        myDialogBis.setContentView(R.layout.popup);
                        TextView txtclose =(TextView) myDialogBis.findViewById(R.id.txtclose);
                        txtclose.setText("X");
                        btnFollow = myDialogBis.findViewById(R.id.btnfollow);
                        displayUserName = myDialogBis.findViewById(R.id.nameSearch);
                        nbFollowers =  myDialogBis.findViewById(R.id.nbFollowers);
                        nbConversation =  myDialogBis.findViewById(R.id.nbChat);
                        nbMessage = myDialogBis.findViewById(R.id.nbMessage);

                        txtclose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                myDialogBis.dismiss();
                            }
                        });

                        btnFollow.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                createFriends();
                                myDialogBis.dismiss();
                                if(isAddNewFriend)
                                {
                                    Toast.makeText(getActivity(),  mUsernameReach + "is your friend now" , Toast.LENGTH_LONG).show();
                                }
                                else
                                {
                                    Toast.makeText(getActivity(), "OOPs! Something went wrong with "+ mUsernameReach +".", Toast.LENGTH_LONG).show();

                                }
                            }
                        });
                        if(mUsernameReach.isEmpty())
                        {
                            error(myDialog);
                        }
                        else {
                            getAndReachFriends();
                            if (isNameExiste) {
                                myDialogBis.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                myDialogBis.show();
                            } else {
                                error(myDialogBis);
                            }
                        }

                    }
                });
                    myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    myDialog.show();
            }
        });

        this.fetchListData();

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void fetchListData() {

        //making the progressbar visible
        //progressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, getString(R.string.url_base) + getString(R.string.url_conv_list),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                             //we have the array named hero inside the object
                            //so here we are getting that json array
                            JSONArray dataArray = new JSONArray(response);

                            //now looping through all the elements of the json array
                            for (int i = 0; i < dataArray.length(); i++) {
                                //getting the json object of the particular index inside the array
                                JSONObject dataObject = dataArray.getJSONObject(i);

                                //creating a hero object and giving them the values from json object
                                convListData data = new convListData(Integer.parseInt(dataObject.getString("idConversation")), dataObject.getString("name"), dataObject.getString("content"), dataObject.getString("date"), dataObject.getBoolean("unread"));

                                //adding the data to the datalist
                                listData.add(data);
                            }

                            //creating custom adapter object
                            convListAdapter adapter = new convListAdapter(getContext(), listData);

                            //adding the adapter to listview
                            setListAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("cookie", convListFragment.this.mCookie);
                params.put("userID", Integer.toString(convListFragment.this.userID));

                return params;
            }
        };

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        //adding the string request to request queue
        requestQueue.add(stringRequest);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        int conversationID = listData.get(position).getConversationID();
        String conversationName = listData.get(position).getConversationName();

        listData.get(position).toggleUnread();

        Intent intent = new Intent(getActivity(), Chatroom.class);

        intent.putExtra("conversationID", conversationID);
        intent.putExtra("conversationName", conversationName);

        startActivity(intent);
    }

    public void loadPopUpFriend()
    {

        getAndReachFriends();
    }

    public void error(Dialog dial)
    {
        dial.dismiss();
        if (myDialogError != null)
        {
            myDialogError.dismiss();
            myDialogError = null;
        }
        myDialogError = new Dialog(getContext());
        myDialogError.setContentView(R.layout.search_error);
        TextView txtcloseE =(TextView) myDialogError.findViewById(R.id.txtclose);

        txtcloseE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialogError.dismiss();
            }
        });
        myDialogError.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialogError.show();
        isNameExiste = false;
    }

    public void getAndReachFriends()
    {

        final RequestQueue mQueue = Volley.newRequestQueue(getContext());

        mQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
            }
        });

        //The request..
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getString(R.string.url_base) + getString(R.string.url_getInfoUser),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try
                        {
                            if (response.contains("false"))
                            {
                                isNameExiste = false;
                                return;
                            }
                            else
                                {
                                    //so here we are getting that json array
                                    JSONArray dataArray = new JSONArray(response);
                                    JSONObject dataObject ;//= new JSONObject(response);
                                    //getting the json object of the particular index inside the array
                                    dataObject = dataArray.getJSONObject(0);
                                    displayUserName.setText(mUsernameReach);
                                    nbConversation.setText(dataObject.getString("NbConversation"));
                                    nbFollowers.setText(dataObject.getString("Friends"));
                                    nbMessage.setText(dataObject.getString("NbMessage"));
                                    isNameExiste = true;
                                }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                            isNameExiste = false;
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
                params.put("friendName", mUsernameReach);
                params.put("cookie", mCookie);
                return params;
            }

        };
        mQueue.add(stringRequest);
    }

    public void createFriends()
    {

        final RequestQueue mQueue = Volley.newRequestQueue(getContext());

        mQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
            }
        });

        //The request..
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getString(R.string.url_base) + getString(R.string.url_addFriend),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.contains("true"))
                        {
                            isAddNewFriend = true;
                        }
                        else
                        {
                            isAddNewFriend = false;
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
                params.put("friendName", mUsernameReach);
                params.put("cookie", mCookie);
                params.put("idUser", String.valueOf(userID));
                return params;
            }

        };
        mQueue.add(stringRequest);
    }
}
