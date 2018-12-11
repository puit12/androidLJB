package com.example.ljb.jbapp.Service;

import android.graphics.Bitmap;
import android.os.Environment;
import android.view.View;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class SaveService {
    String folderName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/TestApp/";
    ;
    int saveCount = 0;

    public void screenshot(View view) throws Exception {
        view.setDrawingCacheEnabled(true);
        Bitmap screenshot = view.getDrawingCache();
        String filename = "screenshot" + saveCount + ".png";

        try {
            File f = new File(filename);
            OutputStream outStream = new FileOutputStream(folderName + f);
            screenshot.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        view.setDrawingCacheEnabled(false);
    }

    public void original(String contents) {
        try {
            File dir = new File(folderName);
            String filename = "original" + saveCount + ".txt";
            if (!dir.exists()) {
                dir.mkdir();
            }

            FileOutputStream fos = new FileOutputStream(folderName + filename, true);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));
            writer.write(contents);
            writer.flush();

            writer.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
