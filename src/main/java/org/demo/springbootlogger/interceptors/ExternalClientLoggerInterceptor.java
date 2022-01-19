package org.sds.springbootlogger.interceptors;

import org.sds.springbootlogger.utils.ExternalRestClientLoggerUtil;
import org.sds.springbootlogger.utils.ExternalSoapClientLoggerUtil;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;

import java.util.Collections;
import java.util.List;

public class ClientLoggerInterceptor {

    /**
     * Logger Utility for {@link org.springframework.ws.client.core.WebServiceTemplate}
     * Sets the client interceptors to apply to all web service invocations made by this template : {@link org.springframework.ws.client.core.WebServiceTemplate} as:
     * @implNote: webServiceTemplate.setInterceptors(ExternalClientLoggerInterceptor.getSoapClientLogger())
     * @return {@code ClientInterceptor[] }
     */
    public static ClientInterceptor[] getSoapClientLogger() {
        return new ClientInterceptor[] {new ExternalSoapClientLoggerUtil()};
    }

    /**
     * Logger Utility for {@link org.springframework.web.client.RestTemplate}
     * RestTemplate restTemplate = new RestTemplate(new BufferingClientHttpRequestFactory(<any-class>));
     * <any-class> : any-class-implementing-ClientHttpRequestFactory : {@link org.springframework.http.client.ClientHttpRequestFactory}
     * e.g., new SimpleClientHttpRequestFactory() : {@link org.springframework.http.client.SimpleClientHttpRequestFactory} or
     * new HttpComponentsClientHttpRequestFactory() {@link org.springframework.http.client.HttpComponentsClientHttpRequestFactory} etc.
     * Set the request interceptor of this template : {@link org.springframework.web.client.RestTemplate} as:
     * @implNote: restTemplate.setInterceptors(ExternalClientLoggerInterceptor.getRestClientLogger());
     * @return {@code List<ClientHttpRequestInterceptor> }
     */
    public static List<ClientHttpRequestInterceptor> getRestClientLogger() {
        return Collections.singletonList(new ExternalRestClientLoggerUtil());
    }
}
