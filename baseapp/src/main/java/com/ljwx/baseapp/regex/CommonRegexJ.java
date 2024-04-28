package com.ljwx.baseapp.regex;

public class CommonRegexJ {

    public final static String URL_FULL = "^(http|https|ftp)://[a-zA-Z0-9-.]+\\.[a-zA-Z]{2,}(?:/[^\\s]*)?$";
    public final static String URL_FULL_HOST = "(?<=http://|\\.)[^.]*?\\.(com|cn|net|org|biz|info|cc|tv)";

}
