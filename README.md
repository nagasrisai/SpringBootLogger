<h1>Spring boot logger</h1>
<p>This application logs both request and response when hit the external server
with input params (with methods, eg. GET, POST, PUT,DELETE), request Url, query string,
also response of this action, both success and errors and Status code.It logs not only
incoming https request/response but also outgoing https requests/response in json format as shown below.<br>
2021-11-15T06:57:16,676 INFO [RestClientLogger] -<br>
====================== Outbound REST REQUEST =========================<br>
Request URI: uri<br>
Request Method: HTTP-Methods<br>
Request Headers: []<br>
Request Body: {}<br>
======================================================================<br>
2021-11-15T06:57:17,768 INFO [RestClientLogger] -<br>
====================== Inbound REST RESPONSE =========================<br>
Response Status: HTTP-Status-Code<br>
Response Headers: []<br>
Response Body: {}<br>

======================================================================<br>
</p>



<h1>Dependencies</h1>
<h2>Dependencies used in this application are</h2>

<dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-web-services</artifactId>
       <exclusions>
       <exclusion>
       <groupId>org.apache.logging.log4j</groupId>
       <artifactId>log4j-to-slf4j</artifactId>
       </exclusion>
       </exclusions>
</dependency>
<dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-test</artifactId>
</dependency>

<dependency>
            <groupId>commons-io</groupId>
            <artifactId>commons-io</artifactId>
            <version>${apache-commons-io-version}</version>
</dependency>

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
5. This completes setting the environment before we run the program.




<h1>How to run</h1>
<ul>
       <li>
 Download the spring-boot-logger project.
       </li>
       <li>
 Unzip the file into target folder.
       </li>
       <li>
 load the project into your local ide.
       </li>
       <li>
 Open the terminal and run the below command.
       </li>
       <pre>
       <code>
   mvn clean package
   </code>
   </pre>
       <li>
              A path will be generated in the console as shown below.</li>
   ![img_1.png](img_1.png)
       <li>
 Copy this path of jar file and return to original application.
       </li>
       <li>
 Import jar file into your classpath.
       </li>
       <li>
 run the command.
       </li>
       <pre>
       <code>
mvn clean install
</code>
</pre>





