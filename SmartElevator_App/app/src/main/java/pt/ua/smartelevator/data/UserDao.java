package pt.ua.smartelevator.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM users_table ORDER BY id ASC")
    LiveData<List<User>> getAll();

    @Query("SELECT COUNT() FROM users_table WHERE email LIKE :email AND " +
            "password LIKE :password LIMIT 1")
    int authenticate(String email, String password);

    @Query("SELECT COUNT() FROM users_table WHERE email = :email")
    int count(String email);

    @Query("SELECT * FROM users_table WHERE email = :email")
    User getUser(String email);

    @Insert
    void insertAll(User... users);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertUser(User user);

    @Delete
    void delete(User user);
}
