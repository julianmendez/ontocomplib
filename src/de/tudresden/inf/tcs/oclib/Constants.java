package de.tudresden.inf.tcs.oclib;

public interface Constants {

	// Type of individual object description update (whether it is after a modification, or after an undo)
	static final int AFTER_MODIFICATION = 0;
	static final int AFTER_UNDO = 1;
	
	// The ID of the CEL reasoner. Looks like it contains redundancies, but OK. CEL identifies it like this
	static final String CEL_REASONER_ID="de.tudresden.inf.lat.cel.de.tudresden.inf.lat.cel.protege.CelReasonerFactory";
	static final String PELLET_REASONER_ID="com.owldl.pellet.pellet.reasoner.factory";
	static final String FACTPLUSPLUS_REASONER_ID="uk.ac.manchester.cs.owl.factplusplus.factplusplus-factory";
	
	// The prefix that is used for name of the complement concept added to an EL context
	static final String EL_COMPLEMENT_CONCEPT_PREFIX="complementOf-";
}
