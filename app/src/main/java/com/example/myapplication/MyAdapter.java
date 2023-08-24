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

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    ArrayList<String> name, tags;
    ArrayList<Integer> ids;
    Context ctx;
    String user_name;
    public MyAdapter(Context ct, ArrayList<String> v1,ArrayList<String> v2,ArrayList<Integer> v3,String un){
        ctx=ct;
        name=v1;
        tags=v2;
        ids=v3;
        user_name=un;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(ctx);
        View view=inflater.inflate(R.layout.my_row,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.myText1.setText(name.get(position));
        holder.myText2.setText("tags: "+tags.get(position));
        holder.mainLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent=new Intent(ctx,View_Recipe.class);
                int h=ids.get(position);
                intent.putExtra("RecipeID",h);
                intent.putExtra("origin","recipe");
                intent.putExtra("user_name",user_name);
                ctx.startActivity(intent);
            }
        });

        holder.deletebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Delete_from_database delete_from_database=new Delete_from_database();
                delete_from_database.execute(Integer.toString(ids.get(position)));
                name.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, name.size());
                tags.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, tags.size());
                ids.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,ids.size());
            }
        });

    }

    @Override
    public int getItemCount() {
        return name.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView myText1, myText2, myText3;
        Button deletebutton;
        ConstraintLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            myText1=itemView.findViewById(R.id.name_text);
            myText2=itemView.findViewById(R.id.tags_view);
            deletebutton=(Button)itemView.findViewById(R.id.delete_button);
            mainLayout=itemView.findViewById(R.id.mainLayout);
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
                String query = "DELETE FROM [dbo].[URecipe] WHERE RecipeID="+id+"";
                Statement st = connect.createStatement();
                st.executeUpdate(query);
            }
            catch (Exception ex){

            }
            return "";
        }
    }

}
