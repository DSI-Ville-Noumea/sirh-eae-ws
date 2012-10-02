<%@page import="java.net.InetAddress"%>
sirh.eae.ws.version=<%= this.getServletContext().getInitParameter("VERSION") %>
sirh.eae.ws.localhost.hostaddress=<%=InetAddress.getLocalHost().getHostAddress() %>
sirh.eae.ws.localhost.canonicalhostname=<%=InetAddress.getLocalHost().getCanonicalHostName() %>
sirh.eae.ws.localhost.hostname=<%=InetAddress.getLocalHost().getHostName() %>
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
	
