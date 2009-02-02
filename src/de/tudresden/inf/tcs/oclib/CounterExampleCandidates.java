package de.tudresden.inf.tcs.oclib;

import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;

import org.semanticweb.owl.model.OWLDataFactory;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.inference.OWLReasoner;
import org.semanticweb.owl.inference.OWLReasonerException;

import org.apache.log4j.Logger;

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


public class CounterExampleCandidates extends ArrayList<OWLIndividual> {

	private static final long serialVersionUID = 1L;
	/**
	 * The logger.
	 */
	private static final Logger logger = Logger.getLogger(CounterExampleCandidates.class);
	
	
	private OWLReasoner reasoner;
	private OWLDataFactory factory;
	private OWLDescription counterExampleDescription;
	private FCAImplication<OWLClass> question;
	
	// public CounterExampleCandidates(FCAImplication<OWLClass> question, OWLReasoner r, OWLDataFactory f) {
	public CounterExampleCandidates(IndividualContext context) {
		reasoner = context.getReasoner();
		factory = context.getFactory();
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
	
	public void setQuestion(FCAImplication<OWLClass> q) {
		question = q;
		OWLDescription desc1 = factory.getOWLObjectIntersectionOf(question.getConclusion());
		Set<OWLDescription> tmp = new HashSet<OWLDescription>();
		tmp.add(desc1);
		for (OWLClass cls : question.getPremise()) {
			tmp.add(factory.getOWLObjectComplementOf(cls));
		}
		counterExampleDescription = factory.getOWLObjectUnionOf(tmp);
	}
	
	public FCAImplication<OWLClass> getQuestion() {
		return question;
	}
	
	public void update() {
		clear();
		try {
			for (OWLIndividual individual : reasoner.getIndividuals(factory.getOWLThing(), false)) {
				if (!reasoner.hasType(individual, counterExampleDescription,false)) {
					add(individual);
				}
			}
		}
		catch (OWLReasonerException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		logger.debug("Number of counterexample candidates: " + size());
	}
	
	public OWLIndividual getCandidateAtIndex(int index) {
		return get(index);
	}
	
}
