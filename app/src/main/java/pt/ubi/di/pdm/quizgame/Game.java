package pt.ubi.di.pdm.quizgame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.app.Dialog;
import android.app.TaskInfo;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import java.io.File;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class Game extends AppCompatActivity {
    private AdapterQuestion adapter;
    private ArrayList<String> Files = new ArrayList<>();
    private ArrayList<Integer> Random = new ArrayList<>();
    ArrayList<User> Data;
    private Dialog Theme;
    private ListView Answer;
    private int round = 1; //Variável que vai indo contando em que round do jogo o jogador se encontrar
    private Integer rounds; //Variável onde fica armazenado o número de rounds selecionado pelo utilizador na activity anterior

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Files.add("questionVeryEasy.xml");
        Files.add("questionEasy.xml");
        Files.add("QuestionNormal.xml");
        Files.add("QuestionHard.xml");

        Data = getIntent().getParcelableArrayListExtra("playerlist");
        rounds = Integer.parseInt(getIntent().getStringExtra("rounds").toString().substring(0,2));
        List<Question> item = new ArrayList<>();
        adapter = new AdapterQuestion(this, android.R.layout.simple_list_item_1, item);
        Answer = findViewById(R.id.ListAnswer);

        for(User u: Data){
            u.setScore(0);
        }

        Answer.setAdapter(adapter);
        Theme = new Dialog(this);

        StartGame(GetNodeList(Files.get(0)), 0, Data, 0, DificultSelect());

        //ShowTheme();
        Handler handle = new Handler();
        handle.postDelayed(new Runnable() {
            @Override
            public void run() {
                Theme.dismiss();
            }
        }, 900);
    }

    private void StartGame(NodeList Node, int index, ArrayList<User> PlayersList, int player, ArrayList<Integer> ChangeDificulty){
        Question question = new Question();
        if (index != Node.getLength() && round <= rounds){
            ShowPlayer(PlayersList.get(player));
            Node node = Node.item(Random.get(index));
            Element element2 = (Element) node;
            question.setCategory(getValue("Category", element2));
            question.setDifficulty(Integer.valueOf(getValue("Difficulty", element2)));
            question.setQuestion(getValue("Tittle", element2));
            question.setAnswer1(getValue("Answer1", element2));
            question.setAnswer2(getValue("Answer2", element2));
            question.setAnswer3(getValue("Answer3", element2));
            question.setAnswer4(getValue("Answer4", element2));

            TextView data = findViewById(R.id.Data);
            data.setText(question.getQuestion());
            data.setBackground(ContextCompat.getDrawable(this, R.drawable.question));

            ShowTheme(question);
            WriteAnswer(question);
            round++;
        }   else{
            Intent finished = new Intent(Game.this, GameEnd.class);
            finished.putExtra("PlayerList", PlayersList);
            finished.putExtra("Rounds", String.valueOf(rounds));
            startActivity(finished);
        }
        List<TextView> TextList = new ArrayList<>();
        ViewGroup GroupAnswer = (ViewGroup) adapter.getView(0, null, (ViewGroup) Answer.getParent());
        //Drawable RigthDrawable = getResources().getDrawable(R.drawable.answertrue);
        Handler wait = new Handler();
        Answer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                wait.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (adapter.getCorreto() == 1) PlayersList.get(player).setScore(PlayersList.get(player).getScore() + 5);
                        if (PlayersList.size() != player + 1){
                            if (ChangeDificulty.contains(round))
                                StartGame(GetNodeList(Files.get(ChangeDificulty.indexOf(round) + 1)), 0, PlayersList, player + 1, ChangeDificulty);
                            else StartGame(Node, index + 1, PlayersList, player + 1, ChangeDificulty);
                        }   else {
                            if (ChangeDificulty.contains(round))
                                StartGame(GetNodeList(Files.get(ChangeDificulty.indexOf(round) + 1)), 0, PlayersList, 0, ChangeDificulty);
                            else StartGame(Node, index + 1, PlayersList, 0, ChangeDificulty);
                        }
                    }
                }, 850);
            }
        });
    }

    //Write the answer in ListView:
    private void WriteAnswer(Question question){
        adapter.clear();
        adapter.add(question);
    }

    private NodeList GetNodeList(String FileName){
        NodeList nList = new NodeList() {
            @Override
            public Node item(int i) {
                return null;
            }

            @Override
            public int getLength() {
                return 0;
            }
        };

        try {
            InputStream is = getAssets().open(FileName);

            DocumentBuilderFactory dbFatory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFatory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);

            Element element = doc.getDocumentElement();
            element.normalize();

            nList = doc.getElementsByTagName("Question");
        }   catch (Exception e) { e.printStackTrace(); }

        Random.clear();
        for(int index = 0; index < nList.getLength(); index++){
            Random.add(index);
        }
        Collections.shuffle(Random);

        return nList;
    }

    private void ShowTheme(Question q){
        Theme.setContentView(R.layout.themedialog);
        Theme.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView Tittle = Theme.findViewById(R.id.TiitlTheme);
        ImageView Img = Theme.findViewById(R.id.ThemeIMG);
        Tittle.setText(q.getCategory());
        if (q.getCategory().equals("Filosofia")) Img.setImageDrawable(getResources().getDrawable(R.drawable.filosofo));
        else if (q.getCategory().equals("História")) Img.setImageDrawable(getResources().getDrawable(R.drawable.historia));
        else if (q.getCategory().equals("Física")) Img.setImageDrawable(getResources().getDrawable(R.drawable.fisica));
        else if (q.getCategory().equals("Química")) Img.setImageDrawable(getResources().getDrawable(R.drawable.quimica));
        else if (q.getCategory().equals("Biologia")) Img.setImageDrawable(getResources().getDrawable(R.drawable.biologia));
        else if (q.getCategory().equals("Geografia")) Img.setImageDrawable(getResources().getDrawable(R.drawable.geografia));
        else if (q.getCategory().equals("Matemática")) Img.setImageDrawable(getResources().getDrawable(R.drawable.matematica));
        else if (q.getCategory().equals("Musica")) Img.setImageDrawable(getResources().getDrawable(R.drawable.musica));
        else if (q.getCategory().equals("Mitologia")) Img.setImageDrawable(getResources().getDrawable(R.drawable.mitologia));
        else if (q.getCategory().equals("Linguas")) Img.setImageDrawable(getResources().getDrawable(R.drawable.lingua));
        else if (q.getCategory().equals("Geral")) Img.setImageDrawable(getResources().getDrawable(R.drawable.geral));
        else if (q.getCategory().equals("Jogo")) Img.setImageDrawable(getResources().getDrawable(R.drawable.jogos));
        Theme.show();
        Handler wait = new Handler();
        wait.postDelayed(new Runnable() {
            @Override
            public void run() {
                Theme.dismiss();
            }
        }, 650);
    }


    private static String getValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = (Node) nodeList.item(0);
        return node.getNodeValue();
    }

    //Mostra os jogadores que estão a jogar na parte superios da tela
    //Recebe como argumento um user que é o utilizador que esta na jogador naquele momento
    //Para identificar esse jogador ele coloca uma borda cor de laranja a volta do nome do jogador.
    private void ShowPlayer(User user){
        LinearLayout PlayerBar = findViewById(R.id.PlayerBar);
        PlayerBar.removeAllViews();
        for (User u : Data){
            TextView PlayView = new TextView(this);
            PlayView.setText(u.getName() + " - " + String.valueOf(u.getScore()));
            if (u == user) PlayView.setBackground(ContextCompat.getDrawable(this, R.drawable.playerbox2));
            else PlayView.setBackground(ContextCompat.getDrawable(this, R.drawable.playerbox1));
            PlayerBar.addView(PlayView);
        }
    }

    //Função que retorna uma lista com os rounds onde irá haver uma troca de dificuldade.
    private ArrayList<Integer> DificultSelect(){
        ArrayList<Integer> List = new ArrayList<>();
        List.add(rounds/4 + 1);
        List.add(rounds/4 * 2 + 1);
        List.add(rounds/4 * 3 + 1);
        return List;
    }
}