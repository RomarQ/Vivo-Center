package pt.ubi.pdm.vivo.Visit;


import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import pt.ubi.pdm.vivo.R;
import pt.ubi.pdm.vivo.session;

public class VisitsLogAdapter extends RecyclerView.Adapter<VisitsLogAdapter.VisitsViewHolder> {

    private Context context;

    public static class VisitsViewHolder extends  RecyclerView.ViewHolder {

        private TextView TV_visitDate, TV_visitDuration, TV_visitUser;
        private LinearLayout LL_visit;
        private CardView CV_visit;

        public VisitsViewHolder(View itemView) {
            super(itemView);

            CV_visit = itemView.findViewById(R.id.CV_visit);
            LL_visit = itemView.findViewById(R.id.LL_visit);
            TV_visitUser = itemView.findViewById(R.id.TV_visitUser);
            TV_visitDate = itemView.findViewById(R.id.TV_visitDate);
            TV_visitDuration = itemView.findViewById(R.id.TV_visiDuration);

        }
    }

    @Override
    public VisitsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_visit_log, parent, false);
        VisitsViewHolder vvh = new VisitsViewHolder(v);
        context = parent.getContext();
        return vvh;
    }

    @Override
    public void onBindViewHolder(VisitsViewHolder holder, final int position) {
        final Visit currentVisit = session.visits.get(position);

        if (currentVisit.getEvaluations().size() < 1) {
            holder.CV_visit.setEnabled(false);
            holder.LL_visit.setBackgroundColor(ContextCompat.getColor(context, R.color.primary_light));
            holder.TV_visitUser.setTextColor(ContextCompat.getColor(context, R.color.accent));
            holder.TV_visitDate.setTextColor(ContextCompat.getColor(context, R.color.accent));
            holder.TV_visitDuration.setTextColor(ContextCompat.getColor(context, R.color.accent));
        }


        holder.TV_visitUser.setText(currentVisit.getUser());
        holder.TV_visitDate.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(currentVisit.getDate()));
        holder.TV_visitDuration.setText(currentVisit.getDuration());

        holder.CV_visit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, evalShow.class);
                i.putExtra("index", position);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return session.visits.size();
    }
}
