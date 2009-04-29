package de.tudresden.inf.tcs.oclib;

import java.util.Set;
import java.util.HashSet;

import org.apache.log4j.Logger;

import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLOntologyManager;
import org.semanticweb.owl.model.OWLDataFactory;
import org.semanticweb.owl.model.OWLOntology;
// import org.semanticweb.owl.model.OWLObjectIntersectionOf;
import org.semanticweb.owl.model.OWLClassAssertionAxiom;
import org.semanticweb.owl.model.OWLSubClassAxiom;
import org.semanticweb.owl.model.AddAxiom;
import org.semanticweb.owl.model.RemoveAxiom;
import org.semanticweb.owl.model.OWLOntologyChangeException;
import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.inference.OWLReasonerException;
import org.semanticweb.owl.inference.OWLReasoner;
// import org.semanticweb.owl.inference.OWLReasonerAdapter;

// import de.tudresden.inf.tcs.fcalib.utils.ListSet;
// import de.tudresden.inf.tcs.fcaapi.utils.IndexedSet;
import de.tudresden.inf.tcs.fcaapi.FCAImplication;
import de.tudresden.inf.tcs.fcaapi.exception.IllegalAttributeException;
import de.tudresden.inf.tcs.fcaapi.exception.IllegalObjectException;
import de.tudresden.inf.tcs.fcalib.PartialContext;
import de.tudresden.inf.tcs.fcalib.Implication;
import de.tudresden.inf.tcs.oclib.change.HistoryManager;
import de.tudresden.inf.tcs.oclib.change.ClassAssertionChange;
import de.tudresden.inf.tcs.oclib.change.NewIndividualChange;
import de.tudresden.inf.tcs.oclib.CounterExampleCandidates;


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
 * Context implementation for DL ontology completion where attributes are of type OWLClass, and 
 * objects are of type  OWLIndividual.
 * @author Baris Sertkaya
 * Technischae Universitaet Dresden
 * sertkaya@tcs.inf.tu-dresden.de
 */

public class IndividualContext extends PartialContext<OWLClass,OWLIndividual,IndividualObject> {

	/**
	 * The ontology manager.
	 */
	private OWLOntologyManager manager;
	
	/**
	 * The reasoner.
	 */
	private OWLReasoner reasoner;

	/**
	 * The data factory.
	 */
	private OWLDataFactory factory;
	
	/**
	 * The ontology.
	 */
	private OWLOntology ontology;
	
	/**
	 * The set of ontologies
	 */
	private Set<OWLOntology> ontologies = null;
	
	// /**
	//  * The 'local' set of objects.
	//  */
	// private IndexedSet<OWLIndividual> objects;
	
	/**
	 * The history manager for this context
	 */
	private HistoryManager history;
	
	/**
	 * Counterexample candidates.
	 */
	private CounterExampleCandidates counterExampleCandidates;
	
	/**
	 * The logger.
	 */
	private static final Logger logger = Logger.getLogger(IndividualContext.class);
	
	/**
	 * Creates an individual context.
	 */
	public IndividualContext(OWLOntologyManager m, OWLReasoner r, OWLOntology o) {
		reasoner = r;
		manager = m;
		factory = manager.getOWLDataFactory();
		ontology = o;
		ontologies = manager.getImportsClosure(ontology);
		// objects = new ListSet<OWLIndividual>();
		// history= new HistoryManager(this);
		history= new HistoryManager();
		counterExampleCandidates = new CounterExampleCandidates(this);
	}

	/** Sets the reasoner of this context to the given reasoner.
	 * @param r the given reasoner
	 */
	public void setReasoner(OWLReasoner r) {
		reasoner = r;
	}
	
	/**
	 * Returns the reasoner.
	 * @return the reasoner
	 */
	public OWLReasoner getReasoner() {
		return reasoner;
	}
	
	/**
	 * Returns the data factory.
	 * @return the data factory
	 */
	public OWLDataFactory getFactory() {
		return factory;
	}
	
