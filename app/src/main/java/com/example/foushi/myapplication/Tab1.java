package com.example.foushi.myapplication;

import android.support.v4.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class Tab1 extends Fragment {

    public String ville;
    private int lastIndex;
    public Temps[] predTemps;
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
                index = position;
                new WeatherLoader(getContext(), bar, index).loadWeatherData();
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
        bus.register(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    public void onEvent(EventWeather ev) {
        this.predTemps = ev.predTemps;
        updateWeather();
    }

    @Override
    public void onResume() {
        super.onResume();
        ville = new PreferenceClass(getActivity()).getVille();
        spinner.setSelection(0);
        String text5 = getResources().getString(R.string.Prevision) + " " + ville + " " + getResources().getString(R.string.Pourles) + " " + (spinner.getSelectedItemPosition() + 1) * 3 + " " + getResources().getString(R.string.Prochaine);
        textCity.setText(text5);
        new WeatherLoader(getContext(), bar, 0).GetCoord();
    }

    public void updateWeather() {
        PreferenceClass preferences = new PreferenceClass(getActivity());
        String temp = "Min : " + String.format("%.1f", predTemps[0].min) + " °C Max : " + String.format("%.1f", predTemps[0].max) + " °C";
        img1.setImageResource(WeatherLoader.getIcon(predTemps[0].code));
        textDesc.setText(predTemps[0].description + " (0-3h)");
        String ratio5 = "1/" + (index + 1);
        Ratio.setText(ratio5);
        textTemp.setText(temp);
        preferences.setDesc(predTemps[0].description);
        preferences.setTemp(temp);
        preferences.setCode(predTemps[0].code);
        String text = getResources().getString(R.string.Prevision) + " " + ville + " " + getResources().getString(R.string.Pourles) + " " + (index + 1) * 3 + " " + getResources().getString(R.string.Prochaine);
        textCity.setText(text);
        bar.setVisibility(CircleProgressBar.INVISIBLE);

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
        }
    }
}
