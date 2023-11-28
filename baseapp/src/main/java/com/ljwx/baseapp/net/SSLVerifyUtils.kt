package com.ljwx.baseapp.net

import com.ljwx.baseapp.net.ssl.TrustAllCerts
import com.ljwx.baseapp.net.ssl.TrustAllHostnameVerifier
import java.security.SecureRandom
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509TrustManager

object SSLVerifyUtils {

    fun getSSLSocketFactory(): SSLSocketFactory? {
        var ssfFactory: SSLSocketFactory? = null
        try {
            val sc = SSLContext.getInstance("TLS");
            sc.init(null, arrayOf(TrustAllCerts()), SecureRandom());
            ssfFactory = sc.socketFactory;
        } catch (e: Exception) {
        }
        return ssfFactory
    }

    fun getX509TrustManager(): X509TrustManager {
        return TrustAllCerts()
    }

    fun getHostnameVerifier(): HostnameVerifier {
        return TrustAllHostnameVerifier()
    }

}