package com.sisensing.common.algorithom;

import com.blankj.utilcode.util.ObjectUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * ProjectName: CGM_C
 * Package: com.sisensing.common.algorithom
 * Author: f.deng
 * CreateDate: 2021/8/17 18:00
 * Description:
 */
public class AlgorithmFactory {

    private static final Map<String, IAlgorithm> alMap = new HashMap<>();

    private static final String VERSION_110 = "ALGORITHM VER 1.0 (2021_05_24C)";
    private static final String VERSION_111 = "ALGORITHM V1.1.1 (2021_10_20)";
    private static final String VERSION_112 = "ALGORITHM V1.1.2 (2022_06_25)";

    public static synchronized IAlgorithm createAlgorithm(String version) {

        IAlgorithm algorithm;

        if (ObjectUtils.isNotEmpty(version)) {
            if (alMap.containsKey(version)) {
                return alMap.get(version);
            } else {
                if (VERSION_110.equals(version)) {
                    algorithm = new Algorithm_V110();
                } else if (VERSION_111.equals(version)) {
                    algorithm = new Algorithm_V111();
                } else if (VERSION_112.equals(version)) {
                    algorithm = new Algorithm_V112();
                } else {
                    return null;
                }
            }
            alMap.put(version, algorithm);
        } else {
            algorithm = new Algorithm_V112();
            alMap.put(VERSION_112, algorithm);
        }

        return algorithm;

    }

}
