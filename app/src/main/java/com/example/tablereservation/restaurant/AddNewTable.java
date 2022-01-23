package com.example.tablereservation.restaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tablereservation.MainActivity;
import com.example.tablereservation.R;
import com.example.tablereservation.Test;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddNewTable extends AppCompatActivity {
    Button btn_add_new_table;
    EditText table_seats, table_location, table_number;

    String id;
     ImageView imageView;

    JSONObject jsonObject;
    RequestQueue rQueue;
    final String REQUEST_URL = "http://www.table-reservation2021.com/add_table.php";
    Bitmap bitmap;
    String table_seats_txt, table_location_txt, table_number_txt,imgname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_table);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getString("id");

        }

        imageView = (ImageView) findViewById(R.id.iv);


        table_seats = findViewById(R.id.table_seats);
        table_location = findViewById(R.id.table_location);
        table_number = findViewById(R.id.table_number);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();

                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);

            }
        });
        btn_add_new_table = findViewById(R.id.btn_add_new_table);
        btn_add_new_table.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                table_seats_txt = table_seats.getText().toString();
                table_location_txt = table_location.getText().toString();
                table_number_txt = table_number.getText().toString();


                if (table_seats_txt.isEmpty())
                {
                    table_seats.setError("add seats number");
                    return;
                }

                if (table_location_txt.isEmpty())
                {
                    table_location.setError("add table location");

                    return;
                }

                if (table_number_txt.isEmpty())
                {
                    table_number.setError("add  table number");

                    return;
                }if (imgname.isEmpty())
                {
                    Toast.makeText(AddNewTable.this, "select an image !", Toast.LENGTH_SHORT).show();

                    return;
                }

                uploadImage(bitmap);


            }
        });




    }


    @Override
    public void onActivityResult ( int requestCode, int resultCode, Intent data){

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == 1) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                     bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    imageView.setImageBitmap(bitmap);
                     imgname = String.valueOf(Calendar.getInstance().getTimeInMillis());

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(AddNewTable.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    private void uploadImage (Bitmap bitmap){

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
        try {
            jsonObject = new JSONObject();
            jsonObject.put("name", imgname);
            jsonObject.put("image", encodedImage);
            jsonObject.put("resturant_id", id);
            jsonObject.put("seat_num", table_seats_txt);
            jsonObject.put("table_num", table_number_txt);
            jsonObject.put("location", table_location_txt);
        } catch (JSONException e) {
            Log.e("JSONObject Here", e.toString());
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, REQUEST_URL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        rQueue.getCache().clear();
                        Toast.makeText(getApplication(), "table added Successfully", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("aaaaaaa", volleyError.toString());
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rQueue = Volley.newRequestQueue(AddNewTable.this);

        rQueue.add(jsonObjectRequest);

    }


}