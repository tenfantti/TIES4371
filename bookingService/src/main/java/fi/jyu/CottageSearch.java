package fi.jyu;

import org.eclipse.rdf4j.RDF4JException;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.query.*;
import org.eclipse.rdf4j.query.algebra.In;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.http.HTTPRepository;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/CottageBooking")
public class CottageSearch extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();
        String title = "Using POST method to read forms";
        // 处理中文
        String docType = "<!DOCTYPE html> \n";
        out.println(docType +
                "<html>\n" +
                "<head><title>" + title + "</title></head>\n" +
                "<body bgcolor=\"#f0f0f0\">\n" +
                "<h1 align=\"center\">" + title + "</h1>\n" +
                "<ul>\n" +
                "  <li><b>站点名</b>："+
                "  <li><b>网址</b>："
                + "\n" +
                "</ul>\n" +
                "</body></html>");
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String filter = "";
        String booker = "";
        Integer cottage_places = null;
        Integer cottage_bedrooms = null;
        Integer cottage_lake_distance = null;
        Integer cottage_city_distance = null;
        Integer required_days = null;
        String booking_startDate = "";
        if (!req.getParameter("booker_name").equals("")){
            booker = req.getParameter("booker_name");
            filter = filter + "regex (?name,\""+booker+"\") && ";}
        if (!req.getParameter("cottage_places").equals("")){
            cottage_places = Integer.valueOf(req.getParameter("cottage_places"));
            filter = filter + "?places >= "+cottage_places+" && ";}
        if (!req.getParameter("cottage_bedrooms").equals("")){
            cottage_bedrooms = Integer.valueOf(req.getParameter("cottage_bedrooms"));
            filter = filter + "?bedrooms >= "+cottage_bedrooms+" && ";}
        if (!req.getParameter("cottage_lake_distance").equals("")){
            cottage_lake_distance = Integer.valueOf(req.getParameter("cottage_lake_distance"));
            filter = filter + "?lake_distance <= "+cottage_lake_distance+" && ";}
        if (!req.getParameter("cottage_city_distance").equals("")){
            cottage_city_distance = Integer.valueOf(req.getParameter("cottage_city_distance"));
            filter = filter + "?city_distance <= "+cottage_city_distance+" && ";}
        if (!req.getParameter("required_days").equals("")){
            required_days = Integer.valueOf(req.getParameter("required_days"));
            filter = filter + "day(xsd:dateTime(?end))-day(xsd:dateTime(?start)) >= "+required_days+") ||" +
                    "(month(xsd:dateTime(?end))-month(xsd:dateTime(?start)) >=1 && " +
                    "31+day(xsd:dateTime(?end))-day(xsd:dateTime(?start)) >= "+required_days+") && ";}
        if (!req.getParameter("booking_startDate").equals("")){
            booking_startDate = req.getParameter("booking_startDate");
            filter = filter + "xsd:dateTime(?start) > xsd:dateTime(\""+booking_startDate+"\")";}
        if (filter.endsWith("&& "))
            filter = filter.substring(0,filter.length()-3);

        String sparqlString = "PREFIX bookingServiceOntology:<http://localhost:8080/bookingService/onto/bookingServiceOntology.owl#>\n" +
                "SELECT ?name ?booking_number ?address ?image ?places ?bedrooms ?lake_distance ?nearest_city ?city_distance ?start ?end\n" +
                "WHERE { ?booking bookingServiceOntology:booker_name ?name. ?booking bookingServiceOntology:booking_number ?booking_number. ?booking bookingServiceOntology:hasBooked ?cottage.?booking bookingServiceOntology:booking_endDate ?end. ?booking bookingServiceOntology:booking_startDate ?start. ?cottage bookingServiceOntology:cottage_address ?address. ?cottage bookingServiceOntology:cottage_bedrooms ?bedrooms. ?cottage bookingServiceOntology:cottage_nearest_city ?nearest_city. ?cottage bookingServiceOntology:cottage_city_distance ?city_distance. ?cottage bookingServiceOntology:cottage_lake_distance ?lake_distance. ?cottage bookingServiceOntology:cottage_places ?places. ?cottage bookingServiceOntology:cottage_image ?image\n" +
                "    FILTER( "+filter+" )\n" +
                "}";

        String rdf4jServer = "http://localhost:8080/rdf4j-server/";
        String repositoryID = "ties4371";
        Repository repo = new HTTPRepository(rdf4jServer, repositoryID);
        repo.initialize();

        boolean ask = false;
        boolean graph = false;
        String askResult = "No";
        List<BindingSet> selectList = null;
        List<Statement> graphList = null;
        List<String> bindingNames = null;

        try (RepositoryConnection conn = repo.getConnection()) {
            try {
                String queryString = sparqlString;
                //boolean query
                if(queryString.toUpperCase().indexOf("ASK")!=-1){
                    ask = true;
                    int askIndex = queryString.toUpperCase().indexOf("ASK");
                    String askQuery = queryString.substring(0,askIndex) + "SELECT *" + queryString.substring(askIndex+3);
                    TupleQuery tupleQuery = conn.prepareTupleQuery(QueryLanguage.SPARQL, askQuery);
                    try (TupleQueryResult result = tupleQuery.evaluate()){
                        if(result.hasNext())
                            askResult = "Yes";
                    } finally {
                        // TODO: handle finally clause
                    }
                    //graph query
                }else if (queryString.toUpperCase().indexOf("CONSTRUCT")!=-1 || queryString.toUpperCase().indexOf("DESCRIBE")!=-1) {
                    graph = true;
                    GraphQuery graphQuery = conn.prepareGraphQuery(QueryLanguage.SPARQL, queryString);
                    try (GraphQueryResult result = graphQuery.evaluate()){
                        graphList = QueryResults.asList(result);
                    } finally {
                        // TODO: handle finally clause
                    }
                    //tuple query
                }else{
                    TupleQuery tupleQuery = conn.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
                    try (TupleQueryResult result = tupleQuery.evaluate()){
                        selectList = QueryResults.asList(result);
                        bindingNames = result.getBindingNames();
                    } finally {
                        // TODO: handle finally clause
                    }
                }
            } finally {
                conn.close();
            }

        } catch (RDF4JException e) {
            // TODO: handle exception
        }
        req.setAttribute("ask", ask);
        req.setAttribute("graph", graph);
        req.setAttribute("askResult", askResult);
        req.setAttribute("selectList", selectList);
        req.setAttribute("graphList", graphList);
        req.setAttribute("bindingNames", bindingNames);
        RequestDispatcher dispatcher = req.getRequestDispatcher("index.jsp");
        dispatcher .forward(req, res);

        sparql();
    }

    private void sparql() {

    }


}
