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

public class View_Uploaded_Recipe extends AppCompatActivity {

    static public ArrayList<String> name=new ArrayList<String>();
    static public ArrayList<String> tags=new ArrayList<String>();
    private RecyclerView recyclerView;
    private TextView message;
    static public ArrayList<Integer> ids=new ArrayList();
    public String user_name;
    public TextView view_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__uploaded__recipe);
        Bundle extras=getIntent().getExtras();
        user_name = extras.getString("user_name");
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        view_message=(TextView)findViewById(R.id.message_view_uploaded);
        view_message.setText("Your Recipes:");
        
        name.clear();
        tags.clear();
        ids.clear();
        GetRecipes getRecipes=new GetRecipes();
        try {
            getRecipes.execute("").get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        MyAdapter myAdapter=new MyAdapter(this, name, tags,ids,user_name);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        message=(TextView)findViewById(R.id.message);


    }


    public class GetRecipes extends AsyncTask<String, Void, ArrayList<ArrayList<String>>> {
        Context ctx;
        Connection connect;

        @Override
        protected ArrayList<ArrayList<String>> doInBackground(String... strings){
            ArrayList<ArrayList<String>> rt=new ArrayList<ArrayList<String>>();
            try {

                ConnectionHelper connectionHelper=new ConnectionHelper();
                connect=connectionHelper.connectionclass();
                String query="SELECT * FROM [dbo].[Uaccount] WHERE Uname='"+user_name+"'";
                Statement st=connect.createStatement();
                ResultSet rs = st.executeQuery(query);
                rs.next();
                String uploaded_recipe_id=rs.getString(4);
                connect.close();
                connect=connectionHelper.connectionclass();
                if(uploaded_recipe_id!="") {
                    query = "SELECT * FROM [dbo].[URecipe] WHERE RecipeID IN "+"("+uploaded_recipe_id+")";
                    st=connect.createStatement();
                    rs = st.executeQuery(query);
                }
                else{
                    return rt;
                }


                while(rs.next()){

                    name.add(rs.getString(2));
                    tags.add(rs.getString(5).replace("|",","));
                    ids.add(rs.getInt(1));
                }

                rt.add(name);
                rt.add(tags);

                return rt;




            }
            catch (Exception ex){

            }
            return rt;
        }


        @Override
        protected void onPostExecute(ArrayList<ArrayList<String>> a){


        }
    }

    public void Back_to_search_onclick(View v){
        Intent k=new Intent(View_Uploaded_Recipe.this,Search.class);
        k.putExtra("user_name",user_name);
        startActivity(k);
    }

}