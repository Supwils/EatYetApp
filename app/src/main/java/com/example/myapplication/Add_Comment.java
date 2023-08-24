package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Add_Comment extends AppCompatActivity {

    public int RecipeID;
    private Button Back;
    private TextView Comment;
    private TextView message;
    public String user_name;
    public String origin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__comment);
        Intent intent=getIntent();
        RecipeID=intent.getIntExtra("RecipeID",0);
        user_name=intent.getStringExtra("user_name");
        origin=intent.getStringExtra("origin");
        Back=(Button)findViewById(R.id.back_button);
        Comment=(TextView)findViewById(R.id.Comment_Text);
        message=(TextView)findViewById(R.id.message);
    }

    public void Back_Onclick(View v){
        Intent k=new Intent(Add_Comment.this,View_Recipe.class);
        k.putExtra("RecipeID",RecipeID);
        k.putExtra("user_name",user_name);
        k.putExtra("origin",origin);
        startActivity(k);
    }

    public void Add_Comment_Onclick(View v){
        Add_to_database add_to_database=new Add_to_database();
        add_to_database.execute("");

    }

    public class Add_to_database extends AsyncTask<String,Void,String>{
        Context ctx;
        Connection connect;
        @Override
        protected String doInBackground(String... strings){
            try {
                ConnectionHelper connectionHelper = new ConnectionHelper();
                connect = connectionHelper.connectionclass();
                String query = "INSERT INTO [dbo].[UComments](RecipeID, Comment, UserName)\n" +
                        "OUTPUT Inserted.CommentID\n"+
                        "VALUES ("+Integer.toString(RecipeID)+",'"+Comment.getText().toString()+"','"+user_name+"')";
                Statement st = connect.createStatement();
                ResultSet rs=st.executeQuery(query);
                rs.next();
                String comment_id=Integer.toString(rs.getInt(1));
                connect.close();
                connect = connectionHelper.connectionclass();
                query="SELECT * FROM [dbo].[Uaccount] WHERE Uname='"+user_name+"'";
                st=connect.createStatement();
                rs=st.executeQuery(query);
                rs.next();
                String user_comment=rs.getString(5);
                connect.close();
                if(user_comment==null){
                    user_comment=comment_id;
                }
                else{
                    user_comment=user_comment+","+comment_id;
                }
                connect = connectionHelper.connectionclass();
                query="UPDATE [dbo].[Uaccount] " +
                        "SET UComment='" +user_comment+
                        "' WHERE Uname='"+user_name+"'";
                st=connect.createStatement();
                st.executeUpdate(query);
                return comment_id;
            }
            catch (Exception ex){

            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            message.setText("Leave Comment Success!");
        }


    }




}