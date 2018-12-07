package com.example.ljb.jbapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ljb.jbapp.utils.AudioWriterPCM;
import com.naver.speech.clientapi.SpeechRecognitionResult;

import org.videolan.libvlc.IVLCVout;

import java.lang.ref.WeakReference;
import java.util.List;



public class TabFragment1 extends Fragment {

    int count = 0;

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String CLIENT_ID = "clientid";
    // 1. "내 애플리케이션"에서 Client ID를 확인해서 이곳에 적어주세요.
    // 2. build.gradle (Module:app)에서 패키지명을 실제 개발자센터 애플리케이션 설정의 '안드로이드 앱 패키지 이름'으로 바꿔 주세요

    private RecognitionHandler handler;
    private NaverRecognizer naverRecognizer;

    private TextView txtResult;
    private TextView txtPartialResult;
    private Button btnStart;
    private String mResult;
    private CustomAdapter m_Adapter;
    private ListView m_ListView;

    private AudioWriterPCM writer;

    private OnFragmentInteractionListener mListener;

    public TabFragment1() {
        // Required empty public constructor
    }


    @Override
    public void onStart() {
        super.onStart();
        // NOTE : initialize() must be called on start time.
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
        // NOTE : release() must be called on stop time.
        naverRecognizer.getSpeechRecognizer().release();
    }

    // Declare handler for handling SpeechRecognizer thread's Messages.
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tab_fragment1, container, false);

        handler = new RecognitionHandler(this);

        naverRecognizer = new NaverRecognizer(getActivity(), handler, CLIENT_ID);

        txtResult = (TextView) v.findViewById(R.id.txt_result);
        btnStart = (Button) v.findViewById(R.id.btn_start);
        txtPartialResult = (TextView) v.findViewById(R.id.txt_partialResult);

        m_Adapter = new CustomAdapter();

        // Xml에서 추가한 ListView 연결
        m_ListView = (ListView) v.findViewById(R.id.listView1);

        // ListView에 어댑터 연결
        m_ListView.setAdapter(m_Adapter);

        btnStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                    if (!naverRecognizer.getSpeechRecognizer().isRunning()) {
                        mResult = "";
                        txtPartialResult.setText("Connecting...");
                        btnStart.setText(R.string.str_stop);
                        naverRecognizer.recognize();
                    }
            }
        });
        return v;
    }



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


    // Handle speech recognition Messages.
    private void handleMessage(Message msg) {
        switch (msg.what) {
            case R.id.clientReady:
                // Now an user can speak.
                txtPartialResult.setText("Connected");
                writer = new AudioWriterPCM(
                        Environment.getExternalStorageDirectory().getAbsolutePath() + "/NaverSpeechTest");
                writer.open("Test");
                break;

            case R.id.audioRecording:
                writer.write((short[]) msg.obj);
                break;

            case R.id.partialResult:
                // Extract obj property typed with String.
                mResult = (String) (msg.obj);
                txtPartialResult.setText(mResult);
                break;

            case R.id.finalResult:
                // Extract obj property typed with String array.
                // The first element is recognition result for speech.
                UploadEC2 uploadEC2 = new UploadEC2();
                SpeechRecognitionResult speechRecognitionResult = (SpeechRecognitionResult) msg.obj;
                List<String> results = speechRecognitionResult.getResults();
                StringBuilder strBuf = new StringBuilder();
                strBuf.append(results.get(0));
                strBuf.append(".");

                mResult = strBuf.toString();
                uploadEC2.uploadToEC2(getActivity(),mResult);
                txtResult.append(mResult);
                m_Adapter.add(mResult,0);
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

                btnStart.setEnabled(true);
                break;
        }
    }
}
