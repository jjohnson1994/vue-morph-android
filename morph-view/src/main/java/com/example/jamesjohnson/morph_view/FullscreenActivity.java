package com.example.jamesjohnson.morph_view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jamesjohnson.morph_view.widgets.CheckBoxWidget;
import com.example.jamesjohnson.morph_view.widgets.EditTextWidget;
import com.example.jamesjohnson.morph_view.widgets.LinearLayoutWidget;
import com.example.jamesjohnson.morph_view.widgets.NativeWidget;
import com.example.jamesjohnson.morph_view.widgets.RadioButtonWidget;
import com.example.jamesjohnson.morph_view.widgets.ScrollViewWidget;
import com.example.jamesjohnson.morph_view.widgets.TextViewWidget;
import com.example.jamesjohnson.morph_view.widgets.ButtonWidget;
import com.jamesjohnson.vuemorph.VueMorph;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {
    LinearLayout appLayout;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);

        appLayout = findViewById(R.id.app);

        VueMorph vueMorph = new VueMorph(this);

        appLayout.addView(vueMorph);

        // Setup WebView, where JS app is run
//        webView = findViewById(R.id.webview);
//        webView = new WebView(this);
//        webView.setClickable(true);
//        webView.setWebContentsDebuggingEnabled(true);
//        webView.addJavascriptInterface(new WebAppInterface(this), "Android");
//
//        WebSettings webSettings = webView.getSettings();
//        webSettings.setJavaScriptEnabled(true);
//
//        webView.loadUrl("http://10.0.2.2:8080/#/");
//        webView.loadUrl("file:///android_asset/index.html");
    }

    /**
     * Draw the apps layout based on the JSON from the WebView.
     * @param app JSON describing the layout
     * @param parent The parent ViewGroup to add any Views to
     */
    public void drawFullApp(JSONObject app, ViewGroup parent) {
        Log.d("drawFullApp", app.toString());

        // Get the name of the tag
        String tag = null;

        try {
            tag = app.getString("tag");
        } catch (JSONException e) {
            Log.e("drawFullApp", "Could not get tag name" + e);
        }

        if (tag == null) {
            try {
                JSONArray children = app.getJSONArray("children");

                Log.d("Tag has x children", tag + " " + children.length());
                for (int i = 0; i < children.length(); i++) {
                    drawFullApp(children.getJSONObject(i), null);
                }
            } catch (JSONException e) {
                Log.e("drawFullApp", "Could not get children" + e);
            }

            return;
        }

        // Get the uid
        String uid = null;

        try {
            uid = app.getString("uid");
        } catch (JSONException e) {
            Log.e("drawFullApp", "Could not get uid" + e);
        }

        // Get Styles
        JSONObject styles = null;

        try {
            styles = app.getJSONObject("styles");
        } catch (JSONException e) {
            Log.e("Error getting styles", "Could not get styles on tag" + tag + " " + e);
        }

        // Get Text
        String text = null;

        try {
            text = app.getString("text");
        } catch (JSONException e) {
            Log.e("Error getting text", "error: " + e);
        }

        // Create a new View based on the tag name
        View newView;

        Log.d("got tag", "text: " + text);

        switch (tag.toUpperCase()) {
            case "LINEARLAYOUT":
                newView = new LinearLayoutWidget(this);
                break;
            case "SCROLLVIEW":
                newView = new ScrollViewWidget(this);
                break;
            case "TEXTVIEW":
                newView = new TextViewWidget(this);
                ((TextViewWidget) newView).setText(text);
                break;
            case "BTN":
                newView = new ButtonWidget(this, webView);
                ((ButtonWidget) newView).setText(text);
                break;
            case "EDITTEXT":
                newView = new EditTextWidget(this, webView);
                break;
            case "CHECKBOX":
                newView = new CheckBoxWidget(this, webView);
                ((CheckBoxWidget) newView).setText(text);
                break;
            case "RADIOBUTTON":
                newView = new RadioButtonWidget(this, webView);
                ((RadioButtonWidget) newView).setText(text);
                break;
            case "RADIOGROUP":
                newView = new RadioButtonWidget.RadioGroupWidget(this);
                break;
            default:
                newView = new TextViewWidget(this);
                ((TextView) newView).setText("No Matching Components for Tag");
        }

        // Set styles on View
        if (styles != null) {
            ((NativeWidget) newView).setStyles(styles);
        }

        // Set uid
        ((NativeWidget) newView).setUid(uid);
        newView.setId(Integer.parseInt(uid));

        // Add the new View to the correct ViewGroup
        if (parent != null) {
            addViewToViewGroup(newView, parent);
        } else {
            addViewToViewGroup(newView, appLayout);
        }

        // Call this method about with the Views children
        try {
            JSONArray children = app.getJSONArray("children");

            Log.d("Tag has x children", tag + " " + children.length());
            for (int i = 0; i < children.length(); i++) {
                drawFullApp(children.getJSONObject(i), (ViewGroup) newView);
            }
        } catch (JSONException e) {
            Log.e("drawFullApp", "Could not get children" + e);
        }
    }

    public void runOnUIThread(Runnable run) {
        runOnUiThread(run);
    }

    public void addViewToViewGroup(final View view, final ViewGroup viewGroup) {
        runOnUiThread(new Runnable() {
            public void run() {
                viewGroup.addView(view);
            }
        });
    }

    // Clear app layout
    public void clear() {
        runOnUiThread(new Runnable() {
            public void run() {
                if (appLayout.getChildCount() > 0) {
                    appLayout.removeAllViews();
                }
            }
        });
    }
}
