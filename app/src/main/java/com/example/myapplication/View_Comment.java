package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.sql.Array;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

public class View_Comment extends AppCompatActivity {


    static public ArrayList<String> comments = new ArrayList<String>();
    private RecyclerView recyclerView;
    private TextView message;
    static public ArrayList<Integer> comment_ids = new ArrayList();
    static public ArrayList<Integer> recipe_ids = new ArrayList();
    public String user_name;
    public TextView view_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__comment);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_c);
        view_message = (TextView) findViewById(R.id.message_comment_view);
        view_message.setText("Your Comments:");
        Bundle extras=getIntent().getExtras();
        user_name = extras.getString("user_name");

        comments.clear();
        comment_ids.clear();
        recipe_ids.clear();
        GetComments getRecipes = new GetComments();
        try {
            getRecipes.execute("").get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //view_message.setText(comments.get(1));
        MyAdapter_Comment myAdapter = new MyAdapter_Comment(this, comments, comment_ids,recipe_ids,user_name);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



    }


    public class GetComments extends AsyncTask<String, Void, ArrayList<ArrayList<String>>> {
        Context ctx;
        Connection connect;

        @Override
        protected ArrayList<ArrayList<String>> doInBackground(String... strings) {
            ArrayList<ArrayList<String>> rt = new ArrayList<ArrayList<String>>();
            try {

                ConnectionHelper connectionHelper = new ConnectionHelper();
                connect = connectionHelper.connectionclass();
                String query = "SELECT * FROM [dbo].[Uaccount] WHERE Uname='" + user_name + "'";
                Statement st = connect.createStatement();
                ResultSet rs = st.executeQuery(query);
                rs.next();
                String uploaded_comments_id = rs.getString(5);
                connect.close();
                connect = connectionHelper.connectionclass();
                if (uploaded_comments_id!= "") {
                    query = "SELECT * FROM [dbo].[UComments] WHERE CommentID IN " + "(" + uploaded_comments_id + ")";
                    st = connect.createStatement();
                    rs = st.executeQuery(query);
                } else {
                    return rt;
                }


                while (rs.next()) {


                    comments.add(rs.getString(3));
                    comment_ids.add(rs.getInt(1));
                    recipe_ids.add(rs.getInt(2));
                }



                return rt;


            } catch (Exception ex) {

            }
            return rt;
        }


        @Override
        protected void onPostExecute(ArrayList<ArrayList<String>> a) {


        }
    }

    public void Back_to_search_c(View v){
        Intent k=new Intent(View_Comment.this,Search.class);
        k.putExtra("user_name",user_name);
        startActivity(k);
    }
}

