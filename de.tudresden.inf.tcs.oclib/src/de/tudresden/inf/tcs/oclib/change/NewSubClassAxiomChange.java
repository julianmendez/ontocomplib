package de.tudresden.inf.tcs.oclib.change;

import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.AddAxiom;
// import org.semanticweb.owl.model.RemoveAxiom;
// import org.semanticweb.owl.model.OWLOntologyChangeException;

import de.tudresden.inf.tcs.oclib.IndividualContext;

import de.tudresden.inf.tcs.fcaapi.FCAImplication;
import de.tudresden.inf.tcs.fcaapi.change.ContextChange;


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


public class NewSubClassAxiomChange extends AbstractContextModification {

	private FCAImplication<OWLClass> implication;
	
	public NewSubClassAxiomChange(IndividualContext c, FCAImplication<OWLClass> i,
			AddAxiom ch) {
		super(c,ch);
		implication = i;
	}
	
	// public void undo() {
	// 	getContext().getImplications().remove(implication);
	// 	RemoveAxiom removeAxiom = new RemoveAxiom(getContext().getOntology(),
	// 			getChange().getAxiom());
	// 	// apply the change
	// 	try {
	// 		getContext().getManager().applyChange(removeAxiom);
	// 		// context.reClassifyOntology();
	// 	}
	// 	catch (OWLOntologyChangeException e) {
	// 		e.printStackTrace();
	// 		System.exit(-1);
	// 	}
	// }
	
	public FCAImplication<OWLClass> getImplication() {
		return implication;
	}
	
	public int getType() {
		return ContextChange.NEW_IMPLICATION_MODIFICATION;
	}
	
	// public boolean canUndo() {
	// 	return true;
	// }
	
	@Override
	public void undo() {
		super.undo();
		getContext().getImplications().remove(getImplication());
	}
}
