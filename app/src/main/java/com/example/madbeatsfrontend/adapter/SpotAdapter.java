package com.example.madbeatsfrontend.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madbeatsfrontend.R;
import com.example.madbeatsfrontend.entity.Event;
import com.example.madbeatsfrontend.entity.Spot;

import java.util.List;

public class SpotAdapter extends RecyclerView.Adapter<SpotAdapter.SpotViewHolder> {

    private List<Spot> spotList;
    private OnItemClickListener listener;

    public SpotAdapter(List<Spot> spotList, OnItemClickListener listener) {
        this.spotList = spotList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SpotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.spot_rv_item, parent, false);
        return new SpotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SpotViewHolder holder, int position) {
        Spot spot = spotList.get(position);
        holder.bind(spot);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int clickedPosition = holder.getAdapterPosition();
                if (listener != null && clickedPosition != RecyclerView.NO_POSITION) {
                    listener.onItemClick(clickedPosition);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return spotList.size();
    }

    static class SpotViewHolder extends RecyclerView.ViewHolder {
        private TextView spotNameTextView;

        SpotViewHolder(@NonNull View itemView) {
            super(itemView);
            spotNameTextView = itemView.findViewById(R.id.txtSpotRV);
        }

        void bind(Spot spot) {
            spotNameTextView.setText(spot.getNameSpot());
            // Set other spot details to corresponding views if needed
        }

    }

    public void updateSpotList(List<Spot> newList) {
        spotList.clear();
        spotList.addAll(newList);
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public Spot getSpot(int position) {
        return spotList.get(position);
    }
}
