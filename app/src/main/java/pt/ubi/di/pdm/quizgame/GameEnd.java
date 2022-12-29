package pt.ubi.di.pdm.quizgame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import pt.ubi.di.pdm.quizgame.database.dao.UserDAO;
import pt.ubi.di.pdm.quizgame.database.database;

public class GameEnd extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_end);
        ArrayList<User> PlayerList = getIntent().getParcelableArrayListExtra("PlayerList");
        Integer Rounds = Integer.parseInt(getIntent().getStringExtra("Rounds"));
        LinearLayout PlayerTable = findViewById(R.id.PlayerTable);
        int MaxScore = (Rounds * 5) / PlayerList.size();

        //Initialize database and get dao:
        database db = Room.databaseBuilder(getApplicationContext(), database.class, "UserData").allowMainThreadQueries().build();
        UserDAO dao = db.getUserDAO();
        //********************************

        for (User Player : PlayerList){
            dao.update(Player);
            View view = getLayoutInflater().inflate(R.layout.tablerow, null);
            TextView name = view.findViewById(R.id.Playername);
            name.setText(Player.getName());
            TextView score = view.findViewById(R.id.PlayerScore);
            score.setText(String.valueOf(Player.getScore()) + "/" + String.valueOf(MaxScore));
            ProgressBar progressBar = view.findViewById(R.id.progressBar2);
            progressBar.setMax(MaxScore);
            progressBar.setProgress(Player.getScore());
            PlayerTable.addView(view);
        }
        Button Home = findViewById(R.id.Inicio);
        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GameEnd.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}