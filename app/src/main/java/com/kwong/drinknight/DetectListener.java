package com.kwong.drinknight;

/**
 * Created by 锐锋 on 2017/8/24.
 */

public interface DetectListener {
    void onTimeBeforeDrink(int time);
    void onTimeToDrink();
}
