<h1>Spring boot logger</h1>

<p>
This application logs internal flow data means it logs the data which was sent from post man
to external server whether it would be any format(Rest/Soap), then depends on server it will converts.
After Server completes its processing it sends back response to the post man, then internal flow logs ends.
<br>
$CURRENT_DATE_TIME$  INFO InternalRestClientLoggerUtil<br>
======================================== Internal Flow Starts ========================================<br>
Request URI: /<uri> <br>
Request Method: <HTTP METHODS> <br>
Request Headers: [] <br>
Request Body: {} <br>
======================================== <br>
... <br>
Logs as per SoapClientLogger/RestClientLogger <br>
... <br>
$CURRENT_DATE_TIME$  INFO InternalRestClientLoggerUtil     : <br>
======================================== <br>
Response Status: <HTTP STATUS CODE> <br>
Response Headers: [] <br>
Response Body: {} <br>
======================================== Internal Flow Ends ======================================== <br>
</p>


<p>This application logs both request and response when hit the external server
with input params (with methods, eg. GET, POST, PUT,DELETE), request Url, query string,
also response of this action, both success and errors and Status code.It logs not only
incoming https request/response but also outgoing https requests/response in json format as shown below.<br>
$CURRENT_DATE_TIME$ INFO [RestClientLogger] -<br>
====================== Outbound REST REQUEST =========================<br>
Request URI: uri<br>
Request Method: HTTP-Methods<br>
Request Headers: []<br>
Request Body: {}<br>
======================================================================<br>
$CURRENT_DATE_TIME$ INFO [RestClientLogger] -<br>
====================== Inbound REST RESPONSE =========================<br>
Response Status: HTTP-Status-Code<br>
Response Headers: []<br>
Response Body: {}<br>
======================================================================<br>
</p>
This application logs both request and response when hit the external server,then server converts Rest format
to Soap format with input params (with methods, eg. GET, POST, PUT,DELETE), request Url, query string,
also response of this action(which again converts Soap to Rest format), both success and errors and Status code.It logs not only
incoming https request/response but also outgoing https requests/response in json format as shown below.
<p>
<br>
$CURRENT_DATE_TIME$ INFO [SoapClientLogger] -<br>
====================== Outbound SOAP REQUEST =========================<br>
Request URI: uri<br>
Request Body: {}<br>
======================================================================<br>
$CURRENT_DATE_TIME$ INFO [SoapClientLogger] -<br>
====================== Inbound SOAP RESPONSE =========================<br>
Response Status: HTTP-Status-Code<br>
Response/Fault Response Body: {}<br>
======================================================================<br>
</p>



<h1>How to setup</h1>
<ul>
       <li>
 First, we need to call and set the interceptor for external logging by calling its object.
              The externalClientInterceptor class has methods for external soap and rest loggers.</li>
       <li>
 For Soap client logger use following code.
              `soapCalculatorClient.setInterceptors(ExternalClientLoggerInterceptor.getSoapClientLogger());`</li>
       <li>
 For Rest Client logger use this code.It should be inside the code block where we create the bean for rest template.
              ` restTemplate.setInterceptors(ExternalClientLoggerInterceptor.getRestClientLogger());</li>
       <li>
 For internal logging we need to create a bean for restInternalLoggingUtil since rest is used for both soap and rest in
              internal logging.</li>
       </ul>
  <pre>
  <code>
   @Bean
   public InternalRestClientLoggerUtil getInternalRestClientLoggerUtil(){
   return new InternalRestClientLoggerUtil();
   }
   </code>
   </pre>
   <ul>
   <li>
    This completes setting the environment before we run the program.
    </li>
    </ul>






