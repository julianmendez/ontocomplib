package de.tudresden.inf.tcs.oclib;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDisjointClassesAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntologyChangeException;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

import de.tudresden.inf.tcs.fcaapi.exception.IllegalAttributeException;
import de.tudresden.inf.tcs.fcaapi.exception.IllegalObjectException;
import de.tudresden.inf.tcs.oclib.change.ClassAssertionChange;
import de.tudresden.inf.tcs.oclib.change.NewIndividualChange;

public class ELIndividualContext extends IndividualContext {

	public ELIndividualContext(OWLReasoner r) {
		super(r);
		// TODO Auto-generated constructor stub
	}

	public IndividualObject createIndividualObject(OWLNamedIndividual individual) {
		return new ELIndividualObject(individual, this);
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
			for (OWLNamedIndividual individual : getReasoner().getInstances(attribute, false).getFlattened()) {
				IndividualObject o = createIndividualObject(individual);
				o.getDescription().addAttribute(attribute);
				addObject(o);
			}
			// the CEL reasoner is being used
			// add new concept name for the complement of the added attribute
			OWLClass complementOfAttribute = getFactory().getOWLClass(IRI.create(getOntology().getOntologyID().getOntologyIRI() + 
					Constants.EL_COMPLEMENT_CONCEPT_PREFIX + attribute.getIRI().getFragment()));
			// make them disjoint
			Set<OWLClass> disjoint = new HashSet<OWLClass>();
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
		OWLClassAssertionAxiom axiom = getFactory().getOWLClassAssertionAxiom( 
				getFactory().getOWLClass(IRI.create(getOntology().getOntologyID().getOntologyIRI() 
						+ Constants.EL_COMPLEMENT_CONCEPT_PREFIX + type.getIRI().getFragment())),
				indObj.getIdentifier());
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

	@Override
	public void updateObjects(int updateType) {
		switch (updateType) {
		case Constants.AFTER_MODIFICATION:
				for (OWLClass attribute : getAttributes()) {
					for (OWLNamedIndividual ind : getReasoner().getInstances(attribute, false).getFlattened()) {
						if (!containsObject(ind)) {
							IndividualObject indObj = createIndividualObject(ind);
							// indObj.updateDescription();
							indObj.getDescription().addAttribute(attribute);
							addObject(indObj);
						}
					}
					for (OWLNamedIndividual ind : getReasoner().getInstances(
							getFactory().getOWLClass(
									IRI.create(getOntology().getOntologyID().getOntologyIRI() 
											+ Constants.EL_COMPLEMENT_CONCEPT_PREFIX + attribute.getIRI().getFragment())),
							false).getFlattened()) {
						if (!containsObject(ind)) {
							IndividualObject indObj = createIndividualObject(ind);
							// indObj.updateDescription();
							indObj.getDescription().addNegatedAttribute(attribute);
							addObject(indObj);
						}
					}
				}
			break;
		case Constants.AFTER_UNDO:
			try {
				Set<IndividualObject> toBeRemoved = new HashSet<IndividualObject>();
				for (IndividualObject object : getObjects()) {
					if (!getOntology().containsEntityInSignature(object.getIdentifier())) {
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

}
