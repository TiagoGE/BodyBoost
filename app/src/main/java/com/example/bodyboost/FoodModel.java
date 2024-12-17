package com.example.bodyboost;

public class FoodModel {

    private String foodTitle;
    private String foodCalories;
    private String foodImage;
    private Double foodProtein;
    private Double foodCarbohydrates;
    private Double foodFat;

    public FoodModel(String foodTitle, String foodDescription, String foodImage, Double foodProtein, Double foodCarbohydrates, Double foodFat) {
        this.foodTitle = foodTitle;
        this.foodCalories = foodDescription;
        this.foodImage = foodImage;
        this.foodProtein = foodProtein;
        this.foodCarbohydrates = foodCarbohydrates;
        this.foodFat = foodFat;
    }

    public String getFoodTitle() {
        return foodTitle;
    }

    public String getFoodCalories() {
        return foodCalories;
    }

    public String getFoodImage() {
        return foodImage;
    }

    public Double getFoodProtein() {
        return foodProtein;
    }

    public Double getFoodCarbohydrates() {
        return foodCarbohydrates;
    }

    public Double getFoodFat() {
        return foodFat;
    }
}
