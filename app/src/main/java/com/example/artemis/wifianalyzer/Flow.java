package com.example.artemis.wifianalyzer;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import androidx.appcompat.app.AppCompatActivity;

public class Flow extends AppCompatActivity {

    Timer T;
    String fireintheroom;
    static String string = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor);

        final long msec = 5000;

        T = new Timer();
        T.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        findIfFire();
                        //addSignalItems();
                    }
                });
            }
        }, 0, msec);
    }

    private void addSignalItems() {

        final ArrayList<Signal> signals = new ArrayList<>();

        // final ListView signalListView = (ListView) findViewById(R.id.list);



    }

    public void findIfFire() {

        Map<String, String> sendList1 = new HashMap<String, String>();
//        sendList1.put("IMEI", IMEI+"");
        sendList1.put("action", "CHECK_FIRE");



    }

    public void processFinish(Map<String, Object> output) {

        fireintheroom = output.get("status").toString();
//        Toast.makeText(this,"!!!"+ fireintheroom,Toast.LENGTH_LONG).show();
        int img=0, dbm, level;

        WifiManager wifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        // Level of a Scan Result
        List<ScanResult> wifiList = wifiManager.getScanResults();

        int min = 0;
        String string = "room1";
        for (ScanResult scanResult : wifiList) {
            //Toast.makeText(getApplicationContext(),"" + scanResult.SSID,Toast.LENGTH_LONG).show();
            if(scanResult.SSID.equalsIgnoreCase("room1") ||
                    scanResult.SSID.equalsIgnoreCase("room2") ||
                    scanResult.SSID.equalsIgnoreCase("room3") ||
                    scanResult.SSID.equalsIgnoreCase("room4")) {
                int level1 = WifiManager.calculateSignalLevel(scanResult.level, 15);
                if (min < level1) {
                    min = level1;
                    string = scanResult.SSID;


                }
            }

            // string = string + scanResult.SSID + "Level = " + level1;
        }
        ImageView i = (ImageView)findViewById(R.id.imgview);
        if(fireintheroom == null)
        {
            i.setImageResource(R.drawable.not);
        }else
            Toast.makeText(getApplicationContext(), string+"!!!"+ fireintheroom.toString(), Toast.LENGTH_SHORT).show();

            if(string.equalsIgnoreCase("room1"))
            {
                i.setImageResource(R.drawable.p11);
            }
            else if(string.equalsIgnoreCase("room2"))
            {
                i.setImageResource(R.drawable.p21);
            }
            else if(string.equalsIgnoreCase("room3"))
            {
                i.setImageResource(R.drawable.p31);
            }
            else if(string.equalsIgnoreCase("room4"))
            {
                i.setImageResource(R.drawable.p41);
            }


/*
        Intent i1 = new Intent(this,MonitorActivity.class);
        startActivity(i1);*/
    }
}
