/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jena.examples.rdf;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.*;

import org.apache.jena.sparql.vocabulary.FOAF;

/**
 * Tutorial 3 Statement attribute accessor methods
 */
public class Tutorial03 extends Object {

    public static void main(String args[]) {

        // some definitions
        String personURI = "http://somewhere/JohnSmith";
        String givenName = "John";
        String familyName = "Smith";
        String fullName = givenName + " " + familyName;
        // create an empty model
        Model model = ModelFactory.createDefaultModel();

        // create the resource
        //   and add the properties cascading style
        Resource johnSmith
                = model.createResource(personURI)
                        .addProperty(VCARD.FN, fullName)
                        .addProperty(VCARD.N,
                                model.createResource()
                                        .addProperty(VCARD.Given, givenName)
                                        .addProperty(VCARD.Family, familyName));

        // list the statements in the graph
        StmtIterator statementIterator = model.listStatements();
        printStatements(model);
        writeRdfStatements(model, System.out);
        /*
        // print out the predicate, subject and object of each statement
        while (statementIterator.hasNext()) {
            System.out.println(" .");
            Statement statement = statementIterator.nextStatement();         // get next statement
            Resource subject = statement.getSubject();   // get the subject
            Property predicate = statement.getPredicate(); // get the predicate
            RDFNode object = statement.getObject();    // get the object

            System.out.println("subject: " + subject.toString());
            System.out.println("Predicate: " + predicate.toString() + " ");

            System.out.println(" .");
        }*/
    }

    public static void printStatements(Model model) {
        StmtIterator statementIterator = model.listStatements();

        Stream<Statement> targetStream = StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(statementIterator, Spliterator.ORDERED),
                false);
        targetStream
                .forEach(statement -> {
                    System.out.println("");
                    System.out.println("Subject: " + statement.getSubject().toString());
                    System.out.println("Predicate: " + statement.getPredicate().toString());
                    getObjectsFromStatement(statement.getObject());
                    System.out.println("");
                });
    }

    public static List<String> getObjectsFromStatement(RDFNode object) {

        ArrayList<String> statementObjects = new ArrayList<>();

        if (object instanceof Resource) {
            Resource resource = (Resource) object;

            System.out.println("Object(Resource): " + object.toString());
            // printStatements(model1);
        } else {
            // object is a literal
            System.out.println(" Object(Literal): " + object.toString());
        }
        return statementObjects;
    }

    private static void writeRdfStatements(Model model, OutputStream output) {
        model.write(output);
    }

}
