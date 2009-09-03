package de.tudresden.inf.tcs.oclib.change;

import org.semanticweb.owl.model.AddAxiom;
import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.model.OWLClass;
// import org.semanticweb.owl.model.RemoveAxiom;
// import org.semanticweb.owl.model.OWLOntologyChangeException;

import de.tudresden.inf.tcs.fcaapi.change.ContextChange;
import de.tudresden.inf.tcs.oclib.IndividualContext;
import de.tudresden.inf.tcs.oclib.change.AbstractContextModification;


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


public class ClassAssertionChange extends AbstractContextModification {

	private OWLIndividual object;
	private OWLClass attribute;
	private boolean complemented;
	public ClassAssertionChange(IndividualContext c, AddAxiom ch, OWLIndividual o, OWLClass a,
			boolean complement) {
	// public ClassAssertionChange(IndividualContext c, AddAxiom ch) {
		super(c,ch);
		object = o;
		attribute = a;
		complemented = complement;
	}
	
	public OWLIndividual getObject() {
		return object;
	}
	
	public OWLClass getAttribute() {
		return attribute;
	}
	
	public boolean isComplemented() {
		return complemented;
	}
	
	public int getType() {
		return ContextChange.OBJECT_HAS_ATTRIBUTE_MODIFICATION;
	}
	
	public boolean canUndo() {
		return true;
	}
	
	// public OWLIndividual getIndividual() {
	// 	return getChange().getAxiom().get
	// }
	
	// public void undo() {
	// 	RemoveAxiom removeAxiom = new RemoveAxiom(getContext().getOntology(),
	// 			getChange().getAxiom());
	// 	try {
	// 		getContext().getManager().applyChange(removeAxiom);
	// 		// context.reClassifyOntology();
	// 	}
	// 	catch (OWLOntologyChangeException e) {
	// 		e.printStackTrace();
	// 		System.exit(-1);
	// 	}
	// }
}
