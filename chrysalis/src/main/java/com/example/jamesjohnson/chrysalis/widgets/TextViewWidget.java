package com.example.jamesjohnson.chrysalis.widgets;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jamesjohnson on 19/06/2018.
 */

public class TextViewWidget extends AppCompatTextView implements NativeTextWidget {

    public TextViewWidget(Context context) {
        super(context);
    }

    public void setUid(String uid) {

    }

    public void update(JSONObject description) {
        Log.d("Text Widget", "Update");
        try {
            this.setText(description.getString("text"));
        } catch (JSONException e) {
            Log.e("Text Widget", "Update JSON Error" + e);
        }
    }

    public void setText(String text) {
        super.setText(text);
    }

    public void setStyles(JSONObject styles) {

    }
}
