package de.tudresden.inf.tcs.oclib.change;

import org.semanticweb.owl.model.OWLClass;

import de.tudresden.inf.tcs.fcaapi.change.ContextChange;
import de.tudresden.inf.tcs.fcaapi.FCAImplication;
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


public class AutomaticallyAcceptedSubClassAxiomChange extends
		AbstractContextModification {

	private FCAImplication<OWLClass> implication;
	
	public AutomaticallyAcceptedSubClassAxiomChange(IndividualContext c, FCAImplication<OWLClass> i) {
		super(c,null);
		implication = i;
	}
	
	public FCAImplication<OWLClass> getImplication() {
		return implication;
	}
	
	// @Override
	// public boolean canUndo() {
	// 	return false;
	// }

	public int getType() {
		return ContextChange.AUTOMATICALLY_ACCEPTED_IMPLICATION;
	}

}
