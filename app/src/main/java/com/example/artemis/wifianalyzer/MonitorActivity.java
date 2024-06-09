package com.example.artemis.wifianalyzer;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;

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

import android.telephony.SmsManager;
import android.telephony.SubscriptionInfo;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MonitorActivity extends AppCompatActivity {

    Timer T;
    String fireintheroom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor);

        final long msec = 25000;

        findIfFire();
        //addSignalItems();

    }

    private void addSignalItems() {

        final ArrayList<Signal> signals = new ArrayList<>();

        // final ListView signalListView = (ListView) findViewById(R.id.list);



    }

    public void findIfFire() {

        DatabaseReference reff=
                FirebaseDatabase.getInstance("https://fire123-b63f3-default-rtdb.firebaseio.com").getReference("fire");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                fireintheroom = snapshot.getValue().toString();
//        Toast.makeText(this,"!!!"+ fireintheroom,Toast.LENGTH_LONG).show();
                int img=0, dbm, level;

                WifiManager wifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                // Level of a Scan Result
                List<ScanResult> wifiList = wifiManager.getScanResults();

                int min = 0;
                String string = "room1";
                for (ScanResult scanResult : wifiList) {
                    //  Toast.makeText(getApplicationContext(),"demo" + scanResult.SSID,Toast.LENGTH_LONG).show();
                    if(scanResult.SSID.equalsIgnoreCase("room1") ||
                            scanResult.SSID.equalsIgnoreCase("room2") ||
                            scanResult.SSID.equalsIgnoreCase("room3") ||
                            scanResult.SSID.equalsIgnoreCase("room4")) {
                        int level1 = WifiManager.calculateSignalLevel(scanResult.level, 15);
                        if (min < level1) {
                            min = level1;
                            string = scanResult.SSID;
                 //           Toast.makeText(this,string+"",Toast.LENGTH_LONG).show();

                        }
                    }

                    // string = string + scanResult.SSID + "Level = " + level1;
                }
                //Toast.makeText(this, fireintheroom+"@@"+string.toString(), Toast.LENGTH_SHORT).show();
                ImageView i = (ImageView)findViewById(R.id.imgview);
                if(fireintheroom.equalsIgnoreCase("zero"))
                {
                    i.setImageResource(R.drawable.not);
                }else{
                    showNotification(getApplicationContext(),"Fire Alert","Follow Map",new Intent(getApplicationContext(),MonitorActivity.class),98765);
                    if(flag == 1) {
                        flag = 2;

                    }





                }


                if(fireintheroom != null && fireintheroom.toString().equalsIgnoreCase("one") )
                {
                    if(string.equalsIgnoreCase("room1"))
                    {
                        i.setImageResource(R.drawable.o11);
                    }
                    else if(string.equalsIgnoreCase("room2"))
                    {
                        i.setImageResource(R.drawable.o21);
                    }
                    else if(string.equalsIgnoreCase("room3"))
                    {
                        i.setImageResource(R.drawable.o31);
                    }
                    else if(string.equalsIgnoreCase("room4"))
                    {
                        i.setImageResource(R.drawable.o41);
                    }
                }
                if(fireintheroom != null && fireintheroom.toString().equalsIgnoreCase("two") )
                {
                    if(string.equalsIgnoreCase("room1"))
                    {
                        i.setImageResource(R.drawable.o12);
                    }
                    else if(string.equalsIgnoreCase("room2"))
                    {
                        i.setImageResource(R.drawable.o22);
                    }
                    else if(string.equalsIgnoreCase("room3"))
                    {
                        i.setImageResource(R.drawable.o32);
                    }
                    else if(string.equalsIgnoreCase("room4"))
                    {
                        i.setImageResource(R.drawable.o42);
                    }
                }
                if(fireintheroom != null && fireintheroom.toString().equalsIgnoreCase("three") )
                {
                    if(string.equalsIgnoreCase("room1"))
                    {
                        i.setImageResource(R.drawable.o13);
                    }
                    else if(string.equalsIgnoreCase("room2"))
                    {
                        i.setImageResource(R.drawable.o23);
                    }
                    else if(string.equalsIgnoreCase("room3"))
                    {
                        i.setImageResource(R.drawable.o33);
                    }
                    else if(string.equalsIgnoreCase("room4"))
                    {
                        i.setImageResource(R.drawable.o43);
                    }
                }
                if(fireintheroom != null && fireintheroom.toString().equalsIgnoreCase("four") )
                {
                    if(string.equalsIgnoreCase("room1"))
                    {
                        i.setImageResource(R.drawable.o14);
                    }
                    else if(string.equalsIgnoreCase("room2"))
                    {
                        i.setImageResource(R.drawable.o24);
                    }
                    else if(string.equalsIgnoreCase("room3"))
                    {
                        i.setImageResource(R.drawable.o34);
                    }
                    else if(string.equalsIgnoreCase("room4"))
                    {
                        i.setImageResource(R.drawable.o44);
                    }
                }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // calling on cancelled method when we receive
                // any error or we are not able to get the data.
                //Toast.makeText(a, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });




    }
    public void sendSMS(String phoneNo, String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
            Toast.makeText(getApplicationContext(), "Message Sent",
                    Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(),ex.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }
static int flag =1;

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
