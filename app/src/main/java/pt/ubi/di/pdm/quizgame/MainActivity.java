package pt.ubi.di.pdm.quizgame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import pt.ubi.di.pdm.quizgame.database.dao.UserDAO;
import pt.ubi.di.pdm.quizgame.database.database;

public class MainActivity extends AppCompatActivity {
    private Dialog InsertUser; //POPUP de adicionar jogador
    private Dialog PlayGame; //POPUP de começar o jogo
    private AdapterCustom adapter; //Custom Adapter
    private UserDAO dao; //DAO database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Initialize database and get dao:
        database db = Room.databaseBuilder(getApplicationContext(), database.class, "UserData").allowMainThreadQueries().build();
        dao = db.getUserDAO();
        //********************************

        if(dao.getAll().size() == 0){
            User Player1 = new User();
            Player1.setName("Player1");
            dao.insert(Player1);
        }

        ListView UserView = findViewById(R.id.ListUser); //ListView with contain the users:
        List<User> item = new ArrayList<>(); //ArrayList de itens que vão ser adicionados á ListView
        adapter = new AdapterCustom(this, android.R.layout.simple_list_item_1, item, dao);
        UserView.setAdapter(adapter);
        InsertUser = new Dialog(this);
        PlayGame = new Dialog(this);
        Button Play = findViewById(R.id.Play);

        Play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowDialogPlay();
            }
        });

        adapter.ShowPlayer();
    }

    //Criaçao e mostra ao utilizador o POPUP de jogar:
    private void ShowDialogPlay(){
        PlayGame.setContentView(R.layout.playpopup);
        PlayGame.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button Playbtn = PlayGame.findViewById(R.id.Play);
        TextView Tittle = PlayGame.findViewById(R.id.Players);
        RadioButton dez = PlayGame.findViewById(R.id.dez);
        RadioButton vinte = PlayGame.findViewById(R.id.vinte);
        RadioButton trinta = PlayGame.findViewById(R.id.trinta);
        LinearLayout ListPlayer = PlayGame.findViewById(R.id.HorizontalLayout);
        //Vereficar se foi escolhido algum jogador:
        if (adapter.getLastUser().size() == 0){
            Tittle.setText("Por favor selecione um jogador:");
            Tittle.setTextColor(Color.RED);
        }   else{
            for(User str : adapter.getLastUser()){
                TextView text = new TextView(PlayGame.getContext());
                text.setTextColor(Color.BLACK);
                text.setText(str.getName());
                text.setBackground(ContextCompat.getDrawable(PlayGame.getContext(), R.drawable.playerbox));
                ListPlayer.addView(text);
            }
            Playbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (dez.isChecked() || vinte.isChecked() || trinta.isChecked()){
                        Intent intent = new Intent(MainActivity.this, Game.class);
                        intent.putExtra("playerlist", adapter.getLastUser());
                        if (dez.isChecked()) intent.putExtra("rounds", dez.getText().toString());
                        else if (vinte.isChecked()) intent.putExtra("rounds", vinte.getText().toString());
                        else intent.putExtra("rounds", trinta.getText().toString());
                        startActivity(intent);
                    }
                }
            });
        }
        RadioButtonAddStyle(dez, PlayGame.getContext());
        RadioButtonAddStyle(vinte, PlayGame.getContext());
        RadioButtonAddStyle(trinta, PlayGame.getContext());
        PlayGame.show();

    }

    //Coloca um estilo custom no radio Button:
    private void RadioButtonAddStyle(RadioButton button, Context C){
        button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == true) button.setBackground(ContextCompat.getDrawable(C, R.drawable.customradiobuttontrue));
                else button.setBackground(ContextCompat.getDrawable(C, R.drawable.customradiubuttonfalse));
            }
        });
    }
}