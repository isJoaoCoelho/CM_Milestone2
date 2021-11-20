package com.cm.milestone2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A fragment representing a list of Items.
 */
public class TitleFragment extends Fragment implements TaskManager.Callback{

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;

    private MainViewModel mViewModel;

    //LISTA DE TITLES
    List<NoteItemClass>  itemstmep;

    // Global buttons
    ImageView globalAdd;
    SearchView globalSearch;

    // Global recyclre view and adapter
    RecyclerView recyclerView;
    TitleRecyclerViewAdapter globaladapter;
    SearchView globalSearchView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TitleFragment() {
    }

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

        globalSearchView = (SearchView) searchmenu.getActionView();
        globalSearchView.setQueryHint(getString(R.string.shearch_index_toobar));

        globalSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                globaladapter.getFilter().filter(s);
                return false;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
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

        // Creates an global array that stores all the data
        if(mViewModel.getList().size() == 0) {
            itemstmep = new ArrayList<NoteItemClass>();
        }else {
            itemstmep = mViewModel.getList();
        }

        // change the name of the toolbar to the name of the project
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.app_name);

        // shared preferences loeader 2.0
        /*SharedPreferences prefs = getActivity().getPreferences(Context.MODE_PRIVATE);
        Map<String, ?> allEntries = prefs.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            itemstmep.add(new NoteItemClass(String.valueOf(entry.getKey()), entry.getValue().toString(),""));
        }*/
        //mViewModel.setList(itemstmep);

        //Get content of notes
        //TaskManager taskManager = new TaskManager();
        //taskManager.getContent(getActivity().getFilesDir().toString(), this, itemstmep, view.getContext());


        // Set the adapter
        Context context = view.getContext();
        recyclerView = view.findViewById(R.id.list);
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }

        globaladapter = new TitleRecyclerViewAdapter(itemstmep, new TitleRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(NoteItemClass item) {
                //mViewModel.setTitle(item.getContent());
                //mViewModel.setContent(item.getDetails());
                //mViewModel.setList(itemstmep);
                //mViewModel.setId(item.getId());
                mViewModel.setItem(item);

                // Apaga o querry do search view. Resolve o problema quando se passa para outro fragment enquanto se pesquisa
                // e todos os dados não procurardos são apagados no recycle view
                globalSearchView.setQuery("",false);

                String test = itemstmep.toString();

                MainInterface listener = (MainInterface) getActivity();
                listener.replaceFragment("edit");

            }

            @Override
            public void onLongItemClick(NoteItemClass item) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setTitle(R.string.Opcao).setItems(R.array.PopUpOption, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0){
                            //recyclerView.getAdapter().notifyItemRemoved(Integer.parseInt(item.getId())-1);
                            dialog.cancel();
                            changetext(item,itemstmep,recyclerView);

                        }else{
                            int location = itemstmep.indexOf(item);
                            itemstmep.remove(item);
                            mViewModel.setList(itemstmep);
                            //UpdateIds();
                            CleanandUpdateSharedPreferences();
                            //recyclerView.getAdapter().notifyItemRemoved(location);
                            recyclerView.getAdapter().notifyDataSetChanged();

                        }

                    }});

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        recyclerView.setAdapter(globaladapter);

        return view;
    }

    private void UpdateIds() {
        /*if(itemstmep.size()>0) {
            if (Integer.parseInt(itemstmep.get(0).getId()) > 0) {
                itemstmep.get(0).setId("0");
            }
            for (int i = 1; i < itemstmep.size(); i++) {
                int itemId = Integer.parseInt(itemstmep.get(i).getId());
                int previousId = Integer.parseInt(itemstmep.get(i - 1).getId());

                if (itemId - previousId > 1) {
                    int updatedId = previousId + 1;
                    itemstmep.get(i).setId(String.valueOf(updatedId));
                }
            }
        }*/
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    public void UpdateSharedPreferences(){
        // updates the shared preferences with the values
        SharedPreferences prefs = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();

        for (int i = 0; i< itemstmep.size(); i++){
            prefsEditor.putString(itemstmep.get(i).getId(), itemstmep.get(i).toString());
        }
        prefsEditor.apply();
    }

    public void CleanandUpdateSharedPreferences(){
        SharedPreferences prefs = getActivity().getPreferences(Context.MODE_PRIVATE);
        // CLean shares preferences
        prefs.edit().clear().apply();

        // updates the shared preferences with the values
        SharedPreferences.Editor prefsEditor = prefs.edit();
        for (int i = 0; i< itemstmep.size(); i++){
            prefsEditor.putString(itemstmep.get(i).getId(), itemstmep.get(i).toString());
        }
        prefsEditor.apply();
    }

    private void changetext(NoteItemClass item, List<NoteItemClass> itemstmep, RecyclerView recyclerView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        EditText editText = new EditText(getContext());

        final EditText edittext = new EditText(getContext());
        edittext.setHint(item.getContent());

        builder.setTitle(R.string.Context_change_text_label);

        builder.setView(edittext);

        builder.setPositiveButton(R.string.Change_name_string, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String YouEditTextValue = edittext.getText().toString();

                if(!YouEditTextValue.isEmpty()) {
                    int location = itemstmep.indexOf(item);
                    itemstmep.get(location).setContent(YouEditTextValue);
                    recyclerView.getAdapter().notifyItemChanged(location);
                }

            }
        });

        builder.setNegativeButton(R.string.cancel_change_name_string, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });

        builder.show();

    }

    private void AddNote(List<NoteItemClass> itemstmep, RecyclerView recyclerView) {
        // cria dialog box com opção de aceitar ou não e com edittext... altera também no recycler view isso
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        EditText editText = new EditText(getContext());
        final EditText edittext = new EditText(getContext());

        builder.setTitle(R.string.Add_name_title);
        builder.setView(edittext);
        builder.setPositiveButton(R.string.Add_name_value, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String YouEditTextValue = edittext.getText().toString();

                if(!YouEditTextValue.isEmpty()) {

                    int itemid = MakeUniqueId();
                    NoteItemClass item = new NoteItemClass(String.valueOf(itemid), YouEditTextValue, "");

                    itemstmep.add(item);
                    recyclerView.getAdapter().notifyItemInserted(itemstmep.size());
                    mViewModel.setList(itemstmep);

                    // update das shared prefrences quando a nota é adicionada
                    UpdateSharedPreferences();

                }
            }
        });

        builder.setNegativeButton(R.string.Cancel_name_value, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });

        builder.show();

    }

    private int MakeUniqueId(){
        // create a new unused id based in the datasets. Return the next available number of id

        if (itemstmep.size() == 0){
            return 0;
        }
        else{

            // search finds the bigest id
            int bigger = -1;
            for (NoteItemClass item : itemstmep){
                if (Integer.parseInt(item.getId())> -1) {
                    bigger = Integer.parseInt(item.getId());
                }
            }
            // adds 1
            int nextid = bigger + 1;
            //int nextid = Integer.parseInt(itemstmep.get(itemstmep.size()-1).getId()) + 1;
            return nextid;
        }

    }


    @Override
    public void onCompleteGet(List<NoteItemClass> list) {
        Toast.makeText(getActivity(), "Notas carregadas!" , Toast.LENGTH_LONG).show();
        itemstmep = new ArrayList<>(list);
        mViewModel.setList(list);
    }

    @Override
    public void onCompleteSave(List<NoteItemClass> list) {
        itemstmep = new ArrayList<>(list);
        mViewModel.setList(list);
        //TaskManager taskManager = new TaskManager();
        //taskManager.getContent(getActivity().getFilesDir().toString(), this, mViewModel.getList(), getContext());

    }
}