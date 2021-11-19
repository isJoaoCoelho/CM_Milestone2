package com.cm.milestone2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class EditFragment extends Fragment {

    // Variables
    private String NoteName;
    private MainViewModel mViewModel;

    //TODO fazer OnStop para guardar o conteudo do item que recebeu

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

        ImageView returnbtn = view.findViewById(R.id.Arrow_return_icon);
        TextView notename = view.findViewById(R.id.Edit_note_name);

        // load arguments
        notename.setText(title);

        returnbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Maybe mudar para fora, na activity
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.mainLayout, TitleFragment.class, null)
                        .commit();
            }
        });

        return view;
    }
}