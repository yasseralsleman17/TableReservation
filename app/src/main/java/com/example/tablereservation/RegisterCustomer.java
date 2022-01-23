package com.example.tablereservation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.example.tablereservation.customer.ReservationList;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RegisterCustomer extends AppCompatActivity {


    Button reg_cus_bt;

    EditText reg_cus_full_name, reg_cus_number, reg_cus_user_name, reg_cus_password, reg_cus_confirm_pass;
    String reg_cus_full_name_tx, reg_cus_number_tx, reg_cus_user_name_tx, reg_cus_password_tx, reg_cus_confirm_pass_tx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_customer);


        reg_cus_full_name = findViewById(R.id.reg_cus_full_name);
        reg_cus_number = findViewById(R.id.reg_cus_number);
        reg_cus_user_name = findViewById(R.id.reg_cus_user_name);
        reg_cus_password = findViewById(R.id.reg_cus_password);
        reg_cus_confirm_pass = findViewById(R.id.reg_cus_confirm_pass);


        reg_cus_bt = findViewById(R.id.reg_cus_bt);
        reg_cus_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reg_cus_full_name_tx = reg_cus_full_name.getText().toString();
                reg_cus_number_tx = reg_cus_number.getText().toString();
                reg_cus_user_name_tx = reg_cus_user_name.getText().toString();
                reg_cus_password_tx = reg_cus_password.getText().toString();
                reg_cus_confirm_pass_tx = reg_cus_confirm_pass.getText().toString();


                if (reg_cus_full_name_tx.isEmpty()) {
                    reg_cus_full_name.setError("This field is required");
                    return;
                }
                if (reg_cus_number_tx.isEmpty()) {
                    reg_cus_number.setError("This field is required");
                    return;
                }
                if (reg_cus_user_name_tx.isEmpty()) {
                    reg_cus_user_name.setError("This field is required");
                    return;
                }
                if (reg_cus_password_tx.isEmpty()) {
                    reg_cus_password.setError("This field is required");
                    return;
                }
                if (reg_cus_password_tx.length() < 8) {
                    reg_cus_password.setError("At least 6 characters");
                    return;
                }
                if (!isValidPassword(reg_cus_password_tx)) {
                    reg_cus_password.setError("password must contain [a-zA-Z0-9] and less than 24 ");
                    return;
                }

                if (!(reg_cus_password_tx.equals(reg_cus_confirm_pass_tx))) {
                    reg_cus_password.setError("Passwords must match");
                    reg_cus_confirm_pass.setError("Passwords must match");
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
                                        finish();;

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
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

                    protected Map<String, String> getParams() {
                        Map<String, String> MyData = new HashMap<String, String>();
                        MyData.put("name", reg_cus_full_name_tx);
                        MyData.put("phone", reg_cus_number_tx);
                        MyData.put("username", reg_cus_user_name_tx);
                        MyData.put("password", reg_cus_password_tx);
                        MyData.put("user_type", "1");

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

}