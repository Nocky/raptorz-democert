package com.we8log.democert;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by hying on 14-1-21.
 */
public class AsyncDemo extends AsyncTask<Object, Integer, String> {

    private static final String TAG = "AsyncDemo";
    private static int BUFFSIZE = 8192;

    public interface AsyncDemoListener {
        public void onSuccess(String result);
        public void onErrorOrCancel();
    }

    private AsyncDemoListener mListener;

    private static String parseString(HttpEntity entity) {
        try {
            ByteArrayOutputStream ostr = new ByteArrayOutputStream();
            InputStream istr = entity.getContent();
            byte[] buf = new byte[BUFFSIZE];
            int count = -1;
            while ((count = istr.read(buf, 0, BUFFSIZE)) != -1) {
                ostr.write(buf, 0, count);
            }
            buf = null;
            return new String(ostr.toByteArray());
        }
        catch (Throwable e) {
            e.printStackTrace();
            Log.e(TAG,e.getMessage());
            return "";
        }
    }

    public static String doDemo(int n, String url, Context context) {
        HttpUriRequest request = null;
        try {
            request = new HttpGet(url);
        }
        catch (Throwable e) {
            Log.e(TAG, e.getMessage());
        }
        HttpClient client;
        switch (n) {
            case 1:
                client = DemoHttp.getClient();
                break;
            case 2:
                client = DemoHttp.getHttpsClient();
                break;
            case 3:
                client = DemoHttp.getTrustAllClient();
                break;
            case 4:
                client = DemoHttp.getCustomClient(context);
                break;
            default:
                return "Invalid selection";
        }
        try {
            HttpResponse httpResponse = client.execute(request);
            int responseCode = httpResponse.getStatusLine().getStatusCode();
            String message = httpResponse.getStatusLine().getReasonPhrase();
            HttpEntity entity = httpResponse.getEntity();
            if (responseCode == 200 && entity != null)
            {
                return parseString(entity);
            }
            else {
                return String.format("Http error!\n %d: %s",
                        responseCode, message);
            }
        }
        catch (ClientProtocolException e) {
            return e.getMessage();
        }
        catch (Throwable e) {
            e.printStackTrace();
            return e.getMessage();
        }
        finally {
            client.getConnectionManager().shutdown();
        }
    }

    public AsyncDemo(AsyncDemoListener listener) {
        this.mListener = listener;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        mListener.onSuccess(s);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        mListener.onErrorOrCancel();
    }

    @Override
    protected String doInBackground(Object... objects) {
        int n = (Integer)objects[0];
        String url = (String)objects[1];
        Context context = (Context)objects[2];
        return doDemo(n, url, context);
    }
}
