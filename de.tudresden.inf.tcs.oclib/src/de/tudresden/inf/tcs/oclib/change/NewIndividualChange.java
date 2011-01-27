package de.tudresden.inf.tcs.oclib.change;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

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



public class NewIndividualChange extends AbstractContextModification {

	private OWLNamedIndividual object;
	private HashSet<OWLClass> attributes;
	
 	public NewIndividualChange(IndividualContext c, AddAxiom ch, OWLNamedIndividual o, Set<OWLClass> attrs) {
		super(c,ch);
		object = o;
		attributes = new HashSet<OWLClass>(attrs);
	}
	
 	public OWLNamedIndividual getObject() {
 		return object;
 	}
 	
 	public Set<OWLClass> getAttributes() {
 		return attributes;
 	}
 	
	public int getType() {
		return ContextChange.NEW_OBJECT_MODIFICATION;
	}
	
	// public boolean canUndo() {
	// 	return true;
	// }
}
