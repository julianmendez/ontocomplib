package de.tudresden.inf.tcs.oclib;

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

}
