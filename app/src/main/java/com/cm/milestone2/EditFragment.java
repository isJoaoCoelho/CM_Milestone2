package com.cm.milestone2;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class EditFragment extends Fragment {

    // Variables
    private String NoteName;
    private MainViewModel mViewModel;

    //TODO fazer OnStop para guardar o conteudo do item que recebeu


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.edit_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        MainInterface listener = (MainInterface) getActivity();
        switch (item.getItemId()){
            case R.id.exit_item:

                listener.replaceFragment("main");
            case R.id.save_item:
                Toast.makeText(getContext(), "save clicado", Toast.LENGTH_LONG).show();

                listener.replaceFragment("main");
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //TODO mostrar conteudo do item que recebeu
        mViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        View view = inflater.inflate(R.layout.fragment_edit, container, false);
        String content = mViewModel.getContent();
        String title = mViewModel.getTitle();

        EditText contentEdit = view.findViewById(R.id.titleText);
        contentEdit.setText(content);

        // change the name of the toolbar to the name of the note
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(title);


        return view;
    }
}