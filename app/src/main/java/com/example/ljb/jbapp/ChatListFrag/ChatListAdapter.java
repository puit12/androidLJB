package com.example.ljb.jbapp.ChatListFrag;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ljb.jbapp.R;

import java.util.ArrayList;


public class ChatListAdapter extends BaseAdapter {
    private ArrayList<Listitem> list;

    public ChatListAdapter(ArrayList<Listitem> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Listitem getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        View v = convertView;
        final Context context = parent.getContext();

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.listitem, parent, false);

            ImageView profile = (ImageView) v.findViewById(R.id.profile_image);
            TextView name = (TextView) v.findViewById(R.id.name);

            profile.setImageResource(getItem(pos).getProfile());
            name.setText(getItem(pos).getName());
        }
        return v;
    }
}
