package de.tudresden.inf.tcs.oclib.test;

import java.net.URI;
import java.util.Set;
// import java.util.HashSet;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.apache.log4j.Logger;

import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLDataFactory;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.model.OWLOntologyCreationException;
import org.semanticweb.owl.model.OWLOntologyManager;
import org.semanticweb.owl.inference.OWLReasoner;
import org.semanticweb.owl.apibinding.OWLManager;

import de.tudresden.inf.tcs.fcaapi.exception.IllegalExpertException;
import de.tudresden.inf.tcs.fcaapi.exception.IllegalObjectException;
import de.tudresden.inf.tcs.fcaapi.exception.IllegalContextException;
import de.tudresden.inf.tcs.fcalib.action.StartExplorationAction;
import de.tudresden.inf.tcs.oclib.IndividualContext;


/*
 * OClib: An Ontology Completion Library
 * Copyright (C) 2009  Baris Sertkaya
 *
 * This file is part of OClib.
 * OClib is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OClib is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OClib.  If not, see <http://www.gnu.org/licenses/>.
 */


public class TestDLContext {

	/**
	 * The logger.
	 */
	private static final Logger logger = Logger.getLogger(TestDLContext.class);

	final String ONTOLOGY_PATH = "file:/home/aat/sertkaya/workspace/OCP/ontologies/myCountriesNotCompleted.owl";
	
	public TestDLContext() throws IllegalObjectException, IllegalExpertException, 
		IllegalContextException, OWLOntologyCreationException, Exception {
		
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLDataFactory factory = manager.getOWLDataFactory();
		
		// the test ontology
		URI ontologyPhysicalURI = URI.create(ONTOLOGY_PATH);
		OWLOntology ontology = manager.loadOntologyFromPhysicalURI(ontologyPhysicalURI);
		URI ontologyURI = ontology.getURI();
		OWLReasoner reasoner = createReasoner(manager);
		Set<OWLOntology> ontologies = manager.getImportsClosure(ontology);
		reasoner.loadOntologies(ontologies);
		reasoner.classify();
		System.out.println("ontology URI:" + ontology.getURI());
	
		IndividualContext context = new IndividualContext(manager,reasoner,ontology);
		NoDLExpert expert = new NoDLExpert(context,ontologyURI,factory);
		context.setExpert(expert);
		expert.addExpertActionListener(context);
		
		URI clsURI = URI.create(ontologyURI + "#country");
		OWLClass clsCountry = factory.getOWLClass(clsURI);
		context.addAttribute(clsCountry);
		// System.out.println(cl.getURI());
		
		clsURI = URI.create(ontologyURI + "#MediterraneanCountry");
		OWLClass cl = factory.getOWLClass(clsURI);
		context.addAttribute(cl);
		// System.out.println(cl.getURI());
		
		clsURI = URI.create(ontologyURI + "#EuropeanCountry");
		OWLClass clsEuropeanCountry = factory.getOWLClass(clsURI);
		context.addAttribute(clsEuropeanCountry);
		// System.out.println(cl.getURI());
		
		clsURI = URI.create(ontologyURI + "#EUmember");
		OWLClass clsEUmember = factory.getOWLClass(clsURI);
		context.addAttribute(clsEUmember);
		// System.out.println(cl.getURI());
		
		clsURI = URI.create(ontologyURI + "#AsianCountry");
		OWLClass clsAsianCountry = factory.getOWLClass(clsURI);
		context.addAttribute(clsAsianCountry);
		// System.out.println(cl.getURI());
		
		// Set<OWLClass> p = new HashSet<OWLClass>();
		// Set<OWLClass> c = new HashSet<OWLClass>();
		// p.add(clsEuropeanCountry);
		// c.add(clsCountry);
		// FCAImplication<OWLClass> imp = new Implication<OWLClass>(p,c);
		// System.out.println("Implication:"+imp);
		// System.out.println("country:"+clsCountry.getURI());
		// System.out.println("EuropeanCountry:"+clsEuropeanCountry.getURI());
		// if (context.followsFromBackgroundKnowledge(imp)) {
		// 	System.out.println("yes");
		// }
		// else {
		// 	System.out.println("no");
		// }
		// if (reasoner.isSubClassOf(clsEuropeanCountry, clsCountry)) {
		// 	System.out.println("yes");
		// }
		// else {
		// 	System.out.println("no");
		// }
		
		// for (OWLClass cls : ontology.getReferencedClasses()) {
		// 	System.out.println(cls.getURI());
		// }
		
		System.out.println("attrs:" + context.getAttributes());
		logger.info("Attributes: " + context.getAttributes());
		logger.info("Objects: ");
		System.out.println("objs: ");
		for (OWLIndividual object : context.getObjects()) {
			logger.info(object.getURI().getFragment() + " ");
			System.out.println(object.getURI().getFragment() + " ");
		}
		System.out.println();
		System.out.println("asd");
		
		StartExplorationAction<OWLClass,OWLIndividual> action = 
			new StartExplorationAction<OWLClass,OWLIndividual>();
		action.setContext(context);
		expert.fireExpertAction(action);
		System.out.println("asd");
		logger.info("Attributes: " + context.getAttributes());
		logger.info("Objects: ");
		System.out.println("objs: ");
		for (OWLIndividual object : context.getObjects()) {
			logger.info(object.getURI().getFragment() + " ");
			System.out.println(object.getURI().getFragment() + " ");
		}
	}
	
	private static OWLReasoner createReasoner(OWLOntologyManager man) {
		try {
			String reasonerClassName = "org.mindswap.pellet.owlapi.Reasoner";
			Class reasonerClass = Class.forName(reasonerClassName);
			Constructor<OWLReasoner> con = reasonerClass.getConstructor(OWLOntologyManager.class);
			return con.newInstance(man);
		}
		catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
		catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
		catch (InstantiationException e) {
			throw new RuntimeException(e);
		}
	}
		   
	public static void main(String[] args) {
		// PropertyConfigurator.configure("/home/aat/sertkaya/workspace/OClib/log4j.xml");
		try {
			new TestDLContext();
		}
		 catch (Exception e) {
				e.printStackTrace();
				System.exit(0);
		 }
	}
}
