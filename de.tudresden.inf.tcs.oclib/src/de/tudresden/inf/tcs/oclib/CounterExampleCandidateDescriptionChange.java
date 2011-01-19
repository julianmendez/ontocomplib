package de.tudresden.inf.tcs.oclib;

// import java.util.Set;

// import org.semanticweb.owl.model.OWLOntologyManager;
// import org.semanticweb.owl.model.OWLDataFactory;
// import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.model.OWLClassAssertionAxiom;
import org.semanticweb.owl.model.RemoveAxiom;
import org.semanticweb.owl.model.OWLOntologyChangeException;
// import org.semanticweb.owl.inference.OWLReasoner;
import org.semanticweb.owl.inference.OWLReasonerException;


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


public class CounterExampleCandidateDescriptionChange {

	// private OWLOntologyManager manager;
	// private OWLReasoner reasoner;
	// private OWLDataFactory factory;
	// private OWLOntology ontology;
	private OWLIndividual candidate;
	private OWLClass changedType;
	// private Set<OWLOntology> ontologies = null;
	private boolean isTypePlus;
	private IndividualContext theContext;
	
	public CounterExampleCandidateDescriptionChange(OWLIndividual candidate, OWLClass cls, boolean plus,
			IndividualContext context) {
		// manager = context.getManager();
		// reasoner = context.getReasoner();
		// factory = context.getFactory();
		// ontology = context.getOntology();
		// ontologies = manager.getImports(ontology);
		theContext = context;
		isTypePlus = plus;
		changedType = cls;
	}
	
	public void undo() {
		OWLClassAssertionAxiom axiom = null;
		if (isTypePlus) {
			axiom = theContext.getFactory().getOWLClassAssertionAxiom(candidate, changedType);
		}
		else {
			axiom = theContext.getFactory().getOWLClassAssertionAxiom(candidate, 
					theContext.getFactory().getOWLObjectComplementOf(changedType));
		}
		RemoveAxiom  removeAxiom = new RemoveAxiom(theContext.getOntology(),axiom); 
		try {
			theContext.getManager().applyChange(removeAxiom);
			theContext.getReasoner().unloadOntologies(theContext.getManager().getImports(theContext.getOntology()));
			theContext.getReasoner().loadOntologies(theContext.getManager().getImports(theContext.getOntology()));
		}
		catch (OWLOntologyChangeException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		catch (OWLReasonerException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
		
}
