package de.tudresden.inf.tcs.oclib;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
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
	
	public void updateDescription(int updateType) {
		switch (updateType) {
		case Constants.AFTER_MODIFICATION:
			// if it is after context modification, then only check question marks. pluses and minuses have not changed.
			for (OWLClass type : getContext().getAttributes()) {
				if (!getDescription().containsAttribute(type) && !getDescription().containsNegatedAttribute(type)) {
						// logger.debug("Asking the reasoner whether " + getName() + " has type " + type.getURI().getFragment());
						if (getContext().getReasoner().getTypes(getIdentifier(), false).containsEntity(type)) {
						    // logger.info(getName() + " has type " + type.getURI().getFragment());
							getDescription().addAttribute(type);
						}
						else {
							// logger.debug("Asking the reasoner whether " + getName() + " has complement of type " + type.getURI().getFragment());
							// if (getContext().getReasoner().hasType(getIdentifier(), getContext().getFactory().getOWLObjectComplementOf(type), false)) {
							if (getContext().getReasoner().getTypes(getIdentifier(), false).containsEntity(
									getContext().getFactory().getOWLClass(
											IRI.create(getContext().getOntology().getOntologyID().getOntologyIRI() +  
													Constants.EL_COMPLEMENT_CONCEPT_PREFIX + type)))) {
							    // logger.debug(getName() + " has complement of type " + type.getURI().getFragment());
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
					// if (!getContext().getReasoner().hasType(getIdentifier(), getContext().getFactory().getOWLObjectComplementOf(attribute), false)) {
					if (!getContext().getReasoner().getTypes(getIdentifier(), false).containsEntity(
							getContext().getFactory().getOWLClass(
									IRI.create(getContext().getOntology().getOntologyID().getOntologyIRI() +  
											Constants.EL_COMPLEMENT_CONCEPT_PREFIX + attribute)))) {
						// getDescription().getNegatedAttributes().remove(attribute);
						toBeRemoved.add(attribute);
					}
				}
				getDescription().getNegatedAttributes().removeAll(toBeRemoved);
			break;
		}
	}

}
