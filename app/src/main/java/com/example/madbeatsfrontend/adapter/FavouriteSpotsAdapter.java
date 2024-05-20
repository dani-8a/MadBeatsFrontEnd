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

import java.util.ArrayList;
import java.util.List;

public class FavouriteSpotsAdapter extends RecyclerView.Adapter<FavouriteSpotsAdapter.EventViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Spot spot);
    }

    private List<Spot> spots = new ArrayList<>();
    private final OnItemClickListener listener;

    public FavouriteSpotsAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.spot_favourites_rv_item, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Spot spot = spots.get(position);
        holder.bind(spot, listener);
    }

    @Override
    public int getItemCount() {
        return spots.size();
    }

    public void setSpots(List<Spot> spots) {
        this.spots = spots;
        notifyDataSetChanged();
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        private final TextView spotNameTextView;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            spotNameTextView = itemView.findViewById(R.id.txtSpotNameFavouriteRV);
        }

        public void bind(final Spot spot, final OnItemClickListener listener) {
            spotNameTextView.setText(spot.getNameSpot());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(spot);
                }
            });
        }
    }
}
