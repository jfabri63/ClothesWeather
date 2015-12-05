package com.example.foushi.myapplication;

import android.support.v4.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import org.json.JSONObject;
import java.net.URLEncoder;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class Tab1 extends Fragment {

    private final Volley helper = Volley.getInstance();
    public String ville;
    private int lastIndex;
    public Temps[] predTemps = {null, null, null, null};
    public int index;
    private EventBus bus = EventBus.getDefault();

    public Tab1() {
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_main, container, false);
    }


    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View v = getView();
        ButterKnife.bind(this, v);
        index = 1;
        bar.setColorSchemeResources(android.R.color.holo_blue_light);
        bar.setVisibility(CircleProgressBar.INVISIBLE);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.temps, R.layout.spinner);
        adapter.setDropDownViewResource(R.layout.spinner);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadWeatherData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });
        PreferenceClass preferences = new PreferenceClass(getActivity());
        ville = preferences.getVille();
        String temp = preferences.getTemp();
        img1.setImageResource(WeatherLoader.getIcon(preferences.getCode()));
        String text = getResources().getString(R.string.Prevision) + " " + ville + " " + getResources().getString(R.string.Pourles) + " " + (spinner.getSelectedItemPosition() + 1) * 3 + " " + getResources().getString(R.string.Prochaine);
        textCity.setText(text);
        lastIndex = 0;
        int index1 = lastIndex * 3;
        int index2 = (lastIndex + 1) * 3;
        textDesc.setText(preferences.getDesc() + " (" + index1 + "-" + index2 + "h)");
        textTemp.setText(temp);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        ville = new PreferenceClass(getActivity()).getVille();
        spinner.setSelection(0);
        String text5 = getResources().getString(R.string.Prevision) + " " + ville + " " + getResources().getString(R.string.Pourles) + " " + (spinner.getSelectedItemPosition() + 1) * 3 + " " + getResources().getString(R.string.Prochaine);
        textCity.setText(text5);
        GetCoord();
    }

    private void GetCoord() {
        StringBuilder sb;
        String url = "";
        try {
            sb = new StringBuilder(getResources().getString(R.string.GoogleAutoplacePlaceId));
            sb.append(URLEncoder.encode(new PreferenceClass(getActivity()).getPlace(), "utf8"));
            sb.append("&key=");
            sb.append(getResources().getString(R.string.KeyGoogleAPI));
            url = sb.toString();
        } catch (Exception e) {
            Log.e("API", "Error processing Places API URL", e);
        }
        CustomJsonRequest request = new CustomJsonRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String latitude = response.getJSONObject("result").getJSONObject("geometry").getJSONObject("location").getString("lat");
                            String longitude = response.getJSONObject("result").getJSONObject("geometry").getJSONObject("location").getString("lng");
                            new PreferenceClass(getActivity()).setLat(latitude);
                            new PreferenceClass(getActivity()).setLong(longitude);
                            loadWeatherData();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.ErreurChargement), Toast.LENGTH_SHORT).show();
                    }
                });
        request.setPriority(Request.Priority.HIGH);
        helper.add(request);
    }

    private void loadWeatherData() {
        bar.setVisibility(CircleProgressBar.VISIBLE);
        final PreferenceClass preferences = new PreferenceClass(getActivity());
        String lat = preferences.getLat();
        String lng = preferences.getLong();
        StringBuilder sb;
        String url = null;
        try {
            sb = new StringBuilder(getResources().getString(R.string.OpenWeatherEndPoint));
            sb.append(URLEncoder.encode(lat, "utf8"));
            sb.append("&lon=");
            sb.append(URLEncoder.encode(lng, "utf8"));
            sb.append("&appid=");
            sb.append(getResources().getString(R.string.KEYOpenWeather));
            url = sb.toString();
        } catch (Exception e) {
            Log.e("API", "Error processing Places API URL", e);
        }
        CustomJsonRequest request = new CustomJsonRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            index = spinner.getSelectedItemPosition();
                            String minTemp, maxTemp, moyTemp;
                            String weathercode;
                            boolean pluie = false;
                            for (int i = 0; i <= 3; i++) {
                                if (i > index) {
                                    predTemps[i] = null;
                                } else {
                                    predTemps[i] = new Temps();
                                }

                            }
                            for (int i = 0; i <= index; i++)

                            {
                                String desc = "";
                                String code = response.getJSONArray("list").getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("icon");
                                predTemps[i].code = code;
                                switch (code) {
                                    case "01d":
                                        desc = getResources().getString(R.string.Soleil);
                                        break;
                                    case "01n":
                                        desc = getResources().getString(R.string.Soleil);
                                        break;
                                    case "02d":
                                        desc = getResources().getString(R.string.SoleilNuage);
                                        break;
                                    case "02n":
                                        desc = getResources().getString(R.string.SoleilNuage);
                                        break;
                                    case "03d":
                                        desc = getResources().getString(R.string.Nuageux);
                                        break;
                                    case "03n":
                                        desc = getResources().getString(R.string.Nuageux);
                                        break;
                                    case "04d":
                                        desc = getResources().getString(R.string.Nuageux);
                                        break;
                                    case "04n":
                                        desc = getResources().getString(R.string.Nuageux);
                                        break;
                                    case "09d":
                                        desc = getResources().getString(R.string.AttentionPluie);
                                        pluie = true;
                                        break;
                                    case "09n":
                                        desc = getResources().getString(R.string.AttentionPluie);
                                        pluie = true;
                                        break;
                                    case "10d":
                                        desc = getResources().getString(R.string.PluieSoleil);
                                        pluie = true;
                                        break;
                                    case "10n":
                                        desc = getResources().getString(R.string.PluieSoleil);
                                        pluie = true;
                                        break;
                                    case "11d":
                                        desc = getResources().getString(R.string.AttentonOrage);
                                        pluie = true;
                                        break;
                                    case "11n":
                                        desc = getResources().getString(R.string.AttentonOrage);
                                        pluie = true;
                                        break;
                                    case "13d":
                                        desc = getResources().getString(R.string.AttentionNeige);
                                        pluie = true;
                                        break;
                                    case "13n":
                                        desc = getResources().getString(R.string.AttentionNeige);
                                        pluie = true;
                                        break;
                                    case "50d":
                                        desc = getResources().getString(R.string.AttentionBrouillard);
                                        break;
                                    case "50n":
                                        desc = getResources().getString(R.string.AttentionBrouillard);
                                        break;
                                }
                                predTemps[i].description = desc;
                            }

                            double max = 0;
                            double min = 400;
                            double moy = 0;
                            for (int i = 0; i <= index; i++) {
                                maxTemp = response.getJSONArray("list").getJSONObject(i).getJSONObject("main").getString("temp_max");
                                minTemp = response.getJSONArray("list").getJSONObject(i).getJSONObject("main").getString("temp_min");
                                moyTemp = response.getJSONArray("list").getJSONObject(i).getJSONObject("main").getString("temp");
                                double temp = Double.parseDouble(maxTemp);
                                double temp2 = Double.parseDouble(minTemp);
                                double temp3 = Double.parseDouble(moyTemp);
                                if (temp > max) {
                                    max = temp;
                                }
                                if (temp2 < min) {
                                    min = temp2;
                                }
                                temp3 = temp3 - 273.15;
                                moy += temp3;
                            }
                            moy = moy / (index + 1);
                            min = min - 273.15;
                            max = max - 273.15;
                            String temp = "Min : " + String.format("%.1f", min) + " °C Max : " + String.format("%.1f", max) + " °C";
                            weathercode = response.getJSONArray("list").getJSONObject(0).getJSONArray("weather").getJSONObject(0).getString("icon");
                            img1.setImageResource(WeatherLoader.getIcon(weathercode));
                            textDesc.setText(predTemps[0].description + " (0-3h)");
                            String ratio5 = "1/" + (index + 1);
                            Ratio.setText(ratio5);
                            textTemp.setText(temp);
                            bar.setVisibility(CircleProgressBar.INVISIBLE);
                            preferences.setDesc(predTemps[0].description);
                            preferences.setTemp(temp);
                            preferences.setCode(predTemps[0].code);
                            bus.post(new EventClothes(moy, pluie));
                            String text = getResources().getString(R.string.Prevision) + " " + ville + " " + getResources().getString(R.string.Pourles) + " " + (spinner.getSelectedItemPosition() + 1) * 3 + " " + getResources().getString(R.string.Prochaine);
                            textCity.setText(text);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.ErreurChargement), Toast.LENGTH_SHORT).show();
                        bar.setVisibility(CircleProgressBar.INVISIBLE);
                    }
                });

        request.setPriority(Request.Priority.HIGH);
        helper.add(request);
    }

    @Bind(R.id.Text1)
    TextView textDesc;
    @Bind(R.id.Text2)
    TextView textTemp;
    @Bind(R.id.fab)
    FloatingActionButton fab;

    @OnClick(R.id.fab)
    protected void click() {
        CharSequence colors[] = new CharSequence[]{getResources().getString(R.string.SelectCity)};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getResources().getString(R.string.QueFaire));
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    Intent i = new Intent(getActivity(), CityActivity.class);
                    startActivity(i);
                }
            }
        });
        builder.show();
    }

    @Bind(R.id.ProgressBar)
    CircleProgressBar bar;
    @Bind(R.id.choix)
    CustomSpinner spinner;
    @Bind(R.id.HelloWorld)
    TextView textCity;
    @Bind(R.id.Img1)
    ImageView img1;
    @Bind(R.id.ratio)
    TextView Ratio;
    @Bind(R.id.Img3)
    ImageButton Img3;

    @OnClick(R.id.Img3)
    public void onClickImg3(View v) {
        if (predTemps[0] != null) {
            if (lastIndex < 3 && predTemps[lastIndex + 1] != null) {
                img1.setImageResource(WeatherLoader.getIcon(predTemps[lastIndex + 1].code));
                int index1 = (lastIndex + 1) * 3;
                int index2 = (lastIndex + 2) * 3;
                textDesc.setText(predTemps[lastIndex + 1].description + " (" + index1 + "-" + index2 + "h)");
                String ratio = (lastIndex + 2) + "/" + (index + 1);
                Ratio.setText(ratio);
                lastIndex++;
            } else {
                img1.setImageResource(WeatherLoader.getIcon(predTemps[0].code));
                int index1 = 0;
                int index2 = 3;
                textDesc.setText(predTemps[0].description + " (" + index1 + "-" + index2 + "h)");
                String ratio2 = "1/" + (index + 1);
                Ratio.setText(ratio2);
                lastIndex = 0;
            }
        } else {
            Log.i("MARCHE PAS", "TEST");
        }

    }

    @Bind(R.id.Img4)
    ImageButton Img4;

    @OnClick(R.id.Img4)
    public void onClickImg4(View v) {
        if (predTemps[0] != null) {
            if (lastIndex > 0 && predTemps[lastIndex - 1] != null) {
                img1.setImageResource(WeatherLoader.getIcon(predTemps[lastIndex - 1].code));
                int index1 = (lastIndex - 1) * 3;
                int index2 = (lastIndex) * 3;
                textDesc.setText(predTemps[lastIndex - 1].description + " (" + index1 + "-" + index2 + "h)");
                String ratio3 = (lastIndex) + "/" + (index + 1);
                Ratio.setText(ratio3);
                lastIndex--;
            } else {
                img1.setImageResource(WeatherLoader.getIcon(predTemps[index].code));
                int index1 = (index) * 3;
                int index2 = (index + 1) * 3;
                textDesc.setText(predTemps[index].description + " (" + index1 + "-" + index2 + "h)");
                String ratio4 = (index + 1) + "/" + (index + 1);
                Ratio.setText(ratio4);
                lastIndex = index;
            }
        } else {
            Log.i("MARCHE PAS", "TEST");
        }
    }
}
