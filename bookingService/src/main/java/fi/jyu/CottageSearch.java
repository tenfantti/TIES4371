package fi.jyu;

import org.eclipse.rdf4j.RDF4JException;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.query.*;
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
                String queryString =req.getParameter("sparql");
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
    }
}
