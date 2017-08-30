package com.kwong.drinknight.drinking_data_view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kwong.drinknight.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import lecho.lib.hellocharts.formatter.ColumnChartValueFormatter;
import lecho.lib.hellocharts.formatter.SimpleColumnChartValueFormatter;
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by Administrator on 2017/8/29.
 */

public class TodayData extends AppCompatActivity implements View.OnClickListener{
    private ColumnChartView chart;

    private ColumnChartData data;
    private double drinksum;

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.TodayData:
                setFirstChart();
                break;
            case R.id.WeekData:
                setWeekChart();
                break;
            case R.id.MonthData:
               setMonthChart();
                break;
            default:
                break;
        }
    }

    private TextView textView;
    private String today;

    private ArrayList<String> DrinkingTime=new ArrayList<>();
    private int Xcount=0;
    private ArrayList <Integer>DrinkingDose=new ArrayList<>();
    private HashMap<String,HashMap<String,Double>> DrinkDateTime = new HashMap<String,HashMap<String,Double>>();
    private List<PointValue> mPointValues = new ArrayList<PointValue>();
    private List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_data);
        Button todaydata=(Button)findViewById(R.id.TodayData);
        Button Weekdata=(Button)findViewById(R.id.WeekData);
        Button Monthdata=(Button)findViewById(R.id.MonthData);
        todaydata.setOnClickListener(this);
        Weekdata.setOnClickListener(this);
        Monthdata.setOnClickListener(this);
        Intent intent=getIntent();
       // DrinkingTime=intent.getStringArrayListExtra("drink_time");
      //  DrinkingDose=intent.getIntegerArrayListExtra("drink_dose");
        Calendar now = Calendar.getInstance();
        System.out.println("年：" + now.get(Calendar.YEAR));
        System.out.println("月：" + (now.get(Calendar.MONTH) + 1));
        System.out.println("日：" + now.get(Calendar.DAY_OF_MONTH));
        today=now.get(Calendar.YEAR)+"/"+(now.get(Calendar.MONTH) + 1)+"/"+now.get(Calendar.DAY_OF_MONTH);
        DrinkDateTime=(HashMap)intent.getSerializableExtra("drink_datetime");
        drinksum=intent.getDoubleExtra("drink_sum",0.00);
        textView=(TextView)findViewById(R.id.textView);
        textView.setText("  饮水总量:"+drinksum+"ml");
        initView();
        setFirstChart();

      //  getAxisXLables();//获取x轴的标注
     //  getAxisPoints();//获取坐标点
    //    initLineChart();//初始化
    }
    protected void initView() {
        chart= (ColumnChartView) findViewById(R.id.chart);
        chart.setZoomEnabled(false);//禁止手势缩放
    }
    private void setFirstChart() {
        int numSubcolumns = 1;
        if(DrinkDateTime!=null&&DrinkDateTime.get(today)!=null) {
            int numColumns = DrinkDateTime.get(today).size();
            Set<String> keys = DrinkDateTime.get(today).keySet();
            Iterator iterator = keys.iterator();
            //定义一个圆柱对象集合
            List<Column> columns = new ArrayList<Column>();
            //子列数据集合
            List<SubcolumnValue> values;

            List<AxisValue> axisValues = new ArrayList<AxisValue>();
            //遍历列数numColumns
            for (String Drinktime : keys) {

                values = new ArrayList<SubcolumnValue>();
                //遍历每一列的每一个子列
                for (int j = 0; j < numSubcolumns; ++j) {
                    //为每一柱图添加颜色和数值
                    float f = (float) DrinkDateTime.get(today).get(Drinktime).doubleValue();
                    values.add(new SubcolumnValue(f, ChartUtils.pickColor()));
                }
                Column column = new Column(values);
                ColumnChartValueFormatter chartValueFormatter = new SimpleColumnChartValueFormatter(2);
                column.setFormatter(chartValueFormatter);
                //是否有数据标注
                column.setHasLabels(false);
                //是否是点击圆柱才显示数据标注
                column.setHasLabelsOnlyForSelected(true);
                columns.add(column);
                //给x轴坐标设置描述
                axisValues.add(new AxisValue(Xcount++).setLabel(Drinktime));
            }
            Xcount = 0;
            data = new ColumnChartData(columns);

            //定义x轴y轴相应参数
            Axis axisX = new Axis();
            Axis axisY = new Axis().setHasLines(true);
            axisY.setName("饮水量(ml)");//轴名称
            axisX.setName("时间");
            axisY.hasLines();//是否显示网格线
            axisY.setTextColor(R.color.blue);//颜色

            axisX.hasLines();
            axisX.setTextColor(R.color.blue);
            axisX.setValues(axisValues);
            //把X轴Y轴数据设置到ColumnChartData 对象中
            data.setAxisXBottom(axisX);
            data.setAxisYLeft(axisY);
            //给表填充数据，显示出来
            chart.setColumnChartData(data);
            chart.startDataAnimation(2000);
        }
    }
    private void setWeekChart(){
        Calendar c = Calendar.getInstance();
       c.add(Calendar.DAY_OF_MONTH,-6);
        Date date = c.getTime();
        String WeekBefore=c.get(Calendar.YEAR)+"/"+(c.get(Calendar.MONTH) + 1)+"/"+c.get(Calendar.DAY_OF_MONTH);
        int numSubcolumns = 1;
        if(DrinkDateTime!=null) {
            int numColumns = 7;
            //定义一个圆柱对象集合
            List<Column> columns = new ArrayList<Column>();
            //子列数据集合
            List<SubcolumnValue> values;

            List<AxisValue> axisValues = new ArrayList<AxisValue>();
            //遍历列数numColumns
            for (int i=0;i<7;i++) {

                float f=0;
                values = new ArrayList<SubcolumnValue>();
                //遍历每一列的每一个子列
                if(DrinkDateTime.get(WeekBefore)!=null) {
                    Set<String> key = DrinkDateTime.get(WeekBefore).keySet();
                        for (String Drinktime : key) {
                            f += (float) DrinkDateTime.get(WeekBefore).get(Drinktime).doubleValue();
                        }
                    values.add(new SubcolumnValue(f/1000, ChartUtils.pickColor()));
                }
                else
                    values.add(new SubcolumnValue(0, ChartUtils.pickColor()));
                Column column = new Column(values);
                ColumnChartValueFormatter chartValueFormatter = new SimpleColumnChartValueFormatter(2);
                column.setFormatter(chartValueFormatter);
                //是否有数据标注
                column.setHasLabels(false);
                //是否是点击圆柱才显示数据标注
                column.setHasLabelsOnlyForSelected(true);
                columns.add(column);
                //给x轴坐标设置描述
                axisValues.add(new AxisValue(Xcount++).setLabel((c.get(Calendar.MONTH) + 1)+"/"+c.get(Calendar.DAY_OF_MONTH)));
                c.add(Calendar.DAY_OF_MONTH,1);
                WeekBefore=c.get(Calendar.YEAR)+"/"+(c.get(Calendar.MONTH) + 1)+"/"+c.get(Calendar.DAY_OF_MONTH);
            }
            Xcount = 0;
            data = new ColumnChartData(columns);

            //定义x轴y轴相应参数
            Axis axisX = new Axis();
            Axis axisY = new Axis().setHasLines(true);
            axisY.setName("饮水量(L)");//轴名称
            axisX.setName("时间");
            axisY.hasLines();//是否显示网格线
            axisY.setTextColor(R.color.blue);//颜色

            axisX.hasLines();
            axisX.setTextColor(R.color.blue);
            axisX.setValues(axisValues);
            //把X轴Y轴数据设置到ColumnChartData 对象中
            data.setAxisXBottom(axisX);
            data.setAxisYLeft(axisY);
            //给表填充数据，显示出来
            chart.setColumnChartData(data);
            chart.startDataAnimation(2000);
        }
    }
    private void setMonthChart(){
        Calendar c = Calendar.getInstance();
        int numColumns = c.get(Calendar.DAY_OF_MONTH);
        System.out.println(numColumns);
        c.add(Calendar.DAY_OF_MONTH,-c.get(Calendar.DAY_OF_MONTH)+1);
        Date date = c.getTime();
        String MonthBefore=c.get(Calendar.YEAR)+"/"+(c.get(Calendar.MONTH) + 1)+"/"+c.get(Calendar.DAY_OF_MONTH);
        System.out.println(MonthBefore);
        int numSubcolumns = 1;
        if(DrinkDateTime!=null) {

            //定义一个圆柱对象集合
            List<Column> columns = new ArrayList<Column>();
            //子列数据集合
            List<SubcolumnValue> values;

            List<AxisValue> axisValues = new ArrayList<AxisValue>();
            //遍历列数numColumns
            for (int i=1;i<=numColumns;i++) {

                float f=0;
                values = new ArrayList<SubcolumnValue>();
                //遍历每一列的每一个子列
                if(DrinkDateTime.get(MonthBefore)!=null) {
                    Set<String> key = DrinkDateTime.get(MonthBefore).keySet();
                    for (String Drinktime : key) {
                        f += (float) DrinkDateTime.get(MonthBefore).get(Drinktime).doubleValue();
                    }
                    values.add(new SubcolumnValue(f/1000, ChartUtils.pickColor()));
                }
                else
                    values.add(new SubcolumnValue(0, ChartUtils.pickColor()));
                Column column = new Column(values);
                ColumnChartValueFormatter chartValueFormatter = new SimpleColumnChartValueFormatter(2);
                column.setFormatter(chartValueFormatter);
                //是否有数据标注
                column.setHasLabels(false);
                //是否是点击圆柱才显示数据标注
                column.setHasLabelsOnlyForSelected(true);
                columns.add(column);
                //给x轴坐标设置描述
                System.out.println(c.get(Calendar.DAY_OF_MONTH)%7);
              if((c.get(Calendar.DAY_OF_MONTH)%7)==1)
                  axisValues.add(new AxisValue(Xcount++).setLabel((c.get(Calendar.MONTH) + 1)+"/"+c.get(Calendar.DAY_OF_MONTH)));

                    else
                    axisValues.add(new AxisValue(Xcount++).setLabel(""));
                c.add(Calendar.DAY_OF_MONTH,1);
                MonthBefore=c.get(Calendar.YEAR)+"/"+(c.get(Calendar.MONTH) + 1)+"/"+c.get(Calendar.DAY_OF_MONTH);
            }
            Xcount = 0;
            data = new ColumnChartData(columns);

            //定义x轴y轴相应参数
            Axis axisX = new Axis();
            Axis axisY = new Axis().setHasLines(true);
            axisY.setName("饮水量(L)");//轴名称
            axisX.setName("时间");
            axisY.hasLines();//是否显示网格线
            axisY.setTextColor(R.color.blue);//颜色
            axisX.setMaxLabelChars(1);
            axisX.hasLines();
            axisX.setTextColor(R.color.blue);
            axisX.setValues(axisValues);
            //把X轴Y轴数据设置到ColumnChartData 对象中
            data.setAxisXBottom(axisX);
            data.setAxisYLeft(axisY);
            //给表填充数据，显示出来
            chart.setColumnChartData(data);
            chart.startDataAnimation();
        }
    }
    private void getAxisXLables(){
        for (int i = 0; i <DrinkingTime.size() ;i++) {
            mAxisXValues.add(new AxisValue(i).setLabel( DrinkingTime.get(i)));
        }
    }
    private void getAxisPoints() {
        for (int i = 0; i < DrinkingDose.size(); i++) {
            mPointValues.add(new PointValue(i, DrinkingDose.get(i)));
        }
    }
