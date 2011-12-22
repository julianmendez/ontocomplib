package de.tudresden.inf.tcs.oclib;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

public class ELIndividualObject extends IndividualObject {

	public ELIndividualObject(OWLNamedIndividual individual, IndividualContext c) {
		super(individual, c);
		// TODO Auto-generated constructor stub
	}
	
	public ELIndividualObject(OWLNamedIndividual individual,Set<OWLClass> types,IndividualContext c) {
		super(individual,types,c);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public OWLClassExpression getComplement(OWLClass attribute) {
		return getContext().getFactory().getOWLClass(
				IRI.create(getContext().getOntology().getOntologyID().getOntologyIRI() +  
						Constants.EL_COMPLEMENT_CONCEPT_PREFIX + attribute));
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
							OWLClassExpression complement = getComplement(type);
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
					OWLClassExpression complement = getComplement(attribute);
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
