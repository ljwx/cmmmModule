package com.ljwx.baseapp.net.ssl

import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSession

class TrustAllHostnameVerifier : HostnameVerifier {
    override fun verify(hostname: String?, session: SSLSession?): Boolean {
        return true
    }
}