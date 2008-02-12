/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.diagram.factortypes;

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
