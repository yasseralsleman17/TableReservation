package com.example.tablereservation.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tablereservation.MainActivity;
import com.example.tablereservation.R;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ReservationList extends AppCompatActivity {


    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;


    LinearLayout reservation_list_linear;


    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_list);



        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getString("id");

        }



        reservation_list_linear = findViewById(R.id.reservation_list_linear);


        drawerLayout = findViewById(R.id.reservation_list_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);


        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        navigationView = findViewById(R.id.reservation_list_nav);

        navigationView.setNavigationItemSelectedListener((item) -> {

            switch (item.getItemId()) {

                case R.id.search:

                    Intent i = new Intent(getApplicationContext(), BrowseRestaurant.class);
                    i.putExtra("id",id);
                    startActivity(i);



                    break;


                case R.id.reservation_list:

                    Intent i1 = new Intent(getApplicationContext(), ReservationList.class);
                    i1.putExtra("id",id);
                    startActivity(i1);

                    break;

                case R.id.log_out:
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));

                    break;

            }
            return true;

        });

        final String REQUEST_URL = "http://www.table-reservation2021.com/get_customer_reservation.php";

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

                                showreservation(obj.getJSONObject(i));


                            }
                        } catch (JSONException e) {

                            e.printStackTrace();


                            try {
                                JSONObject obj = new JSONObject(response);
                                if(obj.getString("code").equals("-1"))
                                    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();


                            } catch (JSONException jsonException) {
                                jsonException.printStackTrace();
                            }



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

                MyData.put("customer_id", id);



                return MyData;
            }
        };
        requestQueue.add(jsonRequest);
    }

    private void showreservation(JSONObject jsonObject) {

        try {
            JSONObject data = new JSONObject(jsonObject.getString("data"));


            View view = getLayoutInflater().inflate(R.layout.customer_reservation, null);


            TextView restaurant_name = view.findViewById(R.id.restaurant_name);
            TextView table_seats = view.findViewById(R.id.table_seats);
            TextView table_number = view.findViewById(R.id.table_number);
            TextView table_date = view.findViewById(R.id.table_date);
            TextView table_time = view.findViewById(R.id.table_time);
            TextView reservation_statue = view.findViewById(R.id.reservation_statue);


            restaurant_name.setText(data.getString("resturant_name"));
            table_seats.setText(data.getString("persons_no"));


            table_number.setText(data.getString("table_num"));
            table_date.setText(data.getString("date"));
            table_time.setText(data.getString("time"));

            if(data.getString("statue").equals("0"))
            reservation_statue.setText("waiting");
            if(data.getString("statue").equals("1"))
            reservation_statue.setText("Booked");


            String reservation_id=data.getString("reservation_id");


            Button btn_Cancel_reservation=view.findViewById(R.id.btn_Cancel_reservation);
            btn_Cancel_reservation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final String REQUEST_URL = "http://www.table-reservation2021.com/user_cancel_reservation.php";


                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    StringRequest jsonRequest = new StringRequest(
                            Request.Method.POST,
                            REQUEST_URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    Intent i1 = new Intent(getApplicationContext(), ReservationList.class);
                                    i1.putExtra("id",id);
                                    startActivity(i1);
finish();
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


                                MyData.put("reservation_id", reservation_id);

                            return MyData;
                        }
                    };
                    requestQueue.add(jsonRequest);


                }
            });

            reservation_list_linear.addView(view);


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}