This project has been developed on Java 11 and Tomcat 10.0.12, so corresponding libraries have been used via Maven (jakarta.servlet
instead of javax.servlet for previous versions (so much time wasted on that)).

Since Tomcat version is 10.. there is no need for web.xml to bind the servlet, everything is done via annotation @WebServlet.

For sending ajax requests to the servlet I used jquery scripts. And also used them to show the status of the servlet on the page.
JSTL support is added and I tried to implement that with JSTL tags, but I can't see how it is better than doing it via a script.

The project has 4 tests for each type of the request. It can be deployed via maven command "mvn tomcat7:deploy".