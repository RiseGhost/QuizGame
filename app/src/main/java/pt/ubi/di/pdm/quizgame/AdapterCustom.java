package pt.ubi.di.pdm.quizgame;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import pt.ubi.di.pdm.quizgame.database.dao.UserDAO;

public class AdapterCustom extends ArrayAdapter<User> {
    private Context context;
    private int resource;
    private List<User> U;
    public ArrayList<User> user = new ArrayList<>();
    private UserDAO dao;
    private Dialog InsertUser;
    private int CountAllUser;

    public AdapterCustom(@NonNull Context context, int resource, @NonNull List<User> objects, UserDAO dao) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.U = objects;
        this.dao = dao;
        this.CountAllUser = dao.getAll().size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        User player = U.get(position);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        if (CountAllUser - 1 == position) {
            view = inflater.inflate(R.layout.userlist, null);
            TextView AddPlayer = view.findViewById(R.id.AddPlayerTextView);
            AddPlayer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ShowDialogAddPlayer();
                }
            });
        }
        else {view = inflater.inflate(R.layout.users, null);}
        TextView Name = view.findViewById(R.id._Name);
        TextView Level = view.findViewById(R.id._Level);
        ImageView Img = view.findViewById(R.id.Image);
        InsertUser = new Dialog(context);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user.contains(player)) {
                    Name.setText(player.getName());
                    user.remove(player);
                }
                else {
                    Name.setText("✔" + player.getName());
                    user.add(player);
                }
            }
        });
        if (user.contains(player)) Name.setText("✔" + player.getName());
        else Name.setText(player.getName());
        Level.setText(String.valueOf(U.get(position).getScore()));
        Img.setImageResource(R.drawable.gamecontrol);
        return view;
    }

    //Retorna uma ArrayList com os User que estão selecionados:
    public ArrayList<User> getLastUser(){
        return user;
    }

    //Anexa os jogadores ao adapter, que por sua vez são adicionados a ListView
    public void ShowPlayer(){
        CountAllUser = dao.getAll().size();
        this.user.clear();
        List<User> UserList = dao.getAll();
        for (int index = this.getCount(); index < UserList.size(); index++){
            this.add(UserList.get(index));
        }
    }

    //Cria e mostrar o POPUP de adicionar um jogador:
    private void ShowDialogAddPlayer(){
        InsertUser.setContentView(R.layout.insertuserpopup);
        InsertUser.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button Criar = InsertUser.findViewById(R.id.CreateBTN);
        EditText Name = InsertUser.findViewById(R.id.Name);
        Name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                Name.setText("");
            }
        });
        Criar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Name.getText().toString().equals("") || Name.getText().toString().equals("Write player name here")){
                    Toast.makeText(context, "UserName invalid", Toast.LENGTH_SHORT).show();
                }
                else{
                    //Adiciona o User a database:
                    User user = new User();
                    user.setName(Name.getText().toString());
                    dao.insert(user);
                    ShowPlayer();
                    InsertUser.dismiss();
                }
            }
        });
        InsertUser.show();
    }
}
