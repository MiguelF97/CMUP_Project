package pt.ua.smartelevator.data;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class AppViewModel extends AndroidViewModel {

    private AppRepository appRepository;

    private final LiveData<List<User>> allUsers;
    private final LiveData<List<Trip>> allTrips;

    public AppViewModel (Application application) {
        super(application);
        appRepository = new AppRepository(application);
        allTrips = appRepository.getAllTrips();
        allUsers = appRepository.getAllUsers();
    }

    public LiveData<List<User>> getAllUsers() { return allUsers; }

    public void insertUser(User user) { appRepository.insertUser(user); }

    public User getUser(String email){
       return appRepository.getUser(email);
    }

    public boolean authenticate(String email, String pass){
        return appRepository.authenticate(email, pass);
    }

    public boolean checkEmailAvailable(String email){
        return appRepository.checkEmailAvailable(email);
    }

    public LiveData<List<Trip>> getAllTrips() { return allTrips; }

    public LiveData<List<Trip>> getBuildingTrips(String building) {
        return appRepository.getBuildingTrips(building);
    }

    public void insertTrip(Trip trip) { appRepository.insertTrip(trip); }

}