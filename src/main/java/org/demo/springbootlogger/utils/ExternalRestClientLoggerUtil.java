package org.demo.springbootlogger.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/***
 * Implementation of this ExternalClientLoggerUtil class uses the interface
 * {@linkplain org.springframework.http.client.ClientHttpRequestInterceptor} for logging Outbound rest request
 * and inbound rest response.
 * This is util class will hit the external server and sends the request to external server and process it
 * and gets response from the server.
 * <p>The main entry point for interceptors is
 *  {@link #intercept(HttpRequest, byte[], ClientHttpRequestExecution)}
 */

public class ExternalRestClientLoggerUtil implements ClientHttpRequestInterceptor {

    private static final Logger log = LoggerFactory.getLogger(ExternalRestClientLoggerUtil.class);

    /** This class Overrides {@link #intercept(HttpRequest, byte[], ClientHttpRequestExecution)} methods from {@linkplain org.springframework.http.client.ClientHttpRequestInterceptor}
     *{@link HttpRequest} catches the requested data and passes to the {@link #logRequest(HttpRequest, byte[])}
     * will used to format the data.
     * {@link ClientHttpResponse} will gives us response from the external server and will passes
     * to {@link #logResponse(ClientHttpResponse)} there we will format the data and logs in the console.
     */

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        /**
         * logRequest will passes {@link HttpRequest},body to log data in formatted way.
         */
        logRequest(request, body);
        /**
         Here response is final which stores the result from external server.
         */
        final ClientHttpResponse response = execution.execute(request, body);
        /**
         * passes the result to the logResponse method to format the data to log it into the console.
         */
        logResponse(response);
        return response;
    }



    private void logRequest(HttpRequest request, byte[] body) {

        StringBuilder requestBuilder = new StringBuilder();
        requestBuilder.append("\n======================================== OUTBOUND REST REQUEST ========================================")
                .append("\nRequest URI: ").append(request.getURI())
                .append("\nRequest Method: ").append(request.getMethod())
                .append("\nRequest Headers: ").append(request.getHeaders())
                .append("\nRequest body:").append(new String(body, StandardCharsets.UTF_8))
                .append("\n=======================================================================================================");
        log.info(requestBuilder.toString());
    }

    private void logResponse(ClientHttpResponse response) throws IOException {

        StringBuilder responseBuilder = new StringBuilder();
        responseBuilder.append("\n======================================== INBOUND REST RESPONSE ========================================")
                .append("\nResponse Status: ").append(response.getStatusCode().value())
                .append("\nResponse Headers: ").append(response.getHeaders())
                .append("\nResponse body: ").append(StreamUtils.copyToString(response.getBody(), StandardCharsets.UTF_8))
                .append("\n=======================================================================================================");
        log.info(responseBuilder.toString());
    }
}