//    private void initLineChart() {
//        Line line = new Line(mPointValues).setColor(Color.parseColor("#FFCD41"));  //折线的颜色（橙色）
//        List<Line> lines = new ArrayList<Line>();
//        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
//        line.setCubic(false);//曲线是否平滑，即是曲线还是折线
//        line.setFilled(false);//是否填充曲线的面积
//        line.setHasLabels(true);//曲线的数据坐标是否加上备注
////      line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
//        line.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
//        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
//        lines.add(line);
//        LineChartData data = new LineChartData();
//        data.setLines(lines);
//
//        //坐标轴
//       Axis axisX = new Axis(); //X轴
//        axisX.setHasTiltedLabels(true);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
//        axisX.setTextColor(Color.WHITE);  //设置字体颜色
//        //axisX.setName("date");  //表格名称
//        axisX.setTextSize(10);//设置字体大小
//        axisX.setMaxLabelChars(8); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisXValues.length
//        axisX.setValues(mAxisXValues);  //填充X轴的坐标名称
//        data.setAxisXBottom(axisX); //x 轴在底部
//        //data.setAxisXTop(axisX);  //x 轴在顶部
//        axisX.setHasLines(true); //x 轴分割线
//
//        // Y轴是根据数据的大小自动设置Y轴上限(在下面我会给出固定Y轴数据个数的解决方案)
//        Axis axisY = new Axis();  //Y轴
//        axisY.setName("");//y轴标注
//        axisY.setTextSize(10);//设置字体大小
//        data.setAxisYLeft(axisY);  //Y轴设置在左边
//        //data.setAxisYRight(axisY);  //y轴设置在右边
//
//
//        //设置行为属性，支持缩放、滑动以及平移
//        lineChartView.setInteractive(true);
//        lineChartView.setZoomType(ZoomType.HORIZONTAL);
//        lineChartView.setMaxZoom((float) 2);//最大方法比例
//        lineChartView.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
//        lineChartView.setLineChartData(data);
//        lineChartView.setVisibility(View.VISIBLE);
//    }

    }
