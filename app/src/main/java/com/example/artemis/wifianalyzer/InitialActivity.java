package com.example.artemis.wifianalyzer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.READ_SMS;
public class InitialActivity extends Activity {

    private ArrayList<String> permissions = new ArrayList();

    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseDatabase auth;
    Button signOut;
	InitialActivity a;
    private final static int ALL_PERMISSIONS_RESULT = 101;
    private ArrayList permissionsToRequest;
    private ArrayList permissionsRejected = new ArrayList();
	static String pno="";final int REQUEST_CODE = 101;
	String imei;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_initial);
a=this;
		permissions.add(ACCESS_FINE_LOCATION);
		permissions.add(ACCESS_COARSE_LOCATION);
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);


		// in the below line, we are checking for permissions
		if (ActivityCompat.checkSelfPermission(this, READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
			// if permissions are not provided we are requesting for permissions.
			ActivityCompat.requestPermissions(this, new String[]{READ_PHONE_STATE}, REQUEST_CODE);
		}

		// in the below line, we are setting our imei to our text view.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			//pno = telephonyManager.getImei();
		}
		pno ="register";
		//get firebase auth instance

		//auth =  FirebaseDatabase.getInstance();


		Toast.makeText(getApplicationContext(),"pno"+pno,Toast.LENGTH_LONG).show();
		List<SubscriptionInfo> subscription = null;
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
			subscription = SubscriptionManager.from(getApplicationContext()).getActiveSubscriptionInfoList();
		}


		DatabaseReference	reff=
		FirebaseDatabase.getInstance("https://fire123-b63f3-default-rtdb.firebaseio.com").getReference("i");
		List<SubscriptionInfo> finalSubscription = subscription;
		reff.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot snapshot) {


				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
					for (int i = 0; i < finalSubscription.size(); i++) {
						SubscriptionInfo info = finalSubscription.get(i);
				String		TAG=info.getNumber();
						Toast.makeText(getApplicationContext(), "number " + info.getNumber(),Toast.LENGTH_LONG).show();


		long l1 = snapshot.getValue(Long.class);
						Toast.makeText(getApplicationContext(),  "country iso " + l1,Toast.LENGTH_LONG).show();
				if (l1==1) {
					startActivity(new Intent(a,MainActivity.class));
					break;

				} else {
					startActivity(new Intent(a,RegisterActivity.class));

				}

					}
				}
			}

			@Override
			public void onCancelled(@NonNull DatabaseError error) {
				// calling on cancelled method when we receive
				// any error or we are not able to get the data.
				Toast.makeText(a, "Fail to get data.", Toast.LENGTH_SHORT).show();
			}
		});
		
	}
	

	



	private void requestPermission() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			requestPermissions(new String[]{READ_SMS, READ_PHONE_STATE}, 100);
		}
	}
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);

		if (requestCode == REQUEST_CODE) {
			// in the below line, we are checking if permission is granted.
			if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				// if permissions are granted we are displaying below toast message.
				Toast.makeText(this, "Permission granted.", Toast.LENGTH_SHORT).show();
			} else {
				// in the below line, we are displaying toast message
				// if permissions are not granted.
				Toast.makeText(this, "Permission denied.", Toast.LENGTH_SHORT).show();
			}
		}
	}
}

