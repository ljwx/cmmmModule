package com.sisensing.common.base;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

class TrustAllHostnameVerifier implements HostnameVerifier {
    @Override
    public boolean verify(String hostname, SSLSession session) {
        return true;
    }
}
