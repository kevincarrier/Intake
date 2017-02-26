package org.intakers.intake;


import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;


/**
 * A simple {@link Fragment} subclass.
 */
public class GraphFragment extends Fragment {
    Logger log = Logger.getAnonymousLogger();
    LineChart lineChart;
    PieChart pieChart;

    public GraphFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_graph, container, false);
        lineChart = (LineChart) (v.findViewById(R.id.chart1));
        
        retrieveGraphData();
        formatGraph();
        return v;
    }

    public void retrieveGraphData(){
        log.info("retrieveGraphData() called");
        Thread t = new Thread(){
            @Override
            public void run() {
                String urlString = "https://api.blockchain.info/charts/market-price?format=json&timespan=14d";
                try {
                    URL url = new URL(urlString);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                    StringBuilder response = new StringBuilder();
                    String tmpString = "";
                    while(tmpString != null){
                        //Append to response the data
                        response.append(tmpString);
                        //read in input data
                        tmpString = reader.readLine();
                    }
                    Message msg = Message.obtain();
                    msg.obj = response.toString();
                    graphResponseHandler.sendMessage(msg);
                }catch(IOException e){
                    e.printStackTrace();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    Handler graphResponseHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            log.info("graphResponseHandler() called");
            try{
                JSONObject marketPrice = new JSONObject((String) msg.obj);
                JSONArray valArr = marketPrice.getJSONArray("values");
                JSONObject values;
                List<Entry> entries = new ArrayList<>();
                long unixDT;
                double x;
                double y;
                for(int i = 0; i < valArr.length(); i++){
                    values = valArr.getJSONObject(i);
                    x = values.getDouble("x");
                    y = values.getDouble("y");
                    unixDT = (long) x * 1000;
                    Date time = new Date(unixDT);
                    String date = new SimpleDateFormat("MM.dd.yy").format(time);
                    log.info("x: " + x + " " + date + "\ty: " + y);
                    entries.add(new Entry((float) x, (float) y));
                    log.info("size: " + entries.size());
                }
                drawGraph(entries);
            } catch(Exception e){
                e.printStackTrace();
            }
            return true;
        }
    });

    public void drawGraph(List<Entry> entries){
        log.info("drawGraph() called");
        LineDataSet dataSet = new LineDataSet(entries, "Datetime");
        dataSet.setDrawCircles(false);
        dataSet.setDrawValues(false);
        dataSet.setColor(Color.WHITE);
        //fill graph with a color gradient
        dataSet.setDrawFilled(true);
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.fade_color);
        dataSet.setFillDrawable(drawable);
        //set thickness of line graph
        dataSet.setLineWidth(2);
        //Convert to linedata object
        LineData lineData = new LineData(dataSet);
        //Display date on x-axis in specific date format
        XAxis xAxis = lineChart.getXAxis();
        //turn off x-axis grid lines
        xAxis.setDrawGridLines(false);
        //rotate data labels
        xAxis.setLabelRotationAngle(-45);
        //Get labels in date format
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            private SimpleDateFormat date = new SimpleDateFormat("MM.dd.yy");
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                Date dt = new Date((long) value * 1000);
                String val = date.format(dt);
                log.info("original value: " + value + " formatted - " + val);
                return val;
            }
        });
        //set x axis to bottom of graph
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //Set color to white
        xAxis.setTextColor(Color.BLUE);
        xAxis.setAxisLineColor(Color.BLUE);
        //set the data
        lineChart.setData(lineData);
        //draw the graph
        lineChart.animateX(1000);
    }

    public void formatGraph(){
        log.info("formatGraph() called");
        //turn off the legend
        Legend legend = lineChart.getLegend();
        legend.setEnabled(false);
        //turn off right y axis
        YAxis yRight = lineChart.getAxisRight();
        yRight.setEnabled(false);
        //get left y axis
        YAxis yAxis = lineChart.getAxisLeft();
        //set minimum of graph
        yAxis.setAxisMinimum(0);
        //change y-axis color & style
        yAxis.setTextColor(Color.BLUE);
        yAxis.setAxisLineColor(Color.BLUE);
        yAxis.setZeroLineColor(Color.BLUE);
        //turn off y-axis grid lines
        yAxis.setDrawGridLines(false);
        //turn off description
        lineChart.setDescription(null);
        lineChart.getAxisLeft().setDrawGridLines(false);
        //autoscale on y-axis
        lineChart.setAutoScaleMinMaxEnabled(true);
    }
}
