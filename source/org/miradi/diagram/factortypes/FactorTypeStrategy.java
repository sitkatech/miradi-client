/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.diagram.factortypes;


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
