package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class View_Recipe extends AppCompatActivity {
    private TextView name_text;
    private String recipename;
    private String ingrediences;
    private String procedure;
    private String recipeid;
    private TextView ingredience_text;
    private TextView procedure_text;
    private ListView comment;
    private int comment_counter;
    private Boolean end_of_comment;
    private Button more_comment;
    private TextView csign;
    public String user_name;
    public int RecipeID;
    private TextView tags_text;
    public String origin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle(getTitle());
        RecipeID=2;
        Bundle extras=getIntent().getExtras();
        RecipeID = extras.getInt("RecipeID");
        user_name=extras.getString("user_name");
        origin=extras.getString("origin");

        end_of_comment=false;
        ingredience_text=(TextView)findViewById(R.id.ingredience) ;
        tags_text=(TextView)findViewById(R.id.tags);
        name_text=(TextView)findViewById(R.id.rname);
        procedure_text=(TextView)findViewById(R.id.procedure);
        comment=(ListView)findViewById(R.id.ucomment) ;
        more_comment=(Button)findViewById(R.id.moreitem);
        csign=(TextView)findViewById(R.id.comment_sign) ;
        csign.setText("Comment:");
        Rinfo rinfo=new Rinfo();
        rinfo.execute("");
        Cinfo cinfo=new Cinfo();
        cinfo.execute("");

    }

    public class Rinfo extends AsyncTask<String, Void, String>{
        Context ctx;
        Connection connect;
        @Override
        protected String doInBackground(String... strings){
            try {
                ConnectionHelper connectionHelper = new ConnectionHelper();
                connect = connectionHelper.connectionclass();
                String query = "SELECT * FROM [dbo].[URecipe] WHERE RecipeID="+Integer.toString(RecipeID)+"";
                Statement st = connect.createStatement();
                ResultSet rs = st.executeQuery(query);
                rs.next();
                String rname=rs.getString(2);

                String ringredience=rs.getString(3);
                String rprocedure=rs.getString(4);
                String rtags=rs.getString(5);
                String rt=rname+"^"+ringredience+"^"+rprocedure+"^"+rtags;


                return rt;


            }
            catch(Exception ex){

            }
            return "";
        }
        @Override
        protected void onPostExecute(String s) {
            String rname=s.substring(0,s.indexOf('^'));
            name_text.setText(rname);
            s=s.substring(s.indexOf('^')+1);
            String ringredience=s.substring(0,s.indexOf('^'));
            String formatringredience=ringredience.replace("|","\n");
            ingredience_text.setText("INGREDIENCE:\n\n"+formatringredience);
            s=s.substring(s.indexOf('^')+1);
            String rprocedure=s.substring(0,s.indexOf('^'));
            String formatrprocedure=rprocedure.replace("|","\n");
            procedure_text.setText("INSTRUCTION:\n\n"+formatrprocedure);
            s=s.substring(s.indexOf('^')+1);
            String formatrtag=s.replace("|"," #");
            tags_text.setText("Tags: \n"+"#"+formatrtag);


        }


    }

    public class Cinfo extends AsyncTask<String,Void,ArrayList<String>> {
        Context ctx;
        Connection connect;

        @Override
        protected ArrayList<String> doInBackground(String... strings) {
            ArrayList<String> output_comment = new ArrayList<>();
            try {
                ConnectionHelper connectionHelper = new ConnectionHelper();
                connect = connectionHelper.connectionclass();
                String query = "SELECT * FROM [dbo].[UComments] WHERE RecipeID="+Integer.toString(RecipeID);
                Statement st = connect.createStatement();
                ResultSet rs = st.executeQuery(query);

                int counter = 0;
                while (counter < 3 && rs.next()) {
                    output_comment.add(rs.getString(4) + ":\n" + rs.getString(3));

                    counter = counter + 1;
                    comment_counter = comment_counter + 1;
                }
                if (counter < 2) end_of_comment = true;
                return output_comment;


            } catch (Exception ex) {

            }

            return output_comment;
        }

        @Override
        protected void onPostExecute(ArrayList<String> output_comment) {
            ArrayAdapter arrayAdapter = new ArrayAdapter(View_Recipe.this, android.R.layout.simple_list_item_1, output_comment);
            comment.setAdapter(arrayAdapter);
            Utility.setListViewHeightBasedOnChildren(comment);

        }
    }

        public class OnclickMoreComment extends AsyncTask<String,Void,ArrayList<String>>{
            Context ctx;
            Connection connect;
            @Override
            protected ArrayList<String> doInBackground(String... strings) {
                ArrayList<String> output_comment = new ArrayList<>();
                try {

                    ConnectionHelper connectionHelper = new ConnectionHelper();
                    connect = connectionHelper.connectionclass();
                    String query = "SELECT * FROM [dbo].[UComments] WHERE RecipeID="+Integer.toString(RecipeID);
                    Statement st = connect.createStatement();
                    ResultSet rs = st.executeQuery(query);
                    int counter = 0;
                    while(counter<comment_counter){
                        rs.next();
                        counter=counter+1;
                        output_comment.add(rs.getString(4)+":\n"+rs.getString(3));
                    }
                    int temp=counter;
                    while (counter < temp+3&&rs.next()) {
                        output_comment.add(rs.getString(4)+":\n"+rs.getString(3));

                        counter=counter+1;
                        comment_counter=comment_counter+1;
                    }
                    if(counter<temp+3) end_of_comment=true;
                    return output_comment;


                } catch (Exception ex) {

                }

                return output_comment;
            }

            @Override
            protected void onPostExecute(ArrayList<String> output_comment){
                ArrayAdapter arrayAdapter = new ArrayAdapter(View_Recipe.this, android.R.layout.simple_list_item_1, output_comment);
                comment.setAdapter(arrayAdapter);
                Utility.setListViewHeightBasedOnChildren(comment);
                //name_text.setText(Integer.toString(comment_counter));

            }

    }

    public void onclickmorecomment(View v){
        if(end_of_comment==false) {
            OnclickMoreComment onclickMoreComment = new OnclickMoreComment();
            onclickMoreComment.execute();
        }
        else{
            more_comment.setText("No more comment");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    public String getStringPart(String s){
        if(s==""){
            return "";
        }
        else{
            return s.substring(0,s.indexOf('|'));
        }
    }
    public void LeaveCommentOnclick(View V){
        Intent k=new Intent(View_Recipe.this, Add_Comment.class);
        k.putExtra("RecipeID",RecipeID);
        k.putExtra("user_name",user_name);
        k.putExtra("origin",origin);
        startActivity(k);

    }

    public void Back_button(View V){
        if(origin.equals("search")){
            Intent k=new Intent(View_Recipe.this,Search.class);
            k.putExtra("user_name",user_name);
            startActivity(k);
        }
        else if(origin.equals("recipe")){
            Intent k=new Intent(View_Recipe.this,View_Uploaded_Recipe.class);
            k.putExtra("user_name",user_name);
            startActivity(k);
        }
        else if(origin.equals("comment")){
            Intent k=new Intent(View_Recipe.this,View_Comment.class);
            k.putExtra("user_name",user_name);
            startActivity(k);
        }
    }

}