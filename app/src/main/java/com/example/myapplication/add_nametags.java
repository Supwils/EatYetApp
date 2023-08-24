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

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class add_nametags extends AppCompatActivity implements View.OnClickListener {
    EditText nameText;
    Button confirm;
    Button cancel;
    public String user_name;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_nametags);
        Bundle extras=getIntent().getExtras();
        user_name = extras.getString("user_name");
        findViewById(R.id.btnT_add).setOnClickListener(this);
        confirm = findViewById(R.id.btn_confirmT);
        cancel = findViewById(R.id.btn_cancelT);
    }

    public String getName(){
        nameText = (EditText)findViewById(R.id.name);
        return nameText.getText().toString();
    }

    private String getTagsStr() {
        LinearLayout container_tags = (LinearLayout) findViewById(R.id.Container_tags);
        String getTagsStr = "";
        for (int i = 0; i < container_tags.getChildCount(); i++) {
            if (i != container_tags.getChildCount()-1){
                LinearLayout itemLayout = (LinearLayout) container_tags.getChildAt(i);
                EditText editText = (EditText) itemLayout.getChildAt(0);
                if (!(editText.getText().toString().equals(""))){
                    getTagsStr = getTagsStr + editText.getText().toString() + "|";
                }else {continue;}
            }else {
                LinearLayout itemLayout = (LinearLayout) container_tags.getChildAt(i);
                EditText editText = (EditText) itemLayout.getChildAt(0);
                if (!(editText.getText().toString().equals(""))){
                    getTagsStr = getTagsStr + editText.getText().toString();
                }else {continue;}
            }

        }
        return getTagsStr;
    }

    @Override
    public void onClick(View view) {
        final LinearLayout container_tags = (LinearLayout) findViewById(R.id.Container_tags);
        String tag = (String) view.getTag();
        if ("-".equals(tag)) {
            View child = (View) view.getParent();
            container_tags.removeView(child);
        } else {
            View child = LayoutInflater.from(add_nametags.this).inflate(R.layout.tag_item, container_tags, false);
            View btn = child.findViewById(R.id.btnT_add);
            btn.setOnClickListener(this);
            btn.setTag("-");
            ((Button)btn).setText("delete");
            container_tags.addView(child);
        }
        container_tags.invalidate();
    }

    public void BackToPrevPage(View v){
        Intent i = new Intent(getApplicationContext(),Search.class);
        i.putExtra("user_name",user_name);
        startActivity(i);
    }

    public void MoveToIngred(View v){
        Intent i = new Intent(add_nametags.this, add_ingred.class);
        i.putExtra("tags", getTagsStr());
        i.putExtra("name", getName());
        i.putExtra("user_name",user_name);
        startActivity(i);
    }
}
