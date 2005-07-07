/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.nodes;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;


public class RectangleRenderer extends MultilineNodeRenderer
{
	Dimension getInsetDimension()
	{
		return new Dimension(0, 0);
	}
	
	void fillShape(Graphics g)
	{
		g.fillRect(borderWidth - 1, borderWidth - 1, getSize().width - borderWidth, getSize().height - borderWidth);
	}

	void drawBorder(Graphics2D g2)
	{
		g2.drawRect(borderWidth - 1, borderWidth - 1, getSize().width - borderWidth, getSize().height - borderWidth);
	}

}
