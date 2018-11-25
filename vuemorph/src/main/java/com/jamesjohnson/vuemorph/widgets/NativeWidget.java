package com.jamesjohnson.vuemorph.widgets;

import android.view.View;

import org.json.JSONObject;

/**
 * Created by jamesjohnson on 19/06/2018.
 */

public interface NativeWidget {
    public void setStyles(JSONObject styles);
    public void update(JSONObject description);
}
