package com.example.healthbuddy.Model;

public class RecipeModel {

    private String id;
    private int recipePic;

    private String recipeName;
    private String recipeCalories;
    private String recipeDetails;


    public RecipeModel(String id, int recipePic, String recipeName, String recipeCalories, String recipeDetails) {
        this.id = id;
        this.recipePic = recipePic;
        this.recipeName = recipeName;
        this.recipeCalories = recipeCalories;
        this.recipeDetails = recipeDetails;
    }

    public RecipeModel() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getRecipePic() {
        return recipePic;
    }

    public void setRecipePic(int recipePic) {
        this.recipePic = recipePic;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getRecipeCalories() {
        return recipeCalories;
    }

    public void setRecipeCalories(String recipeCalories) {
        this.recipeCalories = recipeCalories;
    }

    public String getRecipeDetails() {
        return recipeDetails;
    }

    public void setRecipeDetails(String recipeDetails) {
        this.recipeDetails = recipeDetails;
    }
}
