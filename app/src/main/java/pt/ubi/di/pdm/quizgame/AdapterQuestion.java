package pt.ubi.di.pdm.quizgame;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AdapterQuestion extends ArrayAdapter<Question> {
    private Context context;
    private int resource;
    private List<Question> Question;

    //Variável que representa se a resposta esta certa ou não
    //Se tiver o valor de zero a resposta esta errada
    //Se tiver o valor de 1 a resposta esta certa
    private int correto = 0;



    public AdapterQuestion(@NonNull Context context, int resource, @NonNull List<Question> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.Question = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Question question = Question.get(position);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.answer, null);
        List<TextView> Answer = new ArrayList<>();
        Answer.add(view.findViewById(R.id.answer1));
        Answer.add(view.findViewById(R.id.answer2));
        Answer.add(view.findViewById(R.id.answer3));
        Answer.add(view.findViewById(R.id.answer4));
        List<Integer> radom = Arrays.asList(0,1,2,3);
        Collections.shuffle(radom);
        Answer.get(radom.get(0)).setText(question.getAnswer1());
        Answer.get(radom.get(1)).setText(question.getAnswer2());
        Answer.get(radom.get(2)).setText(question.getAnswer3());
        Answer.get(radom.get(3)).setText(question.getAnswer4());
        AddEffect(question.getAnswer1(), Answer);
        return view;
    }

    //Adicona efetios a TextView:
    //Se a resposta estiver certa o fundo da TextView fica verde caso contrario fica vermelho.
    //Deixa invisiveis as restantes TextView que o utilizador não escolheu:
    private void AddEffect(String question, List<TextView> List){
        for(TextView textView : List){
            textView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    correto = 0;
                    if (textView.getText().toString() == question) {
                        textView.setBackground(ContextCompat.getDrawable(context, R.drawable.answertrue_animation));
                        AnimationDrawable animationDrawable = (AnimationDrawable) textView.getBackground();
                        animationDrawable.setEnterFadeDuration(950);
                        animationDrawable.setExitFadeDuration(1000);
                        animationDrawable.start();
                        correto = 1;
                    }
                    else {
                        textView.setBackground(ContextCompat.getDrawable(context, R.drawable.answerfalse_animation));
                        AnimationDrawable animationDrawable = (AnimationDrawable) textView.getBackground();
                        animationDrawable.setEnterFadeDuration(950);
                        animationDrawable.setExitFadeDuration(1000);
                        animationDrawable.start();
                    }
                    for(TextView textNotSelect : List){
                        if (textNotSelect.getText().toString() != textView.getText().toString()) textNotSelect.setVisibility(View.INVISIBLE);
                    }
                    return false;
                }
            });
        }
    }

    public int getCorreto() { return this.correto; }
}
