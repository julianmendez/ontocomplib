package de.tudresden.inf.tcs.oclib.action;

import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;

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


public class UndoCounterExampleChangeAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	
	private IndividualContext context;
	
	public UndoCounterExampleChangeAction(IndividualContext c) {
		context = c;
	}
	
	public void actionPerformed(ActionEvent e) {
		System.out.println("undo action");
		context.getHistory().undoLast();
	}
}
