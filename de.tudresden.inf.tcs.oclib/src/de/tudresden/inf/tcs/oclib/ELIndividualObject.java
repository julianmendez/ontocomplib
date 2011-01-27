package de.tudresden.inf.tcs.oclib;

import java.util.Set;
import java.util.HashSet;
import java.net.URI;

import org.semanticweb.owl.inference.OWLReasonerException;

import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.model.OWLClass;

public class ELIndividualObject extends IndividualObject {

	public ELIndividualObject(OWLIndividual individual, IndividualContext c) {
		super(individual, c);
		// TODO Auto-generated constructor stub
	}
	
	public ELIndividualObject(OWLIndividual individual,Set<OWLClass> types,IndividualContext c) {
		super(individual,types,c);
		// TODO Auto-generated constructor stub
	}
	
	public void updateDescription(int updateType) {
		switch (updateType) {
		case Constants.AFTER_MODIFICATION:
			// if it is after context modification, then only check question marks. pluses and minuses have not changed.
			for (OWLClass type : getContext().getAttributes()) {
				if (!getDescription().containsAttribute(type) && !getDescription().containsNegatedAttribute(type)) {
					try {
						// logger.debug("Asking the reasoner whether " + getName() + " has type " + type.getURI().getFragment());
						if (getContext().getReasoner().hasType(getIdentifier(), type, false)) {
						    // logger.info(getName() + " has type " + type.getURI().getFragment());
							getDescription().addAttribute(type);
						}
						else {
							// logger.debug("Asking the reasoner whether " + getName() + " has complement of type " + type.getURI().getFragment());
							// if (getContext().getReasoner().hasType(getIdentifier(), getContext().getFactory().getOWLObjectComplementOf(type), false)) {
							if (getContext().getReasoner().hasType(getIdentifier(), 
									getContext().getFactory().getOWLClass(
											URI.create(getContext().getOntology().getURI() + 
													Constants.EL_COMPLEMENT_CONCEPT_PREFIX + type)),
									false)) {
							    // logger.debug(getName() + " has complement of type " + type.getURI().getFragment());
								getDescription().addNegatedAttribute(type);
							}
						}
					}
					catch (OWLReasonerException e) {
						e.printStackTrace();
						System.exit(-1);
					}
				}
			}
			break;
		case Constants.AFTER_UNDO:
			// if it is after an undo, then check only pluses and minuses. questions marks have not changed.
			try {
				Set<OWLClass> toBeRemoved = new HashSet<OWLClass>();
				for (OWLClass attribute : getDescription().getAttributes()) {
					if (!getContext().getReasoner().hasType(getIdentifier(), attribute, false)) {
						// getDescription().getAttributes().remove(attribute);
						toBeRemoved.add(attribute);
					}
				}
				getDescription().getAttributes().removeAll(toBeRemoved);
				
				toBeRemoved.clear();
				for (OWLClass attribute : getDescription().getNegatedAttributes()) {
					// if (!getContext().getReasoner().hasType(getIdentifier(), getContext().getFactory().getOWLObjectComplementOf(attribute), false)) {
					if (!getContext().getReasoner().hasType(getIdentifier(), 
							getContext().getFactory().getOWLClass(
									URI.create(getContext().getOntology().getURI() + 
											Constants.EL_COMPLEMENT_CONCEPT_PREFIX + attribute)),
							false)) {
						// getDescription().getNegatedAttributes().remove(attribute);
						toBeRemoved.add(attribute);
					}
				}
				getDescription().getNegatedAttributes().removeAll(toBeRemoved);
			}
			catch (OWLReasonerException e) {
				e.printStackTrace();
				System.exit(-1);
			}
			break;
		}
	}

}
