package com.cm.milestone2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity implements MainInterface, TaskManager.Callback {

    List<NoteItemClass> list = new ArrayList<>();
    MainViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        populateList();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.mainLayout, TitleFragment.class, null)
                    .commit();
        }

        //Get content of notes



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

    public void populateList(){
        //Carregar do ficeiro de preferences
        //indexar aos items
        //Carregar do internal storage
        //colocar no viewmodel


        SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
        Map<String, ?> allEntries = prefs.getAll();

        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {

            list.add(new NoteItemClass(String.valueOf(entry.getKey()), entry.getValue().toString(),""));
        }
        list = orderList(list);

        getContent(getFilesDir().toString(), this, list, this);


    }

    public List<NoteItemClass> orderList(List<NoteItemClass> list){
        List<NoteItemClass> tempList = new ArrayList<>(list);
        List<NoteItemClass> returnList = new ArrayList<>();

        while(tempList.size() != 0){
            int menorID = -1;
            int menor = Integer.MAX_VALUE;
            for(int i = 0; i < tempList.size(); i++){
                if(Integer.parseInt(tempList.get(i).getId()) < menor){
                    menor = Integer.parseInt(tempList.get(i).getId());
                    menorID = i;
                }
            }
            returnList.add(tempList.get(menorID));
            tempList.remove(menorID);

        }
        return returnList;

    }

    public void getContent(String path, TaskManager.Callback callback, List<NoteItemClass> list, Context context) {

        Scanner scan;
        //scan = new Scanner(context.openFileInput("notes_ids.txt"));

        for (int j = 0; j < list.size(); j++) {
            try{
                //Por try catch
                scan = new Scanner(context.openFileInput(list.get(j).getId() + ".txt"));
                String details = scan.nextLine();
                list.get(j).setDetails(details);
                scan.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        //String[] array = content.split(separator);
        mViewModel.setList(list);
    }

    @Override
    public void onCompleteGet(List<NoteItemClass> list) {
        this.list = new ArrayList<>(list);
        mViewModel.setList(list);
    }

    @Override
    public void onCompleteSave(List<NoteItemClass> list) {

    }
}