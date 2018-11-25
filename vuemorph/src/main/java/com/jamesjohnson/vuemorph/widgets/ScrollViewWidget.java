package com.jamesjohnson.vuemorph.widgets;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ScrollView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jamesjohnson on 21/06/2018.
 */

public class ScrollViewWidget extends ScrollView implements NativeWidget {

    public ScrollViewWidget(Context context) {
        super(context);
        super.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public void update(JSONObject description) {
        try {
            String uid = description.getString("uid");
            this.setId(Integer.parseInt(uid));

            JSONObject styles = description.getJSONObject("styles");
            this.setStyles(styles);
        } catch (JSONException e) { }
    }

    public void setStyles(JSONObject styles) {
        Log.d("LinearLayoutWidget", "SetStyles" + styles.length());
    }
}
