package com.example.madbeatsfrontend.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madbeatsfrontend.R;
import com.example.madbeatsfrontend.entity.Event;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private List<Event> eventList;
    private OnItemClickListener listener;

    public EventAdapter(List<Event> eventList, OnItemClickListener listener) {
        this.eventList = eventList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_rv_item, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.bind(event, listener);
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public void updateEventList(List<Event> newList) {
        eventList.clear();
        eventList.addAll(newList);
        notifyDataSetChanged();
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        private TextView eventNameTextView;
        private TextView eventDateTextView;
        private TextView eventMusicCatTextView;

        EventViewHolder(@NonNull View itemView) {
            super(itemView);
            eventNameTextView = itemView.findViewById(R.id.txtEventName);
            eventDateTextView = itemView.findViewById(R.id.txtEventDate);
            eventMusicCatTextView = itemView.findViewById(R.id.txtMusicCategory);
        }

        void bind(Event event, OnItemClickListener listener) {
            eventNameTextView.setText(event.getNameEvent());
            eventDateTextView.setText(event.getDate());
            eventMusicCatTextView.setText(event.getMusicCategory());

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(event);
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Event event);
    }
}
