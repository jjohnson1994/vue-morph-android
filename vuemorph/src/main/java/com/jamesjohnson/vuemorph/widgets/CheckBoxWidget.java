package com.jamesjohnson.vuemorph.widgets;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.Log;
import android.webkit.WebView;
import android.widget.CompoundButton;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jamesjohnson on 19/06/2018.
 */

public class CheckBoxWidget extends AppCompatCheckBox implements NativeInputWidget {

    private WebView webView;
    private String uid;

    public CheckBoxWidget(Context context, final WebView webView) {
        super(context);

        this.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                webView.loadUrl("javascript:dispatchOnChange('" + uid + ", " + isChecked + "');");
            }
        });
    }

    public void update(JSONObject description) {
        try {

            Log.d("Checkbox", "Got description " + description.toString());
            String text = description.getString("text");
            String _checked  = description.getString("checked");
            Boolean checked = Boolean.parseBoolean(_checked);

            Log.d("Checkbox", "Got new text " + text);
            this.setText(text);
            this.setChecked(checked);
        } catch (JSONException e) {
            Log.d("Checkbox", "Error Reading Description " + e);
        }
    }

    public void setUid(String _uid) {
        uid = _uid;
    }

    public void setText(String text) {
        Log.d("Set Text", text);
        super.setText(text);
    }

    public void setStyles(JSONObject styles) {
        Log.d("CheckBoxWidget", "SetStyles" + styles.length());
    }
}
