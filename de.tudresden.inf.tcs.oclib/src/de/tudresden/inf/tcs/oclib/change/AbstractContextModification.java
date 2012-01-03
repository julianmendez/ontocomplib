package de.tudresden.inf.tcs.oclib.change;

import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLOntologyChangeException;
import org.semanticweb.owlapi.model.RemoveAxiom;

import de.tudresden.inf.tcs.fcaapi.change.ContextChange;
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


public abstract class AbstractContextModification implements ContextChange<OWLClass> {

	private IndividualContext context;
	private AddAxiom change;
	
	public AbstractContextModification(IndividualContext c, AddAxiom ch) {
		context = c;
		change = ch;
	}
	
	public IndividualContext getContext() {
		return context;
	}
	
	public AddAxiom getChange() {
		return change;
	}
	
	// public abstract boolean canUndo();
	
	public void undo() {
		RemoveAxiom removeAxiom = new RemoveAxiom(getContext().getOntology(),
				getChange().getAxiom());
		try {
			getContext().getManager().applyChange(removeAxiom);
			// context.reClassifyOntology();
		}
		catch (OWLOntologyChangeException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
}
