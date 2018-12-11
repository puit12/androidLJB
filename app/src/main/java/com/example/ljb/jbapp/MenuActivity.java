package com.example.ljb.jbapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.example.ljb.jbapp.ChatListFrag.ChatListAdapter;
import com.example.ljb.jbapp.ChatListFrag.Listitem;

import java.util.ArrayList;


public class MenuActivity extends AppCompatActivity {
    private ListView lv;
    private ChatListAdapter mAdatper;
    private ArrayList<Listitem> list;
    private FloatingActionButton floatingButton;
    String roomName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_activity);

        list = new ArrayList<Listitem>();
        lv = (ListView) findViewById(R.id.lv);
        mAdatper = new ChatListAdapter(list);
        lv.setAdapter(mAdatper);

        floatingButton = (FloatingActionButton) findViewById(R.id.floatingButton);
        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(MenuActivity.this);

                alert.setTitle("생성");
                alert.setView(R.layout.editbox);

                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Dialog dia = (Dialog) dialog;
                        EditText editText = (EditText) dia.findViewById(R.id.addboxdialog);
                        String value = editText.getText().toString();
                        roomName = value;
                        list.add(new Listitem(R.drawable.arrow, roomName));
                        mAdatper.notifyDataSetChanged();
                    }
                });


                alert.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        });
                alert.show();


            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), TabAcitivity.class);
                startActivity(intent);
            }
        });

    }

}
