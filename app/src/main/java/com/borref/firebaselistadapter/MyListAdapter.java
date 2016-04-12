package com.borref.firebaselistadapter;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.borref.*;
import com.firebase.client.Firebase;

import java.util.List;

/**
 * Created by usuario on 11/04/2016.
 */
public class MyListAdapter extends BaseAdapter {
    private Context mContext;
    private List<MainActivity.Item> mList;
    private LayoutInflater mLayoutInflater = null;

    public MyListAdapter(Context context, List list) {
        mContext = context;
        mList = list;
        mLayoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View mView = convertView;

        if (mView == null) {
            mView = mLayoutInflater.inflate(R.layout.list_row,
                    null);
        }

        ImageButton deleteBtn = (ImageButton) mView.findViewById(R.id.delete_btn);
        TextView data = (TextView) mView.findViewById(R.id.textName);


        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Firebase myFirebaseRef = new Firebase(Path.DB_URL);
                myFirebaseRef.child(mList.get(position).getId()).removeValue();
                Snackbar.make(v, "Item deleted from Firebase", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        data.setText(mList.get(position).getName());

        return mView;
    }
}
