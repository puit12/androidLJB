package com.example.ljb.jbapp.ChatTabFrag;

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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ljb.jbapp.TabAcitivity;
import com.example.ljb.jbapp.NaverUtils.NaverRecognizer;
import com.example.ljb.jbapp.R;
import com.example.ljb.jbapp.NaverUtils.AudioWriterPCM;
import com.naver.speech.clientapi.SpeechRecognitionResult;

import java.lang.ref.WeakReference;
import java.util.List;


public class TabFragment1 extends Fragment {
    private static final String CLIENT_ID = "";
    // Client ID 체크

    private RecognitionHandler handler;
    private NaverRecognizer naverRecognizer;

    private TextView txtResult;
    private TextView txtPartialResult;
    private Button btnStart;
    private String mResult;
    private String result;
    private CustomAdapter m_Adapter;
    private ListView m_ListView;
    boolean check;

    private AudioWriterPCM writer;

    private OnFragmentInteractionListener mListener;

    public TabFragment1() {
        handler = new RecognitionHandler(this);
        m_Adapter = new CustomAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment1, container, false);
        naverRecognizer = new NaverRecognizer(getActivity(), handler, CLIENT_ID);
        initUI(view);


        check = false;

        btnStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!naverRecognizer.getSpeechRecognizer().isRunning() && check == false) {
                    mResult = "";
                    check = true;
                    txtPartialResult.setText("Connecting");
                    btnStart.setText(R.string.str_stop);
                    naverRecognizer.recognize();

                } else if (check == true) {
                    btnStart.setText(R.string.str_start);
                    check = false;
                }
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        naverRecognizer.getSpeechRecognizer().initialize();
    }

    @Override
    public void onResume() {
        super.onResume();
        mResult = "";
        txtResult.setText("");
        btnStart.setText(R.string.str_start);
        btnStart.setEnabled(true);
    }

    @Override
    public void onStop() {
        super.onStop();
        naverRecognizer.getSpeechRecognizer().release();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    static class RecognitionHandler extends Handler {
        private final WeakReference<TabFragment1> thisTab;

        RecognitionHandler(TabFragment1 tab) {
            thisTab = new WeakReference<>(tab);
        }

        @Override
        public void handleMessage(Message msg) {
            TabFragment1 tab1 = thisTab.get();

            if (tab1 != null) {
                tab1.handleMessage(msg);
            }
        }
    }

    private void handleMessage(Message msg) {
        switch (msg.what) {
            case R.id.clientReady:
                txtPartialResult.setText("");
                writer = new AudioWriterPCM(
                        Environment.getExternalStorageDirectory().getAbsolutePath() + "/NaverSpeechTest");
                writer.open("Test");
                break;

            case R.id.audioRecording:
                writer.write((short[]) msg.obj);
                break;

            case R.id.partialResult:
                mResult = (String) (msg.obj);
                txtPartialResult.setText(mResult);
                break;

            case R.id.finalResult:
                UploadEC2 uploadEC2 = new UploadEC2();
                SpeechRecognitionResult speechRecognitionResult = (SpeechRecognitionResult) msg.obj;
                List<String> results = speechRecognitionResult.getResults();

                StringBuilder strBuf = new StringBuilder();
                strBuf.append(results.get(0));
                strBuf.append(".");

                mResult = strBuf.toString();
                uploadEC2.uploadToEC2(getActivity(), mResult);
                result = result + mResult;
                m_Adapter.add(mResult, 0);
                m_Adapter.notifyDataSetChanged();
                break;

            case R.id.recognitionError:
                if (writer != null) {
                    writer.close();
                }

                mResult = "Error code : " + msg.obj.toString();
                txtPartialResult.setText(mResult);
                btnStart.setText(R.string.str_start);
                btnStart.setEnabled(true);
                break;

            case R.id.clientInactive:
                if (writer != null) {
                    writer.close();
                }
                if (check == true) {
                    naverRecognizer.recognize();
                    break;
                } else
                    break;
        }
    }

    private void initUI(View view) {
        txtResult = (TextView) view.findViewById(R.id.txt_result);
        btnStart = (Button) view.findViewById(R.id.btn_start);
        txtPartialResult = (TextView) view.findViewById(R.id.txt_partialResult);
        m_ListView = (ListView) view.findViewById(R.id.listView1);
        m_ListView.setAdapter(m_Adapter);
    }

}
