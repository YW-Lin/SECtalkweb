package com.wchat.utils;

import org.springframework.util.StringUtils;

public class XssUtils {

    public static String XSSHtmlFilt(String msg) {
        if(StringUtils.isEmpty(msg))    return msg;
        StringBuffer buffer = new StringBuffer(msg.length());
        for (int i = 0; i < msg.length(); i++) {
            char c = msg.charAt(i);
            switch (c) {
                case '\b':
                    buffer.append("\\b");
                    break;
                case '\f':
                    buffer.append("\\f");
                    break;
                case '\n':
                    buffer.append("<br />");
                    break;
                case '\r':
                    // ignore
                    break;
                case '\t':
                    buffer.append("\\t");
                    break;
//                case '\'':
//                    buffer.append("\\'");
//                    break;
//                case '\"':
//                    buffer.append("\\\"");
//                    break;
                case '\\':
                    buffer.append("\\\\");
                    break;
                case '<':
                    buffer.append("&lt;");
                    break;
                case '>':
                    buffer.append("&gt;");
                    break;
                case '&':
                    buffer.append("&amp;");
                    break;
                default:
                    buffer.append(c);
            }
        }
        return buffer.toString();
    }
}
