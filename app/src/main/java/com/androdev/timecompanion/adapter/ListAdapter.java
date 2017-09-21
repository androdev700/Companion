package com.androdev.timecompanion.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androdev.timecompanion.R;
import com.androdev.timecompanion.handler.AdapterClickListener;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyViewHolder> {

    private TextView text;
    private CardView card;
    private ArrayList<String> dataList;
    private final AdapterClickListener listener;

    public ListAdapter(ArrayList<String> dataList, AdapterClickListener listener) {
        this.dataList = dataList;
        this.listener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.list_text);
            card = itemView.findViewById(R.id.list_card);
        }

        void setData(final int pos, final AdapterClickListener listener) {
            text.setText(dataList.get(pos));
            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(view, pos);
                }
            });
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public ListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListAdapter.MyViewHolder holder, int position) {
        holder.setData(position, listener);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
