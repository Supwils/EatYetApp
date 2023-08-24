package com.example.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Search extends AppCompatActivity {
    //initialize variable
    //SearchView mySearchView;
    ListView listView;
    ArrayList<Bean> stringArrayList = new ArrayList<>();
    ArrayList<Bean> showList = new ArrayList<>();
    public String user_name;
    private TextView message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Connection connect;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Bundle extras=getIntent().getExtras();
        user_name = extras.getString("user_name");
        listView = findViewById(R.id.list_view);
        Retrieve_from_database retrieve_from_database=new Retrieve_from_database();
        try {
            retrieve_from_database.execute("").get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //for (int i = 0; i <= 1000; i++){
        //  stringArrayList.add("Item " + i);

        //}

        //initialize adapter
        //adapter = new ArrayAdapter<>(MainActivity.this,
        //  android.R.layout.simple_list_item_1, stringArrayList);
        //Set adapter on list view
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Display click item position in toast
                //Toast.makeText(getApplicationContext()
                 //       , showList.get(position).getRecipename(), Toast.LENGTH_SHORT).show();
                Intent k=new Intent(Search.this,View_Recipe.class);
                k.putExtra("RecipeID",showList.get(position).getId());
                k.putExtra("user_name",user_name);
                k.putExtra("origin","search");
                startActivity(k);
            }

        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        //initialize menu inflater
        MenuInflater menuInflater = getMenuInflater();
        //Inflate menu
        menuInflater.inflate(R.menu.main_menu,menu);
        //initialize menu item
        MenuItem menuItem = menu.findItem(R.id.search_view);
        //Initialize search view
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //filter array list
                updatelistview(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    public void updatelistview(String key)
    {
        showList.clear();
        if (key.contains("#"))
        {
            if(key.length()>1) {
                String temp_key = key.substring(1, key.length());
                for (int i = 0; i < stringArrayList.size(); i++) {
                    if (stringArrayList.get(i).getRecipetag().contains(temp_key)) {
                        showList.add(stringArrayList.get(i));
                    }
                }
            }
        }
        else if(key==""){
            showList.clear();
        }
        else{
            for(int i=0;i<stringArrayList.size();i++)
            {
                if (stringArrayList.get(i).getRecipename().contains(key)||stringArrayList.get(i).getIngrediences().contains(key)||stringArrayList.get(i).getProcedure().contains(key)||stringArrayList.get(i).getRecipetag().contains(key))
                {
                    showList.add(stringArrayList.get(i));
                }
            }
        }

        adapter.notifyDataSetChanged();

    }

    BaseAdapter adapter =new BaseAdapter() {
        @Override
        public int getCount() {
            return showList.size();
        }

        @Override
        public Object getItem(int i) {
            return showList.size();
        }

        @Override
        public long getItemId(int i) {
            return showList.size();
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view=  LayoutInflater.from(Search.this).inflate(R.layout.item,null);
            TextView r_nametv=view.findViewById(R.id.r_name);
            TextView r_tagtv=view.findViewById(R.id.r_tag);
            //  indextv.setText(shwoList.get(i).getId()+"");
            r_nametv.setText(showList.get(i).getRecipename());
            r_tagtv.setText(showList.get(i).getRecipetag());

            return view;
        }
    };

    public class Retrieve_from_database extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... strings){
            Connection connect;
            try {
                ConnectionHelper connectionHelper = new ConnectionHelper();
                connect = connectionHelper.connectionclass();
                String query = "SELECT TOP (1000) * FROM [dbo].[URecipe]";
                Statement st = connect.createStatement();
                ResultSet rs = st.executeQuery(query);
                while (rs.next()){
                    String tag_add="";
                    if(rs.getString(5)!=null){
                        tag_add="#"+rs.getString(5).replace("|",",#");
                    }
                    String procedure_add="";
                    if(rs.getString(4)!=null){
                        procedure_add=rs.getString(4).replace("|","\n");
                    }
                    String ingredience_add="";
                    if(rs.getString(3)!=null){
                        ingredience_add=rs.getString(3).replace("|","\n");
                    }
                    Bean bean=new Bean(rs.getInt(1),rs.getString(2),tag_add,procedure_add,ingredience_add);
                    stringArrayList.add(bean);
                }

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            return "";
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menu_view_upload_recipe:
                Intent i=new Intent(Search.this,add_nametags.class);
                i.putExtra("user_name",user_name);
                startActivity(i);
                break;

            case R.id.menu_view_uploaded_recipe:
                Intent k=new Intent(Search.this,View_Uploaded_Recipe.class);
                k.putExtra("user_name",user_name);
                startActivity(k);
                break;

            case R.id.menu_view_uploaded_comment:
                Intent intent=new Intent(Search.this,View_Comment.class);
                intent.putExtra("user_name",user_name);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }



}