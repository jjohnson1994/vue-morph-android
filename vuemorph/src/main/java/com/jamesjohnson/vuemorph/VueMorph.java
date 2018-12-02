package com.jamesjohnson.vuemorph;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jamesjohnson.vuemorph.widgets.CheckBoxWidget;
import com.jamesjohnson.vuemorph.widgets.EditTextWidget;
import com.jamesjohnson.vuemorph.widgets.LinearLayoutWidget;
import com.jamesjohnson.vuemorph.widgets.NativeWidget;
import com.jamesjohnson.vuemorph.widgets.RadioGroupWidget;
import com.jamesjohnson.vuemorph.widgets.ScrollViewWidget;
import com.jamesjohnson.vuemorph.widgets.TextViewWidget;
import com.jamesjohnson.vuemorph.widgets.ButtonWidget;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VueMorph extends LinearLayout {
    LinearLayout appLayout;
    WebView webView;
    AppCompatActivity context;

    public VueMorph(AppCompatActivity context) {
        super(context);

        this.context = context;

        // Setup WebView, where JS app is run
        webView = new WebView(context);
        webView.setClickable(true);
        webView.setWebContentsDebuggingEnabled(true);
        webView.addJavascriptInterface(new WebAppInterface(this, context), "Android");

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.loadUrl("http://10.0.2.2:8080/#/");
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

        // Create a new View based on the tag name
        View newView;

        switch (tag.toUpperCase()) {
            case "LINEARLAYOUT":
                newView = new LinearLayoutWidget(this.getContext());
                ((LinearLayoutWidget) newView).update(app);
                break;
            case "SCROLLVIEW":
                newView = new ScrollViewWidget(this.getContext());
                ((ScrollViewWidget) newView).update(app);
                break;
            case "TEXTVIEW":
                newView = new TextViewWidget(this.getContext());
                ((TextViewWidget) newView).update(app);
                break;
            case "BTN":
                newView = new ButtonWidget(this.getContext(), webView);
                ((ButtonWidget) newView).update(app);
                break;
            case "EDITTEXT":
                newView = new EditTextWidget(this, webView);
                ((EditTextWidget) newView).update(app);
                break;
            case "CHECKBOX":
                newView = new CheckBoxWidget(this.getContext(), webView);
                ((CheckBoxWidget) newView).update(app);
                break;
            case "RADIOGROUP":
                newView = new RadioGroupWidget(this, webView);
                ((RadioGroupWidget) newView).update(app);
                break;
            default:
                newView = new TextViewWidget(this.getContext());
                ((TextView) newView).setText("No Matching Components for Tag");
        }

        // Add the new View to the correct ViewGroup
        if (parent != null) {
            addViewToViewGroup(newView, parent);
        } else {
            addViewToViewGroup(newView, this);
        }

        // Call this method about with the Views children
        try {
            JSONArray children = app.getJSONArray("children");

            Log.d("Tag has x children", tag + " " + children.length());
            for (int i = 0; i < children.length(); i++) {
                drawFullApp(children.getJSONObject(i), (ViewGroup) newView);
            }
        } catch (JSONException e) { }
    }

    public void removeWidget(final Integer uid) {
        Log.d("RemoveWidget", "Remove Widget, " + uid);
            this.runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        View view = findViewById(uid);
                        ((ViewGroup) view.getParent()).removeView(view);
                    }
                    catch(NullPointerException exception) { }
                }
            });
    }

    public void runOnUIThread(Runnable run) {
        context.runOnUiThread(run);
    }

    public void addViewToViewGroup(final View view, final ViewGroup viewGroup) {
        runOnUIThread(new Runnable() {
            public void run() {
                viewGroup.addView(view);
            }
        });
    }
}
