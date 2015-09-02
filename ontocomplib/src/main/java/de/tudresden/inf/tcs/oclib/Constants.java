package de.tudresden.inf.tcs.oclib;

public interface Constants {

	// Type of individual object description update (whether it is after a modification, or after an undo)
	static final int AFTER_MODIFICATION = 0;
	static final int AFTER_UNDO = 1;
	
	// The prefix that is used for name of the complement concept added to an EL context
	static final String EL_COMPLEMENT_CONCEPT_PREFIX="complementOf-";
}
