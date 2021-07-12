package pt.ua.smartelevator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import pt.ua.smartelevator.data.Trip;

public class TripListAdapter extends RecyclerView.Adapter<TripListAdapter.TripViewHolder> {

    private List<Trip> tripList = new ArrayList<>();

    @NonNull
    @NotNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new TripViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull TripViewHolder holder, int position) {
        Trip curr = this.tripList.get(position);
        holder.start.setText(curr.startFloor);
        holder.end.setText(curr.endFloor);
        holder.time.setText(curr.time);
    }

    @Override
    public int getItemCount() {
        return this.tripList.size();
    }

    public void setTripList(List<Trip> tripList){
        this.tripList = tripList;
        notifyDataSetChanged();
    }

    public static class TripViewHolder extends RecyclerView.ViewHolder {

        TextView start, end, time;


        public TripViewHolder(@NonNull @NotNull View itemView) {
                    super(itemView);
                    start = itemView.findViewById(R.id.start);
                    end = itemView.findViewById(R.id.end);
                    time = itemView.findViewById(R.id.time);

                }
        }
}
