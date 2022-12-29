package pt.ubi.di.pdm.quizgame.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import pt.ubi.di.pdm.quizgame.User;

@Dao
public interface UserDAO {
    @Insert
    void insert(User user);
    @Delete
    void remove(User user);
    @Update
    void update(User user);
    @Query("SELECT * FROM user")
    List<User> getAll();
    @Query("SELECT Name FROM user")
    List<String> getNames();
}
