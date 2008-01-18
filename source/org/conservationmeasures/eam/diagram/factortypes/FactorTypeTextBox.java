/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.diagram.factortypes;

public class FactorTypeTextBox extends FactorType
{
	public boolean isTextBox()
	{
		return true;
	}
	
	public String toString()
	{
		return TEXT_BOX_TYPE;
	}

	public static final String TEXT_BOX_TYPE = "TextBox";
}