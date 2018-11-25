package com.jamesjohnson.vuemorph;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.jamesjohnson.vuemorph.widgets.NativeWidget;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jamesjohnson on 15/06/2018.
 */

public class WebAppInterface {
    VueMorph vueMorph;
    AppCompatActivity context;

    /** Instantiate the interface and set the context */
    WebAppInterface(VueMorph v, AppCompatActivity c) {
        vueMorph = v;
        context = c;
    }

    /** Show a toast from the web page */
    @JavascriptInterface
    public void showToast(String toast) {
        Toast.makeText(vueMorph.getContext(), toast, Toast.LENGTH_SHORT).show();
    }

    /**
     * Called by a component when it updates
     * @param _description - Stringified JSON description of the component state
     */
    @JavascriptInterface
    public void onComponentUpdated(String _description) {
        Log.d("onAppUpdate", _description);
        try {
            JSONObject description = new JSONObject(_description);

            int widgetId = description.getInt("uid");
            int parent = description.getInt("parent");
            NativeWidget widget = context.findViewById(widgetId);

            if (widget != null) {
                widget.update(description);
            } else {
                Log.d("New Widget", "description: " + _description);
                vueMorph.drawFullApp(description, (ViewGroup) context.findViewById(parent));
            }
        } catch (JSONException e) {
            Log.d("WebAppInterface", "Could not get description: " + e);
        }
    }


    @JavascriptInterface
    public void onComponentDestroyed(String _description) {
        Log.d("WebAppInterface", "destroy: " + _description);
        try {
            JSONObject description = new JSONObject(_description);
            Integer widgetId = description.getInt("uid");
            vueMorph.removeWidget(widgetId);
        } catch (JSONException e) {
            Log.d("WebAppInterface", "Could not get description: " + e);
        }
    }
}
