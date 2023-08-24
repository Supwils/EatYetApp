package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class add_procedure extends AppCompatActivity implements View.OnClickListener{
    String name;
    String tags;
    String ingredients;
    String user_name;
    TextView message;

    private String getProcedStr() {
        LinearLayout container = (LinearLayout) findViewById(R.id.Container_proced);
        String getProcedStr = "";
        for (int i = 0; i < container.getChildCount(); i++) {
            if (i != container.getChildCount()-1){
                LinearLayout itemLayout = (LinearLayout) container.getChildAt(i);
                EditText editText = (EditText) itemLayout.getChildAt(0);
                if (!(editText.getText().toString().equals(""))){
                    getProcedStr = getProcedStr + editText.getText().toString() + "|";
                }else {continue;}
            }else {
                LinearLayout itemLayout = (LinearLayout) container.getChildAt(i);
                EditText editText = (EditText) itemLayout.getChildAt(0);
                if (!(editText.getText().toString().equals(""))){
                    getProcedStr = getProcedStr + editText.getText().toString();
                }else {continue;}
            }
        }
        return getProcedStr;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_procedures);
        findViewById(R.id.btnP_add).setOnClickListener(this);
        message=(TextView) findViewById(R.id.message_p);
        Intent i = getIntent();
        Bundle bundle = i.getExtras();



        if (bundle != null){
            name = bundle.getString("name");
            tags = bundle.getString("tags");
            ingredients = bundle.getString("ingredients");
            user_name = bundle.getString("user_name");
        }

    }

    public class OnclickCheck extends AsyncTask<String, Void, String>{
        Connection connect;
        @Override
        protected String doInBackground(String... strings){
            try {
                ConnectionHelper connectionHelper = new ConnectionHelper();
                connect = connectionHelper.connectionclass();

                String query = "INSERT INTO URecipe (RecipeName,Ingredients,CookingProcedures,RecipeTag) OUTPUT Inserted.RecipeID VALUES ('"+ name+"','"+ ingredients +"','"+ getProcedStr() +"','"+ tags +"')";
                Statement st = connect.createStatement();
                ResultSet rs=st.executeQuery(query);
                rs.next();
                String recipe_id = Integer.toString(rs.getInt(1));
                connect.close();
                connect = connectionHelper.connectionclass();
                query="SELECT * FROM [dbo].[Uaccount] WHERE Uname='"+user_name+"'";
                st=connect.createStatement();
                rs=st.executeQuery(query);
                rs.next();
                String user_recipe = rs.getString(4);
                connect.close();
                if(user_recipe == null){
                    user_recipe = recipe_id;
                }
                else {
                    user_recipe = user_recipe + "," + recipe_id;
                }
                connect = connectionHelper.connectionclass();
                query="UPDATE [dbo].[Uaccount] " + "SET Urecipe='" + user_recipe + "' WHERE Uname='" +user_name+ "'";
                st=connect.createStatement();
                st.executeUpdate(query);
                return recipe_id;
            }catch (Exception e){e.printStackTrace();}
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            message.setText("Upload Recipe Success, Click Back To Search More!");
        }
    }




    @Override
    public void onClick(View view) {
        final LinearLayout container_proced = (LinearLayout) findViewById(R.id.Container_proced);
        String tag = (String) view.getTag();
        if ("-".equals(tag)) {
            View child = (View) view.getParent();
            container_proced.removeView(child);
        } else {
            View child = LayoutInflater.from(add_procedure.this).inflate(R.layout.proced_item, container_proced, false);
            View btn = child.findViewById(R.id.btnP_add);
            btn.setOnClickListener(this);
            btn.setTag("-");
            ((Button)btn).setText("delete");
            container_proced.addView(child);
        }
        container_proced.invalidate();
    }

    public void onclick_check(View v){
        OnclickCheck onclickCheck=new OnclickCheck();
        onclickCheck.execute("");
    }

    public void BackToPrevPage(View v){
        Intent i = new Intent(getApplicationContext(),Search.class);
        i.putExtra("user_name",user_name);
        startActivity(i);
    }
}
