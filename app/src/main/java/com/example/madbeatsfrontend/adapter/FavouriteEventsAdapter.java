package com.example.madbeatsfrontend.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madbeatsfrontend.R;
import com.example.madbeatsfrontend.entity.Event;

import java.util.ArrayList;
import java.util.List;

public class FavouriteEventsAdapter extends RecyclerView.Adapter<FavouriteEventsAdapter.EventViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Event event);
    }

    private List<Event> events = new ArrayList<>();
    private final OnItemClickListener listener;

    public FavouriteEventsAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_favourites_rv_item, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = events.get(position);
        holder.bind(event, listener);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public void setEvents(List<Event> events) {
        this.events = events;
        notifyDataSetChanged();
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        private final TextView eventNameTextView;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            eventNameTextView = itemView.findViewById(R.id.txtEventNameFavouriteRV);
        }

        public void bind(final Event event, final OnItemClickListener listener) {
            eventNameTextView.setText(event.getNameEvent());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(event);
                }
            });
        }
    }
}
