/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.diagram.factortypes;

public class FactorTypeIntermediateResult extends FactorType
{
	public boolean isIntermediateResult()
	{
		return true;
	}
	
	public String toString()
	{
		return INTERMEDIATE_RESULT;
	}

	public static final String INTERMEDIATE_RESULT = "Intermediate Result";
}
