/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.icons;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import org.conservationmeasures.eam.diagram.renderers.FactorRenderer;

public class GoalIcon extends DesireIcon
{

	public void paintIcon(Component c, Graphics g, int x, int y)
	{
		g.setColor(FactorRenderer.ANNOTATIONS_COLOR);
		g.fillOval(x, y + HEIGHT/4, WIDTH, HEIGHT/2);
		g.setColor(Color.BLACK);
		g.drawOval(x, y + HEIGHT/4, WIDTH, HEIGHT/2);
	}
}
