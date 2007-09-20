/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.icons;

import java.awt.Component;
import java.awt.Graphics;

public class CreateResultsChainIcon extends ResultsChainIcon
{
	public void paintIcon(Component c, Graphics g, int x, int y)
	{
		super.paintIcon(c, g, x, y);
		drawCreatePlus(c, g, x, y);
	}

}
