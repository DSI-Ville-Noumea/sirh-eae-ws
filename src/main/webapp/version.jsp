<%@page contentType="text/plain"%>
<%@page import="java.net.InetAddress"%>
sirh.eae.ws.version=${version}<br/>
sirh.eae.ws.hostaddress=<%=InetAddress.getLocalHost().getHostAddress() %><br/>
sirh.eae.ws.canonicalhostname=<%=InetAddress.getLocalHost().getCanonicalHostName() %><br/>
sirh.eae.ws.hostname=<%=InetAddress.getLocalHost().getHostName() %><br/>
sirh.eae.ws.tomcat.version=<%= application.getServerInfo() %><br/>
sirh.eae.ws.tomcat.catalina_base=<%= System.getProperty("catalina.base") %><br/>
<% 
HttpSession theSession = request.getSession( false );

// print out the session id
if( theSession != null ) {
  //pw.println( "<BR>Session Id: " + theSession.getId() );
  synchronized( theSession ) {
    // invalidating a session destroys it
    theSession.invalidate();
    //pw.println( "<BR>Session destroyed" );
  }
}
%>