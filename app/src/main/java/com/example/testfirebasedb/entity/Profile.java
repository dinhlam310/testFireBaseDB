package com.example.testfirebasedb.entity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.testfirebasedb.activity.ProfileActivity;

public class Profile {

    private String name;
    private boolean gender;
    private int age;
    private int height;
    private int weight;
    private int aimCalorie;
    private static Profile profile = null;

    private Profile(Context context) {
        SharedPreferences sPref = context.getSharedPreferences("profile_preferences", Context.MODE_PRIVATE);
        if (!sPref.contains("age")) {
            SharedPreferences.Editor editor = sPref.edit();
            editor.putString("name",name);
            editor.putBoolean("gender", gender);
            editor.putInt("age", age);
            editor.putInt("height", height);
            editor.putInt("weight", weight);
            aimCalorie = calculateCalories();
            editor.putInt("aimCalorie", aimCalorie);
            editor.apply();
        } else {
            name = sPref.getString("name",name);
            gender = sPref.getBoolean("gender", gender);
            age = sPref.getInt("age", age);
            height = sPref.getInt("height", height);
            weight = sPref.getInt("weight", weight);
            aimCalorie = sPref.getInt("aimCalorie", aimCalorie);
        }
    }

    public static Profile getProfile(Context context) {
        if (profile == null) {
            profile = new Profile(context);
        }
        return profile;
    }

    public int calculateCalories() {
        if (gender) //gender = true => Men , this is BMR
            return (int) (66 + 13.7 * weight + 5 * height - 6.8 * age);
        else
            return (int) (655 + 9.6 * weight + 1.8 * height - 4.7 * age);
    }

    public int calculateMyCalories(boolean mygender, int myweight, int myheight, int myage) {
        if (mygender == true) //gender = true => Men , this is BMR
            return (int) (66 + 13.7 * myweight + 5 * myheight - 6.8 * myage);
        else
            return (int) (655 + 9.6 * myweight + 1.8 * myheight - 4.7 * myage);
    }

    //TDEE = BMRR x R ( 1,2 < R < 1,7)


    public Profile() {
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public int getWeight() {
        return weight;
    }

    public int getHeight() {
        return height;
    }

    public boolean getGender() {
        return gender;
    }

    public int getAimCalorie() {
        return aimCalorie;
    }

    public void setName(Activity activity, String name) {
        if (activity instanceof ProfileActivity) {
            this.name = name;
        }
    }

    public void setGender(Activity activity, boolean gender) {
        if (activity instanceof ProfileActivity) {
            this.gender = gender;
        }
    }

    public void setAge(Activity activity, int age) {
        if (activity instanceof ProfileActivity) {
            this.age = age;
        }
    }

    public void setWeight(Activity activity, int weight) {
        if (activity instanceof ProfileActivity) {
            this.weight = weight;
        }
    }

    public void setHeight(Activity activity, int height) {
        if (activity instanceof ProfileActivity) {
            this.height = height;
        }
    }

    public void setAimCalorie(Activity activity, int aimCalorie) {
        if (activity instanceof ProfileActivity) {
            this.aimCalorie = aimCalorie;
        }
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public void saveData(Context context) {
        SharedPreferences sPref = context.getSharedPreferences("profile_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sPref.edit();
        editor.putString("name", name);
        editor.putBoolean("gender", gender);
        editor.putInt("age", age);
        editor.putInt("height", height);
        editor.putInt("weight", weight);
        editor.putInt("aimCalorie", aimCalorie);
        editor.commit();
    }
}
