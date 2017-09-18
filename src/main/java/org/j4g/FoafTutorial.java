/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.j4g;

import java.io.OutputStream;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.sparql.vocabulary.FOAF;

/**
 *
 * @author Alejandro_Tellez
 */
public class FoafTutorial {

    public static void main(String args[]) {
        String personURI = "http://somewhere/alejandroTellez";
        // create an empty model
        Model model = ModelFactory.createDefaultModel();
        Resource alejandro = model.createResource(personURI)
                .addProperty(FOAF.name, "Alejandro Tellez");
        alejandro.addProperty(FOAF.homepage, "http://www.facebook.com/java.util.fck");
        
        writeRdfStatements(model, System.out);
    }
    
     private static void writeRdfStatements(Model model, OutputStream output) {
        model.write(output);
    }
}
