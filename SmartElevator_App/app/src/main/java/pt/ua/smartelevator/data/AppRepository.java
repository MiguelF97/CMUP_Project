package pt.ua.smartelevator.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class AppRepository {

    private UserDao userDao;
    private TripDao tripDao;
    private LiveData<List<Trip>> allTrips;
    private LiveData<List<User>> allUsers;

    AppRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        userDao = db.userDao();
        tripDao = db.tripDao();
        allUsers = userDao.getAll();
        allTrips = tripDao.readTripData();
    }

    LiveData<List<User>> getAllUsers() {
        return allUsers;
    }

    LiveData<List<Trip>> getAllTrips() {
        return allTrips;
    }

    void insertUser(User user) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            userDao.insertUser(user);
        });
    }

    User getUser(String email){
        return userDao.getUser(email);
    }

    boolean authenticate(String email, String pass){
        int c = userDao.authenticate(email, pass);
        return c == 1;
    }

    boolean checkEmailAvailable(String email){
        int c = userDao.count(email);
        return c == 0;
    }

    void insertTrip(Trip trip) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            tripDao.addElevatorTrip(trip);
        });
    }

    LiveData<List<Trip>> getBuildingTrips(String building) {
        return tripDao.getBuildingTrips(building);
    }


}
