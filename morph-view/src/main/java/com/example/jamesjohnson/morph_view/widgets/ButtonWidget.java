package com.example.jamesjohnson.morph_view.widgets;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

import org.json.JSONObject;

/**
 * Created by jamesjohnson on 19/06/2018.
 */

public class ButtonWidget extends AppCompatButton implements NativeButtonWidget {

    private WebView webView;
    private String uid;

    public ButtonWidget(Context context, final WebView webView) {
        super(context);

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.loadUrl("javascript:dispatchOnClick('" + uid + "');");
            }
        });
    }

    public void update(JSONObject description) {

    }

    public void setUid(String _uid) {
        uid = _uid;
    }

    public void setText(String text) {
        Log.d("Set Text", text);
        super.setText(text);
    }

    public void setStyles(JSONObject styles) {
        Log.d("ButtonWidget", "SetStyles" + styles.length());
    }
}
