package com.jamesjohnson.vuemorph;

import android.util.Log;
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

    /** Instantiate the interface and set the context */
    WebAppInterface(VueMorph v) {
        vueMorph = v;
    }

    /** Show a toast from the web page */
    @JavascriptInterface
    public void showToast(String toast) {
        Toast.makeText(vueMorph.getContext(), toast, Toast.LENGTH_SHORT).show();
    }

    /** On Vue.created */
    @JavascriptInterface
    public void created(String app) {
        try {
            JSONObject json = (JSONObject) new JSONObject(app);
            Log.d("created", "json" + json.length());

            vueMorph.drawFullApp(json, null);
        } catch(JSONException e) {
            Log.e("created", "Could not create JSONObject" + e);
        }
    }

    /**
     * Called by a component when it updates
     * @param _description - Stringified JSON description of the component state
     */
    @JavascriptInterface
    public void onAppUpdate(String _description) {
        Log.d("onAppUpdate", _description);
        try {
            JSONObject description = (JSONObject) new JSONObject(_description);

            int widgetId = Integer.parseInt(description.getString("uid"));
            int parent = Integer.parseInt(description.getString("parent"));
            NativeWidget widget = mContext.findViewById(widgetId);

            if (widget != null) {
                widget.update(description);
            } else {
                mContext.drawFullApp(description, (ViewGroup) mContext.findViewById(parent));
            }
        } catch (JSONException e) {
            Log.d("WebAppInterface", "Could not get description: " + e);
        }
    }
}
