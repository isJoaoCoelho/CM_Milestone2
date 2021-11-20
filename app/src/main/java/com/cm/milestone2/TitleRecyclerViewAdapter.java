package com.cm.milestone2;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.cm.milestone2.databinding.FragmentTitleBinding;

import java.util.ArrayList;
import java.util.List;

public class TitleRecyclerViewAdapter extends RecyclerView.Adapter<TitleRecyclerViewAdapter.ViewHolder> implements Filterable {

    public interface OnItemClickListener {
        void onItemClick(NoteItemClass item);
        void onLongItemClick(NoteItemClass item);
    }

    private List<NoteItemClass> mValues;
    private List<NoteItemClass> mValuesFull;
    private final OnItemClickListener listener;

    public TitleRecyclerViewAdapter(List<NoteItemClass> items, OnItemClickListener listener) {
        this.mValues = items;
        this.mValuesFull = new ArrayList<>(items);
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentTitleBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.bind(mValues.get(position), listener);

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdView;
        public final TextView mContentView;
        public NoteItemClass mItem;

        public ViewHolder(@NonNull FragmentTitleBinding binding) {
            super(binding.getRoot());
            mIdView = binding.itemNumber;
            mContentView = binding.content;
        }

        public void bind(final NoteItemClass item, final OnItemClickListener listener){
            mIdView.setText(item.getId());
            mContentView.setText(item.getContent());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(item);
                }

            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    listener.onLongItemClick(item);
                    return true;
                }
            });
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }

    @Override
    public Filter getFilter() {
        return searchFilter;
    }

    private Filter searchFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<NoteItemClass> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0){
                filteredList.addAll(mValuesFull);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (NoteItemClass item : mValuesFull){
                    if (item.getContent().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }

            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mValues.clear();
            mValues.addAll((List)filterResults.values);
            notifyDataSetChanged();
        }
    };

}