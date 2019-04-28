package com.jamesjohnson.vuemorph;

import android.content.Context;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.HashMap;

public class VueMorph extends LinearLayout {
    LinearLayout appLayout;
    WebView webView;
    AppCompatActivity context;

    NavigationView sideNavNavigationView;
    Context sideNavContext;
    HashMap<String, String> titleRoutes;

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
     *
     * @param app    JSON describing the layout
     * @param parent The parent ViewGroup to add any Views to
     */
    public void drawFullApp(JSONObject app, ViewGroup parent) {
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

            for (int i = 0; i < children.length(); i++) {
                drawFullApp(children.getJSONObject(i), (ViewGroup) newView);
            }
        } catch (JSONException e) {
        }
    }

    public void removeWidget(final Integer uid) {
        this.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                try {
                    View view = findViewById(uid);
                    ((ViewGroup) view.getParent()).removeView(view);
                } catch (NullPointerException exception) {
                }
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

    public void setSideNavNavigationView(Context context, final NavigationView listView) {
        sideNavContext = context;
        sideNavNavigationView = listView;

        sideNavNavigationView.setNavigationItemSelectedListener(
            new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem menuItem) {
                    // set item as selected to persist highlight
                    Log.d("menu item clicked", "" + menuItem.getTitle());
                    String title = String.valueOf(menuItem.getTitle());
                    String route = titleRoutes.get("Page Two");
                    Log.d("menu item clicked", "" + route);
                    // TODO close drawer when item is tapped
                    for (String i : titleRoutes.keySet()) {
                        System.out.println("key: " + i + " value: " + titleRoutes.get(i));
                    }

                    return true;
                }
            }
        );
    }

    public void setSideNavListItems(final JSONArray jsonItemsArray) {
        final Menu menu = sideNavNavigationView.getMenu();
        titleRoutes = new HashMap<>();

        runOnUIThread(new Runnable() {
            @Override
            public void run() {
                try {
                    menu.clear();

                    for (int i = 0; i < jsonItemsArray.length(); i++) {
                        JSONObject navItemObject = jsonItemsArray.getJSONObject(i);
                        String navItemTitle = navItemObject.getString("title");
                        String navItemRoute = navItemObject.getString("route");

                        menu.add(navItemTitle);
                        titleRoutes.put(navItemTitle, navItemRoute);
                    }
                } catch (JSONException e) {
                }
            }
        });
    }
}
