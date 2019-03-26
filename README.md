**This Project is for course TIES4371**

**Back-end**: rdf4j  
**Database**: rdf4j-server,rdf4j-workbench  
**ontology**: http://localhost:8080/bookingService/onto/bookingServiceOntology.owl

**an example for SPARQL query:**  
find booking's booker name and start date whose start dates are earlier than 2019.4.5 11:00:00

`PREFIX bookingServiceOntology:<http://localhost:8080/bookingService/onto/bookingServiceOntology.owl#>
SELECT ?name ?start
WHERE { ?booking bookingServiceOntology:booker_name ?name. ?booking bookingServiceOntology:booking_startDate ?start
  FILTER(xsd:dateTime(?start) < xsd:dateTime("2019-04-05T11:00:00"))
}`  
results:  
Name	Start  
"Mikko Haapanen"^^	"2019-03-21T21:32:00"^^  
"James Aironfoot"^^	"2019-04-03T13:20:00"^^
