package textotex.textotex;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
    ProgressBar progressBar;

    public convListFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle onSavedInstanceState) {

        View v = inflater.inflate(R.layout.conv_list_fragment, container, false);

        listData = new ArrayList<>();
        mListView = (ListView)v.findViewById(android.R.id.list);
      //  progressBar = (ProgressBar)v.findViewById(R.id.conv_list_progress);

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

                params.put("cookie", "mabite");
                params.put("userID", Integer.toString(1));

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

        Intent intent = new Intent(getActivity(), Chatroom.class);

        intent.putExtra("conversationID", conversationID);
        intent.putExtra("conversationName", conversationName);

        startActivity(intent);
    }
}
