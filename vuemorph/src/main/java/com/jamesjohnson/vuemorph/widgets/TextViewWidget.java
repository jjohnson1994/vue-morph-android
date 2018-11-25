package com.jamesjohnson.vuemorph.widgets;

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

    public void update(JSONObject description) {
        try {
            String uid = description.getString("uid");
            this.setId(Integer.parseInt(uid));

            String text = description.getString("text");
            this.setText(text);

            JSONObject styles = description.getJSONObject("styles");
            this.setStyles(styles);
        } catch (JSONException e) { }
    }

    public void setText(String text) {
        super.setText(text);
    }

    public void setStyles(JSONObject styles) {

    }
}
