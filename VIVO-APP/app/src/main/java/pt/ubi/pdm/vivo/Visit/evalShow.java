package pt.ubi.pdm.vivo.Visit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

import pt.ubi.pdm.vivo.R;
import pt.ubi.pdm.vivo.session;

public class evalShow extends AppCompatActivity {

    private TextView TV_question1, TV_question2, TV_question3, TV_question4;
    private RadioGroup RG_1, RG_2, RG_3, RG_4;
    private Button Btn_sendEval;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eval_form);

        ArrayList<Visit.Evaluation> evaluations = session.visits.get(getIntent()
                .getIntExtra("index", 0)).getEvaluations();


        disableRadioButtons();

        RG_1 = findViewById(R.id.RG_1);
        RG_2 = findViewById(R.id.RG_2);
        RG_3 = findViewById(R.id.RG_3);
        RG_4 = findViewById(R.id.RG_4);

        TV_question1 = findViewById(R.id.TV_question1);
        TV_question2 = findViewById(R.id.TV_question2);
        TV_question3 = findViewById(R.id.TV_question3);
        TV_question4 = findViewById(R.id.TV_question4);

        Btn_sendEval = findViewById(R.id.Btn_sendEval);
        Btn_sendEval.setText("Voltar atrás");

        Btn_sendEval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        TV_question1.setText(evaluations.get(0).getQuestion());
        answer_1(evaluations.get(0).getAnswer());
        TV_question2.setText(evaluations.get(1).getQuestion());
        answer_2(evaluations.get(1).getAnswer());
        TV_question3.setText(evaluations.get(2).getQuestion());
        answer_3(evaluations.get(2).getAnswer());
        TV_question4.setText(evaluations.get(3).getQuestion());
        answer_4(evaluations.get(3).getAnswer());

    }

    public void answer_1(int answer) {

        switch(answer) {
            case 1:
                RG_1.check(R.id.RB_opt1_1);
                break;
            case 2:
                RG_1.check(R.id.RB_opt2_1);
                break;
            case 3:
                RG_1.check(R.id.RB_opt3_1);
                break;
            case 4:
                RG_1.check(R.id.RB_opt4_1);
        }
    }

    public void answer_2(int answer) {

        switch(answer) {
            case 1:
                RG_2.check(R.id.RB_opt1_2);
                break;
            case 2:
                RG_2.check(R.id.RB_opt2_2);
                break;
            case 3:
                RG_2.check(R.id.RB_opt3_2);
                break;
            case 4:
                RG_2.check(R.id.RB_opt4_2);
        }
    }

    public void answer_3(int answer) {

        switch(answer) {
            case 1:
                RG_3.check(R.id.RB_opt1_3);
                break;
            case 2:
                RG_3.check(R.id.RB_opt2_3);
                break;
            case 3:
                RG_3.check(R.id.RB_opt3_3);
                break;
            case 4:
                RG_3.check(R.id.RB_opt4_3);
        }
    }

    public void answer_4(int answer) {

        switch(answer) {
            case 1:
                RG_4.check(R.id.RB_opt1_4);
                break;
            case 2:
                RG_4.check(R.id.RB_opt2_4);
                break;
            case 3:
                RG_4.check(R.id.RB_opt3_4);
                break;
            case 4:
                RG_4.check(R.id.RB_opt4_4);
        }
    }

    public void disableRadioButtons() {
        findViewById(R.id.RB_opt1_1).setEnabled(false);
        findViewById(R.id.RB_opt1_2).setEnabled(false);
        findViewById(R.id.RB_opt1_3).setEnabled(false);
        findViewById(R.id.RB_opt1_4).setEnabled(false);
        findViewById(R.id.RB_opt2_1).setEnabled(false);
        findViewById(R.id.RB_opt2_2).setEnabled(false);
        findViewById(R.id.RB_opt2_3).setEnabled(false);
        findViewById(R.id.RB_opt2_4).setEnabled(false);
        findViewById(R.id.RB_opt3_1).setEnabled(false);
        findViewById(R.id.RB_opt3_2).setEnabled(false);
        findViewById(R.id.RB_opt3_3).setEnabled(false);
        findViewById(R.id.RB_opt3_4).setEnabled(false);
        findViewById(R.id.RB_opt4_1).setEnabled(false);
        findViewById(R.id.RB_opt4_2).setEnabled(false);
        findViewById(R.id.RB_opt4_3).setEnabled(false);
        findViewById(R.id.RB_opt4_4).setEnabled(false);
    }

}