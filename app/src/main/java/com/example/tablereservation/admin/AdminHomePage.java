package com.example.tablereservation.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toolbar;

import com.example.tablereservation.MainActivity;
import com.example.tablereservation.R;
import com.example.tablereservation.restaurant.Reservation;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class AdminHomePage extends AppCompatActivity {


    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home_page);

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


    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}