package com.example.testfirebasedb.entity;

import java.io.Serializable;
import java.util.Arrays;

public class Dish {
public class Dish implements Serializable {
    private String name; //Ten cua mon an
    private int caloriesPer100Gm; //so calories tren 100 g
    private int fatPer100Gm; //ham luong chat beo tren 100 g
    private int proteinPer100Gm; // ham luong chat dam tren 100 g
    private int fiberPer100Gm; //ham luong chat xo tren 100 g
    private String typeOfFood;
    private String imgUrl;

    private int weight;
    private String imgUrl;

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getProteinPer100Gm() {
        return proteinPer100Gm;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public static enum enumFood{ //Starchy la tinh bot vd(rice,noodles) | nut là các loại hạt vd như ngũ cốc , đậu nành ,...
        Meat,FruitAndVegetable,drink,Starchy,fishAndSeaFood,nut
    }
    public static String[] names() {
        return Arrays.toString(enumFood.values()).replaceAll("^.|.$", "").split(", ");
    }
    public int getFatPer100Gm() {
        return fatPer100Gm;
    }
    public String getTypeOfFood() {
        return typeOfFood;
    }

    public void setTypeOfFood(String typeOfFood) {
        this.typeOfFood = typeOfFood;
    }

    public void setFatPer100Gm(int fatPer100Gm) {
        this.fatPer100Gm = fatPer100Gm;
    }

    public int getProteinPer100Gm(int i) {
        return proteinPer100Gm;
    }

    public void setProteinPer100Gm(int proteinPer100Gm) {
        this.proteinPer100Gm = proteinPer100Gm;
    }

    public int getFiberPer100Gm() {
        return fiberPer100Gm;
    }

    public void setFiberPer100Gm(int fiberPer100Gm) {
        this.fiberPer100Gm = fiberPer100Gm;
    }

    public Dish(){
        this.name = "";
        this.caloriesPer100Gm = 0;
    }

    public void setCaloriesPer100Gm(int caloriesPer100Gm) {
        this.caloriesPer100Gm = caloriesPer100Gm;
    }

    public Dish(String name, int caloriesPer100Gm,int fatPer100Gm,int proteinPer100Gm,int fiberPer100Gm,enumFood enumFood,String imgUrl) {}

    public Dish(String name, int caloriesPer100Gm,int weight,int fatPer100Gm,int proteinPer100Gm,int fiberPer100Gm,enumFood enumFood,String imgUrl) {
        this.caloriesPer100Gm = caloriesPer100Gm;
        this.name = name;
        this.fatPer100Gm = fatPer100Gm;
        this.proteinPer100Gm = proteinPer100Gm;
        this.fiberPer100Gm = fiberPer100Gm;
        this.typeOfFood = String.valueOf(enumFood);
        this.imgUrl = imgUrl;
        this.weight = weight;
    }
    public int parseCalories(int weight) {
        return caloriesPer100Gm * weight / 100;
    }

    public int parseCalories(int weight) {
        return (proteinPer100Gm*4+fatPer100Gm*9-fiberPer100Gm*4) * weight / 100;
    }

    public String getName() {
        return name;
    }

    public int getCaloriesPer100Gm() {
        return caloriesPer100Gm;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCalories(int caloriesPer100Gm) {
        this.caloriesPer100Gm = caloriesPer100Gm;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Dish)
            return name.equals(((Dish) o).name);
        else
            return false;
    }
}
