package com.example.tablereservation.restaurant;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.tablereservation.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Reservation#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Reservation extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static String id;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Reservation(String id) {
        this.id = id;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Reservation.
     */
    // TODO: Rename and change types and number of parameters
    public static Reservation newInstance(String param1, String param2) {
        Reservation fragment = new Reservation(id);
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    LinearLayout linearreservation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    private void viewreservationitem(JSONObject jsonObject) {

        try {
            JSONObject data = new JSONObject(jsonObject.getString("data"));




            View view = getLayoutInflater().inflate(R.layout.reservation_card, null);


            TextView table_number = view.findViewById(R.id.table_number);
            TextView person_number = view.findViewById(R.id.person_number);
            TextView date = view.findViewById(R.id.date);
            TextView time = view.findViewById(R.id.time);
            TextView accept = view.findViewById(R.id.accept);
            TextView reject = view.findViewById(R.id.reject);
            TextView accepted = view.findViewById(R.id.accepted);

            table_number.setText(data.getString("table_no"));
            person_number.setText(data.getString("persons_no"));
            date.setText(data.getString("date"));
            time.setText(data.getString("time"));
String reservation_id=data.getString("reservation_id");

            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                    AcceptRejectReservation(reservation_id,"1");

                    accepted.setText("  Accepted  ");
                    accept.setText("");
                    reject.setText("");
                }
            });


            reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AcceptRejectReservation(reservation_id,"2");

                    linearreservation.removeView(view);
                }
            });


            linearreservation.addView(view);




        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void AcceptRejectReservation(String id, String order) {



        final String REQUEST_URL = "http://www.table-reservation2021.com/accept_reject_reservation.php";


        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
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
                        Toast.makeText(getActivity(), " error " + error.toString(), Toast.LENGTH_LONG).show();

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

                MyData.put("reservation_id", id);
                MyData.put("order", order);

                return MyData;
            }

        };
        requestQueue.add(jsonRequest);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reservation, null);


        linearreservation = view.findViewById(R.id.linearreservation);




        final String REQUEST_URL = "http://www.table-reservation2021.com/get_waiting_reservation.php";


        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest jsonRequest = new StringRequest(
                Request.Method.POST,
                REQUEST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("response!!!!!",response);
                        try {
                            JSONArray obj = new JSONArray(response);
                            Log.d("response", String.valueOf(obj));



                            for (int i = 0; i < obj.length(); i++) {

                                Log.d("\n\n\nresponse", String.valueOf(obj));

                                viewreservationitem(obj.getJSONObject(i));


                            }
                        } catch (JSONException e) {

                            e.printStackTrace();


                            try {
                                JSONObject obj = new JSONObject(response);
                                if(obj.getString("code").equals("-1"))
                                    Toast.makeText(getActivity(), obj.getString("message"), Toast.LENGTH_LONG).show();


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
                MyData.put("r_id",id);

                return MyData;
            }
        };
        requestQueue.add(jsonRequest);






        return view;
    }
}