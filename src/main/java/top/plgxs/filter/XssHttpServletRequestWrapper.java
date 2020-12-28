package top.plgxs.filter;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.web.util.HtmlUtils;

/**
 * 自定义request装饰类
 * @filename XssHttpServletRequestWrapper.java
 * @version 1.0
 * @author Stranger。
 * @email lipian1004@163.com
 * @date 2019年6月22日 上午10:47:19
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {
    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    /**
     * 重写getParameterValues
     * @param parameter
     * @return
     * @see javax.servlet.ServletRequestWrapper#getParameterValues(java.lang.String)
     */
    public String[] getParameterValues(String parameter) {
        String[] values = super.getParameterValues(parameter);
        if (values == null) {
            return null;
        }
        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
            encodedValues[i] = filter(values[i]);
        }
        return encodedValues;
    }

    /**
     * 重写getParameterMap
     * @return
     * @see javax.servlet.ServletRequestWrapper#getParameterMap()
     */
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> map = super.getParameterMap();
        Map<String, String[]> encodedMap = new HashMap<String, String[]>();
        encodedMap.putAll(map);

        for (Map.Entry<String, String[]> entry : encodedMap.entrySet()) {
            String[] value = entry.getValue();
            String[] encodedValues = new String[value.length];
            for (int i = 0; i < value.length; i++) {
                encodedValues[i] = filter(value[i]);
            }
            encodedMap.put(entry.getKey(), encodedValues);
        }
        return encodedMap;
    }

    /**
     * 重写getParameter方法
     * @param parameter
     * @return
     * @see javax.servlet.ServletRequestWrapper#getParameter(java.lang.String)
     */
    public String getParameter(String parameter) {
        String value = super.getParameter(parameter);
        if (value == null) {
            return null;
        }
        return filter(value);
    }

    /**
     * 重写getHeader
     * @param name
     * @return
     * @see javax.servlet.http.HttpServletRequestWrapper#getHeader(java.lang.String)
     */
    public String getHeader(String name) {
        String value = super.getHeader(name);
        if (value == null)
            return null;
        return filter(value);
    }

    /** 
     * 过滤特殊字符 
     */
    public static String filter(String value) {
        if (value == null) {
            return null;
        }
        return StringEscapeUtils.escapeSql(HtmlUtils.htmlEscape(value));
    }
}
