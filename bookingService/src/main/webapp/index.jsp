<%@page import="java.io.Console"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.List"%>
<%@page import="org.eclipse.rdf4j.query.BindingSet"%>
<%@page import="org.eclipse.rdf4j.model.Statement"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ page import="fi.jyu.CottageSearch"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>RDF4J results</title>
    <style type="text/css">
        table {
            border: 1px solid #999;
            border-collapse: collapse;
            width: 100%;
            table-layout: fixed;
        }

        th {
            background: #CCC;
            text-transform: capitalize;
        }

        th, td {
            border: 1px solid #999;
            padding: 2px 5px;
            word-wrap: break-word;
        }
    </style>
</head>
<body>
<h2>Cottage Bookings</h2>
<form action="CottageBooking" method="post">
    <textarea type="text" style="width: 500px; height: 200px;" name="sparql"></textarea>
    <input type="submit" value="Search">
</form>
<%
    if (request.getAttribute("ask")!=null){


        boolean ask = (boolean)request.getAttribute("ask");
        boolean graph = (boolean)request.getAttribute("graph");
        if(ask == true){
            String askResult = (String)request.getAttribute("askResult");
%>
<%=askResult %>

<%
}else if(graph == true){
    List<Statement> list = (List<Statement>)request.getAttribute("graphList");
%>
<table>
    <tr>
        <th>Subject</th>
        <th>Predicate</th>
        <th>Object</th>
    </tr>
    <%
        for(int i=0;i<list.size();i++)
        {
            Statement result = (Statement)list.get(i);
    %>
    <tr>
        <td><%=result.getSubject()%></td>
        <td><%=result.getPredicate()%></td>
        <td><%=result.getObject()%></td>
    </tr>
    <%} %>
</table>
<%       }else{
    List<BindingSet> list = (List<BindingSet>)request.getAttribute("selectList");
    List<String> name = (List<String>)request.getAttribute("bindingNames");
%>
<table>
    <tr>
        <%for(int i=0;i<name.size();i++){%>
        <th><%=name.get(i)%></th>
        <%} %>
    </tr>
    <%
        for(int i=0;i<list.size();i++)
        {
            BindingSet result = (BindingSet)list.get(i);
    %>
    <tr>
        <%for(int n=0;n<result.size();n++){%>
        <td><%=result.getValue(name.get(n)).toString()%></td>
        <%} %>
    </tr>
    <%
                }
            }
        }
    %>
</table>
</body>
</html>