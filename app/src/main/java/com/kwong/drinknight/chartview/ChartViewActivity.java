package com.kwong.drinknight.chartview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kwong.drinknight.MainActivity;
import com.kwong.drinknight.chartview.MyChartView;
import com.kwong.drinknight.R;
import com.kwong.drinknight.chartview.SingleView;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChartViewActivity extends AppCompatActivity {

    private MyChartView myChartView;
    private SingleView mMySingleChartView;
    private List<Float> chartList;
    private List<Float> singlelist;
    private RelativeLayout relativeLayout;



    private LinearLayout llChart;
    private LinearLayout llSingle;
    private RelativeLayout rlSingle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_view);

        myChartView = (MyChartView) findViewById(R.id.my_chart_view);
        initChatView();

        initSingle();
    }

    private void initChatView() {

        myChartView.setLeftColorBottom(getResources().getColor(R.color.leftColorBottom));
        myChartView.setLeftColor(getResources().getColor(R.color.leftColor));
        myChartView.setRightColor(getResources().getColor(R.color.rightColor));
        myChartView.setRightColorBottom(getResources().getColor(R.color.rightBottomColor));
        myChartView.setSelectLeftColor(getResources().getColor(R.color.selectLeftColor));
        myChartView.setSelectRightColor(getResources().getColor(R.color.selectRightColor));
        myChartView.setLineColor(getResources().getColor(R.color.xyColor));
        chartList = new ArrayList<>();

        relativeLayout = (RelativeLayout) findViewById(R.id.linearLayout);
        relativeLayout.removeView(llChart);
        Random random = new Random();
        while (chartList.size() < 24) {
            int randomInt = random.nextInt(100);
            chartList.add((float) randomInt);
        }
        myChartView.setList(chartList);
        myChartView.setListener(new MyChartView.getNumberListener() {
            @Override
            public void getNumber(int number, int x, int y) {
                relativeLayout.removeView(llChart);
                //反射加载点击柱状图弹出布局
                llChart = (LinearLayout) LayoutInflater.from(ChartViewActivity.this).inflate(R.layout.layout_shouru_zhichu, null);
                TextView tvZhichu = (TextView) llChart.findViewById(R.id.tv_zhichu);
                TextView tvShouru = (TextView) llChart.findViewById(R.id.tv_shouru);
                tvZhichu.setText((number + 1) + "月支出" + " " + chartList.get(number * 2));
                tvShouru.setText ( "收入: " + chartList.get(number * 2 + 1));
                llChart.measure(0, 0);//调用该方法后才能获取到布局的宽度
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.leftMargin = x - 100;
                if (x - 100 < 0) {
                    params.leftMargin = 0;
                } else if (x - 100 > relativeLayout.getWidth() - llChart.getMeasuredWidth()) {
                    //设置布局距左侧屏幕宽度减去布局宽度
                    params.leftMargin = relativeLayout.getWidth() - llChart.getMeasuredWidth();
                }
                llChart.setLayoutParams(params);
                relativeLayout.addView(llChart);
            }
        });
    }

    /**
     * 初始化单柱柱状图
     */
    private void initSingle(){
        mMySingleChartView = (SingleView) findViewById(R.id.my_single_chart_view);
        rlSingle = (RelativeLayout) findViewById(R.id.rl_single);
        singlelist = new ArrayList<>();
        Random random = new Random();
        while (singlelist.size() < 12) {
            int randomInt = random.nextInt(100);
            singlelist.add((float) randomInt);
        }

        mMySingleChartView.setList(singlelist);
        rlSingle.removeView(llSingle);
        //原理同双柱
        mMySingleChartView.setListener(new SingleView.getNumberListener() {
            @Override
            public void getNumber(int number, int x, int y) {
                rlSingle.removeView(llSingle);
                llSingle = (LinearLayout) LayoutInflater.from(ChartViewActivity.this).inflate(R.layout.layout_pro_expense, null);
                TextView tvMoney = (TextView) llSingle.findViewById(R.id.tv_shouru_pro);
                tvMoney.setText((number + 1) + "月支出" + (singlelist.get(number)));
                llSingle.measure(0, 0);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.leftMargin = x - 100;
                if (x - 100 < 0) {
                    params.leftMargin = 0;
                } else if (x - 100 > relativeLayout.getWidth() - llSingle.getMeasuredWidth()) {
                    params.leftMargin = relativeLayout.getWidth() - llSingle.getMeasuredWidth();
                }

                llSingle.setLayoutParams(params);
                rlSingle.addView(llSingle);
            }
        });
    }


}

