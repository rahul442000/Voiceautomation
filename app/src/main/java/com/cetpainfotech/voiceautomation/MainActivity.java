package com.cetpainfotech.voiceautomation;

import android.content.Intent;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.speech.RecognitionService;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    Button speak,share;
    TextView info;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        info = findViewById(R.id.info);
        speak =findViewById(R.id.speak);
        share=findViewById(R.id.share);

        speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //to convert voice into text
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                 //to call all languages in our phone
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                //to select the dafault language
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,Locale.getDefault());
                // to display the message to start speak
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Speak Now......");
                //to request the OS to convert speach into text
                startActivityForResult(intent,1);

            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // to share the content to social media apps
                Intent intent=new Intent(Intent.ACTION_SEND);
                //to attach the text info to share
                intent.putExtra(Intent.EXTRA_TEXT,info.getText().toString());
                //to define the info type
                intent.setType("text/plain");
                //to start the intent
                startActivity(Intent.createChooser(intent,"Share Via:"));
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // to check the process is your
        if ( requestCode == 1 &&  resultCode == RESULT_OK && data != null)
        {
            //to pass the message to arraylist object
            ArrayList <String> arrayList = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            //to check the user speak open camera
            if (arrayList.get(0).toString().equals("open camera"))
            {
                Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivity(intent);
            }
            else if (arrayList.get(0).toString().equals("open video recorder"))
            {
                Intent intent=new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                startActivity(intent);
            }
            else if (arrayList.get(0).toString().equals("open Wi-Fi"))
            {
                WifiManager wifiManager=(WifiManager)
                        getApplicationContext().getSystemService(WIFI_SERVICE);
                if (!wifiManager.isWifiEnabled())
                {
                    //to turn wifi on
                    wifiManager.setWifiEnabled(true);
                }
                else
                {
                    Toast.makeText(this, "Wifi is already on...", Toast.LENGTH_SHORT).show();
                }
            }
            //to display the msg to TextView
            info.setText(arrayList.get(0));
        }

    }
}
