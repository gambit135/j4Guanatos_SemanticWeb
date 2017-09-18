package com.oracle.isai;

import java.io.File;

import org.apache.jena.sparql.engine.http.QueryEngineHTTP;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Statement;
import org.apache.log4j.PropertyConfigurator;

/**
 * Created by Isai B. Cicourel
 */
public class QueryExample {

    public static void main(String[] args) throws IOException {
        PropertyConfigurator.configure("./src/main/resources/log4j.properties");
        String szQuery;
        // Placeholder SPARQL Query
        // szQuery = "select * where {?Subject ?Predicate ?Object} LIMIT 1";
        System.out.println("asdf");
        szQuery = new String(
                Files.readAllBytes(
                        Paths.get(
                                new File("src/main/resources/queries/Query1.txt").toURI())));

        // DBPedia Endpoint
        String szEndpoint = "http://dbpedia.org/sparql";

        // Query DBPedia
        try {

            queryEndpoint(szQuery, szEndpoint);
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }

    /**
     * Query an Endpoint using the given SPARQl query
     *
     * @param szQuery
     * @param szEndpoint
     * @throws Exception
     */
    public static void queryEndpoint(String szQuery, String szEndpoint)
            throws Exception {
        // Create a Query with the given String
        Query query = QueryFactory.create(szQuery);

        // Create the Execution Factory using the given Endpoint
        QueryExecution qexec = QueryExecutionFactory.
                sparqlService(szEndpoint, query);

        // Set Timeout
        ((QueryEngineHTTP) qexec).addParam("timeout", "10000");

        // Execute Query
        final int[] resultCounter = {0};
        //Query returns a ResultSet of QuerySolutions
        ResultSet resultSet = qexec.execSelect();

        //Build Stream from ResultSet
        Stream<QuerySolution> resultStream = StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(resultSet, Spliterator.ORDERED),
                false);
        //Iterate through variables from each QuerySolution in ResultSet
        resultStream
                .forEach(querySolution -> {
                    
                    System.out.println("\n# " + ++resultCounter[0] + ": ");
                    //Build an iterator for the names inside a QuerySolution
                    Iterator<String> variableNamesIterator = querySolution.varNames();

                    //Stream of Strings from Iterator
                    Stream<String> variablesStream = StreamSupport.stream(
                            Spliterators.spliteratorUnknownSize(variableNamesIterator,
                                    Spliterator.ORDERED), false);

                    //Print each name/value pair for a QuerySolution
                    variablesStream
                            .forEach(name -> {
                                String value = querySolution.get(name).toString();
                                System.out.println(name + ": " + value);
                            });

                });
        /*
        while (resultSet.hasNext()) {
            // Get Result
            QuerySolution qs = resultSet.next();

            // Get Variable Names
            Iterator<String> variableNamesIterator = qs.varNames();

            // Count
            resultCounter[0]++;
            System.out.println("Result " + resultCounter[0] + ": ");

            // Display Result
            while (variableNamesIterator.hasNext()) {
                String szVar = variableNamesIterator.next().toString();
                String szVal = qs.get(szVar).toString();

                System.out.println("[" + szVar + "]: " + szVal);
            }
        }*/
    } // End of Method: queryEndpoint()

}
