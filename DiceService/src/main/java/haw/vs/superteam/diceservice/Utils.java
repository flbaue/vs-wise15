package haw.vs.superteam.diceservice;

import com.squareup.okhttp.OkHttpClient;

import javax.net.ssl.*;
import java.security.cert.CertificateException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

/**
 * Created by florian on 16.11.15.
 */
public class Utils {

    private static final String LOG_TEMPLATE = "{TIME} - {MESSAGE}";

    public static OkHttpClient getUnsafeOkHttpClient() {

        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {

                        }


                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {

                        }


                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {

                            return null;
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.setSslSocketFactory(sslSocketFactory);
            okHttpClient.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {

                    return true;
                }
            });

            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Logger getLogger(String name) {
        Logger logger = Logger.getLogger(name);
        Handler handler = new ConsoleHandler();
        handler.setFormatter(new Formatter() {
            @Override
            public String format(LogRecord record) {
                Date date = new Date(record.getMillis());
                String msg = record.getMessage();
                String className = record.getSourceClassName();
                String methodName = record.getSourceMethodName();

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

                return format.format(date) + " " + className + " " + methodName + msg;
            }
        });

        return logger;
    }
}
