package com.inside.developed.databaseusers;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Simon on 19.09.2017.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {

    private List<User> userList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView firstName, year, lastName;

        public MyViewHolder(View view) {
            super(view);
            firstName = (TextView) view.findViewById(R.id.firstName);
            lastName = (TextView)view.findViewById(R.id.lastName);
            year = (TextView)view.findViewById(R.id.year);


            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){

//                        User userItem = userList.get(pos);
//                        Toast.makeText(v.getContext(), "You clicked " + pos, Toast.LENGTH_SHORT).show();
//                        Intent userActivity = new Intent(v.getContext(), UserActivity.class);
//                        v.getContext().startActivity(userActivity);


                        Intent intent = new Intent(v.getContext(), UserActivity.class);
                        intent.putExtra("id", userList.get(pos).getId());
                        intent.putExtra("firstName", userList.get(pos).getFirstName());
                        intent.putExtra("lastName",userList.get(pos).getLastName());
                        intent.putExtra("year", userList.get(pos).getYear());
                        v.getContext().startActivity(intent);
                    }
                }
            });
        }
    }
    public UserAdapter(List<User> userList) {
        this.userList = userList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_list_row, parent, false);

        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        User user = userList.get(position);
        holder.firstName.setText(user.getFirstName());
        holder.lastName.setText(user.getLastName());
        holder.year.setText(user.getYear());
    }

    @Override
    public int getItemCount() {

        return userList.size();
    }

}
