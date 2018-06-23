package com.example.jamesjohnson.chrysalis.widgets;

import org.json.JSONObject;

/**
 * Created by jamesjohnson on 19/06/2018.
 */

public interface NativeWidget {
    public void setUid(String uid);
    public void setStyles(JSONObject styles);
    public void update(JSONObject description);
}
