package de.tudresden.inf.tcs.oclib;

import java.util.Set;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;

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
		return getComplement(getContext().getOntology(), attribute);
	}

	public static OWLClass getComplement(OWLOntology ontology, OWLClass type) {
		return ontology.getOWLOntologyManager().getOWLDataFactory().getOWLClass(
				IRI.create(ontology.getOntologyID().getOntologyIRI() 
				+ Constants.EL_COMPLEMENT_CONCEPT_PREFIX + type.getIRI().getFragment()));
	}

}
