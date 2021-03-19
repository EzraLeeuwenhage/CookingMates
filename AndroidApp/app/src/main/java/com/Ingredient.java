package com;

public class Ingredient {

    private int ingredientId;
    private String name;

    public Ingredient(int ingredientId, String name) {
        this.ingredientId = ingredientId;
        this.name = name;
    }

    public int getIngredientId() {
        return ingredientId;
    }

    public String getName() {
        return name;
    }
}
