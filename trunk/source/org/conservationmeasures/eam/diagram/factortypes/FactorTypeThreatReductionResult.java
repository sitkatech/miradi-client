/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.diagram.factortypes;

public class FactorTypeThreatReductionResult extends FactorType
{
	public boolean isThreatReductionResult()
	{
		return true;
	}
	
	public String toString()
	{
		return THREAT_REDUCTION_RESULT;
	}

	public static final String THREAT_REDUCTION_RESULT = "Threat Reduction Result";
}
