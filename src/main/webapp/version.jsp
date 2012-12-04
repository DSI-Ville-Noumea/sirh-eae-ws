<%@page import="java.net.InetAddress"%>
sirh.eae.ws.version=${version}
sirh.eae.ws.hostaddress=<%=InetAddress.getLocalHost().getHostAddress() %>
sirh.eae.ws.canonicalhostname=<%=InetAddress.getLocalHost().getCanonicalHostName() %>
sirh.eae.ws.hostname=<%=InetAddress.getLocalHost().getHostName() %>
sirh.eae.ws.tomcat.version=<%= application.getServerInfo() %>
sirh.eae.ws.tomcat.catalina_base=<%= System.getProperty("catalina.base") %>
