package org.j4g;

import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDFS;

/**
 *
 * @author Alejandro_Tellez
 */
public class SimpleReasonerTutorial {

    public static final String NS = "urn:x-hp-jena:eg/";
    
    public static void main(String args[]) {
        

        // Build a trivial example data set
        Model rdfsExampleModel = ModelFactory.createDefaultModel();

        //Create properties p and q
        Property p = rdfsExampleModel.createProperty(NS, "p");
        Property q = rdfsExampleModel.createProperty(NS, "q");

        //Specify that p is a subproperty of q
        rdfsExampleModel.add(p, RDFS.subPropertyOf, q);

        //Create a new instance of p, with value foo, call it "NS + a"
        rdfsExampleModel.createResource(NS + "a").addProperty(p, "foo");

        //Create inference model with current model
        InfModel inf = ModelFactory.createRDFSModel(rdfsExampleModel);  // [1]


        /*
        //[1] Could also be  written manually like this:
        Reasoner reasoner = ReasonerRegistry.getRDFSReasoner();
        InfModel inf = ModelFactory.createInfModel(reasoner, rdfsExample);
         */
 /*
        //Or even more manually like
        Reasoner reasoner = RDFSRuleReasonerFactory.theInstance().create(null);
        InfModel inf = ModelFactory.createInfModel(reasoner, rdfsExample);
         */
        //get a reference to Resource named "NS + a"
        Resource a = inf.getResource(NS + "a");

        //Print new inferred property q of Resource a. Value should be "foo"
        System.out.println("Statement: " + a.getProperty(q));
    }
}
