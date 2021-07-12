package pt.ua.smartelevator.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName= "users_table")
public class User {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "Email")
    public String email;

    @ColumnInfo(name = "Password")
    public String password;

    @ColumnInfo(name = "Building")
    public String building;

    @ColumnInfo(name = "Floor")
    public int floor;
}

