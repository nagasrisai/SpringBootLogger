package org.demo.springbootlogger.utils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.TeeOutputStream;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.mock.web.DelegatingServletInputStream;
import org.springframework.mock.web.DelegatingServletOutputStream;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * For Logging Internal rest request-response of a {@link SpringBootApplication}
 * To use this just create the bean of this class in your application config or spring-boot-application class like this:
 *  @Bean
 *  public InternalRestClientLoggerUtil getInternalRestClientLoggerUtil() {
 * 		return new InternalRestClientLoggerUtil();
 *  }
 */
public class InternalRestClientLoggerUtil implements Filter {

    private static final Logger log = LoggerFactory.getLogger(InternalRestClientLoggerUtil.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        CustomHttpRequestWrapper requestWrapper = new CustomHttpRequestWrapper((HttpServletRequest) servletRequest);
        StringBuilder requestBuilder = new StringBuilder();
        requestBuilder.append("\n======================================== Internal Flow Starts ========================================")
                .append("\nRequest URI: ").append(requestWrapper.getRequestURI())
                .append("\nRequest Method: ").append(requestWrapper.getMethod())
                .append("\nRequest Headers: ").append(getRequestHeaders(requestWrapper))
                .append("\nRequest Body: ").append(requestWrapper.getHttpRequestBody())
                .append("\n========================================");
        log.info(requestBuilder.toString());

        CustomHttpResponseWrapper responseWrapper = new CustomHttpResponseWrapper((HttpServletResponse) servletResponse);
        filterChain.doFilter(requestWrapper, responseWrapper);

        StringBuilder responseBuilder = new StringBuilder();
        responseBuilder.append("\n========================================")
                .append("\nResponse Status: ").append(responseWrapper.getStatus())
                .append("\nResponse Headers: ").append(getResponseHeaders(responseWrapper))
                .append("\nResponse Body: ").append(responseWrapper.getHttpResponseBody())
                .append("\n======================================== Internal Flow Ends ========================================");
        log.info(responseBuilder.toString());
    }

    private String getRequestHeaders(CustomHttpRequestWrapper requestWrapper) {
        List<String> headers = new ArrayList<>();
        Enumeration<String> headerNames = requestWrapper.getHeaderNames();
        while (headerNames.hasMoreElements())
        {
            String headerName = headerNames.nextElement();
            headers.add(headerName + " : " + requestWrapper.getHeader(headerName));
        }
        return headers.toString();
    }

    private String getResponseHeaders(CustomHttpResponseWrapper responseWrapper) {
        List<String> headers = new ArrayList<>();
        responseWrapper.getHeaderNames().forEach(headerNames -> headers.add(headerNames + " : " + responseWrapper.getHeader(headerNames)));
        return headers.toString();
    }

    private class CustomHttpRequestWrapper extends HttpServletRequestWrapper {

        private byte[] byteArray;

        public CustomHttpRequestWrapper(HttpServletRequest request) {
            super(request);
            try {
                byteArray = IOUtils.toByteArray(request.getInputStream());
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }

        @Override
        public ServletInputStream getInputStream() {
            return new DelegatingServletInputStream(new ByteArrayInputStream(byteArray));
        }

        public byte[] getByteArray() {
            return byteArray;
        }

        public String getHttpRequestBody() {
            String httpRequestBody = null;
            try {
                httpRequestBody = new JSONParser(new String(getByteArray())).parse().toString();
            } catch (ParseException e) {
                log.error(e.getMessage());
            }
            return httpRequestBody;
        }
    }

    private class CustomHttpResponseWrapper extends HttpServletResponseWrapper {

        private ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        private PrintStream printStream = new PrintStream(byteArrayOutputStream);

        public CustomHttpResponseWrapper(HttpServletResponse servletResponse) {
            super(servletResponse);
        }

        @Override
        public ServletOutputStream getOutputStream() throws IOException {
            return new DelegatingServletOutputStream(new TeeOutputStream(super.getOutputStream(), printStream));
        }

        @Override
        public PrintWriter getWriter() throws IOException {
            return new PrintWriter(new TeeOutputStream(super.getOutputStream(), printStream));
        }

        public ByteArrayOutputStream getByteArrayOutputStream() {
            return byteArrayOutputStream;
        }

        public String getHttpResponseBody() {
            return getByteArrayOutputStream().toString();
        }
    }
}