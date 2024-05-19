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

public class EventListBySpotAdapter extends RecyclerView.Adapter<EventListBySpotAdapter.EventViewHolder> {

    private List<Event> eventList;
    private OnItemClickListener listener;

    public EventListBySpotAdapter(List<Event> eventList, OnItemClickListener listener) {
        this.eventList = eventList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_list_rv_item, parent, false);
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

    public Event getItem(int position) {
        return eventList.get(position);
    }

    public void clearEventList() {
        eventList.clear();
        notifyDataSetChanged();
    }


    static class EventViewHolder extends RecyclerView.ViewHolder {
        private TextView eventNameTextView;

        EventViewHolder(@NonNull View itemView) {
            super(itemView);
            eventNameTextView = itemView.findViewById(R.id.txtEventListRV);

        }

        void bind(Event event, OnItemClickListener listener) {
            eventNameTextView.setText(event.getNameEvent());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClick(event);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Event event);
    }

    public void updateEventList(List<Event> newList) {
        if (eventList == null) {
            eventList = new ArrayList<>(); // Inicializar eventList si es nulo
        }
        eventList.clear();
        eventList.addAll(newList);
        notifyDataSetChanged();
    }
}
