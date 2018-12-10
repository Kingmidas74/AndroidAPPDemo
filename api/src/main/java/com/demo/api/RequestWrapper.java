package com.demo.api;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


/**
 * Created by midas on 17.02.18.
 */

final class RequestWrapper {

    private String API_URL = "https://rest.api.demo.com";


    public Boolean IsSuccessRequest;

    private static void trustAllHosts()
    {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager()
        {
            public java.security.cert.X509Certificate[] getAcceptedIssuers()
            {
                return new java.security.cert.X509Certificate[] {};
            }

            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException
            {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException
            {
            }
        } };

        // Install the all-trusting trust manager
        try
        {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private String GetToken(String userCredentials)
    {
        String encode =  Base64.encode("PORTAL,YXgdLtAKGseCxs6oZnUfrLTg9pVtjWjh,"+userCredentials).replaceAll("(\\r|\\n)", "");
        return new StringBuilder(encode.substring(0,encode.length()-1)).reverse().append(encode.charAt(encode.length()-1)).toString();
    }

    public String MakeRequest(String url, RequestMethods method, List<RequestHeader> headers, String body)
            throws ProtocolException, IOException
    {
            URL urlObj = new URL(API_URL + '/' + url);
            IsSuccessRequest=false;
            InputStream inStream = null;
            String result = "";
            trustAllHosts();
            HttpsURLConnection urlConnection = (HttpsURLConnection)urlObj.openConnection();
            urlConnection.setRequestMethod(method.toString());
            if (headers == null) headers = new ArrayList<RequestHeader>();
            for (RequestHeader header : headers) {
                if(header.Key.equals("x-user-auth-key"))
                {
                    urlConnection.setRequestProperty("Authorization", "Bearer "+GetToken(header.Value));
                }
                else {
                    urlConnection.setRequestProperty(header.Key, header.Value);
                }
            }
            int startParamsIndex = url.indexOf("?")+1;
            if(startParamsIndex>0 && !method.equals(RequestMethods.GET))
            {
                urlConnection.setDoOutput(true);
                String paramsText = url.substring(startParamsIndex);
                String[] paramsArray = paramsText.split("&");
                StringBuilder tokenUri=new StringBuilder("");
                for(String parameter:paramsArray)
                {
                    String[] pair = parameter.split("=");
                    tokenUri.append("&").append(pair[0]).append("=").append(URLEncoder.encode(pair[1],"UTF-8"));
                }
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(urlConnection.getOutputStream());
                outputStreamWriter.write(tokenUri.toString().substring(1));
                outputStreamWriter.flush();
            }
            else if(!method.equals(RequestMethods.GET) && body!=null && body.length()>0)
            {
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(urlConnection.getOutputStream());
                outputStreamWriter.write(body);
                outputStreamWriter.flush();
            }
            urlConnection.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String s, SSLSession sslSession) {
                    return true;
                }
            });

            try {
                inStream = new BufferedInputStream(urlConnection.getInputStream());
                IsSuccessRequest = urlConnection.getResponseCode()>=200 && urlConnection.getResponseCode()<400;
                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
                StringBuilder sb = new StringBuilder();
                String line;
                while((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                reader.close();
                return sb.toString();
            }
            catch (Exception e)
            {
                e.printStackTrace();
                IsSuccessRequest=false;
            }
            finally {
                if (inStream != null) {
                    inStream.close();
                }
                urlConnection.disconnect();
            }
            return result;
    }
}
