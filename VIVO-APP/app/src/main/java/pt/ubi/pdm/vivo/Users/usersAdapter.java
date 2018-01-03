package pt.ubi.pdm.vivo.Users;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.text.SimpleDateFormat;
import java.util.Date;

import pt.ubi.pdm.vivo.R;
import pt.ubi.pdm.vivo.session;

public class usersAdapter extends RecyclerView.Adapter<usersAdapter.UsersViewHolder> {

    private Context context;

    public static class UsersViewHolder extends  RecyclerView.ViewHolder {

        private TextView TV_username, TV_LastLogin;
        private LinearLayout LL_user;
        private CardView CV_user;


        public UsersViewHolder(View itemView) {
            super(itemView);

            CV_user = itemView.findViewById(R.id.CV_user);
            LL_user = itemView.findViewById(R.id.LL_user);
            TV_username = itemView.findViewById(R.id.TV_username);
            TV_LastLogin = itemView.findViewById(R.id.TV_LastLogin);

        }
    }

    @Override
    public UsersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        UsersViewHolder uvh = new UsersViewHolder(v);
        context = parent.getContext();
        return uvh;
    }

    @Override
    public void onBindViewHolder(UsersViewHolder holder, int position) {
        final User currentUser = session.users.get(position);

        if (currentUser.getIsAdmin()) {
            holder.LL_user.setBackgroundColor(ContextCompat.getColor(context, R.color.primary_light));
            holder.TV_username.setTextColor(ContextCompat.getColor(context, R.color.accent));
            holder.TV_LastLogin.setTextColor(ContextCompat.getColor(context, R.color.accent));
        }

        holder.TV_username.setText(currentUser.getUsername());
        holder.TV_LastLogin.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(currentUser.getLastLogin()));

        holder.CV_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, aboutUser.class);
                i.putExtra("userId", currentUser.getUserId());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return session.users.size();
    }
}
