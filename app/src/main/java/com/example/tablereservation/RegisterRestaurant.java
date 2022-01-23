package com.example.tablereservation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tablereservation.admin.AdminHomePage;
import com.example.tablereservation.restaurant.RestaurantHomePage;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;


public class RegisterRestaurant extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    Button reg_res_bt;

    EditText reg_res_full_name, reg_res_number, reg_res_user_name, reg_res_password, reg_res_confirm_pass;
    String reg_res_full_name_tx, reg_res_number_tx, reg_res_user_name_tx, reg_res_password_tx, reg_res_confirm_pass_tx, reg_res_adress_tx = "";

    String address="المروج", longitude="42.55919054820468",latitude="18.21223934457212";




    String[] items = new String[]{    "Al nasim"       ,      "Al nahdah "    ,  "Mansak"           ,
                                      "Al faisaliyah"  ,      "Shamasan"      ,  "Al sharafiyah"    ,
                                      "Al rasras"      ,      "Al aziziyah"   ,  "Al muftaha"       ,
                                      "Al mossa"       ,      "Al raqi"       ,  "Al mahallah"      ,  };

    String[] longi = new String[]{ "39.22964734554727"   , "39.12954986511228"   ,   "42.53862264633138" ,
                                   "39.181558975869706"  , "42.509127643858115"  ,   "42.50547203780598"  ,
                                   "42.7767971961928"    , "42.50069997972064"   ,   "42.49328890722894"  ,
                                   "42.77049369611658"   ,"42.75201372184749"    ,   "42.59182767886983"  , };

    String[] lati = new String[]{  "21.52158993243662"   , "21.61768890415657"   ,   "18.2179375808885"   ,
                                   "21.568061055753127"  , "18.229965654003617"  ,   "18.240178201748144" ,
                                   "18.250479197167085"  , "18.212159039479996"  ,   "18.21288633818587"  ,
                                   "18.330586791035213"  , "18.334862744600247"  ,   "18.276110309125087" , };



    private static final int REQUEST = 112;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_restaurant);


        reg_res_full_name = findViewById(R.id.reg_res_full_name);
        reg_res_number = findViewById(R.id.reg_res_number);
        reg_res_user_name = findViewById(R.id.reg_res_user_name);
        reg_res_password = findViewById(R.id.reg_res_password);
        reg_res_confirm_pass = findViewById(R.id.reg_res_confirm_pass);

        Spinner dropdown = findViewById(R.id.spinner1);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);

        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(this);


        reg_res_bt = findViewById(R.id.reg_res_bt);
        reg_res_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                reg_res_full_name_tx = reg_res_full_name.getText().toString();
                reg_res_number_tx = reg_res_number.getText().toString();
                reg_res_user_name_tx = reg_res_user_name.getText().toString();
                reg_res_password_tx = reg_res_password.getText().toString();
                reg_res_confirm_pass_tx = reg_res_confirm_pass.getText().toString();


                if (reg_res_full_name_tx.isEmpty()) {
                    reg_res_full_name.setError("This field is required");
                    return;
                }

                if (reg_res_number_tx.isEmpty()) {
                    reg_res_number.setError("This field is required");
                    return;
                }

                if (reg_res_user_name_tx.isEmpty()) {
                    reg_res_user_name.setError("This field is required");
                    return;
                }

                if (reg_res_password_tx.isEmpty()) {
                    reg_res_password.setError("This field is required");
                    return;
                }
                if (reg_res_password_tx.length() < 8) {
                    reg_res_password.setError("At least 6 characters");
                    return;
                }
                if (!isValidPassword(reg_res_password_tx)) {
                    reg_res_password.setError("password must contain [a-zA-Z0-9] and less than 24 ");
                    return;
                }
                if (!(reg_res_password_tx.equals(reg_res_confirm_pass_tx))) {
                    reg_res_password.setError("Passwords must match");
                    reg_res_confirm_pass.setError("Passwords must match");
                    return;
                }

                final String REQUEST_URL = "http://www.table-reservation2021.com/register.php";

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
                                    Log.d("response", obj.getString("data"));
                                    if (obj.getString("code").equals("-1")) {
                                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                                    }
                                    if (obj.getString("code").equals("1")) {
                                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                        finish();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                                Log.d("response", error.toString());

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
                        MyData.put("name", reg_res_full_name_tx);
                        MyData.put("phone", reg_res_number_tx);
                        MyData.put("username", reg_res_user_name_tx);
                        MyData.put("password", reg_res_password_tx);
                        MyData.put("user_type", "2");
                        MyData.put("log", longitude);
                        MyData.put("lag", latitude);

                        return MyData;
                    }
                };
                requestQueue.add(jsonRequest);
            }
        });
    }
    public static boolean isValidPassword(String s) {
        Pattern PASSWORD_PATTERN
                = Pattern.compile(
                "[a-zA-Z0-9]{8,24}");

        return !TextUtils.isEmpty(s) && PASSWORD_PATTERN.matcher(s).matches();
    }
    /*
        private static boolean hasPermissions(Context context, String... permissions) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
                for (String permission : permissions) {
                    if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                        return false;
                    }
                }
            }
            return true;
        }
    */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {



address=items[position];
longitude=longi[position];
latitude=lati[position];

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
/*
    private class MyLocationListener implements LocationListener {


        @SuppressLint("MissingPermission")
        @Override
        public void onLocationChanged(Location loc) {

            location = loc;
            LatLng mylocation = new LatLng(location.getLatitude(), location.getLongitude());
                getadress(mylocation);


        }


        private void getadress(LatLng mylocation) {


            String cityName = " ";
            Geocoder gcd = new Geocoder(getApplicationContext(), Locale.getDefault());
            List<Address> addresses;


                Log.d("Location : ", mylocation.latitude+" , "+mylocation.longitude);


            reg_res_adress_tx = "Location Stored";

            res_adress.setText(reg_res_adress_tx);


            progressDialog.dismiss();
        }









}}

*/