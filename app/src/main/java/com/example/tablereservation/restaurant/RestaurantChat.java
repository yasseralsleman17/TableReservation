package com.example.tablereservation.restaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tablereservation.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class RestaurantChat extends AppCompatActivity {


    LinearLayout linearmessages;

    ImageView bt_message;
    EditText ed_message;
    String myid, otherid;

    String mymessage;
Timer time=new Timer();
    @Override
    protected void onStop() {

        time.cancel();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_chat);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            myid = extras.getString("myid");
            otherid = extras.getString("otherid");

        }

        linearmessages = findViewById(R.id.linearmessages);


        ed_message = findViewById(R.id.ed_message);
        bt_message = findViewById(R.id.bt_message);


        getallmessage();



        bt_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mymessage = ed_message.getText().toString();

                if (!mymessage.isEmpty()) {

                    sendmymessage(mymessage);

                    ed_message.setText("");
                }
            }
        });


    }

    private void getallmessage() {


        final String REQUEST_URL = "http://www.table-reservation2021.com/read_all_message.php";


        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest jsonRequest = new StringRequest(
                Request.Method.POST,
                REQUEST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray obj = new JSONArray(response);
                            Log.d("response", String.valueOf(obj));

                            for (int i = 0; i < obj.length(); i++) {
                                Log.d("\n\n\nresponse", String.valueOf(obj));
                                if (obj.getJSONObject(i).getString("code").equals("2"))
                                    viewOtherMessage(obj.getJSONObject(i));
                                else if (obj.getJSONObject(i).getString("code").equals("1"))
                                    viewmyMessage(obj.getJSONObject(i));
                            }
                            time.scheduleAtFixedRate(new TimerTask() {
                                @Override
                                public void run() {
                                    getmessage();
                                }
                            }, 0, 2000);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }

                }) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                int mStatusCode = response.statusCode;
                Log.d("mStatusCode", String.valueOf(mStatusCode));


                return super.parseNetworkResponse(response);

            }

            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();

                MyData.put("r_id", otherid);
                MyData.put("s_id",myid );


                return MyData;
            }

        };
        requestQueue.add(jsonRequest);


    }

    private void viewmyMessage(JSONObject jsonObject) {

        try {
            JSONObject data = new JSONObject(jsonObject.getString("data"));
            View view = getLayoutInflater().inflate(R.layout.my_message, null);


            TextView username = view.findViewById(R.id.mymessagetext);
            username.setText(data.getString("content"));
            linearmessages.addView(view);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    private void getmessage() {

        final String REQUEST_URL = "http://www.table-reservation2021.com/read_message.php";

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest jsonRequest = new StringRequest(
                Request.Method.POST,
                REQUEST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray obj = new JSONArray(response);
                            Log.d("response", String.valueOf(obj));


                            for (int i = 0; i < obj.length(); i++) {

                                Log.d("\n\n\nresponse", String.valueOf(obj));

                                viewOtherMessage(obj.getJSONObject(i));


                            }
                        } catch (JSONException e) {

                            e.printStackTrace();


                        }


                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }

                }) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                int mStatusCode = response.statusCode;
                Log.d("mStatusCode", String.valueOf(mStatusCode));


                return super.parseNetworkResponse(response);

            }

            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();

                MyData.put("r_id", myid);
                MyData.put("s_id", otherid);


                return MyData;
            }

        };
        requestQueue.add(jsonRequest);


    }


    private void sendmymessage(String mymessage) {


        View view = getLayoutInflater().inflate(R.layout.my_message, null);


        TextView username = view.findViewById(R.id.mymessagetext);
        username.setText(mymessage);


        final String REQUEST_URL = "http://www.table-reservation2021.com/send_message.php";


        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest jsonRequest = new StringRequest(
                Request.Method.POST,
                REQUEST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        Log.d("response", response);


                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                    }

                }) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                int mStatusCode = response.statusCode;
                Log.d("mStatusCode", String.valueOf(mStatusCode));

                return super.parseNetworkResponse(response);

            }

            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();

                MyData.put("s_id", myid);
                MyData.put("r_id", otherid);
                MyData.put("content", mymessage);


                return MyData;
            }
        };
        requestQueue.add(jsonRequest);


        linearmessages.addView(view);


    }


    private void viewOtherMessage(JSONObject jsonObject) {

        try {
            JSONObject data = new JSONObject(jsonObject.getString("data"));
            View view = getLayoutInflater().inflate(R.layout.other_message, null);


            TextView username = view.findViewById(R.id.othermessagetext);
            username.setText(data.getString("content"));


            linearmessages.addView(view);


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


}