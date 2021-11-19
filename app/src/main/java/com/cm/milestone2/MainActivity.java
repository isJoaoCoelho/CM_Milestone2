package com.cm.milestone2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements MainInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.mainLayout, TitleFragment.class, null)
                    .commit();
        }
        MainViewModel mViewModel = new ViewModelProvider(this).get(MainViewModel.class);


    }

    @Override
    public void replaceFragment(String fragmentName) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment;
        if(fragmentName.equals("edit")) {
            fragment = new EditFragment();
            fragmentTransaction.replace(R.id.mainLayout, fragment, fragment.toString());
            fragmentTransaction.commit();
        }else if(fragmentName.equals("main")){
            fragment = new TitleFragment();
            fragmentTransaction.replace(R.id.mainLayout, fragment, fragment.toString());
            fragmentTransaction.commit();
        }
    }
}