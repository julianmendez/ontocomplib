package de.tudresden.inf.tcs.oclib.action;

import java.awt.event.ActionEvent;

import org.apache.log4j.Logger;

import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.model.AddAxiom;
import org.semanticweb.owl.model.RemoveAxiom;
import org.semanticweb.owl.model.OWLClassAssertionAxiom;
import org.semanticweb.owl.model.OWLOntologyChangeException;

import de.tudresden.inf.tcs.fcaapi.FCAImplication;
import de.tudresden.inf.tcs.fcaapi.Expert;
// import de.tudresden.inf.tcs.oclib.action.AbstractExpertAction;
import de.tudresden.inf.tcs.oclib.IndividualContext;
import de.tudresden.inf.tcs.oclib.change.ClassAssertionChange;


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
 * The expert action fired when a counterexample is provided.
 * @author Baris Sertkaya
 * Technische Universtaet Dresden
 * sertkaya@tcs.inf.tu-dresden.de
 */

// public class CounterExampleProvidedAction extends AbstractExpertAction {
public class CounterExampleProvidedAction extends 
	de.tudresden.inf.tcs.fcalib.action.CounterExampleProvidedAction<OWLClass,OWLIndividual> {

	private static final long serialVersionUID = 1L;
	
	// private FCAImplication<OWLClass> question;
	
	private OWLClassAssertionAxiom axiom;
	
	private IndividualContext context;
	
	/**
	 * The logger.
	 */
	private static final Logger logger = Logger.getLogger(CounterExampleProvidedAction.class);
	
	public CounterExampleProvidedAction(IndividualContext c,FCAImplication<OWLClass> q,
			OWLClassAssertionAxiom ax) {
		super(c,q,null);
		// question = q;
		axiom = ax;
	}
	
	// /**
	//  * Returns the question rejected to which the counterexample in this action is provided.
	//  * @return the question rejected to which the counterexample in this action is provided.
	//  */
	// public FCAImplication<OWLClass> getQuestion() {
	// 	return question;
	// }
	
	/**
	 * Returns the class assertion axiom associated to this action.
	 * @return class assertion asserted as counterexample
	 */
	public OWLClassAssertionAxiom getAxiom() {
		return axiom;
	}
	
	/**
	 * Not applicable for IndividualContext and its experts. Returns <code>null</code>.
	 * @deprecated
	 * @return <code>null</code>
	 */
	@Override
	public OWLIndividual getCounterExample() {
		return null;
	}
	
	@Override
	public IndividualContext getContext() {
		return context;
	}
	
	/**
	 * Checks whether the counterexample is valid. If it is it asserts the counterexample to 
	 * ABox, reclassifies the ontology and continues the completion with the same premise. If it is not,
	 * then tells the expert why it is not valid and requests another counterexample.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		logger.info("Counterexample provided");
		AddAxiom  addAxiom = new AddAxiom(getContext().getOntology(),axiom);
		try {
			getContext().getManager().applyChange(addAxiom);
			getContext().reClassifyOntology();
			if (getContext().isOntologyConsistent() && getContext().refutes(getQuestion())) {
				// getContext().getHistory().push(new ClassAssertionChange(getContext(),addAxiom));
				getContext().continueExploration(getQuestion().getPremise());
			}
			else {
				RemoveAxiom removeAxiom = new RemoveAxiom(getContext().getOntology(), getAxiom());
				getContext().getManager().applyChange(removeAxiom);
				getContext().reClassifyOntology();
				getContext().getExpert().counterExampleInvalid(getCounterExample(), 
						Expert.COUNTEREXAMPLE_INVALID);
				getContext().getExpert().requestCounterExample(getQuestion());
			}
		}
		catch (OWLOntologyChangeException x) {
			x.printStackTrace();
			System.exit(-1);
		}
	}
}
