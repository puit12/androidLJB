package com.example.ljb.jbapp;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ljb.jbapp.utils.AudioWriterPCM;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.naver.speech.clientapi.SpeechRecognitionResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


public class TabFragment3 extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Button resultBtn;
    private Button countBtn;
    private TextView resultTextview;
    UploadEC2 uploadEC2;
    ResultHandler resultHandler;
    PieChart pieChart;

    public TabFragment3() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_tab_fragment3, container, false);
        resultBtn = (Button) v.findViewById(R.id.resultBtn);
        countBtn =(Button) v.findViewById(R.id.countBtn);

        resultTextview = (TextView) v.findViewById(R.id.resultTextview);
        resultHandler = new ResultHandler(this);

        pieChart = (PieChart)v.findViewById(R.id.piechart);
        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setDrawHoleEnabled(false);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(61f);


        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5,10,5,5);

        uploadEC2 = new UploadEC2();

        resultBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             uploadEC2.resultSentenceFromEC2(getActivity(),resultHandler,"returnresult");
            }
        });

        countBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadEC2.resultWordCountFromEC2(getActivity(),resultHandler,"returncount");
            }
        });

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
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

        for(int i = 0; i < jsonArray.length(); i++){
            jsonArray2 = jsonArray.getJSONArray(i);
            String word = jsonArray2.getString(0);
            int count = jsonArray2.getInt(1);
            yValues.add(new PieEntry(count,word));
        }

        pieChart.animateY(1000,Easing.EasingOption.EaseInOutCubic); //애니메이션

        PieDataSet dataSet = new PieDataSet(yValues,"Countries");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        PieData data = new PieData((dataSet));
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.YELLOW);

        pieChart.setData(data);
    }
}
