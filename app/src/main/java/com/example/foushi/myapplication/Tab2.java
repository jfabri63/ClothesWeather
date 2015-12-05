package com.example.foushi.myapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

public class Tab2 extends Fragment {

    public Tab2() {
    }

    @Bind(R.id.hautvetement)
    TextView txt1;
    @Bind(R.id.hautvetement2)
    TextView txt2;
    @Bind(R.id.bas)
    TextView txt3;
    @Bind(R.id.pluieWarning)
    TextView txt4;
    @Bind(R.id.image1)
    ImageView image1;
    @Bind(R.id.image2)
    ImageView image2;
    @Bind(R.id.image3)
    ImageView image3;
    @Bind(R.id.warning)
    ImageView warning;

    private EventBus bus = EventBus.getDefault();
    private SelectClothes select;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.vetement_layout, container, false);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View v = getView();
        ButterKnife.bind(this,v);
        select = new SelectClothes(txt1, txt2, txt3, txt4, image1, image2, image3, warning, getContext());
        bus.register(this);
    }

    public void onEvent(EventClothes event) {
        select.VetementTemp(event.getMoy(), event.isRaining());
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void test()
    {

    }


}