package top.plgxs.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * 处理Xss攻击的过滤器
 * @filename XssFilter.java
 * @version 1.0
 * @author Stranger。
 * @email lipian1004@163.com
 * @date 2019年6月22日 上午10:46:23
 */
public class XssFilter implements Filter {
    FilterConfig     fc = null;
    private String   excludedPage;
    private String[] excludedPages;

    @Override
    public void init(FilterConfig fc) throws ServletException {
        excludedPage = fc.getInitParameter("excludedPages");//此处的ignores就是在web.xml定义的名称一样。
        if (excludedPage != null && excludedPage.length() > 0) {
            excludedPages = excludedPage.split(",");
        }
        this.fc = fc;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain fc) throws IOException, ServletException {
        // 定义表示变量 并验证用户请求URL 是否包含不过滤路径
        boolean flag = false;
        for (String page : excludedPages) {
            String uri = ((HttpServletRequest) request).getRequestURI();
            if (page.indexOf("*") > 0) {
                // uri格式为 /sinosoft-cms/content/save
                // page格式为 /content/* 或 /content/save
                // 第二个/的下标
                int index = uri.indexOf("/", 1);
                // 第三个/的下标
                int tindex = uri.indexOf("/", index + 1);
                if (tindex > 0) {
                    uri = uri.substring(index + 1, tindex);
                } else {
                    uri = uri.substring(index + 1);
                }
                page = page.substring(page.indexOf("/") + 1, page.lastIndexOf("/"));
            }
            if (uri.equals(page)) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            fc.doFilter(new XssHttpServletRequestWrapper((HttpServletRequest) request), response);
        } else {
            fc.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        fc = null;
    }
}
