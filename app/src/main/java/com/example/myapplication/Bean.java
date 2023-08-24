package com.example.myapplication;

public class Bean {

    int id;
    String recipename;

    String recipetag;

    String procedure;
    String ingrediences;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRecipename() {
        return recipename;
    }

    public void setRecipename(String recipename) {
        this.recipename = recipename;
    }

    public String getRecipetag() {
        return recipetag;
    }

    public void setRecipetag(String recipetag) {
        this.recipetag = recipetag;
    }

    public String getProcedure(){return procedure;}

    public void setProcedure(String procedure){this.procedure=procedure;}

    public String getIngrediences(){return ingrediences;}

    public void setIngrediences(String ingrediences){this.ingrediences=ingrediences;}


    public Bean(int id, String recipename,String recipetag, String procedure, String ingrediences) {
        this.id = id;
        this.recipename = recipename;
        this.recipetag = recipetag;
        this.procedure=procedure;
        this.ingrediences=ingrediences;
    }
}
