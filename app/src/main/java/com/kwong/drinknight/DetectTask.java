package com.kwong.drinknight;

import android.icu.util.Calendar;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;


/**
 * Created by 锐锋 on 2017/8/24.
 */

public class DetectTask extends AsyncTask<Calendar,Integer,Integer>{
    public static final int WAIT= 0;
    public static final int DRINK_NOW =1;

    private boolean hasDrunk = false;
    private boolean RemindLater = false;
    private DetectListener listener;
    public DetectTask(DetectListener listener){
        this.listener = listener;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected Integer doInBackground(Calendar... params) {
        Calendar now =  Calendar.getInstance();
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int minute = now.get(Calendar.MINUTE);
        int suggestedHour = params[0].get(Calendar.HOUR_OF_DAY);
        int suggestedMinute = params[0].get(Calendar.MINUTE);
        try {
            if ((hour == suggestedHour)&&(minute==suggestedMinute)){
                return DRINK_NOW;
            }else if ((hour < suggestedHour)||((hour == suggestedHour)&&(minute < suggestedHour))){
                return WAIT;
            }else if (RemindLater == true){//如何返回数据等待

                return WAIT;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return DRINK_NOW;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {

        listener.onTimeBeforeDrink(values[0]);
    }

    @Override
    protected void onPostExecute(Integer status) {
        switch (status){
            case DRINK_NOW:
                listener.onTimeToDrink();
                break;

            default:
                break;
        }
    }
    public void remindLater(){
        RemindLater = true;
    }

    public void drinking(){
        hasDrunk = true;
    }


}
