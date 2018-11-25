package com.jamesjohnson.vuemorph.widgets;

import android.content.Context;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.jamesjohnson.vuemorph.VueMorph;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.Iterator;

/**
 * Created by jamesjohnson on 19/06/2018.
 */

public class RadioGroupWidget extends RadioGroup implements NativeInputWidget {

    private WebView webView;
    private String uid;
    private VueMorph vueMorph;

    private JSONArray options = null;
    private CharSequence selectedValue = null;

    public RadioGroupWidget(VueMorph vueMorph, final WebView webView) {
        super(vueMorph.getContext());
        this.vueMorph = vueMorph;

        Log.d("RadioGroup", "New RadioGroup Widget");

        this.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == -1) {
                    return;
                }

                RadioButton selectedRadioButton = findViewById(checkedId);
                selectedValue = selectedRadioButton.getText();
                webView.loadUrl("javascript:dispatchOnChange('" + uid + ", " + selectedRadioButton.getText() + "');");
            }
        });
    }

    public void update(JSONObject description) {
        try {
            String value = description.getString("value");

            String uid = description.getString("uid");
            this.setId(Integer.parseInt(uid));
            this.uid = uid;

            JSONArray options = description.getJSONArray("options");
            this.removeAllOptions();
            this.setOptions(options, value);

            JSONObject styles = description.getJSONObject("styles");
            this.setStyles(styles);
        } catch (JSONException error) { }
    }

    public void removeAllOptions() {
        final ViewGroup radioGroup = this;

        this.vueMorph.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                radioGroup.removeAllViews();
            }
        });
    }

    public void setOptions(JSONArray options, String selectedValue) {
        try {
            for (int i = 0; i < options.length(); i++) {
                String option = options.getString(i);

                final RadioButton radioButton = new RadioButton(this.getContext());
                radioButton.setText(option);

                if (option.equals(selectedValue)) {
                    radioButton.setChecked(true);
                }

                final ViewGroup radioGroup = this;

                this.vueMorph.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        radioGroup.addView(radioButton);
                    }
                });
            }

            this.options = options;
        } catch (JSONException error) {
            Log.e("RadioGroupWidget", "update(), " + error);
        }
    }

    public void setText(String text) {}

    public void setStyles(JSONObject styles) {
        Log.d("RadioButton", "SetStyles" + styles.length());
    }
}
