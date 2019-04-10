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
        form {
            width: 500px;
            margin: 0 auto;
            line-height: 35px;
        }
        input,select {
            width:150px;
        }
        .button {
            margin: 0 auto;
            width: 100px;
            display: block;
        }
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
        img{
            width: 100%;
        }
    </style>
</head>
<body>
<h2 style="text-align: center;">Cottage Bookings</h2>
<form action="CottageBooking" method="post" id="bookingSearch">
    booker name: <input type="text" name="booker_name">
    <br>
    cottage places: <select name="cottage_places" form="bookingSearch">
        <option value="4">4</option>
        <option value="5">5</option>
        <option value="7">7</option>
        <option value="10">10</option>
    </select>
    <br>
    bedroom amount:
    <select name="cottage_bedrooms" form="bookingSearch">
        <option value="1">1</option>
        <option value="2">2</option>
        <option value="3">3</option>
        <option value="4">4</option>
        <option value="5">5</option>
        <option value="6">6</option>
        <option value="7">7</option>
        <option value="8">8</option>
    </select>
    <br>
    max distance from a lake (meters): <input type="text" name="cottage_lake_distance">
    <br>
    max distance to nearest city: <input type="text" name="cottage_city_distance">
    <br>
    required days: <input type="text" name="required_days">
    <br>
    start date: <input type="text" name="booking_startDate">
    <br>
    <input type="submit" value="Search" class="button">
    <%--<textarea type="text" style="width: 500px; height: 200px;" name="sparql"></textarea>--%>
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
            <%if (n == 3){%>
                <td><img src=<%=result.getValue(name.get(n)).toString()%>></td>
            <%} else {%>
                <td><%=result.getValue(name.get(n)).toString()%></td>
            <%} %>
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