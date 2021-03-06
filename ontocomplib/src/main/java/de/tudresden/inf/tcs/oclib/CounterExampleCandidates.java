package de.tudresden.inf.tcs.oclib;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;

import de.tudresden.inf.tcs.fcaapi.FCAImplication;


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


public class CounterExampleCandidates extends ArrayList<IndividualObject> {

	private static final long serialVersionUID = 1L;
	/**
	 * The logger.
	 */
	private static final Logger logger = Logger.getLogger(CounterExampleCandidates.class);
	
	
	// private OWLReasoner reasoner;
	private OWLDataFactory factory;
	// private OWLDescription counterExampleDescription;
	private FCAImplication<OWLClass> question;
	private IndividualContext context;
	
	// public CounterExampleCandidates(FCAImplication<OWLClass> question, OWLReasoner r, OWLDataFactory f) {
	public CounterExampleCandidates(IndividualContext c) {
		context = c;
		// reasoner = getContext().getReasoner();
		factory = getContext().getFactory();
		// OWLDescription desc1 = factory.getOWLObjectIntersectionOf(question.getConclusion());
		// Set<OWLDescription> tmp = new HashSet<OWLDescription>();
		// tmp.add(desc1);
		// for (OWLClass cls : question.getPremise()) {
		// 	tmp.add(factory.getOWLObjectComplementOf(cls));
		// }
		// try {
		// 	counterExampleDescription = factory.getOWLObjectUnionOf(tmp);
		// 	for (OWLIndividual individual : reasoner.getIndividuals(factory.getOWLThing(), false)) {
		// 		if (!reasoner.hasType(individual, counterExampleDescription,false)) {
		// 			add(individual);
		// 		}
		// 	}
		// }
		// catch (OWLReasonerException e) {
		// 	e.printStackTrace();
		// 	System.exit(-1);
		// }
		// logger.debug("Number of counterexample candidates: " + size());
	}
	
	public IndividualContext getContext() {
		return context;
	}
	
	public void setQuestion(FCAImplication<OWLClass> q) {
		question = q;
		OWLClassExpression desc1 = factory.getOWLObjectIntersectionOf(question.getConclusion());
		Set<OWLClassExpression> tmp = new HashSet<OWLClassExpression>();
		tmp.add(desc1);
		for (OWLClass cls : question.getPremise()) {
			tmp.add(factory.getOWLObjectComplementOf(cls));
		}
		// counterExampleDescription = factory.getOWLObjectUnionOf(tmp);
	}
	
	public FCAImplication<OWLClass> getQuestion() {
		return question;
	}
	
	// public void update() {
	// 	for (IndividualObject ind : this) {
	// 		if (ind.getDescription().getAttributes().containsAll(getQuestion().getConclusion())) {
	// 			// then ind is not a counterexample candidate
	// 			remove(ind);
	// 		}
	// 		else {
	// 			boolean removed = false;
	// 			for (OWLClass attr : getQuestion().getPremise()) {
	// 				if (ind.getDescription().getNegatedAttributes().contains(attr)) {
	// 					remove(ind);
	// 					removed = true;
	// 					break;
	// 				}
	// 			}
	// 			if (!removed) {
	// 				ind.updateDescription();
	// 			}
	// 		}
	// 	}
	// 	try {
	// 		for (OWLIndividual individual : reasoner.getIndividuals(factory.getOWLThing(), false)) {
	// 			if (!reasoner.hasType(individual, counterExampleDescription,false)) {
	// 				if (!containsOWLIndividual(individual)) {
	// 					IndividualObject obj = new IndividualObject(individual,getContext());
	// 					obj.updateDescription();
	// 					add(obj);
	// 				}
	// 			}
	// 		}
	// 	}
	// 	catch (OWLReasonerException e) {
	// 		e.printStackTrace();
	// 		System.exit(-1);
	// 	}
	// 	logger.debug("Number of counterexample candidates: " + size());
	// }
	
	public boolean containsOWLIndividual(OWLIndividual ind) {
		for (IndividualObject obj : this) {
			if (obj.getIdentifier().equals(ind)) {
				return true;
			}
		}
		return false;
	}
	
	// public void updateObjectDescriptions() {
	// 	for (IndividualObject obj : this) {
	// 		obj.updateDescription();
	// 	}
	// }
	
	public void update() {
		clear();
		search:
			for (IndividualObject indObj : getContext().getObjects()) {
				if (indObj.getDescription().getAttributes().containsAll(getQuestion().getConclusion())) {
					continue search;
				}
				for (OWLClass attr : getQuestion().getPremise()) {
					if (indObj.getDescription().getNegatedAttributes().contains(attr)) {
						continue search;
					}
				}
				add(indObj);
			}
		logger.debug("Number of counterexample candidates: " + size());
	}
	// public void update() {
	// 	clear();
	// 	try {
	// 		for (OWLIndividual individual : reasoner.getIndividuals(factory.getOWLThing(), false)) {
	// 			if (!reasoner.hasType(individual, counterExampleDescription,false)) {
	// 				add(individual);
	// 			}
	// 		}
	// 	}
	// 	catch (OWLReasonerException e) {
	// 		e.printStackTrace();
	// 		System.exit(-1);
	// 	}
	// 	logger.debug("Number of counterexample candidates: " + size());
	// }
	
	public IndividualObject getCandidateAtIndex(int index) {
		return get(index);
	}
	
}
