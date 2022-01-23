package com.example.tablereservation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tablereservation.admin.AdminHomePage;
import com.example.tablereservation.customer.BrowseRestaurant;
import com.example.tablereservation.customer.RestaurantDetails;
import com.example.tablereservation.restaurant.RestaurantHomePage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LogIn extends AppCompatActivity {


    EditText log_user_name, log_password;

    Button log_bt;

    String log_user_name_tx, log_password_tx;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        log_user_name = findViewById(R.id.log_user_name);
        log_password = findViewById(R.id.log_password);

        log_bt = findViewById(R.id.log_bt);

        log_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                log_user_name_tx = log_user_name.getText().toString();
                log_password_tx = log_password.getText().toString();


                if (log_user_name_tx.isEmpty()) {
                    log_user_name.setError("This field is required");
                    return;
                }
                if (log_password_tx.isEmpty()) {
                    log_password.setError("This field is required");
                    return;
                }


                if (log_user_name_tx.equals("admin@gmail.com") && log_password_tx.equals("admin123")) {

                    startActivity(new Intent(getApplicationContext(), AdminHomePage.class));
                } else {
                    final String REQUEST_URL = "http://www.table-reservation2021.com/login.php";

                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    StringRequest jsonRequest = new StringRequest(
                            Request.Method.POST,
                            REQUEST_URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.d("response", response);
                                    try {
                                        JSONObject obj = new JSONObject(response);
                                        Log.d("response", obj.getString("code"));
                                        if (obj.getString("code").equals("-1")) {
                                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                                        } else {
                                            JSONObject data = new JSONObject(obj.getString("data"));

                                            if(data.getString("user_type").equals("1"))
                                            {
                                                Intent i = new Intent(getApplicationContext(), BrowseRestaurant.class);
                                                i.putExtra("id",data.getString("id"));
                                                startActivity(i);

                                            }
                                            else  if(data.getString("user_type").equals("2"))
                                            {
                                                Intent i = new Intent(getApplicationContext(), RestaurantHomePage.class);
                                                i.putExtra("id",data.getString("id"));
                                                startActivity(i);
                                            }
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

                            MyData.put("username", log_user_name_tx);
                            MyData.put("password", log_password_tx);


                            return MyData;
                        }
                    };
                    requestQueue.add(jsonRequest);


                }


            }
        });
    }
}