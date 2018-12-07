package com.example.ljb.jbapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.SurfaceTexture;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import org.videolan.libvlc.IVLCVout;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;

import java.util.ArrayList;


public class TabFragment2 extends Fragment implements IVLCVout.Callback {
    private LibVLC libvlc;
    private MediaPlayer mMediaPlayer = null;
    private String path = "";

    private int mVideoWidth;

    private EditText idText;
    private EditText pwText;
    private TextureView mTexture;
    private OnFragmentInteractionListener mListener;

    public TabFragment2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tab_fragment2, container, false);
        // Inflate the layout for this fragment

        mTexture = (TextureView) v.findViewById(R.id.hiddensurface);
        Button button = (Button)v.findViewById(R.id.recordStart);
        idText = (EditText) v.findViewById(R.id.idInput);
        pwText = (EditText) v.findViewById(R.id.passwordInput);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = idText.getText().toString();
                String pw = pwText.getText().toString();
                path = "rtsp://"+ id +":" + pw +"@118.222.51.157:7777/1";
                createPlayer(path);
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

    public interface OnFragmentInteractionListener  {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onSurfacesCreated(IVLCVout ivlcVout) {
        ;
    }

    @Override
    public void onSurfacesDestroyed(IVLCVout ivlcVout) {

    }

    //VLC 플레이어 실행
    private void createPlayer(String media) {
        releasePlayer();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        mVideoWidth = displayMetrics.widthPixels;

        // Create LibVLC
        ArrayList<String> options = new ArrayList<>();
        //options.add("--subsdec-encoding <encoding>");
        options.add("--aout=opensles");
        options.add("--audio-time-stretch"); // time stretching
        options.add("--rtsp-tcp");
        options.add("--file-caching=2000");
        options.add("-vvv");
        libvlc = new LibVLC(getActivity(), options);

        // Create media player
        mMediaPlayer = new MediaPlayer(libvlc);

        // Set up video output
        final IVLCVout vout = mMediaPlayer.getVLCVout();
        vout.setVideoView(mTexture);
        vout.setWindowSize(mVideoWidth, mVideoWidth*9/16);
        vout.addCallback(this);
        vout.attachViews();



        Media m = new Media(libvlc, Uri.parse(media));
        int cache = 500;

        m.addOption(":network-caching=" + cache);
        m.addOption(":file-caching=" + cache);
        m.addOption(":live-caching=" + cache);
        m.addOption(":sout-mux-caching=" + cache);
        m.addOption(":codec=mediacodec,iomx,all");
        m.addOption(":fullscreen");


        mTexture.setLayoutParams(new FrameLayout.LayoutParams(mVideoWidth,mVideoWidth*9/16));
        Toast.makeText(getContext(), mTexture.getWidth() +" "+ mTexture.getHeight(), Toast.LENGTH_LONG).show();

        mMediaPlayer.setMedia(m);
        mMediaPlayer.play();

    }

    //플레이어 종료
    private void releasePlayer() {
        if (libvlc == null)
            return;
        mMediaPlayer.stop();
        final IVLCVout vout = mMediaPlayer.getVLCVout();
        vout.removeCallback(this);
        vout.detachViews();
        libvlc.release();
        libvlc = null;

        mVideoWidth = 0;
    }


    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }
}
