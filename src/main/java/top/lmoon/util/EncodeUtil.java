package top.lmoon.util;

import java.net.URLEncoder;

/**
 * 过滤非法字符工具类
 * 
 */
public class EncodeUtil {

    //过滤大部分html字符
    public static String encode(String input) {
        if (input == null) {
            return input;
        }
        StringBuilder sb = new StringBuilder(input.length());
        for (int i = 0, c = input.length(); i < c; i++) {
            char ch = input.charAt(i);
            switch (ch) {
                case '&': sb.append("&amp;");
                    break;
                case '<': sb.append("&lt;");
                    break;
                case '>': sb.append("&gt;");
                    break;
                case '"': sb.append("&quot;");
                    break;
                case '\'': sb.append("&#39;");
                    break;
//                case '/': sb.append("/");
//                    break;
                default: sb.append(ch);
            }
        }
        return sb.toString();
    }

    //js端过滤
    public static String encodeForJS(String input) {
        if (input == null) {
            return input;
        }

        StringBuilder sb = new StringBuilder(input.length());

        for (int i = 0, c = input.length(); i < c; i++) {
            char ch = input.charAt(i);

            // do not encode alphanumeric characters and ',' '.' '_'
            if (ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z' ||
                    ch >= '0' && ch <= '9' ||
                    ch == ',' || ch == '.' || ch == '_') {
                sb.append(ch);
            } else {
                String temp = Integer.toHexString(ch);

                // encode up to 256 with \\xHH
                if (ch < 256) {
                    sb.append('\\').append('x');
                    if (temp.length() == 1) {
                        sb.append('0');
                    }
                    sb.append(temp.toLowerCase());

                // otherwise encode with \\uHHHH
                } else {
                    sb.append('\\').append('u');
                    for (int j = 0, d = 4 - temp.length(); j < d; j ++) {
                        sb.append('0');
                    }
                    sb.append(temp.toUpperCase());
                }
            }
        }

        return sb.toString();
    }

    /**
     * css非法字符过滤
     * http://www.w3.org/TR/CSS21/syndata.html#escaped-characters
    */
    public static String encodeForCSS(String input) {
        if (input == null) {
            return input;
        }

        StringBuilder sb = new StringBuilder(input.length());

        for (int i = 0, c = input.length(); i < c; i++) {
            char ch = input.charAt(i);

            // check for alphanumeric characters
            if (ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z' ||
                    ch >= '0' && ch <= '9') {
                sb.append(ch);
            } else {
                // return the hex and end in whitespace to terminate
                sb.append('\\').append(Integer.toHexString(ch)).append(' ');
            }
        }
        return sb.toString();
    }

    /**
     * URL参数编码 
     * http://en.wikipedia.org/wiki/Percent-encoding
     */ 
    public static String encodeURIComponent(String input) {
        return encodeURIComponent(input, "utf-8");
    }

    public static String encodeURIComponent(String input, String encoding) {
        if (input == null) {
            return input;
        }
        String result;
        try {
            result = URLEncoder.encode(input, encoding);
        } catch (Exception e) {
            result = "";
        }
        return result;
    }


}