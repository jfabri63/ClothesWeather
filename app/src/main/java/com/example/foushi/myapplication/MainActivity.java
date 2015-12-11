package com.example.foushi.myapplication;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import models.PreferenceClass;
import fragment.ViewPageClass;

import butterknife.Bind;
import butterknife.ButterKnife;
import fragment.FragmentContact;
import fragment.FragmentExtension;


public class MainActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @Bind(R.id.nav_view)
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (new PreferenceClass(this).getVille().equals("Error")) {
            Intent i = new Intent(this, CityActivity.class);
            startActivity(i);
        }
        setContentView(R.layout.mainfragment);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        Fragment newFragment;
        newFragment = new ViewPageClass();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.mContent, newFragment).commit();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {


                if(menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);

                mDrawerLayout.closeDrawers();

                switch (menuItem.getItemId()){

                    case R.id.weather:
                        Toast.makeText(getApplicationContext(), "Météo", Toast.LENGTH_SHORT).show();
                        Fragment newFragment;
                        newFragment = new ViewPageClass();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.mContent, newFragment).commit();
                        return true;

                    case R.id.contact:
                        Toast.makeText(getApplicationContext(), "Contact", Toast.LENGTH_SHORT).show();
                        FragmentContact new1Fragment= new FragmentContact();
                        android.support.v4.app.FragmentManager fragmentManager1 = getSupportFragmentManager();
                        fragmentManager1.beginTransaction().replace(R.id.mContent, new1Fragment).commit();
                        return true;

                    case R.id.extension:
                        Toast.makeText(getApplicationContext(), "Extension", Toast.LENGTH_SHORT).show();
                        FragmentExtension new2Fragment= new FragmentExtension();
                        android.support.v4.app.FragmentManager fragmentManager2 = getSupportFragmentManager();
                        fragmentManager2.beginTransaction().replace(R.id.mContent, new2Fragment).commit();
                        return true;

                    default:
                        Toast.makeText(getApplicationContext(),"Somethings Wrong",Toast.LENGTH_SHORT).show();
                        return true;

                }
            }
        });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,toolbar,R.string.drawer_open, R.string.drawer_close){

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        mDrawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if (id==R.id.action_search)
        {
            Intent i = new Intent(this, CityActivity.class);
            startActivity(i);
           // startActivityForResult(i,100);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}