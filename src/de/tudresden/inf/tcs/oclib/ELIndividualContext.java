package de.tudresden.inf.tcs.oclib;

import java.util.Set;
import java.util.HashSet;
import java.net.URI;

import org.semanticweb.owl.inference.OWLReasoner;
import org.semanticweb.owl.inference.OWLReasonerException;

import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.model.OWLOntologyManager;
import org.semanticweb.owl.model.OWLDisjointClassesAxiom;
import org.semanticweb.owl.model.OWLClassAssertionAxiom;
import org.semanticweb.owl.model.AddAxiom;
import org.semanticweb.owl.model.OWLOntologyChangeException;

import de.tudresden.inf.tcs.fcaapi.exception.IllegalAttributeException;
import de.tudresden.inf.tcs.fcaapi.exception.IllegalObjectException;
import de.tudresden.inf.tcs.oclib.change.ClassAssertionChange;
import de.tudresden.inf.tcs.oclib.change.NewIndividualChange;

public class ELIndividualContext extends IndividualContext {

	public ELIndividualContext(OWLOntologyManager m, OWLReasoner r, OWLOntology o) {
		super(m, r, o);
		// TODO Auto-generated constructor stub
	}
	
	/** 
	 * Adds a given attribute to the attributes of this context, updates the 'local' set of objects.
	 * @param attribute the attribute to be added
	 * @return <code>true</code> if the <code>attribute</code> is successfully added
	 * @throws IllegalAttributeException if the given attribute is already in the set of 
	 * attributes
	 */
	@Override
	public boolean addAttribute(OWLClass attribute) {
		boolean added = getAttributes().add(attribute);
		if (!added) {
			throw new IllegalAttributeException("Attribute " + attribute + " has already been added");
		}
		try {
			for (OWLIndividual individual : getReasoner().getIndividuals(attribute, false)) {
				ELIndividualObject o = new ELIndividualObject(individual,this);
				o.getDescription().addAttribute(attribute);
				addObject(o);
			}
			// the CEL reasoner is being used
			// add new concept name for the complement of the added attribute
			OWLClass complementOfAttribute = getFactory().getOWLClass(URI.create(getOntology().getURI() + 
					Constants.EL_COMPLEMENT_CONCEPT_PREFIX +  attribute));
			// make them disjoint
			HashSet<OWLClass> disjoint = new HashSet<OWLClass>();
			disjoint.add(attribute);
			disjoint.add(complementOfAttribute);
			OWLDisjointClassesAxiom disjointnessAxiom = getFactory().getOWLDisjointClassesAxiom(disjoint);
			// create a new AddAxiom object
			AddAxiom addAxiom = new AddAxiom(getOntology(),disjointnessAxiom);
			getManager().applyChange(addAxiom);
			reClassifyOntology();
			//TODO
			// updateObjectDescriptions should be overridden for EL contexts
			updateObjectDescriptions(Constants.AFTER_MODIFICATION);
		}
		catch (OWLReasonerException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		catch (OWLOntologyChangeException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		return added;
	}
	
	/**
	 * Asserts that the given individual is an instance of the complement of the given type.
	 * @param type the given type
	 * @param ind the given individual about which the assertion will be made
	 * @return <code>true</code> if the assertion is successfull
	 */
	@Override
	public boolean addNegatedAttributeToObject(OWLClass type,IndividualObject indObj) {
		OWLClassAssertionAxiom axiom = getFactory().getOWLClassAssertionAxiom(indObj.getIdentifier(), 
				getFactory().getOWLClass(URI.create(getOntology().getURI() + Constants.EL_COMPLEMENT_CONCEPT_PREFIX + type)));
		AddAxiom  addAxiom = new AddAxiom(getOntology(),axiom); 
		try {
			getManager().applyChange(addAxiom);
			reClassifyOntology();
			updateObjects(Constants.AFTER_MODIFICATION);
			updateObjectDescriptions(Constants.AFTER_MODIFICATION);
		}
		catch (OWLOntologyChangeException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		getHistory().push(new ClassAssertionChange(this,addAxiom,indObj.getIdentifier(),type,true));
		return true;
	}
	
	/**
	 * Adds a given individual to the ontology as an instance of <code>Thing</code>
	 * @param object the given object to be added
	 * @return <code>true</code> if the object is successfully added
	 */
	@Override
	public boolean addIndividualToOntology(OWLIndividual object) {
		OWLClassAssertionAxiom axiom = getFactory().getOWLClassAssertionAxiom(object, getFactory().getOWLThing());
		AddAxiom  addAxiom = new AddAxiom(getOntology(),axiom); 
		Set<OWLClass> attrs = new HashSet<OWLClass>();
		try {
			getManager().applyChange(addAxiom);
			reClassifyOntology();
			ELIndividualObject indObj = new ELIndividualObject(object,this);
			indObj.updateDescription(Constants.AFTER_MODIFICATION);
			addObject(indObj);
		}
		catch (OWLOntologyChangeException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		attrs.add(getFactory().getOWLThing());
		getHistory().push(new NewIndividualChange(this,addAxiom,object,attrs));
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
		OWLClassAssertionAxiom axiom = getFactory().getOWLClassAssertionAxiom(object, description);
		AddAxiom  addAxiom = new AddAxiom(getOntology(),axiom); 
		try {
			getManager().applyChange(addAxiom);
			reClassifyOntology();
			ELIndividualObject indObj = new ELIndividualObject(object,this);
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
		getHistory().push(new NewIndividualChange(this,addAxiom,object,attributes));
		return true;
	}
	@Override
	public void updateObjects(int updateType) {
		switch (updateType) {
		case Constants.AFTER_MODIFICATION:
			try {
				for (OWLClass attribute : getAttributes()) {
					for (OWLIndividual ind : getReasoner().getIndividuals(attribute, false)) {
						if (!containsObject(ind)) {
							ELIndividualObject indObj = new ELIndividualObject(ind,this);
							// indObj.updateDescription();
							indObj.getDescription().addAttribute(attribute);
							addObject(indObj);
						}
					}
					for (OWLIndividual ind : getReasoner().getIndividuals(
							getFactory().getOWLClass(
									URI.create(getOntology().getURI() + Constants.EL_COMPLEMENT_CONCEPT_PREFIX + attribute)),
							false)) {
						if (!containsObject(ind)) {
							ELIndividualObject indObj = new ELIndividualObject(ind,this);
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
				HashSet<IndividualObject> toBeRemoved = new HashSet<IndividualObject>();
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
	
	/**
	 * Reclassifies the ontology.
	 */
	@Override
	public void reClassifyOntology() {
		// TODO: Is there a more efficient way to do this?
		try {
			// Julian tells to use clearOntology() and loadOntologies()
			getReasoner().clearOntologies();
			getReasoner().loadOntologies(getManager().getImportsClosure(getOntology()));
			getReasoner().classify();
		}
		catch (OWLReasonerException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

}
