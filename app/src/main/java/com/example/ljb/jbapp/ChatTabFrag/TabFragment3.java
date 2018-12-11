package com.example.ljb.jbapp.ChatTabFrag;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.ljb.jbapp.R;
import com.example.ljb.jbapp.Service.SaveService;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.ref.WeakReference;
import java.util.ArrayList;


public class TabFragment3 extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Button resultBtn;
    private Button saveSSBtn;
    private Button saveOriBtn;
    private TextView resultTextview;
    UploadEC2 uploadEC2;
    SaveService saveServiceService;
    ResultHandler resultHandler;
    PieChart pieChart;


    public TabFragment3() {
        resultHandler = new ResultHandler(this);
        saveServiceService = new SaveService();
        uploadEC2 = new UploadEC2();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.tab_fragment3, container, false);

        initUI(view);

        saveOriBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveServiceService.original(resultTextview.getText().toString());
            }
        });
        resultBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadEC2.resultSentenceFromEC2(getActivity(), resultHandler, "returnresult");
            }
        });
        saveSSBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    saveServiceService.screenshot(view);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    static class ResultHandler extends Handler {
        private final WeakReference<TabFragment3> thisTab;

        ResultHandler(TabFragment3 tab) {
            thisTab = new WeakReference<>(tab);
        }

        @Override
        public void handleMessage(Message msg) {
            TabFragment3 tab1 = thisTab.get();

            if (tab1 != null) {
                try {
                    tab1.handleMessage(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void handleMessage(Message msg) throws JSONException {
        switch (msg.what) {
            case 1:
                String result = uploadEC2.returnResult();
                resultTextview.setText(result);
                uploadEC2.resultWordCountFromEC2(getActivity(), resultHandler, "returncount");
                break;
            case 2:
                String count = uploadEC2.returnResult();
                jsonParsing(count);
                break;
        }
    }


    private void jsonParsing(String json) throws JSONException {
        ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();

        JSONArray jsonArray = new JSONArray(json);
        JSONArray jsonArray2;

        for (int i = 0; i < jsonArray.length(); i++) {
            jsonArray2 = jsonArray.getJSONArray(i);
            String word = jsonArray2.getString(0);
            int count = jsonArray2.getInt(1);
            yValues.add(new PieEntry(count, word));
        }

        PieDataSet dataSet = new PieDataSet(yValues, "Countries");
        PieData data = new PieData((dataSet));
        createChart(dataSet, data);
    }

    private void initUI(View view) {
        resultBtn = (Button) view.findViewById(R.id.resultBtn);
        saveSSBtn = (Button) view.findViewById(R.id.saveScreenshot);
        saveOriBtn = (Button) view.findViewById(R.id.saveOriginal);
        resultTextview = (TextView) view.findViewById(R.id.resultTextview);
        pieChart = (PieChart) view.findViewById(R.id.piechart);
    }

    private void createChart(PieDataSet pieDataSet, PieData pieData) {
        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.setHoleColor(Color.BLACK);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.getLegend().setEnabled(false);
        pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic);

        pieDataSet.setSliceSpace(3f);
        pieDataSet.setSelectionShift(5f);
        pieDataSet.setColors(ColorTemplate.PASTEL_COLORS);

        pieData.setValueTextSize(10f);
        pieData.setValueTextColor(Color.YELLOW);
        pieChart.setData(pieData);
    }

}
