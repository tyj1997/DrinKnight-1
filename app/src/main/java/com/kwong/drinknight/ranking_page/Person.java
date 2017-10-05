package com.kwong.drinknight.ranking_page;

/**
 * Created by 锐锋 on 2017/8/25.
 */

public class Person {
    private String id;
    private String name;

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAccount() {

        return account;
    }

    private String account;
    private int rank;
    private String imageName;
    private int dose;


    public Person(int rank, String id, String name, String imageName, int dose) {
        this.rank = rank;
        this.id = id;
        this.name = name;
        this.imageName = imageName;
        this.dose = dose;
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

    public int getDose() {
        return dose;
    }

}
