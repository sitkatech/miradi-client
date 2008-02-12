/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.diagram.renderers;

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
