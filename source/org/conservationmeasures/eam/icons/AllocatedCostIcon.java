/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.icons;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

public class AllocatedCostIcon extends AbstractMiradiIcon
{
	public void paintIcon(Component component, Graphics g, int x, int y)
	{
		g.setColor(Color.BLACK);
		g.drawString("*", x, y + getIconHeight());
	}

}
