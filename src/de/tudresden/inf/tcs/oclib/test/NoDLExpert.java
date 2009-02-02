/**
 * An expert implementation for a partial context that rejects every question and
 * provides a default counterexample.
 * @author Baris Sertkaya
 */
package de.tudresden.inf.tcs.oclib.test;

import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.net.URI;

import org.apache.log4j.Logger;

import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.model.OWLClassAssertionAxiom;
import org.semanticweb.owl.model.OWLDataFactory;

import de.tudresden.inf.tcs.fcaapi.Expert;
import de.tudresden.inf.tcs.fcaapi.action.ExpertAction;
import de.tudresden.inf.tcs.fcaapi.FCAImplication;
import de.tudresden.inf.tcs.fcalib.AbstractExpert;
import de.tudresden.inf.tcs.fcalib.action.QuestionRejectedAction;
import de.tudresden.inf.tcs.oclib.IndividualContext;
import de.tudresden.inf.tcs.oclib.action.CounterExampleProvidedAction;


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


public class NoDLExpert extends AbstractExpert<OWLClass,OWLIndividual> {
	
	/**
	 * To be used as name for default counterexamples.
	 */
	private int name = 0;
	
	/**
	 * The URI of the ontology that is being completed.
	 */
	private URI ontologyURI;
	
	
	private OWLDataFactory factory;
	
	private IndividualContext context;
	
	/**
	 * The logger.
	 */
	private static final Logger logger = Logger.getLogger(NoDLExpert.class);
	
	/**
	 * Creates an instance of NoExpertPartial.
	 *
	 */
	public NoDLExpert(IndividualContext c, URI ontology, OWLDataFactory f) {
		super();
		context = c;
		ontologyURI = ontology;
		factory = f;
	}
	
	public synchronized void requestCounterExample(FCAImplication<OWLClass> question) {
		URI counterExampleURI = URI.create(ontologyURI + "#" + name);
		++name;
		OWLIndividual counterExample = factory.getOWLIndividual(counterExampleURI); 
		// description of counterExample
		// add the first attribute in the conclusion of the question as a negated attribute to the
		Set<OWLDescription> description = new HashSet<OWLDescription>(question.getPremise());
		Iterator<OWLClass> it = question.getConclusion().iterator();
		description.add(factory.getOWLObjectComplementOf(it.next()));
		OWLClassAssertionAxiom ax = factory.getOWLClassAssertionAxiom(counterExample,
				factory.getOWLObjectIntersectionOf(description));
		// counterExampleDescription = new PartialObjectDescription<OWLClass>(question.getPremise());
		// counterExampleDescription.addNegatedAttribute(it.next());
		// ExpertAction<OWLClass,OWLIndividual> action = 
		// 	new ExpertAction<OWLClass,OWLIndividual>(this,Expert.PROVIDED_COUNTEREXAMPLE, question,
		// 			counterExample);
		CounterExampleProvidedAction action = new CounterExampleProvidedAction(context,question,ax);
		fireExpertAction(action);
	}
	
	/**
	 * Rejects every given question, i.e., fires action 
	 * {@link de.tudresden.inf.tcs.fcaapi.Expert#REJECTED_QUESTION}
	 * @param question the given question to be rejected
	 */
	@Override
	public synchronized void askQuestion(FCAImplication<OWLClass> question) {
		 QuestionRejectedAction<OWLClass,OWLIndividual> action = 
				new QuestionRejectedAction<OWLClass,OWLIndividual>();
		 action.setContext(context);
		 action.setQuestion(question);
		 fireExpertAction(action);
	}
	
	public void counterExampleInvalid(OWLIndividual counterExample, int reason) {
		switch (reason) {
		case Expert.COUNTEREXAMPLE_EXISTS:
			logger.error("An object with the same name already exists\n");
			break;
		case Expert.COUNTEREXAMPLE_INVALID:
			logger.error("The object is not a valid counter example\n");
			break;
		}
	}
	
	public void explorationFinished() {
		logger.info("=== End of exploration ===");
	}
	
}
