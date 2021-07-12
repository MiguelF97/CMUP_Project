package pt.ua.smartelevator.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Time;

@Entity(tableName= "trips_table")
public class Trip {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "StartFloor")
    public String startFloor;

    @ColumnInfo(name = "EndFloor")
    public String endFloor;

    @ColumnInfo(name = "Time")
    public String time;

    @ColumnInfo(name = "Weight")
    public Float weight;

    @ColumnInfo(name = "Building")
    public String building;


    public static Trip fromString(String s){
        String[] parts = s.split("/*");
        Trip trip = new Trip();
        trip.startFloor = parts[0];
        trip.endFloor = parts[1];
        trip.time = parts[2];
        trip.weight = Float.parseFloat(parts[3]);
        trip.building = parts[4];
        return trip;
    }
}