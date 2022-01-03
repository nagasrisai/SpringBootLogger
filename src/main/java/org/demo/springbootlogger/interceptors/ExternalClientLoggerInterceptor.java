package org.demo.springbootlogger.interceptors;

import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.demo.springbootlogger.utils.ExternalRestClientLoggerUtil;
import org.demo.springbootlogger.utils.ExternalSoapClientLoggerUtil;

import java.util.Collections;
import java.util.List;

public class ExternalClientLoggerInterceptor {

    /**
     * Logger Util for {@link org.springframework.web.client.RestTemplate}
     * RestTemplate restTemplate = new RestTemplate(new BufferingClientHttpRequestFactory(<any-class>));
     * <any-class> : any-class-implementing-ClientHttpRequestFactory : {@link org.springframework.http.client.ClientHttpRequestFactory}
     * e.g., new SimpleClientHttpRequestFactory() : {@link org.springframework.http.client.SimpleClientHttpRequestFactory} or
     * new HttpComponentsClientHttpRequestFactory() {@link org.springframework.http.client.HttpComponentsClientHttpRequestFactory} etc.
     * Set the request interceptors that this accessor: {@link org.springframework.web.client.RestTemplate} should use.
     * restTemplate.setInterceptors(ExternalClientLoggerInterceptor.getRestClientLogger());
     * @return {@link List<ClientHttpRequestInterceptor>}
     */
    public static List<ClientHttpRequestInterceptor> getRestClientLogger() {
        return Collections.singletonList(new ExternalRestClientLoggerUtil());
    }

    /**
     * Logger Util for {@link org.springframework.ws.client.core.WebServiceTemplate}
     * Sets the client interceptors to apply to all web service invocations made by this template : {@link org.springframework.ws.client.core.WebServiceTemplate}
     * webServiceTemplate.setInterceptors(ExternalClientLoggerInterceptor.getSoapClientLogger())
     * @return {@link ClientInterceptor[] }
     */
    public static ClientInterceptor[] getSoapClientLogger() {
        return new ClientInterceptor[] {new ExternalSoapClientLoggerUtil()};
    }
}