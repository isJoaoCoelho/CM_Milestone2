package com.cm.milestone2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.cm.milestone2.placeholder.PlaceholderContent;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class TitleFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_title_list, container, false);

        // TEMP
        List<PlaceholderContent.PlaceholderItem>  itemstmep = new ArrayList<PlaceholderContent.PlaceholderItem>();
        // Add some sample items.
        for (int i = 1; i <= 25; i++) {
            itemstmep.add(new PlaceholderContent.PlaceholderItem(String.valueOf(i),"test " + String.valueOf(i),"vazio"));
        }


        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new TitleRecyclerViewAdapter(itemstmep, new TitleRecyclerViewAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(PlaceholderContent.PlaceholderItem item) {
                    Toast.makeText(getContext(), "Item Clicked", Toast.LENGTH_LONG).show();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.mainLayout, EditFragment.class, null)
                            .commit();
                }

                @Override
                public void onLongItemClick(PlaceholderContent.PlaceholderItem item) {
                    Toast.makeText(getContext(), "Item Long Clicked", Toast.LENGTH_LONG).show();

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

        }
        return view;
    }

    private void changetext(PlaceholderContent.PlaceholderItem item, List<PlaceholderContent.PlaceholderItem> itemstmep, RecyclerView recyclerView) {
        // todo: meter tudo em strings e arranjar codigo
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        EditText editText = new EditText(getContext());

        final EditText edittext = new EditText(getContext());
        edittext.setHint(item.content);

        builder.setTitle("Escolha o novo Nome");

        builder.setView(edittext);

        builder.setPositiveButton("Yes Option", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String YouEditTextValue = edittext.getText().toString();

                // todo: meter com setter and getter os valores de item
                int location = itemstmep.indexOf(item);
                itemstmep.get(location).content = YouEditTextValue;
                recyclerView.getAdapter().notifyItemChanged(location);

            }
        });

        builder.setNegativeButton("No Option", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });

        builder.show();

    }
}