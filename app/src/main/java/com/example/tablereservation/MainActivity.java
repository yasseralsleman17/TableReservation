package com.example.tablereservation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.tablereservation.customer.ReservationList;
import com.example.tablereservation.customer.RestaurantDetails;
import com.example.tablereservation.restaurant.RestaurantHomePage;

public class MainActivity extends AppCompatActivity {


    ImageView im_customer, im_restaurant;

    Button bt_sign_in, bt_customer, bt_restaurant;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        im_customer = findViewById(R.id.im_customer);
        im_restaurant = findViewById(R.id.im_restaurant);

        bt_sign_in = findViewById(R.id.bt_sign_in);
        bt_customer = findViewById(R.id.bt_customer);
        bt_restaurant = findViewById(R.id.bt_restaurant);


        bt_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LogIn.class));
               // startActivity(new Intent(getApplicationContext(), ReservationList.class));

            }
        });


        bt_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterCustomer.class));
               // startActivity(new Intent(getApplicationContext(), ReservationList.class));

            }
        });


        im_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterCustomer.class));

            }
        });


        bt_restaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterRestaurant.class));
               // startActivity(new Intent(getApplicationContext(), RestaurantHomePage.class));

            }
        });


        im_restaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterRestaurant.class));

            }
        });


    }
}