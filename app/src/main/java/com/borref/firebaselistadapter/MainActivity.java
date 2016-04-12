package com.borref.firebaselistadapter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    Firebase myFirebaseRef;
    private List<Item> currentItems = new ArrayList<>();
    private ListView mListView;
    private TextView currentDBUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Firebase.setAndroidContext(this);
        mListView = (ListView) findViewById(R.id.listView);
        currentDBUrl = (TextView) findViewById(R.id.currentDB);

        setFirebaseValueListener();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pushValue();
                Snackbar.make(view, "New item added to Firebase", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void setFirebaseValueListener() {
        currentDBUrl.setText(Path.DB_URL);
        myFirebaseRef = new Firebase(Path.DB_URL);

        myFirebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentItems.clear();
                for (DataSnapshot itemSnapshot: dataSnapshot.getChildren()) {
                    Item item = new Item(itemSnapshot.getKey(), itemSnapshot.child("name").getValue(String.class));
                    currentItems.add(item);
                }

                mListView.setAdapter(new MyListAdapter(
                        getApplicationContext(), currentItems));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void pushValue() {
        Map<String, Object> item = new HashMap<String, Object>();
        item.put("name", "item "+ (currentItems.size() + 1));
        myFirebaseRef.push().setValue(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivityForResult(i, 22);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 22) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            String urlDB = sharedPreferences.getString("urlDB", null);
            Path.DB_URL = urlDB;
            setFirebaseValueListener();
            Toast.makeText(this, "URL DB "+Path.DB_URL, Toast.LENGTH_LONG).show();
        }
    }

    public class Item {
        private String id;
        private String name;

        public Item (String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }
}
