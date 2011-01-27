package de.tudresden.inf.tcs.oclib.action;

import java.awt.event.ActionEvent;

import org.apache.log4j.Logger;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLOntologyChangeException;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

import de.tudresden.inf.tcs.fcaapi.FCAImplication;
import de.tudresden.inf.tcs.oclib.Constants;
import de.tudresden.inf.tcs.oclib.change.NewSubClassAxiomChange;


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


/**
 * The expert action for confirming a question.
 * @author Baris Sertkaya
 * Technische Universtaet Dresden
 * sertkaya@tcs.inf.tu-dresden.de
 */

public class QuestionConfirmedAction extends AbstractExpertAction {
// public class QuestionConfirmedAction extends 
// 	de.tudresden.inf.tcs.fcalib.action.QuestionConfirmedAction<OWLClass,OWLIndividual> {

	private static final long serialVersionUID = 1L;
	
	private FCAImplication<OWLClass> question;
	
	// private IndividualContext context;
	
	/**
	 * The logger.
	 */
	private static final Logger logger = Logger.getLogger(QuestionConfirmedAction.class);
	
	// public QuestionConfirmedAction(IndividualContext c, FCAImplication<OWLClass> q) {
	// 	super(c,q);
	// 	context = c;
	// 	// question = q;
	// }

	/**
	 * Returns the question that is confirmed.
	 * @return the confirmed question
	 */
	public FCAImplication<OWLClass> getQuestion() {
		return question;
	}
	
	public void setQuestion(FCAImplication<OWLClass> q) {
		question = q;
	}
	
	// @Override
	// public IndividualContext getContext() {
	// 	return context;
	// }
	
	/**
	 * Adds the confirmed question to the set of implications of the context, adds the corresponing
	 * axiom to the ontology, pushes the change to the history stack and continues exploration with 
	 * the next premise computed by using the new set of implications.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		logger.info("Expert accepted implication: " + getQuestion());
		// first create the GCI
		OWLSubClassOfAxiom axiom = getContext().getFactory().getOWLSubClassOfAxiom(
				getContext().toOWLDescription(getQuestion().getPremise()),
				getContext().toOWLDescription(getQuestion().getConclusion()));
		// create a new AddAxiom object
		AddAxiom addAxiom = new AddAxiom(getContext().getOntology(),axiom);
		// apply the change
		try {
			// add the new implication to the base
			getContext().getImplications().add(getQuestion());
			// also to the KB as a GCI
			getContext().getManager().applyChange(addAxiom);
			getContext().getHistory().push(new NewSubClassAxiomChange(getContext(),getQuestion(),addAxiom));
			getContext().reClassifyOntology();
			// update objects, update object descriptions
			getContext().updateObjects(Constants.AFTER_MODIFICATION);
			getContext().updateObjectDescriptions(Constants.AFTER_MODIFICATION);
			getContext().continueExploration(getContext().getNextPremise(getQuestion().getPremise()));
		}
		catch (OWLOntologyChangeException x) {
			x.printStackTrace();
			System.exit(-1);
		}
		
	}

}
