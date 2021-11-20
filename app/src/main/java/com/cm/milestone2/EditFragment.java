package com.cm.milestone2;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.provider.ContactsContract;
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

import java.util.ArrayList;
import java.util.List;

public class EditFragment extends Fragment implements TaskManager.Callback {

    // Variables
    private String NoteName;
    private MainViewModel mViewModel;
    private EditText contentEdit;

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
        int iditem = item.getItemId();
        switch (item.getItemId()){
            case R.id.exit_item:

                listener.replaceFragment("main");
                return super.onOptionsItemSelected(item);
            case R.id.save_item:
                //Toast.makeText(getContext(), "save clicado", Toast.LENGTH_LONG).show();
                TaskManager taskManager = new TaskManager();
                //mViewModel.setContent(contentEdit.getText().toString());
                List<NoteItemClass> list = new ArrayList<>(mViewModel.getList());
                for(int i = 0; i< list.size() ; i++) {
                        if(mViewModel.getItem().getId().equals(list.get(i).getId())) {
                            list.get(i).setDetails(contentEdit.getText().toString());
                            mViewModel.setList(list);
                        }
                }

                taskManager.saveContent(getActivity().getFilesDir().toString(), this, mViewModel.getList(), getContext());
                listener.replaceFragment("main");
                return super.onOptionsItemSelected(item);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        View view = inflater.inflate(R.layout.fragment_edit, container, false);
        NoteItemClass item = mViewModel.getItem();

        contentEdit = view.findViewById(R.id.titleText);
        contentEdit.setText(item.getDetails());

        // change the name of the toolbar to the name of the note
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(item.getContent());


        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onCompleteGet(List<NoteItemClass> list) {

    }

    @Override
    public void onCompleteSave(List<NoteItemClass> list) {
        mViewModel.setList(list);
    }
}