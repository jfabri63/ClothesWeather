package volley;

import android.app.Application;

import com.android.volley.Request;
import com.android.volley.RequestQueue;

public class Volley extends Application {

    private RequestQueue mRequestQueue;
    private static Volley mInstance;
    public static final String TAG = "Requete";

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mRequestQueue = com.android.volley.toolbox.Volley.newRequestQueue(getApplicationContext());
    }

    public static synchronized Volley getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    public <T> void add(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

}