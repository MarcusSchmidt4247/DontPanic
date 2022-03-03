// MS: 2/2/22 - Created default React Native file
// MS: 2/18/22 - Overrided onCreate() to interact with the database
// SC: 2/22/22 - Port-over of old code to new repo
// DB: 2/24/22 - Modified this file to be the splash screen of the app

package com.example.dontpanic;

import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView imageSplashScreen = (ImageView) findViewById(R.id.imageSplashScreen);
        imageSplashScreen.setImageResource(R.drawable.icon);

    }
}
