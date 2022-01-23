package com.example.tablereservation.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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

import java.util.Objects;

public class Reservations extends AppCompatActivity {


    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;

    LinearLayout reservations_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservations);

        drawerLayout = findViewById(R.id.admin_drawer_layout2);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        navigationView = findViewById(R.id.admin_nav2);

        navigationView.setNavigationItemSelectedListener((item) -> {

            switch (item.getItemId()) {


                case R.id.restaurant_reristaration:

                    startActivity(new Intent(getApplicationContext(), RestaurantRegistration.class));
                    break;


                case R.id.customer_reristaration:

                    startActivity(new Intent(getApplicationContext(), CustomerRegistaration.class));
                    break;


                case R.id.view_restaurants:

                    startActivity(new Intent(getApplicationContext(), ViewRestaurants.class));
                    break;


                case R.id.view_customers:

                    startActivity(new Intent(getApplicationContext(), ViewCustomers.class));
                    break;

                case R.id.view_reservations:

                    startActivity(new Intent(getApplicationContext(), Reservations.class));
                    break;


                case R.id.log_out:

                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    break;


            }
            return true;

        });


        reservations_list = findViewById(R.id.reservations_list);


        final String REQUEST_URL = "http://www.table-reservation2021.com/get_admin_reservations.php";


        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest jsonRequest = new StringRequest(
                Request.Method.POST,
                REQUEST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        Log.d("response", response);


                        try {
                            JSONArray obj = new JSONArray(response);
                            Log.d("response", String.valueOf(obj));


                            for (int i = 0; i < obj.length(); i++) {

                                Log.d("\n\n\nresponse", String.valueOf(obj));

                                reservationList(obj.getJSONObject(i));


                            }
                        } catch (JSONException e) {

                            e.printStackTrace();


                            try {
                                JSONObject obj = new JSONObject(response);
                                if (obj.getString("code").equals("-1"))
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
                        Toast.makeText(getApplicationContext(), " error " + error.toString(), Toast.LENGTH_LONG).show();

                    }

                }) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                int mStatusCode = response.statusCode;
                Log.d("mStatusCode", String.valueOf(mStatusCode));


                return super.parseNetworkResponse(response);

            }


        };
        requestQueue.add(jsonRequest);


    }

    private void reservationList(JSONObject jsonObject) {


        try {
            JSONObject data = new JSONObject(jsonObject.getString("data"));


            View view = getLayoutInflater().inflate(R.layout.admin_reservations, null);


            TextView restaurant_name = view.findViewById(R.id.restaurant_name);
            TextView customer_name = view.findViewById(R.id.customer_name);
            TextView date = view.findViewById(R.id.date);
            TextView time = view.findViewById(R.id.time);


            restaurant_name.setText(data.getString("resturant_name"));
            customer_name.setText(data.getString("client_name"));
            date.setText(data.getString("date"));
            time.setText(data.getString("time"));


            reservations_list.addView(view);


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