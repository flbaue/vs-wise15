package de.hawhamburg.vs.wise15.superteam.client;

import com.squareup.okhttp.OkHttpClient;
import de.hawhamburg.vs.wise15.superteam.client.model.Game;
import de.hawhamburg.vs.wise15.superteam.client.ui.CreateForm;
import de.hawhamburg.vs.wise15.superteam.client.ui.LobbyForm;
import de.hawhamburg.vs.wise15.superteam.client.ui.SearchForm;
import de.hawhamburg.vs.wise15.superteam.client.ui.StartForm;

import javax.net.ssl.*;
import javax.swing.*;
import java.io.IOException;
import java.security.cert.CertificateException;

/**
 * Created by florian on 16.11.15.
 */
public class Client {

    private JFrame frame;
    private ServiceDirectory serviceDirectory;
    private OkHttpClient httpClient = getUnsafeOkHttpClient();


    public Client() {

        try {
            serviceDirectory = new ServiceDirectory(httpClient);
        } catch (IOException e) {
            errorGameServiceNotReachable(e.getMessage());
        }
    }


    public static void main(String[] args) {

        new Client().run();
    }


    private static OkHttpClient getUnsafeOkHttpClient() {

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


    private void errorGameServiceNotReachable(String message) {
        // TODO show error and allow user to retry
    }


    private void run() {

        frame = new JFrame("RESTopoly");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.getContentPane().add(new StartForm(this).getPanel());
        frame.setVisible(true);
    }


    public void openSearchForm() {

        frame.getContentPane().removeAll();
        frame.getContentPane().add(new SearchForm(this, serviceDirectory, httpClient).getPanel());
        frame.getContentPane().revalidate();
        frame.getContentPane().repaint();
    }


    public void openCreateForm() {

        frame.getContentPane().removeAll();
        frame.getContentPane().add(new CreateForm(this, serviceDirectory).getPanel());
        frame.getContentPane().revalidate();
        frame.getContentPane().repaint();
    }


    public void openStartForm() {

        frame.getContentPane().removeAll();
        frame.getContentPane().add(new StartForm(this).getPanel());
        frame.getContentPane().revalidate();
        frame.getContentPane().repaint();
    }


    public void openLobbyForm(Game game) {

        frame.getContentPane().removeAll();
        frame.getContentPane().add(new LobbyForm(this).getPanel());
        frame.getContentPane().revalidate();
        frame.getContentPane().repaint();
    }
}