	/**
	 * Returns the ontology manager.
	 * @return the ontology manager
	 */
	public OWLOntologyManager getManager() {
		return manager;
	}
	
	/**
	 * Returns the ontology being completed
	 * @return the ontology being completed
	 */
	public OWLOntology getOntology() {
		return ontology;
	}
	
	/**
	 * Returns the completion history
	 * @return the history
	 */
	public HistoryManager getHistory() {
		return history;
	}
	
	/**
	 * Returns the list of counterexample candidates
	 * @return the list of counterexample candidates
	 */
	public CounterExampleCandidates getCounterExampleCandidates() {
		return counterExampleCandidates;
	}
	
	/** 
	 * Adds a given attribute to the attributes of this context, updates the 'local' set of objects.
	 * @param attribute the attribute to be added
	 * @return <code>true</code> if the <code>attribute</code> is successfully added
	 * @throws IllegalAttributeException if the given attribute is already in the set of 
	 * attributes
	 */
	@Override
	// public boolean addAttribute(OWLClass attribute) {
	// 	boolean added = getAttributes().add(attribute);
	// 	if (!added) {
	// 		throw new IllegalAttributeException("Attribute " + attribute + " has already been added");
	// 	}
	// 	// TODO
	// 	// this is not very efficient, but we need to update the set of 'local' objects
	// 	getObjects();
	// 	return added;
	// }
	public boolean addAttribute(OWLClass attribute) {
		boolean added = getAttributes().add(attribute);
		if (!added) {
			throw new IllegalAttributeException("Attribute " + attribute + " has already been added");
		}
		try {
			for (OWLIndividual individual : reasoner.getIndividuals(attribute, false)) {
				IndividualObject o = new IndividualObject(individual,this);
				o.getDescription().addAttribute(attribute);
				addObject(o);
			}
			for (OWLIndividual individual : reasoner.getIndividuals(factory.getOWLObjectComplementOf(attribute), false)) {
				IndividualObject o = new IndividualObject(individual,this);
				o.getDescription().addNegatedAttribute(attribute);
				addObject(o);
			}
			updateObjectDescriptions(Constants.AFTER_MODIFICATION);
		}
		catch (OWLReasonerException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		// catch (IllegalObjectException e) {
		// 	e.printStackTrace();
		// 	System.exit(-1);
		// }
		return added;
	}
	
	/**
	 * Adds a given individual to the ontology as an instance of <code>Thing</code>
	 * @param object the given object to be added
	 * @return <code>true</code> if the object is successfully added
	 */
	// @Override
	public boolean addIndividualToOntology(OWLIndividual object) {
		OWLClassAssertionAxiom axiom = factory.getOWLClassAssertionAxiom(object, factory.getOWLThing());
		AddAxiom  addAxiom = new AddAxiom(ontology,axiom); 
		Set<OWLClass> attrs = new HashSet<OWLClass>();
		try {
			manager.applyChange(addAxiom);
			reClassifyOntology();
			IndividualObject indObj = new IndividualObject(object,this);
			indObj.updateDescription(Constants.AFTER_MODIFICATION);
			addObject(indObj);
		}
		catch (OWLOntologyChangeException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		attrs.add(factory.getOWLThing());
		history.push(new NewIndividualChange(this,addAxiom,object,attrs));
		return true;
	}
	
	/**
	 * Adds a given individual to the ontology as an instance of the conjunction of the 
	 * given set of classes
	 * @param object the individual to be added
	 * @param attributes the set of classes
	 * @return <code>true</code> if the individual is successfully added
	 */
	public boolean addIndividualToOntology(OWLIndividual object, Set<OWLClass> attributes) {
		// OWLObjectIntersectionOf description = toOWLDescription(attributes);
		OWLDescription description = toOWLDescription(attributes);
		OWLClassAssertionAxiom axiom = factory.getOWLClassAssertionAxiom(object, description);
		AddAxiom  addAxiom = new AddAxiom(ontology,axiom); 
		try {
			manager.applyChange(addAxiom);
			reClassifyOntology();
			IndividualObject indObj = new IndividualObject(object,this);
			// for (OWLClass attribute : attributes) {
			// 	indObj.getDescription().addAttribute(attribute);
			// }
			indObj.getDescription().addAttributes(attributes);
			indObj.updateDescription(Constants.AFTER_MODIFICATION);
			addObject(indObj);
		}
		catch (OWLOntologyChangeException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		history.push(new NewIndividualChange(this,addAxiom,object,attributes));
		return true;
	}

	// /**
	//  * Gets the objects of this individual context.
	//  * @return the ABox individuals that are instances of some attribute of this context
	//  */
	// @Override
	// public IndexedSet<OWLIndividual> getObjects() {
	// 	try {
	// 		// TODO: Which method is better?
	// 		// objects.addAll(reasoner.getIndividuals(factory.getOWLObjectUnionOf(getAttributes()), false));
	// 		for (OWLClass attribute : getAttributes()) {
	// 			objects.addAll(reasoner.getIndividuals(attribute, false));
	// 		}
	// 		// TODO: Which method is better?
	// 		// objects.addAll(reasoner.getIndividuals(
	// 		// 		factory.getOWLObjectComplementOf(factory.getOWLObjectIntersectionOf(getAttributes())),
	// 		// 		false));
	// 		for (OWLClass attribute : getAttributes()) {
	// 			objects.addAll(reasoner.getIndividuals(
	// 					factory.getOWLObjectComplementOf(attribute), false));
	// 		}
	// 	}
	// 	catch (OWLReasonerException e) {
	// 		e.printStackTrace();
	// 		System.exit(-1);
	// 	}
	// 	return objects;
	// }
	
	// /**
	//  * TODO: This is might be time consuming for a big ontology
	//  */
	// public OWLIndividual getObjectAtIndex(int index) {
	// 	getObjects();
	// 	return objects.getElementAt(index);
	// }
	
	// /**
	//  * Returns the object whose name is given.
	//  * @param name the given name
	//  * @return the object with name <code>name</code>, <code>null</code> if such an object
	//  * does not exist
	//  * @deprecated
	//  */
	// public OWLIndividual getObject(String name) {
	// 	return null;
	// }
	
	// /**
	//  * Returns the number of objects of this context.
	//  * @return the number of objects of this context
	//  */
	// public int getObjectCount() {
	// 	// TODO: This is might be time consuming for a big ontology
	// 	getObjects();
	// 	return objects.size();
	// }
	
	// /**
	//  * Clears the 'local' set of objects
	//  */
	// public void clearObjects() {
	// 	objects.clear();
	// }
	
	public void updateObjectDescriptions(int updateType) {
		for (IndividualObject obj : getObjects()) {
			obj.updateDescription(updateType);
		}
	}
	
	public void updateObjects(int updateType) {
		switch (updateType) {
		case Constants.AFTER_MODIFICATION:
			try {
				for (OWLClass attribute : getAttributes()) {
					for (OWLIndividual ind : reasoner.getIndividuals(attribute, false)) {
						if (!containsObject(ind)) {
							IndividualObject indObj = new IndividualObject(ind,this);
							// indObj.updateDescription();
							indObj.getDescription().addAttribute(attribute);
							addObject(indObj);
						}
					}
					for (OWLIndividual ind : reasoner.getIndividuals(getFactory().getOWLObjectComplementOf(attribute), false)) {
						if (!containsObject(ind)) {
							IndividualObject indObj = new IndividualObject(ind,this);
							// indObj.updateDescription();
							indObj.getDescription().addNegatedAttribute(attribute);
							addObject(indObj);
						}
					}
				}
			}
			catch (OWLReasonerException e) {
				e.printStackTrace();
				System.exit(-1);
			}
			break;
		case Constants.AFTER_UNDO:
			try {
				Set<IndividualObject> toBeRemoved = new HashSet<IndividualObject>();
				for (IndividualObject object : getObjects()) {
					if (!getOntology().containsEntityReference(object.getIdentifier())) {
						toBeRemoved.add(object);
					}
				}
				for (IndividualObject object : toBeRemoved) {
					removeObject(object);
				}
			}
			catch (IllegalObjectException e) {
				e.printStackTrace();
				System.exit(-1);
			}
			break;
		}
	}
	
	// TODO
	/**
	 * Asserts that the given individual is an instance of the given type.
	 * @param type the given type
	 * @param ind the given individual about which the assertion will be made
	 * @return <code>true</code> if the assertion is successfull
	 */
	public boolean addAttributeToObject(OWLClass type,IndividualObject indObj) {
		OWLClassAssertionAxiom axiom = factory.getOWLClassAssertionAxiom(indObj.getIdentifier(), type);
		AddAxiom  addAxiom = new AddAxiom(ontology,axiom); 
		try {
			manager.applyChange(addAxiom);
			reClassifyOntology();
			updateObjects(Constants.AFTER_MODIFICATION);
			updateObjectDescriptions(Constants.AFTER_MODIFICATION);
		}
		catch (OWLOntologyChangeException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		history.push(new ClassAssertionChange(this,addAxiom,indObj.getIdentifier(),type,false));
		return true;
	}
	
	// TODO
	/**
	 * Asserts that the given individual is an instance of the complement of the given type.
	 * @param type the given type
	 * @param ind the given individual about which the assertion will be made
	 * @return <code>true</code> if the assertion is successfull
	 */
	public boolean addNegatedAttributeToObject(OWLClass type,IndividualObject indObj) {
		OWLClassAssertionAxiom axiom = factory.getOWLClassAssertionAxiom(indObj.getIdentifier(), factory.getOWLObjectComplementOf(type));
		AddAxiom  addAxiom = new AddAxiom(ontology,axiom); 
		try {
			manager.applyChange(addAxiom);
			reClassifyOntology();
			updateObjects(Constants.AFTER_MODIFICATION);
			updateObjectDescriptions(Constants.AFTER_MODIFICATION);
		}
		catch (OWLOntologyChangeException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		history.push(new ClassAssertionChange(this,addAxiom,indObj.getIdentifier(),type,true));
		return true;
	}
	
	// /**
	//  * Removes the object from the set of objects of this context whose name is given.
	//  * @param name name of the object to be removed
	//  * @return <code>true</code> if the object with name <code>name</code> is successfully removed, 
	//  * <code>false</code>  otherwise
	//  * @throws IllegalObjectException if an object with name <code>name</code> does not exist
	//  * @deprecated
	//  */
	// @Override
	// public boolean removeObject(String arg0) throws IllegalObjectException {
	// 	// TODO Auto-generated method stub
	// 	return false;
	// }

	// /**
	//  * Removes a given  object from the set of objects of this context.
	//  * @param object the object to be removed
	//  * @return <code>true</code> if the object <code>object</code> is successfully removed, 
	//  * <code>false</code>  otherwise
	//  * @throws IllegalObjectException if the object <code>object</code> does not exist
	//  * @deprecated
	//  */
	// @Override
	// public boolean removeObject(OWLIndividual arg0)
	// 		throws IllegalObjectException {
	// 	// TODO Auto-generated method stub
	// 	return false;
	// }

	// /**
	//  * Adds a given attribute to the attributes of the given object. 
	//  * @param attribute the attribute to be added
	//  * @param name name of the object to which <code>attribute</code> is to be added
	//  * @return <code>true</code> if of the <code>attribute</code> is successfully added, 
	//  * <code>false</code> otherwise
	//  * @throws IllegalAttributeException if the object with name <code>name</code> already has 
	//  * the attribute
	//  * @throws IllegalObjectException if an object with name <code>name</code> does not exist in 
	//  * this  context
	//  * @deprecated
	//  */
	// public boolean addAttributeToObject(OWLClass arg0, String name)
	// 		throws IllegalAttributeException, IllegalObjectException {
	// 	// TODO Auto-generated method stub
	// 	return false;
	// }
	

	/**
	 * Reclassifies the ontology.
	 */
	public void reClassifyOntology() {
		// TODO: Is there a more efficient way to do this?
		try {
			// Dmitry says for FaCT++ it is the safest to unload/load and still call classiyf()
			// this is probably slower, but to be on the safe side we do so
			reasoner.unloadOntologies(ontologies);
			reasoner.loadOntologies(ontologies);
			reasoner.classify();
		}
		catch (OWLReasonerException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	/**
	 * Checks whether the ontology is consistent.
	 * @return <code>true</code> if the ontology is consistent, <code>false</code> otherwise
	 */
	public boolean isOntologyConsistent() {
		boolean consistent = false;
		try {
			consistent = reasoner.isConsistent(ontology);
		}
		catch (OWLReasonerException e) {
			logger.error("Ontology became inconsistent");
		}
		return consistent;
	}
	
	/**
	 * Checks whether the given individual is an instance of the given class.
	 * @param ind the individual whose going to be tested
	 * @param cls the class the that is going to be checked for membership
	 * @return <code>true</code> if <code>ind</code> is an instance of <code>cls</code>, 
	 * <code>false</code> otherwise
	 */
	// public boolean objectHasAttribute(OWLIndividual ind, OWLClass cls) {
	// 	boolean hasType = false;
	// 	try {
	// 		logger.debug("Asking the reasoner whether " + ind.getURI().getFragment() + " has type " + cls.getURI().getFragment());
	// 		hasType = reasoner.hasType(ind, cls, false);
	// 	}
	// 	catch (OWLReasonerException e) {
	// 		e.printStackTrace();
	// 		System.exit(-1);
	// 	}
	// 	return hasType;
	// }
	
	// public boolean objectHasAttribute(IndividualObject obj, OWLClass cls) {
	// 	return obj.getDescription().containsAttribute(cls);
	// }
	
	// /**
	//  * Checks whether the given individual is an instance of the complement of the given class.
	//  * @param ind the individual whose going to be tested
	//  * @param cls the class the that is going to be checked for membership
	//  * @return <code>true</code> if <code>ind</code> is an instance of the complement of 
	//  * <code>cls</code>,  <code>false</code> otherwise
	//  */
	// public boolean objectHasNegatedAttribute(OWLIndividual ind, OWLClass cls) {
	// 	boolean hasType = false;
	// 	try {
	// 		hasType = reasoner.hasType(ind, factory.getOWLObjectComplementOf(cls), false);
	// 	}
	// 	catch (OWLReasonerException e) {
	// 		e.printStackTrace();
	// 		System.exit(-1);
	// 	}
	// 	return hasType;
	// }
	// public boolean objectHasNegatedAttribute(IndividualObject obj, OWLClass cls) {
	// 	return obj.getDescription().containsNegatedAttribute(cls);
	// }
	
	// /**
	//  * Returns the second derivative of a given set of attributes. Works on the ABox, not on
	//  * a local set of objects.
	//  * @param x the given set of attributes
	//  * @return set of attributes equivalent to the second derivative of <code>x</code>
	//  */
	// @Override
	// public Set<OWLClass> doublePrime(Set<OWLClass> x) {
	// 	Set<OWLClass> tmp = new HashSet<OWLClass>(getAttributes());
	// 	Set<OWLClass> types = null; // OWLReasonerAdapter.flattenSetOfSets(subClsSets);
	// 	try {
	// 		for (OWLIndividual individual : getObjects()) {
	// 			types = OWLReasonerAdapter.flattenSetOfSets(reasoner.getTypes(individual, false));
	// 			if (types.containsAll(x)) {
	// 				for (OWLClass attribute : getAttributes()) {
	// 					if (reasoner.hasType(individual, factory.getOWLObjectComplementOf(attribute), false)) {
	// 						tmp.remove(attribute);
	// 					}
	// 				}
	// 			}
	// 		}
	// 	}
	// 	catch (OWLReasonerException e) {
	// 		e.printStackTrace();
	// 		System.exit(-1);
	// 	}
	// 	return tmp;
	// }
	
	// /**
	//  * Checks whether the ontology refutes a given implication (subclass axiom)
	//  * @param question the implication to be checked for refutation
	//  * @return <code>true</code> if the ontology refutes <code>question</code>, <code>false</code> 
	//  * otherwise
	//  */
	// public boolean refutes(FCAImplication<OWLClass> question) {
	// 	try {
	// 		for (OWLIndividual ind : reasoner.getIndividuals(toOWLDescription(question.getPremise()),false)) {
	// 			for (OWLClass cls : question.getConclusion()) {
	// 				if (reasoner.hasType(ind, factory.getOWLObjectComplementOf(cls), false)) {
	// 					return true;
	// 				}
	// 			}
	// 		}
	// 	}
	// 	catch (OWLReasonerException e) {
	// 		e.printStackTrace();
	// 		System.exit(-1);
	// 	}
	// 	return false;
	// }
	
	// /**
	//  * Prepares a class of 'potential' counterexample to a given question.
	//  * @param question the question to which a class of counterexample candidates are going to be 
	//  * prepared
	//  * @return the class of counterexample candidates to the <code>question</code>
	//  */
	// public CounterExampleCandidates prepareCounterExampleCandidates(FCAImplication<OWLClass> question) {
	// 	return new CounterExampleCandidates(question,getReasoner(),getFactory());
	// }
	
	// /**
	//  * Checks whether a given object is a valid counterexample to a given implication. Is not used
	//  * for IndividualContext.
	//  * @param counterExample the object given 
	//  * @param implication the given implication 
	//  * @deprecated
	//  */
	// @Override
	// public boolean isCounterExampleValid(OWLIndividual counterExample, FCAImplication<OWLClass> implication) {
	// 	return refutes(implication);
	// }
	
	/**
	 * Given a set of class names, returns the description corresponding to the conjunction of them
	 * @param x the given set of class names
	 * @return the concept description that is the conjunction of the names in <code>x</code>
	 */
	// public OWLObjectIntersectionOf toOWLDescription(Set<OWLClass> x) {
	public OWLDescription toOWLDescription(Set<OWLClass> x) {
		if (x.isEmpty()) {
			return getFactory().getOWLThing();
		}
		return factory.getOWLObjectIntersectionOf(x);
	}
	
	/**
	 * Checks whether an implication (subclass axiom) already follows from the TBox.
	 * @param implication the implication to be checked
	 * @return  <code>true</code> if the axiom corresponding to <code>implication</code> follows from
	 * the TBox, <code>false</code> otherwise
	 */
	@Override
	protected boolean followsFromBackgroundKnowledge(FCAImplication<OWLClass> implication) {
		boolean retCode = false;
		try {
			retCode = reasoner.isSubClassOf(toOWLDescription(implication.getPremise()),
					toOWLDescription(implication.getConclusion()));
		}
		catch (OWLReasonerException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		return retCode;
	}
	
	// @Override
	// public void questionConfirmed(FCAImplication<OWLClass> question) {
	// 	// add the new implication to the base
	// 	implications.add(question);
	// 	// also to the KB as a GCI
	// 	// first create the GCI
	// 	OWLSubClassAxiom axiom = factory.getOWLSubClassAxiom(toOWLDescription(question.getPremise()),
	// 			toOWLDescription(question.getConclusion()));
	// 	// create a new AddAxiom object
	// 	AddAxiom addAxiom = new AddAxiom(ontology,axiom);
	// 	// apply the change
	// 	try {
	// 		manager.applyChange(addAxiom);
	// 		history.push(new NewSubClassAxiomChange(this,question,addAxiom));
	// 		reClassifyOntology();
	// 		premise = implications.nextClosure(premise);
	// 		continueExploration();
	// 	}
	// 	catch (OWLOntologyChangeException e) {
	// 		e.printStackTrace();
	// 		System.exit(-1);
	// 	}
	// }
	
	// /**
	//  * Performs the necessary operations when the expert rejects a question. For instance, ask the expert
	//  * for a counterexample.
	//  * @param question the question that the expert has rejected
	//  */
	// @Override
	// public synchronized void questionRejected(FCAImplication<OWLClass> question) {
	// 	logger.info("Expert rejected implication: " + question);
	// 	expert.requestCounterExample(question);
	// }
	
	// @Override
	// public synchronized void counterExampleProvided(OWLIndividual counterExample, FCAImplication<OWLClass> question) {
	// 	continueExploration();
	// }
	
	// @Override
	// public void counterExampleProvided(OWLIndividual counterExample, FCAImplication<OWLClass> question) {
	// 	// get the description of the counterexample from the expert
	// 	PartialObjectDescription<OWLClass> counterExampleDescription =  expert.getCounterExampleDescription();
	// 	logger.debug("Counterexample description: " + counterExampleDescription);
	// 	// check whether counterexample is valid
	// 	if (isCounterExampleValid(counterExampleDescription, question)) {
	// 		addObject(counterExample,counterExampleDescription);
	// 		// continueExploration(question.getPremise());
	// 		continueExploration();
	// 	}
	// 	else {
	// 		expert.counterExampleInvalid(counterExample, Expert.COUNTEREXAMPLE_INVALID);
	// 		// expert.getCounterExample(question);
	// 		expert.requestCounterExample(question);
	// 	}
	// }
	
	// @Override
	// protected void explorationFinished() {
	// 	// try {
	// 	// 	manager.saveOntology(ontology);
	// 	// }
	// 	// catch (OWLOntologyStorageException e) {
	// 	// 	e.printStackTrace();
	// 	// 	System.exit(-1);
	// 	// }
	// 	expert.explorationFinished();
	// }
	
	@Override
	public Set<FCAImplication<OWLClass>> getStemBase() {
		// TODO: 
		return null;
	}

	// @Override
	public Set<FCAImplication<OWLClass>> getDuquenneGuiguesBase() {
		// TODO: 
		return null;
	}
	
	private boolean implicationMakesOntologyInconsistent(FCAImplication<OWLClass> imp) {
		boolean retCode = false;
		// first create the GCI
		OWLSubClassAxiom axiom = getFactory().getOWLSubClassAxiom(
				toOWLDescription(imp.getPremise()),
				toOWLDescription(imp.getConclusion()));
		// create a new AddAxiom object
		AddAxiom addAxiom = new AddAxiom(getOntology(),axiom);
		RemoveAxiom removeAxiom = new RemoveAxiom(getOntology(),axiom);
		// apply the change
		try {
			getManager().applyChange(addAxiom);
			// reasoner.unloadOntologies(ontologies);
			// reasoner.loadOntologies(ontologies);
			// reClassifyOntology unloads/loads and classifies
			reClassifyOntology();
			if (reasoner.isConsistent(getOntology())) {
				retCode = false;
			}
			else {
				retCode = true;
			}
			getManager().applyChange(removeAxiom);
			// reasoner.unloadOntologies(ontologies);
			// reasoner.loadOntologies(ontologies);
			// reClassifyOntology unloads/loads and classifies
			reClassifyOntology();
		}
		catch (OWLOntologyChangeException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		catch (OWLReasonerException e) {
			e.printStackTrace();
			retCode = true;
			logger.debug("dnm1");
			// try {
			// 	getManager().applyChange(removeAxiom);
			// 	reasoner.unloadOntologies(ontologies);
			// 	reasoner.loadOntologies(ontologies);
			// }
			// catch (OWLOntologyChangeException x) {
			// 	x.printStackTrace();
			// 	System.exit(-1);
			// }
			// catch (OWLReasonerException x) {
			// 	x.printStackTrace();
			// 	logger.fatal("reasoner exception, exiting...");
			// 	System.exit(-1);
			// }
			
		}
		// logger.debug("dnm2");
		// // if (!retCode) {
		// 	try {
		// 		getManager().applyChange(removeAxiom);
		// 		reasoner.unloadOntologies(ontologies);
		// 		reasoner.loadOntologies(ontologies);
		// 	}
		// 	catch (OWLOntologyChangeException x) {
		// 		x.printStackTrace();
		// 		System.exit(-1);
		// 	}
		// 	catch (OWLReasonerException x) {
		// 		x.printStackTrace();
		// 		logger.fatal("reasoner exception, exiting...");
		// 		System.exit(-1);
		// 	}
		// 	
		// // }
		return retCode;
	}
	
	// /**
	//  * Sets the expert for this context to the given expert
	//  * @param e the given expert
	//  */
	// @Override
	// public void setExpert(DLExpert e) {
	// 	// logger.debug("Setting the expert " + e);
	// 	dlExpert = e;
	// }
	
	// /**
	//  * Returns the expert of this context.
	//  * @return the expert of this context
	//  */
	// @Override
	// public DLExpert getExpert() {
	// 	// logger.debug("Getting the expert");
	// 	return (DLExpert) expert;
	// }
	
	// /**
	//  * Checks whether the expert has already been set.
	//  * @return <code>true</code> if the <code>expert</code> has already been set,
	//  * <code>false</code> otherwise
	//  */
	// @Override
	// public boolean isExpertSet() {
	// 	return getExpert() != null;
	// }
	
	/**
	 * The main method that keeps the exploration going. Given a set of attributes, it computes
	 * a new left handside using the current implications of this context, computes a right handside
	 * by using the current objects of this context, first checks if the new implication already
	 * follows from some background knowledge. If yes, the implication is added to the current
	 * set of implications and the method calls itself with the next premise computed by using the
	 * new set of implications. If not, it first checks whether adding this implication to the TBox 
	 * makes the KB inconsistent, which can be the case due to anonymous ABox individuals. If this is
	 * not the case, the expert is requested to answer the implication question.
	 */
	@Override
	public void continueExploration(Set<OWLClass> premise) {
		Set<OWLClass> conclusion = null;
		FCAImplication<OWLClass> implication = null;
		
		logger.debug("premise: " + premise);
		if (premise != null) {
			conclusion = doublePrime(premise);
			logger.debug("conclusion: " + conclusion);
			if (!premise.equals(conclusion)) {
				conclusion.removeAll(premise);
				implication = new Implication<OWLClass>(premise,conclusion);
				setCurrentQuestion(implication);
				if (followsFromBackgroundKnowledge(implication)) {
					logger.debug("Follows from background knowledge: " + implication);
					getImplications().add(implication);
					premise = getNextPremise(premise);
					continueExploration(premise);
				}
				else {
					// Before asking the expert first check whether this implication makes the KB
					// inconsistent, which can be the case due to anonymous ABox individuals
					if (implicationMakesOntologyInconsistent(implication)) {
						logger.info("IMPLICATION " + implication + " WILL MAKE THE ONTOLOGY INCONSISTENT!");
						getExpert().forceToCounterExample(implication);
					}
					else {
						logger.info("Implication " + implication + " will not make the ontology inconsistent");
						// if the implication does not follow from the TBox and adding it does not make
						// the KB inconsistent, ask the expert
						getExpert().askQuestion(implication);
					}
				}
			}
			else {
				// if the premise is equal to the conclusion
				// trivial implication, compute the next premise 
				premise = getNextPremise(premise);
				// and continue with the next question
				continueExploration(premise);
			}
		}
		else {
			// if the premise is null
			// stop the exploration
			explorationFinished();
			logger.debug("objects: " + getObjects());
		}
	}

}
