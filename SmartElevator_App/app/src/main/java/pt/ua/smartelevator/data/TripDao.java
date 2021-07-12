package pt.ua.smartelevator.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TripDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addElevatorTrip(Trip trip);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addAll(Trip... trips);

    @Query(value = "SELECT * FROM trips_table ORDER BY id DESC")
    LiveData<List<Trip>> readTripData();

    @Query(value = "SELECT * FROM trips_table WHERE Building LIKE :building ORDER BY id DESC")
    LiveData<List<Trip>> getBuildingTrips(String building);

    @Query(value = "SELECT * FROM trips_table WHERE StartFloor LIKE :floor OR EndFloor LIKE :floor")
    LiveData<List<Trip>> getFloorTrips(int floor);

}