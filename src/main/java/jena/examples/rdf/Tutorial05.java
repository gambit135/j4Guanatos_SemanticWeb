
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

import org.apache.jena.rdf.model.*;
import org.apache.jena.util.FileManager;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.apache.jena.sparql.function.library.namespace;
import org.apache.jena.vocabulary.VCARD;

/**
 * Tutorial 5 - read RDF XML from a file and write it to standard out
 */
public class Tutorial05 extends Object {

    /**
     * NOTE that the file is loaded from the class-path and so requires that the
     * data-directory, as well as the directory containing the compiled class,
     * must be added to the class-path when running this and subsequent
     * examples.
     */
    public static final String inputFileName = "src/main/resources/vcardsDatabase.rdf";
    public static final String nameSpace = "http://somewhere/";
    //new File("src/main/resources/queries/Query1.txt").toURI()
    public static void main(String args[]) {
        // create an empty model
        Model model = ModelFactory.createDefaultModel();

        try {

            // read the RDF/XML file
            model.read(new FileInputStream(inputFileName), "");

            createPerson(model, nameSpace + "SarahConnor", "Sarah Connor", "Jeanette", "Connor");
                        
            // write it to standard out
            model.write(System.out);
            
            //model.write(new FileOutputStream(inputFileName));
            
            
            //printStatements(model);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Tutorial05.class.getName()).log(Level.SEVERE, null, ex);
        }

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
    
    public static void createPerson(Model model, String personURI, String fullName, String givenName, String familyName){
        Resource person
                = model.createResource(personURI)
                        .addProperty(VCARD.FN, fullName)
                        .addProperty(VCARD.N,
                                model.createResource()
                                        .addProperty(VCARD.Given, givenName)
                                        .addProperty(VCARD.Family, familyName));
    }

}
