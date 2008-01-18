/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.diagram.factortypes;

import org.conservationmeasures.eam.main.EAM;



public class FactorTypeDirectThreat extends FactorTypeCause
{
	public boolean isDirectThreat() 
	{
		return true;
	}
	
	public String toString()
	{
		return EAM.text("Type|Direct Threat");
	}
}
