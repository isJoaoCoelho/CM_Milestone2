package com.cm.milestone2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A fragment representing a list of Items.
 */
public class TitleFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    private MainViewModel mViewModel;

    //LISTA DE TITLES
    List<NoteItemClass>  itemstmep;

    // Global buttons
    ImageView globalAdd;
    SearchView globalSearch;

    // Global recyclre view
    RecyclerView recyclerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TitleFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static TitleFragment newInstance(int columnCount) {
        TitleFragment fragment = new TitleFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }

        // allows options in the app bar
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        // when the menu is created in the title bar, this function is called. Search listener is in here too

        inflater.inflate(R.menu.titles_menu,menu);

        MenuItem searchmenu = menu.findItem(R.id.search_item_bar);

        SearchView searchView = (SearchView) searchmenu.getActionView();
        // todo meter como string.xml
        searchView.setQueryHint("Search here");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                // TODO meter numa função em separado
                // Todo meter com apadter filter

                ArrayList<NoteItemClass> temparray = new ArrayList<NoteItemClass>();

                for (int i = 0; i < itemstmep.size(); i++) {

                    if (itemstmep.get(i).content.contains(s)){
                        temparray.add(itemstmep.get(i));
                    }

                }

                itemstmep = temparray;
                recyclerView.getAdapter().notifyItemRangeChanged(0,itemstmep.size());
                //recyclerView.invalidate();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // TODO listener n deu, logo feito desta maneira. Pode ser feito de maneira mais bonita, n sendo necessário
        MainInterface listener = (MainInterface) getActivity();
        if (item.getItemId() == R.id.add_item_bar){
            AddNote(itemstmep,recyclerView);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        View view = inflater.inflate(R.layout.fragment_title_list, container, false);

        // TEMP
        itemstmep = new ArrayList<NoteItemClass>();
        //itemstmep.add(new NoteItemClass("1","test","abcs"));
        //itemstmep.add(new NoteItemClass("2","tost","abcs"));
        //itemstmep.add(new NoteItemClass("3","tcst","abcs"));
        //itemstmep.add(new NoteItemClass("4","tvbost","abcs"));

        // change the name of the toolbar to the name of the project
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.app_name);

        // Add some sample items.
        SharedPreferences prefs = getActivity().getPreferences(Context.MODE_PRIVATE);
        int defaultValueInt = 0;
        String defaultValueString = null;
        int size = prefs.getInt("size", defaultValueInt);

        for (int i = 0; i < size; i++) {
            String title = prefs.getString(String.valueOf(i), defaultValueString);

            itemstmep.add(new NoteItemClass(String.valueOf(i), title,"bla bla bla"));
        }


        // Set the adapter
        Context context = view.getContext();
        recyclerView = view.findViewById(R.id.list);
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        recyclerView.setAdapter(new TitleRecyclerViewAdapter(itemstmep, new TitleRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(NoteItemClass item) {

                mViewModel.setTitle(item.content);
                mViewModel.setContent(item.details);

                MainInterface listener = (MainInterface) getActivity();
                listener.replaceFragment("edit");

            }

            @Override
            public void onLongItemClick(NoteItemClass item) {
                //Toast.makeText(getContext(), "Item Long Clicked", Toast.LENGTH_LONG).show();

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setTitle(R.string.Opcao).setItems(R.array.PopUpOption, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0){
                            //recyclerView.getAdapter().notifyItemRemoved(Integer.parseInt(item.id)-1);
                            dialog.cancel();
                            changetext(item,itemstmep,recyclerView);

                        }else{
                            int location = itemstmep.indexOf(item);
                            itemstmep.remove(item);
                            recyclerView.getAdapter().notifyItemRemoved(location);
                            //recyclerView.getAdapter().notifyItemRangeChanged(location,itemstmep.size());

                        }

                    }});

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }));

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences prefs = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();

        for (int i = 0; i< itemstmep.size(); i++){
            prefsEditor.putString(String.valueOf(i), itemstmep.get(i).toString());
        }
        prefsEditor.putInt("size", itemstmep.size());
        prefsEditor.apply();
    }

    @Override
    public void onStop() {
        super.onStop();
        SharedPreferences prefs = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();

        for (int i = 0; i< itemstmep.size(); i++){
            prefsEditor.putString(String.valueOf(i), itemstmep.get(i).toString());
        }
        prefsEditor.putInt("size", itemstmep.size());
        prefsEditor.apply();
    }

    private void changetext(NoteItemClass item, List<NoteItemClass> itemstmep, RecyclerView recyclerView) {
        // todo: meter tudo em strings e arranjar codigo
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        EditText editText = new EditText(getContext());

        final EditText edittext = new EditText(getContext());
        edittext.setHint(item.content);

        builder.setTitle("Escolha o novo Nome");

        builder.setView(edittext);

        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String YouEditTextValue = edittext.getText().toString();

                // todo: meter com setter and getter os valores de item
                int location = itemstmep.indexOf(item);
                itemstmep.get(location).content = YouEditTextValue;
                recyclerView.getAdapter().notifyItemChanged(location);

            }
        });

        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });

        builder.show();

    }

    private void AddNote(List<NoteItemClass> itemstmep, RecyclerView recyclerView) {
        // todo: meter tudo em strings e arranjar codigo
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        EditText editText = new EditText(getContext());

        final EditText edittext = new EditText(getContext());

        builder.setTitle(R.string.Add_name_title);

        builder.setView(edittext);

        builder.setPositiveButton(R.string.Add_name_value, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String YouEditTextValue = edittext.getText().toString();

                // todo: meter o string id a mudar corretamente para um novo valor
                NoteItemClass item = new NoteItemClass("3",YouEditTextValue,"");

                // todo: meter com setter and getter os valores de item
                itemstmep.add(item);
                recyclerView.getAdapter().notifyItemInserted(itemstmep.size());

            }
        });

        builder.setNegativeButton(R.string.Cancel_name_value, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });

        builder.show();

    }


}