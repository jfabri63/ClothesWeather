package com.example.foushi.myapplication;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.URLEncoder;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CityActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    public static final String LOG_TAG = "Google Autocomplete";
    private String place;
    PreferenceClass preference ;
    public static ArrayList<String> placeid;
    boolean back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chose_your_city);
        preference = new PreferenceClass(this);
        ButterKnife.bind(this);
        bar.setColorSchemeResources(android.R.color.holo_blue_light);
        bar.setVisibility(CircleProgressBar.INVISIBLE);
        autoCompView.setThreshold(0);
        autoCompView.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.list_item));
        autoCompView.setOnItemClickListener(this);
        back= false;
        String ville = new PreferenceClass(this).getVille();
        if (!ville.equals("Error")){
            autoCompView.setText(ville);
            back = true;
        }
        b1.setEnabled(false);
        b1.setClickable(false);
    }

    public void onItemClick(AdapterView adapterView, View view, int position, long id) {
        place = placeid.get(position);
        b1.setEnabled(true);
        b1.setClickable(true);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && !back)
        {
            System.exit(0);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Bind(R.id.ProgressBar2)
    CircleProgressBar bar;
    @Bind(R.id.buttonclick)
    Button b1;
    @OnClick(R.id.buttonclick)
    public void click ()
    {
        new PreferenceClass(this).setVille(autoCompView.getText().toString());
        new PreferenceClass(this).setPlace(place);
        this.finish();
    }
    @Bind(R.id.autoCompleteTextView)
    AutoCompleteTextView autoCompView;


}
