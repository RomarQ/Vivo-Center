package pt.ubi.pdm.vivo.Visit;

import java.util.ArrayList;
import java.util.Date;

public class Visit {
    private String id;
    private String user;
    private Date date;
    private String duration;
    private ArrayList<Evaluation> Evaluations = new ArrayList<>();

    public Visit () {
        id = "";
        user = "";
        date = null;
        duration = null;
    }

    public Visit (String id, String user, Date date, String duration) {
        this.id = id;
        this.user = user;
        this.date = date;
        this.duration = duration;
    }

    public void setEvaluations(String question, int answer) {
        Evaluations.add(new Evaluation(question, answer));
    }

    public ArrayList<Evaluation> getEvaluations() {
        ArrayList<Evaluation> evaluations = new ArrayList<>();
        for (Evaluation e : Evaluations){
            evaluations.add(new Evaluation(e.getQuestion(), e.getAnswer()));
        }
        return evaluations;
    }

    public String getId() { return id; }
    public String getUser() { return user; }
    public Date getDate() { return date; }
    public String getDuration() { return duration; }

    public class Evaluation {
        String question;
        int answer;

        Evaluation(String question, int answer) {
            this.question = question;
            this.answer = answer;
        }

        String getQuestion() { return question; }
        int getAnswer() { return answer; }
    }
}
