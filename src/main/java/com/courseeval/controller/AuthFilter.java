package com.courseeval.util;

import com.courseeval.model.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebFilter("/*")
public class AuthFilter implements Filter {

    private static final List<String> PUBLIC_PATHS = Arrays.asList(
        "/login", "/register", "/survey/respond", "/survey/list",
        "/css/", "/js/", "/index.jsp"
    );

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String path = request.getServletPath();

        // Allow public paths
        for (String pub : PUBLIC_PATHS) {
            if (path.startsWith(pub)) {
                chain.doFilter(req, res);
                return;
            }
        }

        // Allow static resources
        if (path.startsWith("/css") || path.startsWith("/js") || path.endsWith(".css") || path.endsWith(".js")) {
            chain.doFilter(req, res);
            return;
        }

        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("loggedUser") : null;

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Role-based path guards
        String role = user.getRoleName();
        if (path.startsWith("/admin") && !"ADMIN".equals(role)) {
            response.sendError(403);
            return;
        }
        if (path.startsWith("/initiator") && !"INITIATOR".equals(role) && !"ADMIN".equals(role)) {
            response.sendError(403);
            return;
        }
        if (path.startsWith("/teacher") && !"TEACHER".equals(role) && !"ADMIN".equals(role)) {
            response.sendError(403);
            return;
        }

        chain.doFilter(req, res);
    }

    @Override public void init(FilterConfig config) {}
    @Override public void destroy() {}
}
