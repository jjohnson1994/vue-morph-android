package com.example.jamesjohnson.morph_view.widgets;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.webkit.WebView;

import com.example.jamesjohnson.morph_view.FullscreenActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by jamesjohnson on 19/06/2018.
 */

public class EditTextWidget extends AppCompatEditText implements NativeInputWidget {

    private WebView webView;
    private String uid;

    public EditTextWidget(Context context, final WebView webView) {
        super(context);

        this.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                webView.loadUrl("javascript:dispatchOnChange('" + uid + ", " + EditTextWidget.this.getText().toString() + "');");
            }
        });
    }

    public void update(JSONObject description) {

    }

    public void setUid(String _uid) {
        uid = _uid;
    }

    public void setText(String value) {
        Log.d("Set Value", value);
        super.setText(value);
    }

    public void setStyles(JSONObject styles) {
        Log.d("EditTextWidget", "SetStyles" + styles.toString());

        Iterator<?> keys = styles.keys();

        while( keys.hasNext() ) {
            String key = (String) keys.next();
            String value = null;

            try {
                value = styles.getString(key);
            } catch (JSONException e) {
                Log.e("EditTextWidget", "Could not get style value");
            }

            switch (key) {
                case "inputType":
                    Log.d("Edit Text Setting Style", key + " : " + value);
                    final AppCompatEditText componentReference = this;
                    if (value.equals("number")) {
                        // Set input type to number
                        ((FullscreenActivity) getContext()).runOnUIThread(
                            new Runnable() {
                                public void run() {
                                    componentReference.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                                }
                            }
                        );
                    } else if(value.equals("email")) {
                        // Set input type to email
                        ((FullscreenActivity) getContext()).runOnUIThread(
                                new Runnable() {
                                    public void run() {
                                        componentReference.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                                    }
                                }
                        );
                    } else if(value.equals("password")) {
                        // Set input type to password
                        ((FullscreenActivity) getContext()).runOnUIThread(
                                new Runnable() {
                                    public void run() {
                                        componentReference.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                                    }
                                }
                        );
                    }
                    break;
            }
        }
    }
}
