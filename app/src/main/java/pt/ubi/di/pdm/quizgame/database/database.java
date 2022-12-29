package pt.ubi.di.pdm.quizgame.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import pt.ubi.di.pdm.quizgame.Game;
import pt.ubi.di.pdm.quizgame.User;
import pt.ubi.di.pdm.quizgame.database.dao.UserDAO;

@Database(entities = {User.class},version = 1, exportSchema = false)
public abstract class database extends RoomDatabase {
    public abstract UserDAO getUserDAO();
}
