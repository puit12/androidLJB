package com.example.ljb.jbapp.ChatTabFrag;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ljb.jbapp.R;

import org.videolan.libvlc.IVLCVout;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;

import java.util.ArrayList;


public class TabFragment2 extends Fragment implements IVLCVout.Callback {
    private LibVLC libvlc;
    private MediaPlayer mMediaPlayer = null;
    private String path = "";
    private Button button;
    private int mVideoWidth;

    private EditText idText;
    private EditText pwText;
    private TextureView mTexture;
    private OnFragmentInteractionListener mListener;

    public TabFragment2() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment2, container, false);
        initUI(view);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = idText.getText().toString();
                String pw = pwText.getText().toString();
                path = "rtsp://" + id + ":" + pw + "@118.222.51.157:7777/1";
                createPlayer(path);
                idText.setText(null);
                pwText.setText(null);
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
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onSurfacesCreated(IVLCVout ivlcVout) {
        ;
    }

    @Override
    public void onSurfacesDestroyed(IVLCVout ivlcVout) {

    }

    //VLC PLAYER EXECUTE
    private void createPlayer(String media) {
        releasePlayer();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        mVideoWidth = displayMetrics.widthPixels;

        // Create LibVLC
        ArrayList<String> options = new ArrayList<>();
        options.add("--aout=opensles");
        options.add("--audio-time-stretch");
        options.add("--rtsp-tcp");
        options.add("--file-caching=2000");
        options.add("-vvv");
        libvlc = new LibVLC(getActivity(), options);

        mMediaPlayer = new MediaPlayer(libvlc);

        final IVLCVout vout = mMediaPlayer.getVLCVout();
        vout.setVideoView(mTexture);
        vout.setWindowSize(mVideoWidth, mVideoWidth * 9 / 16);
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


        mTexture.setLayoutParams(new FrameLayout.LayoutParams(mVideoWidth, mVideoWidth * 9 / 16));
        Toast.makeText(getContext(), mTexture.getWidth() + " " + mTexture.getHeight(), Toast.LENGTH_LONG).show();

        mMediaPlayer.setMedia(m);
        mMediaPlayer.play();
    }

    // PLAYER EXIT
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

    private void initUI(View view) {
        mTexture = (TextureView) view.findViewById(R.id.hiddensurface);
        button = (Button) view.findViewById(R.id.recordStart);
        idText = (EditText) view.findViewById(R.id.idInput);
        pwText = (EditText) view.findViewById(R.id.passwordInput);
    }
}
