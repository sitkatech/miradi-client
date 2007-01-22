/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.diagram.renderers;

public class IconHexagonRenderer extends HexagonRenderer
{
	public IconHexagonRenderer(boolean isDraftIcon)
	{
		isDraft = isDraftIcon;
	}
	
	boolean isDraft()
	{
		return isDraft;
	}

	boolean isDraft;
}
