package de.tudresden.inf.tcs.oclib.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLIndividual;

import de.tudresden.inf.tcs.fcaapi.action.ExpertAction;
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


/**
 * An abstract implementation of actions that the expert can perform.
 * @author Baris Sertkaya
 * Technische Universtaet Dresden
 * sertkaya@tcs.inf.tu-dresden.de
 */

public abstract class AbstractExpertAction extends AbstractAction implements 
	ExpertAction<OWLClass,OWLIndividual> {

	private static final long serialVersionUID = 1L;

	private IndividualContext context;
	
	// public AbstractExpertAction(IndividualContext c) {
	// 	context = c;
	// }
	
	public void setContext(IndividualContext c) {
		context = c;
	}
	
	public abstract void actionPerformed(ActionEvent e);
	
	public IndividualContext getContext() {
		return context;
	}

}
