package com.scorpiusenterprises.scorple.ary;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HomeFragment homeFragment = new HomeFragment();

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.container, homeFragment);
        transaction.commit();
    }

    @Override
    public void onFragmentInteraction(int type) {
        switch (type) {
            case 0:
                SuggestFragment suggestFragment = new SuggestFragment();

                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.container, suggestFragment);
                transaction.commit();
                break;
            case 1:
                HomeFragment homeFragment = new HomeFragment();

                fm = getSupportFragmentManager();
                transaction = fm.beginTransaction();
                transaction.replace(R.id.container, homeFragment);
                transaction.commit();
                break;
            default:
                break;
        }
    }
}
