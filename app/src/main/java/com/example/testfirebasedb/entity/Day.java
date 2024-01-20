package com.example.testfirebasedb.entity;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

public class Day {

    private static Day day = null;
    private String dateOfDiary;
    private ArrayList<Dish> dishes;
    private ArrayList<Exercise> exercises;

    public float caloIn;

    public float caloOut;

    public float totalCalo;

    public Day(String dateOfDiary, ArrayList<Dish> dishes, ArrayList<Exercise> exercises, float caloIn, float caloOut, float totalCalo) {
        this.dateOfDiary = dateOfDiary;
        this.dishes = dishes;
        this.exercises = exercises;
        this.caloIn = caloIn;
        this.caloOut = caloOut;
        this.totalCalo = totalCalo;
    }

    public Day() {
    }

    public static  Day getDay(Context context){
        if(day == null){
            day = new Day();
        }
        return  day;
    }

    public String getDateOfDiary() {
        return dateOfDiary;
    }

    public void setDateOfDiary(String dateOfDiary) {
        this.dateOfDiary = dateOfDiary;
    }

    public ArrayList<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(ArrayList<Dish> dishes) {
        this.dishes = dishes;
    }

    public ArrayList<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(ArrayList<Exercise> exercises) {
        this.exercises = exercises;
    }

    public float getCaloIn() {
        return caloIn;
    }

    public void setCaloIn(int caloIn) {
        this.caloIn = caloIn;
    }

    public float getCaloOut() {
        return caloOut;
    }

    public void setCaloOut(int caloOut) {
        this.caloOut = caloOut;
    }

    public float getTotalCalo() {
        return totalCalo;
    }

    public void setTotalCalo(int totalCalo) {
        this.totalCalo = totalCalo;
    }


}
