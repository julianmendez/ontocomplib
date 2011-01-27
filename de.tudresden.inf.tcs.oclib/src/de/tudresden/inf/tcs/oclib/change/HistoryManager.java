package de.tudresden.inf.tcs.oclib.change;

import java.util.ArrayList;
import java.util.Iterator;

import org.semanticweb.owl.model.OWLClass;

import de.tudresden.inf.tcs.fcaapi.change.ContextChange;
// import de.tudresden.inf.tcs.oclib.IndividualContext;

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



public class HistoryManager extends 
	// de.tudresden.inf.tcs.fcalib.change.HistoryManager<OWLClass,AbstractContextModification> {
	ArrayList<AbstractContextModification> {

	// private IndividualContext context;
	
	private static final long serialVersionUID = 1L;

	// public HistoryManager(IndividualContext c) {
	// 	context = c;
	// }
	
	public void push(AbstractContextModification change) {
		add(change);
	}
	
	// @Override
	public void undo(ContextChange<OWLClass> change) {
		change.undo();
		remove(change);
		// super.undo(change);
		// context.reClassifyOntology();
	}
	
	public void undo(int index) {
		get(index).undo();
		remove(index);
		// super.undo(index);
		// context.reClassifyOntology();
	}
	
	public void undoLast() {
		undo(size()-1);
		// super.undoLast();
		// context.reClassifyOntology();
	}
	
	public void undoLastN(int n) {
		for (int i = 0; i < n; ++i) {
			undoLast();
		}
		// super.undoLastN(n);
		// context.reClassifyOntology();
	}
	
	public void undoAll() {
		undoLastN(size());
		// super.undoAll();
		// context.reClassifyOntology();
	}
	
	public class HistoryIterator implements Iterator<AbstractContextModification> {
		int currentIndex = size() - 1;
		
		public boolean hasNext() {
			return currentIndex >= 0;
		}
		
		public AbstractContextModification next() {
			return get(currentIndex--);
		}
		
		public void remove() {
		}
	}
	
	/**
	 * Returns an iterator for this ListSet
	 */
	public Iterator<AbstractContextModification> iterator() {
		return new HistoryIterator();
	}

}
