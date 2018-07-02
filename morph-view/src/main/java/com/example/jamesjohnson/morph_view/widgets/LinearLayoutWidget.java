package com.example.jamesjohnson.morph_view.widgets;

import android.content.Context;
import android.util.Log;
import android.widget.LinearLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by jamesjohnson on 19/06/2018.
 */

public class LinearLayoutWidget extends LinearLayout implements NativeWidget {

    public LinearLayoutWidget(Context context) {
        super(context);

        // Default Settings
        super.setOrientation(LinearLayout.VERTICAL);
    }

    public void setUid(String uid) {

    }

    public void update(JSONObject description) {

    }

    public void setStyles(JSONObject styles) {
        Iterator<?> keys = styles.keys();

        while( keys.hasNext() ) {
            String key = (String) keys.next();
            String value = null;

            try {
                value = (String) styles.getString(key);
            } catch (JSONException e) {
                Log.e("LinearLayoutWidget", "Could not get style value");
            }

            switch (key) {
                case "orientation":
                    Log.d("Setting Style", key + " : " + value);
                    if (value == "horizontal") {
                        this.setOrientation(LinearLayout.HORIZONTAL);
                    }
                    break;
            }
        }
        Log.d("LinearLayoutWidget", "SetStyles" + styles.length());
    }
}
