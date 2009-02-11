package de.tudresden.inf.tcs.oclib;

import java.util.Set;
import java.util.HashSet;

// import java.net.URI;
// import org.apache.log4j.Logger;

import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.inference.OWLReasonerException;

import de.tudresden.inf.tcs.fcalib.PartialObject;

public class IndividualObject extends PartialObject<OWLClass,OWLIndividual> {

	private IndividualContext context;
	
	// /**
	//  * The logger.
	//  */
	// private static final Logger logger = Logger.getLogger(IndividualObject.class);

	public IndividualObject(OWLIndividual individual,IndividualContext c) {
		super(individual);
		context = c;
	}
	
	public IndividualObject(OWLIndividual individual,Set<OWLClass> types,IndividualContext c) {
		super(individual,types);
		context = c;
	}
	
	@Override
	public String getName() {
		return getIdentifier().getURI().getFragment();
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
					try {
						// logger.debug("Asking the reasoner whether " + getName() + " has type " + type.getURI().getFragment());
						if (getContext().getReasoner().hasType(getIdentifier(), type, false)) {
							getDescription().addAttribute(type);
						}
						else {
							// logger.debug("Asking the reasoner whether " + getName() + " has complement of type " + type.getURI().getFragment());
							if (getContext().getReasoner().hasType(getIdentifier(), getContext().getFactory().getOWLObjectComplementOf(type), false)) {
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
					if (!getContext().getReasoner().hasType(getIdentifier(), getContext().getFactory().getOWLObjectComplementOf(attribute), false)) {
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
