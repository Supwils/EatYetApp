package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;

public class MyAdapter_Comment extends RecyclerView.Adapter<MyAdapter_Comment.MyViewHolder> {
    ArrayList<String> comments;
    ArrayList<Integer> comment_ids, recipe_ids;
    Context ctx;
    String user_name;
    public MyAdapter_Comment(Context ct, ArrayList<String> v1,ArrayList<Integer> v2,ArrayList<Integer> v3,String un){
        ctx=ct;
        comments=v1;
        comment_ids=v2;
        recipe_ids=v3;
        user_name=un;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(ctx);
        View view=inflater.inflate(R.layout.my_row_c,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.myText1.setText(comments.get(position));
        //holder.myText1.setText("test");
        holder.mainLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent=new Intent(ctx,View_Recipe.class);
                int h=recipe_ids.get(position);
                intent.putExtra("RecipeID",h);
                intent.putExtra("origin","comment");
                intent.putExtra("user_name",user_name);
                ctx.startActivity(intent);
            }
        });

        holder.deletebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Delete_from_database delete_from_database=new Delete_from_database();
                delete_from_database.execute(Integer.toString(comment_ids.get(position)));
                comments.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, comment_ids.size());
                comment_ids.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, comment_ids.size());
                recipe_ids.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, recipe_ids.size());
            }
        });

    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView myText1, myText2;
        Button deletebutton;
        ConstraintLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            myText1=itemView.findViewById(R.id.comment);
            deletebutton=(Button)itemView.findViewById(R.id.delete_button_c);
            mainLayout=itemView.findViewById(R.id.mainLayout_c);
        }
    }

    public class Delete_from_database extends AsyncTask<String,Void,String>{
        Context ctx;
        Connection connect;

        @Override
        protected String doInBackground(String... strings){
            try{
                String id=strings[0];
                ConnectionHelper connectionHelper = new ConnectionHelper();
                connect = connectionHelper.connectionclass();
                String query = "DELETE FROM [dbo].[UComments] WHERE CommentID="+id+"";
                Statement st = connect.createStatement();
                st.executeUpdate(query);
            }
            catch (Exception ex){

            }
            return "";
        }
    }

}