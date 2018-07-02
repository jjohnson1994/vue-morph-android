package com.example.jamesjohnson.morph_view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import com.jamesjohnson.vuemorph.VueMorph;

public class FullscreenActivity extends AppCompatActivity {
    LinearLayout appLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        // Create a new instance of Vue Morph
        VueMorph vueMorph = new VueMorph(this);

        // Add Vue Morph to the Apps View
        appLayout = findViewById(R.id.app);
        appLayout.addView(vueMorph);
    }
}
