/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.diagram.factortypes;



public class FactorTypeCause extends FactorType
{
	public boolean isCause()
	{
		return true;
	}
	
	public String toString()
	{
		return CAUSE_TYPE;
	}

	public static final String CAUSE_TYPE = "Factor";

}
