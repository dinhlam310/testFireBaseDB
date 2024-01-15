package com.example.testfirebasedb.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.HorizontalScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testfirebasedb.R;
import com.example.testfirebasedb.entity.Day;
import com.example.testfirebasedb.entity.MyDate;
import com.example.testfirebasedb.entity.Profile;
import com.example.testfirebasedb.listeners.ToWindowOnClickWithClosing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class StatisticActivity extends AppCompatActivity {
    private BarChart barChart;
    Profile profile;
    FirebaseDatabase database;
    private DatabaseReference ref;
    public static final int DAY_DIAGRAM = 0;
    public static final int WEEK_DIAGRAM = 1;
    public static final int MONTH_DIAGRAM = 2;
    public static final int YEAR_DIAGRAM = 3;
    private static int diagramID = DAY_DIAGRAM;
    HorizontalScrollView horizontalScrollView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);
        findViewById(R.id.from_statistic_to_menu).setOnClickListener(new ToWindowOnClickWithClosing(this,MyMenuActivity.class));
//        LocalDate currentDate = LocalDate.now();
//        int day = currentDate.getDayOfMonth();
//        int month = currentDate.getMonthValue();
//        int year = currentDate.getYear();
        barChart = findViewById(R.id.chart);
        barChart.getAxisRight().setDrawLabels(false);
//        MyDate date = new MyDate();
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(date);
        database = FirebaseDatabase.getInstance();
        ref = database.getReference().child("Day");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<BarEntry> dataValsCaloIn = new ArrayList();
                ArrayList<String> dataValsDate = new ArrayList();
                float i = 0;
                if(snapshot.hasChildren()){
                    for(DataSnapshot myDataSnapshot : snapshot.getChildren()){
                        Day day = myDataSnapshot.getValue(Day.class);
                        dataValsCaloIn.add(new BarEntry(i,day.getCaloIn()));
                        dataValsDate.add(day.getDateOfDiary());
                        i++;
                    }
                        showChart(dataValsCaloIn,dataValsDate);
                }else{
                    barChart.clear();
                    barChart.invalidate();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void showChart(ArrayList<BarEntry> dataValsCaloIn, ArrayList<String> dataValsDate) {
        BarDataSet bardataset = new BarDataSet(dataValsCaloIn, "CaloriesIn");
        barChart.animateY(2000);
        BarData data = new BarData(bardataset);
        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
        bardataset.setValueTextSize(16f);
        barChart.setData(data);
        barChart.getDescription().setEnabled(false);
        barChart.invalidate(); //refresh
        data.setValueTextColor(Color.RED);
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(dataValsDate));
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.setScaleEnabled(true);
        barChart.setDragEnabled(true);
        barChart.setScaleEnabled(true);
        Legend l = barChart.getLegend();
        l.setTextSize(10f);
        l.setTextColor(Color.RED);
        l.setForm(Legend.LegendForm.CIRCLE);
    }
}
