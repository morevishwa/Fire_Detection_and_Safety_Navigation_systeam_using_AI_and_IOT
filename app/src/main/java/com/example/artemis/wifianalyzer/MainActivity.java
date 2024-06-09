package com.example.artemis.wifianalyzer;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;

import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity  {
    public static int f = 0;
    Timer T;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DatabaseReference reff=
                FirebaseDatabase.getInstance("https://fire123-b63f3-default-rtdb.firebaseio.com").getReference("fire");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String fireintheroom = snapshot.getValue().toString();
                if(fireintheroom.equalsIgnoreCase("zero"))
                {

                }else {
                    showNotification(getApplicationContext(), "Fire Alert", "Follow Map", new Intent(getApplicationContext(), MonitorActivity.class), 98765);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    111
            );
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    111
            );
        }

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String IMEI = telephonyManager.getDeviceId();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Analyze your Wi-Fi properties and signal strength", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Switch mySwitch = (Switch) findViewById(R.id.switch1);

        WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifi.isWifiEnabled())
            mySwitch.setChecked(true);
        else
            mySwitch.setChecked(false);

        //attach a listener to check for changes in state
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                WifiManager wifi;

                if (isChecked) {
                    wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    wifi.setWifiEnabled(true);
                } else {
                    wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    wifi.setWifiEnabled(false);
                }

            }
        });

        //findWifiStrength();
        monitorWifi();
        Map<String, String> sendList1 = new HashMap<String, String>();
//        sendList1.put("IMEI", IMEI+"");
        sendList1.put("action", "CHECK_FIRE");




    }
    public void updateStrength() {

        final long time = 7000;


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

            final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

            builder.setTitle(R.string.action_settings);
            builder.setIcon(R.drawable.ic_info);
            builder.setMessage(R.string.dialog_text);
            builder.setCancelable(true);
            builder.setPositiveButton("Okay", null);

            AlertDialog alert = builder.create();
            alert.show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    public void monitorWifi() {
        //  Toast.makeText(getApplicationContext(),"-----------",Toast.LENGTH_LONG).show();
        TextView tv = (TextView) findViewById(R.id.wifiText);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION  }, 1);
        WifiManager wifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        // Level of a Scan Result

        List<ScanResult> wifiList = wifiManager.getScanResults();

        int min = 0;
        String string1 = "room1";
        Log.d("dnldjfnl","" );
        for (ScanResult scanResult : wifiList) {
          Log.d("dnldjfnl","" + scanResult.SSID);
            if(scanResult.SSID.equalsIgnoreCase("room1") ||
                    scanResult.SSID.equalsIgnoreCase("room2") ||
                    scanResult.SSID.equalsIgnoreCase("room3") ||
                    scanResult.SSID.equalsIgnoreCase("room4")) {
                int level1 = WifiManager.calculateSignalLevel(scanResult.level, 15);
                if (min < level1) {
                    min = level1;
                    string1 = scanResult.SSID;


                }
            }

            // string = string + scanResult.SSID + "Level = " + level1;
        }
        //Toast.makeText(getApplicationContext(),"" + level1,Toast.LENGTH_LONG).show();
        tv.setText(string1);
    }

    public void clearData(View vew) {
        Intent i = new Intent(this,MonitorActivity.class);
        startActivity(i);

    }
    public void map1(View vew) {
        Intent i = new Intent(this,Flow.class);
        startActivity(i);

    }
    public void call1(View vew) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Context context = this;
        intent.setData(Uri.parse("tel:9403931544"));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        context.startActivity(intent);
    }


    public void processFinish(Map<String, Object> output) {
        String output1 = output.get("status").toString();
        if(output1 != null || output1.length()>1)
        {
            NotificationManager notif = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            Notification n = new Notification.Builder(getApplicationContext()).setContentTitle("Fire Alert").setContentText("Go To MAP").setContentTitle("Fire Alert").setSmallIcon(R.drawable.icon_wifi).build();
            n.flags|=Notification.FLAG_AUTO_CANCEL;
            notif.notify(0,n);
        }
    }
    public void showNotification(Context context, String title, String message, Intent intent, int reqCode) {
        //SharedPreferenceManager sharedPreferenceManager = SharedPreferenceManager.getInstance(context);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, reqCode, intent, PendingIntent.FLAG_ONE_SHOT);
        String CHANNEL_ID = "channel_name";// The id of the channel.
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Channel Name";// The user-visible name of the channel.
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationManager.createNotificationChannel(mChannel);
        }
        notificationManager.notify(reqCode, notificationBuilder.build()); // 0 is the request code, it should be unique id

        Log.d("showNotification", "showNotification: " + reqCode);
    }
}