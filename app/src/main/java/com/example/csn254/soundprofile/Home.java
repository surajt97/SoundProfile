package com.example.csn254.soundprofile;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.net.Uri;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

public class Home extends AppCompatActivity {

    ImageButton floatButton;
    ListView slot_list;
    Context ctx = this;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        display_slot_list();
        floatButton = (ImageButton) findViewById(R.id.addButton);
        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getBaseContext(), "Button Clicked", Toast.LENGTH_SHORT).show();
                launch_add_slot_activity();
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void launch_add_slot_activity() {
        Intent i = new Intent(this, add_slot.class);
        finish();
        startActivity(i);
    }

    public void display_slot_list() {
        int count = 0;
        time_slot_db DOP = new time_slot_db(ctx);
        final Cursor CR = DOP.getInformation(DOP);


        CR.moveToFirst();
        do {
            count++;
        } while (CR.moveToNext());


        try {
            String[] slot_day_list = new String[count];
            String[] slot_time_list = new String[count];
            int i = 0;
            CR.moveToFirst();
            do {
                slot_day_list[i] = CR.getString(0);
                slot_time_list[i] = CR.getString(1) + " - " + CR.getString(2);
                //Log.d("display",slot_day_list[i]+slot_time_list[i]);
                i++;
            } while (CR.moveToNext());


            ListAdapter adapter = new custom_slot_adapter(this, slot_day_list, slot_time_list);
            slot_list = (ListView) findViewById(R.id.slot_list);
            slot_list.setAdapter(adapter);

            slot_list.setOnItemLongClickListener(
                    new AdapterView.OnItemLongClickListener(){
                        @Override
                        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                            String day = String.valueOf(parent.getItemAtPosition(position));
                            String time_duration=String.valueOf(((TextView)parent.getChildAt(position).findViewById(R.id.time_duration)).getText().toString());
                            String times[] = time_duration.split("-");
                            time_slot_db DB = new time_slot_db(ctx);
                            DB.deleteSlot(DB, day, times[0].trim(), times[1].trim());
                            finish();
                            startActivity(getIntent());
                            //Toast.makeText(getBaseContext(),times[0]+" "+times[1]+"-"+id+"-"+position, Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    }
            );

        } catch (CursorIndexOutOfBoundsException e) {
            Toast.makeText(getBaseContext(), "Please add a slot FIRST", Toast.LENGTH_LONG).show();
        }

    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Home Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
