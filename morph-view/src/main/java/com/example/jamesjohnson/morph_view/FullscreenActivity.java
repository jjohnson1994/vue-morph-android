package com.example.jamesjohnson.morph_view;

import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.jamesjohnson.vuemorph.VueMorph;

public class FullscreenActivity extends AppCompatActivity {
    FrameLayout appLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the Apps layout to use the Vue Morph Layout (Mostly an empty LinearLayout)
        setContentView(com.jamesjohnson.vuemorph.R.layout.activity_fullscreen);

        // Create a new instance of Vue Morph
        VueMorph vueMorph = new VueMorph(this);
        vueMorph.setLayoutParams(
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                )
        );
        vueMorph.setSideNavNavigationView(this, (NavigationView) findViewById(R.id.nav_view));

        // Add Vue Morph to the Apps View
        appLayout = findViewById(R.id.content_frame);
        appLayout.addView(vueMorph);
    }
}
