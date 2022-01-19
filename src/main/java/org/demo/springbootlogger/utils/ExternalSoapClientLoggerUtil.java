package org.sds.springbootlogger.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.client.WebServiceClientException;
import org.springframework.ws.client.support.interceptor.ClientInterceptorAdapter;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.transport.WebServiceConnection;
import org.springframework.ws.transport.context.TransportContextHolder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

/**
 * Implementation of ExternalSoapClientLoggerUtil class
 * uses interface {@link ClientInterceptorAdapter}
 * From this interface we are overriding methods like
 * {@link #handleFault(MessageContext)},{@link #handleRequest(MessageContext)}, {@link #handleResponse(MessageContext)}
 * This class is used to hit external server,
 * send the request(this request will be convert soap) and processes it
 * and gives us the result in soap format.
 * <p>The Entry point of contact is {@link #handleRequest(MessageContext)}</p>
 */
public class ExternalSoapClientLoggerUtil extends ClientInterceptorAdapter {

    private static final Logger log = LoggerFactory.getLogger(ExternalSoapClientLoggerUtil.class);

    /**
     * @param messageContext will have getRequest() method is used to get Requested data which was sent to server.
     * The Request is converted into string and uses UTF_8 format.
     * @return {@code super.handleRequest(messageContext)}
     * @throws WebServiceClientException
     */

    @Override
    public boolean handleRequest(MessageContext messageContext) throws WebServiceClientException {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            messageContext.getRequest().writeTo(byteArrayOutputStream);
            String requestPayload = byteArrayOutputStream.toString(StandardCharsets.UTF_8.name());

            StringBuilder requestBuilder = new StringBuilder();
            requestBuilder.append("\n======================================== OUTBOUND SOAP REQUEST ========================================")
                    .append("\nRequest URI:").append(getSoapClientUri())
                    .append("\nRequest Body: ").append(requestPayload)
                    .append("\n=======================================================================================================");
            log.info(requestBuilder.toString());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return super.handleRequest(messageContext);
    }

    /**
     * @param messageContext will have the getResponse method which returns the response data from the server.
     * The Response is converted into string and uses UTF_8 format.
     * @return {@code super.handleResponse(messageContext)}
     * @throws WebServiceClientException
     **/

    @Override
    public boolean handleResponse(MessageContext messageContext) throws WebServiceClientException {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            messageContext.getResponse().writeTo(byteArrayOutputStream);
            String responsePayload = byteArrayOutputStream.toString(StandardCharsets.UTF_8.name());

            StringBuilder responseBuilder = new StringBuilder();
            responseBuilder.append("\n======================================== INBOUND SOAP RESPONSE ========================================")
                    .append("\nResponse Status: ").append(getSoapClientResponseStatus())
                    .append("\nResponse Body: ").append(responsePayload)
                    .append("\n=======================================================================================================");
            log.info(responseBuilder.toString());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return super.handleResponse(messageContext);
    }

    /**
     * This method will invoke when {@link org.springframework.ws.client.core.WebServiceTemplate} gets a fault response.
     * @return {@code super.handleFault(messageContext)}
     * @throws WebServiceClientException
     */

    @Override
    public boolean handleFault(MessageContext messageContext) throws WebServiceClientException {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            messageContext.getResponse().writeTo(byteArrayOutputStream);
            String responsePayload = byteArrayOutputStream.toString(StandardCharsets.UTF_8.name());

            StringBuilder faultResponseBuilder = new StringBuilder();
            faultResponseBuilder.append("\n======================================== INBOUND SOAP FAULT RESPONSE ========================================")
                    .append("\nResponse Status: ").append(getSoapClientResponseStatus())
                    .append("\nFaultResponse Body: ").append(responsePayload)
                    .append("\n=======================================================================================================");
            log.info(faultResponseBuilder.toString());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return super.handleFault(messageContext);
    }

    private String getSoapClientUri() throws URISyntaxException {
        return TransportContextHolder.getTransportContext().getConnection().getUri().toString();
    }

    private String getSoapClientResponseStatus() throws IOException {
        WebServiceConnection webServiceConnection = TransportContextHolder.getTransportContext().getConnection();
        return webServiceConnection.getErrorMessage();
    }
}
