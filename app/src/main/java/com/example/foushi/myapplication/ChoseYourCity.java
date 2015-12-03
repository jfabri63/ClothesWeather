package com.example.foushi.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.URLEncoder;
import java.util.ArrayList;

public class ChoseYourCity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener{

    private static final String LOG_TAG = "Google Autocomplete";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    private static final String API_KEY = "AIzaSyBWgxDrHQNVNDK0F2XUSWieHN1uVC4m7dU";
    private static final String GOOGLE_PLACE = "https://maps.googleapis.com/maps/api/place/details/json?placeid=";
    private final Volley helper = Volley.getInstance();
    private AutoCompleteTextView autoCompView;
    private Button b1 ;
    private SharedPreferences.Editor editor;
    private JSONObject rep;
    private String latitude;
    private String longitude;
    private String place;
    private ArrayList<String> placeid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chose_your_city);
        b1 =  (Button)findViewById(R.id.buttonclick);
        autoCompView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        autoCompView.setThreshold(0);
        autoCompView.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.list_item));
        autoCompView.setOnItemClickListener(this);
        b1.setOnClickListener(this);
        autoCompView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                b1.setEnabled(false);
                b1.setClickable(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        editor=pref.edit();
        if (pref.getString("ville", null) != null){
            autoCompView.setText(pref.getString("ville", null));
        }
        b1.setEnabled(false);
        b1.setClickable(false);
    }

    public void onItemClick(AdapterView adapterView, View view, int position, long id) {
        place = placeid.get(position);
        GetCoord();
        editor.putString("ville", autoCompView.getText().toString());
        editor.commit();
        b1.setEnabled(true);
        b1.setClickable(true);
    }

    public void onClick (View button) {
        switch (button.getId()) {
            case R.id.buttonclick:
                editor.putString("ville", autoCompView.getText().toString());
                editor.commit();
                this.finish();
        }
    }

    private void txtError(Exception e) {
        b1.setVisibility(View.VISIBLE);
        e.printStackTrace();
    }


    private ArrayList<String> autocomplete (String input) {
        ArrayList<String> resultList = null;
        StringBuilder sb;
        String url = "";
        try {
            sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?input=").append(URLEncoder.encode(input, "utf8"));
            sb.append("&types=(cities)");
            sb.append("&key=" + API_KEY);
            url = sb.toString();
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
        }
        CustomJsonRequest request = new CustomJsonRequest
                (Request.Method.GET,url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            rep =response;
                        } catch (Exception e) {
                            Log.e(LOG_TAG,"Error");
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        txtError(error);
                        Toast.makeText(ChoseYourCity.this, getResources().getString(R.string.ErreurChargement), Toast.LENGTH_SHORT).show();
                    }
                });
        request.setPriority(Request.Priority.HIGH);
        helper.add(request);
        if (rep!= null)
        {
            try {
                JSONArray predsJsonArray = rep.getJSONArray("predictions");
                resultList = new ArrayList<>(predsJsonArray.length());
                placeid = new ArrayList<>(predsJsonArray.length());
                for (int i = 0; i < predsJsonArray.length(); i++) {
                    resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
                    placeid.add(predsJsonArray.getJSONObject(i).getString("place_id"));
                }
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Cannot process JSON results", e);
            }
        }
        return resultList;
    }

    static class ViewHolder {
        TextView pville;
    }

    class GooglePlacesAutocompleteAdapter extends ArrayAdapter implements Filterable {
        private ArrayList resultList1;
        private Context context;

        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
            this.context = context;
        }

        public View getView(int r, View convertView, ViewGroup parent) {
            LayoutInflater mInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ViewHolder holder;
            if(convertView == null) {
                convertView  = mInflater.inflate(R.layout.list_item, parent,false);
                holder = new ViewHolder();
                holder.pville = (TextView) convertView.findViewById(R.id.autocompleteText);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }
            holder.pville.setText(getItem(r));
            return convertView;
        }

        @Override
        public int getCount() {
            return resultList1.size();
        }

        @Override
        public String getItem(int index) {
            return resultList1.get(index).toString();

        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList1 = autocomplete(constraint.toString());
                        if (resultList1!=null)
                        {
                            filterResults.values = resultList1;
                            filterResults.count = resultList1.size();
                        }// Assign the data to the FilterResults
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return (keyCode == KeyEvent.KEYCODE_BACK) || super.onKeyDown(keyCode, event);
    }

    private void GetCoord() {
        StringBuilder sb;
        String url = "";
        try {
            sb = new StringBuilder(GOOGLE_PLACE);
            sb.append(URLEncoder.encode(place, "utf8"));
            sb.append("&key=");
            sb.append(API_KEY);
            url = sb.toString();
        }
        catch (Exception e) {
            Log.e("API", "Error processing Places API URL", e);
        }
        CustomJsonRequest request = new CustomJsonRequest
                (Request.Method.GET,url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            latitude= response.getJSONObject("result").getJSONObject("geometry").getJSONObject("location").getString("lat");
                            longitude= response.getJSONObject("result").getJSONObject("geometry").getJSONObject("location").getString("lng");
                            editor.putString("lat", latitude);
                            editor.putString("lng", longitude);
                            editor.commit();
                        } catch (Exception e) {
                            txtError(e);
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        txtError(error);
                    }
                });
        request.setPriority(Request.Priority.HIGH);
        helper.add(request);
    }
}
