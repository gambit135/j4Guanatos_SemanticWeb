/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.j4g;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.reasoner.Derivation;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.rulesys.GenericRuleReasoner;
import org.apache.jena.reasoner.rulesys.Rule;
import org.apache.jena.util.FileManager;

/**
 *
 * @author Alejandro_Tellez
 */
public class SimpleInference {

    public static final String NS = "urn:x-hp-jena:eg/";
    public static final String RULES_DATA_FILE_PATH = "src/main/resources/inferencesRules1.ttl";

    public static void main(String args[]) {

        Model data = FileManager.get().loadModel(RULES_DATA_FILE_PATH);

        String rules = "[rule1: (?a eg:p ?b) (?b eg:p ?c) -> (?a eg:p ?c)]";

        Reasoner reasoner = new GenericRuleReasoner(Rule.parseRules(rules));
        reasoner.setDerivationLogging(true);

        InfModel inf = ModelFactory.createInfModel(reasoner, data);

        printStatementDerivations(inf, new PrintWriter(System.out));

    }

    public static void printStatementDerivations(InfModel infModel, PrintWriter out) {
        StmtIterator statementIterator = infModel.listStatements();

        Stream<Statement> statementsStream = StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(statementIterator, Spliterator.ORDERED),
                false);
        statementsStream
                .forEach(statement -> {
                    System.out.println("");
                    System.out.println("Statement is: " + statement);
                    Iterator derivations = infModel.getDerivation(statement);
                    Stream<Derivation> derivationsStream = StreamSupport.stream(
                            Spliterators.spliteratorUnknownSize(derivations, Spliterator.ORDERED),
                            false);
                    derivationsStream.forEach(derivation -> {
                        derivation.printTrace(out, true);
                    });
                    System.out.println("");
                });
        out.flush();
    }
}
