package com.we8log.democert;

import android.content.Context;
import android.util.Log;

import org.apache.http.conn.ssl.BrowserCompatHostnameVerifier;
import org.apache.http.conn.ssl.SSLSocketFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * Created by hying on 14-1-21.
 */
public class SSLCustomSocketFactory extends SSLSocketFactory {
    private static final String TAG = "SSLCustomSocketFactory";

    private static final String KEY_PASS = "axbycz123";

    public SSLCustomSocketFactory(KeyStore trustStore) throws Throwable {
        super(trustStore);
    }

    public static SSLSocketFactory getSocketFactory(Context context) {
        try {
            InputStream ins = context.getResources().openRawResource(R.raw.xiaomagec);

            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            try {
                trustStore.load(ins, KEY_PASS.toCharArray());
            }
            finally {
                ins.close();
            }
            SSLSocketFactory factory = new SSLCustomSocketFactory(trustStore);
            return factory;
        } catch (Throwable e) {
            Log.d(TAG, e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
