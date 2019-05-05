package com.example.akhi.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.CallLog;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_CALL_LOG)) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_CALL_LOG}, 1);

            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_CALL_LOG}, 1);
            }
        } else {
            //do stuff
            TextView textView=(TextView)findViewById(R.id.textView);
            textView.setText(getcCallDetails());
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED) {

                            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                        //do stuff
                        TextView textView=(TextView)findViewById(R.id.textView);
                        textView.setText(getcCallDetails());
                        }
                    } else {
                        Toast.makeText(this, " No Permission Granted", Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
        }
    }

    private String getcCallDetails() {
        StringBuffer sb = new StringBuffer();
        Cursor managedCursor= managedQuery(CallLog.Calls.CONTENT_URI,null,null,null,null);
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        sb.append("Call Details:\n\n");
        while (managedCursor.moveToNext()) {
            String phonenumber = managedCursor.getString(number);
            String calltype = managedCursor.getString(type);
            String calldate = managedCursor.getString(date);
            Date callDayTime = new Date(Long.valueOf(calldate));
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy PH:num");
            String dateString = formatter.format(callDayTime);
            String callDuration = managedCursor.getString(duration);
            String dir = null;
            int dirCode = Integer.parseInt(calltype);
            switch (dirCode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = "Outgoing";
                    break;
                case CallLog.Calls.INCOMING_TYPE:
                    dir = "Incoming";
                    break;
                case CallLog.Calls.MISSED_TYPE:
                    dir = "Missed";
                    break;
            }
            sb.append("\nPhone number" + phonenumber + "\nCallType:" + dir + "\nCall Date:" + dateString + "\nCall Duration:" + callDuration);

            sb.append("--------------");
        }
        managedCursor.close();
        return sb.toString();
    }
}
