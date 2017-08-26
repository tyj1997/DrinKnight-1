package com.kwong.drinknight;

/**
 * Created by 锐锋 on 2017/8/25.
 */

public class Person {
    private String id;
    private String name;
    private int rank;
    private int imageId;
    private int drinkVolume;

    public Person(int rank, String id, String name, int imageId, int drinkVolume) {
        this.id = id;
        this.name = name;
        this.imageId = imageId;
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

    public int getImageId() {
        return imageId;
    }

    public int getDrinkVolume() {
        return drinkVolume;
    }

}
