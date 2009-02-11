package de.tudresden.inf.tcs.oclib;

import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLIndividual;

import de.tudresden.inf.tcs.fcaapi.Expert;
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


public interface DLExpert extends Expert<OWLClass,OWLIndividual,IndividualObject> {

	/**
	 * Requests a counterexample from the expert. Called in the case where accepting an implication
	 * would make the ontology inconsistent. In this case we do not ask the expert whether the
	 * implication holds, but tell him that accepting this implication would make the ontology
	 * inconsistent and request a counterexample directly using this method. Note that this can
	 * only occur while exploring DL ontologies due to anonymous ABox individuals. In a usual 
	 * formal/partial context exploration, this case can not occur. That is is reason why we 
	 * extend the {@link de.tudresden.inf.tcs.fcaapi.Expert} interface.
	 */
	public void forceToCounterExample(FCAImplication<OWLClass> implication);
}
