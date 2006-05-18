/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
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
