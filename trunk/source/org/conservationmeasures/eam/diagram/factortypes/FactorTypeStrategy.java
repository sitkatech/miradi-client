/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.diagram.factortypes;


public class FactorTypeStrategy extends FactorType
{
	public boolean isStrategy()
	{
		return true;
	}
	
	public String toString()
	{
		return STRATEGY_TYPE; 
	}

	public static final String STRATEGY_TYPE = "Intervention";

}
