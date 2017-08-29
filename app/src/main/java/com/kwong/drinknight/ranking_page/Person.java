package com.kwong.drinknight.ranking_page;

/**
 * Created by 锐锋 on 2017/8/25.
 */

public class Person {
    private String id;
    private String name;
    private int rank;
    private String imageName;
    private int drinkVolume;

    public Person(int rank, String id, String name, String imageName, int drinkVolume) {
        this.rank = rank;
        this.id = id;
        this.name = name;
        this.imageName = imageName;
        this.drinkVolume = drinkVolume;
    }

    public int getRank() {
        return rank;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImageName() {
        return imageName;
    }

    public int getDrinkVolume() {
        return drinkVolume;
    }

}
