package de.tudresden.inf.tcs.oclib;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLClass;
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
	
	public void updateDescription(int updateType) {
		switch (updateType) {
		case Constants.AFTER_MODIFICATION:
			// if it is after context modification, then only check question marks. pluses and minuses have not changed.
			for (OWLClass type : getContext().getAttributes()) {
				if (!getDescription().containsAttribute(type) && !getDescription().containsNegatedAttribute(type)) {
						// logger.debug("Asking the reasoner whether " + getName() + " has type " + type.getIRI().getFragment());
						if (getContext().getReasoner().getTypes(getIdentifier(), false).containsEntity(type)) {
						    // logger.info(getName() + " has type " + type.getIRI().getFragment());
							getDescription().addAttribute(type);
						}
						else {
							OWLClass complement = getContext().getFactory().getOWLObjectComplementOf(type).asOWLClass();
							// logger.debug("Asking the reasoner whether " + getName() + " has complement of type " + type.getIRI().getFragment());
							if (getContext().getReasoner().getTypes(getIdentifier(), false).containsEntity(complement)) {
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
					if (!getContext().getReasoner().getTypes(getIdentifier(), false).containsEntity(attribute)) {
						// getDescription().getAttributes().remove(attribute);
						toBeRemoved.add(attribute);
					}
				}
				getDescription().getAttributes().removeAll(toBeRemoved);
				
				toBeRemoved.clear();
				for (OWLClass attribute : getDescription().getNegatedAttributes()) {
					OWLClass complement = getContext().getFactory().getOWLObjectComplementOf(attribute).asOWLClass();
					if (!getContext().getReasoner().getTypes(getIdentifier(), false).containsEntity(complement)) {
						// getDescription().getNegatedAttributes().remove(attribute);
						toBeRemoved.add(attribute);
					}
				}
				getDescription().getNegatedAttributes().removeAll(toBeRemoved);
			break;
		}
	}
}
