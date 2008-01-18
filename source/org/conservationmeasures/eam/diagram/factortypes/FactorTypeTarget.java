/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.diagram.factortypes;


public class FactorTypeTarget extends FactorType
{
	public boolean isTarget()
	{
		return true;
	}
	
	public String toString()
	{
		return TARGET_TYPE;
	}

	public static final String TARGET_TYPE = "Target";

}
