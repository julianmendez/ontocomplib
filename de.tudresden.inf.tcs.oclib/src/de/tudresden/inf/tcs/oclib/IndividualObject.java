package de.tudresden.inf.tcs.oclib;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import de.tudresden.inf.tcs.fcalib.PartialObject;

public class IndividualObject extends PartialObject<OWLClass,OWLNamedIndividual> {

	private IndividualContext context;
	
	// /**
	//  * The logger.
	//  */
	// private static final Logger logger = Logger.getLogger(IndividualObject.class);

	public IndividualObject(OWLNamedIndividual individual,IndividualContext c) {
		super(individual);
		context = c;
	}
	
	public IndividualObject(OWLNamedIndividual individual,Set<OWLClass> types,IndividualContext c) {
		super(individual,types);
		context = c;
	}
	
	@Override
	public String getName() {
		return getIdentifier().getIRI().getFragment();
	}
	
	public IndividualContext getContext() {
		return context;
	}
	
	private boolean hasType(OWLClassExpression type, OWLNamedIndividual individual) {
		boolean ret = false;
		Set<OWLClassAssertionAxiom> set = getContext().getReasoner().getRootOntology().getClassAssertionAxioms(individual);
		for (OWLClassAssertionAxiom axiom : set) {
			ret = ret || getContext().isSubClassOf(axiom.getClassExpression(), type);
		}
		return ret;
	}

	public void updateDescription(int updateType) {
		switch (updateType) {
		case Constants.AFTER_MODIFICATION:
			// if it is after context modification, then only check question marks. pluses and minuses have not changed.
			for (OWLClass type : getContext().getAttributes()) {
				if (!getDescription().containsAttribute(type) && !getDescription().containsNegatedAttribute(type)) {
						// logger.debug("Asking the reasoner whether " + getName() + " has type " + type.getIRI().getFragment());
						if (hasType(type, getIdentifier())) {
						    // logger.info(getName() + " has type " + type.getIRI().getFragment());
							getDescription().addAttribute(type);
						}
						else {
							OWLClassExpression complement = getContext().getFactory().getOWLObjectComplementOf(type);
							// logger.debug("Asking the reasoner whether " + getName() + " has complement of type " + type.getIRI().getFragment());
							if (hasType(complement, getIdentifier())) {
							    // logger.debug(getName() + " has complement of type " + type.getIRI().getFragment());
								getDescription().addNegatedAttribute(type);
							}
						}
				}
			}
			break;
		case Constants.AFTER_UNDO:
			// if it is after an undo, then check only pluses and minuses. questions marks have not changed.
				Set<OWLClass> toBeRemoved = new HashSet<OWLClass>();
				for (OWLClass attribute : getDescription().getAttributes()) {
					if (!hasType(attribute, getIdentifier())) {
						// getDescription().getAttributes().remove(attribute);
						toBeRemoved.add(attribute);
					}
				}
				getDescription().getAttributes().removeAll(toBeRemoved);
				
				toBeRemoved.clear();
				for (OWLClass attribute : getDescription().getNegatedAttributes()) {
					OWLClassExpression complement = getContext().getFactory().getOWLObjectComplementOf(attribute);
					if (!hasType(complement, getIdentifier())) {
						// getDescription().getNegatedAttributes().remove(attribute);
						toBeRemoved.add(attribute);
					}
				}
				getDescription().getNegatedAttributes().removeAll(toBeRemoved);
			break;
		}
	}
}
