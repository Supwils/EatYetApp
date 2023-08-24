package com.example.myapplication;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;


public class add_ingred extends AppCompatActivity implements View.OnClickListener {
    String name;
    String tags;
    public String user_name;




    private String getIngredient() {
        LinearLayout container = (LinearLayout) findViewById(R.id.Container);
        String ingredientStr = "";
        for (int i = 0; i < container.getChildCount(); i++) {
            if (i != container.getChildCount()-1){
                LinearLayout itemLayout = (LinearLayout) container.getChildAt(i);
                EditText editText = (EditText) itemLayout.getChildAt(0);
                if (!(editText.getText().toString().equals(""))){
                    ingredientStr = ingredientStr + editText.getText().toString() + "|";
                }else {continue;}
            }else {
                LinearLayout itemLayout = (LinearLayout) container.getChildAt(i);
                EditText editText = (EditText) itemLayout.getChildAt(0);
                if (!(editText.getText().toString().equals(""))){
                    ingredientStr = ingredientStr + editText.getText().toString();
                }else {continue;}}
        }
        return ingredientStr;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_ingred);
        Bundle extras=getIntent().getExtras();
        user_name = extras.getString("user_name");
        findViewById(R.id.btnI_add).setOnClickListener(this);

        Intent i = getIntent();
        Bundle bundle = i.getExtras();

        if (bundle != null){
            name = bundle.getString("name");
            tags = bundle.getString("tags");
        }
    }





    @Override
    public void onClick(View view) {
        // 父控件
        final LinearLayout container = (LinearLayout) findViewById(R.id.Container);
        // 根据tag区分是新增view还是删除view
        String tag = (String) view.getTag();
        if ("-".equals(tag)) {
            // 删除view
            // 获取子控件
            View child = (View) view.getParent();
            // 从父控件中移除子控件
            container.removeView(child);
        } else {
            // 新增view
            // 创建子控件实例
            View child = LayoutInflater.from(add_ingred.this).inflate(R.layout.ingred_item, container, false);
            // 获取其中的button
            View btn = child.findViewById(R.id.btnI_add);
            // 监听点击事件
            btn.setOnClickListener(this);
            // 设置删除的tag
            btn.setTag("-");
            ((Button)btn).setText("delete");
            // 添加进父控件
            container.addView(child);
        }
        // 请求重绘
        container.invalidate();
    }




    public void BackToPrevPage(View v){
        Intent i = new Intent(getApplicationContext(),Search.class);
        i.putExtra("user_name",user_name);
        startActivity(i);
    }

    public void MoveToProced(View v){
        Intent i = new Intent(add_ingred.this,add_procedure.class);
        i.putExtra("name", name);
        i.putExtra("tags",tags);
        i.putExtra("ingredients",getIngredient());
        i.putExtra("user_name",user_name);
        startActivity(i);
    }

}
