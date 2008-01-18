/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.icons;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Rectangle;

import org.conservationmeasures.eam.diagram.renderers.HexagonRenderer;
import org.conservationmeasures.eam.main.AppPreferences;
import org.conservationmeasures.eam.main.EAM;

public abstract class AbstractDiagramIcon extends AbstractMiradiIcon
{

	protected void drawTarget(Graphics g, int x, int y)
	{
		g.setColor(getFactorColor(AppPreferences.TAG_COLOR_TARGET));
		g.fillOval(x, y, getFactorWidth(), getFactorHeight());
		g.setColor(Color.BLACK);
		g.drawOval(x, y, getFactorWidth(), getFactorHeight());
	}

	protected void drawThreat(Graphics g, int x, int y)
	{
		drawCause(g, x, y, getFactorColor(AppPreferences.TAG_COLOR_DIRECT_THREAT));
	}

	protected void drawThreatReductionResult(Graphics g, int x, int y)
	{
		drawCause(g, x, y, getFactorColor(AppPreferences.TAG_COLOR_THREAT_REDUCTION_RESULT));
	}

	private void drawCause(Graphics g, int x, int y, Color color)
	{
		g.setColor(color);
		g.fillRect(x, y, getFactorWidth() + 2, getFactorHeight() + 1);
		g.setColor(Color.BLACK);
		g.drawRect(x, y, getFactorWidth() + 2, getFactorHeight() + 1);
	}

	protected void drawStrategy(Graphics g, int x, int y)
	{
		Rectangle rect = new Rectangle(x, y, getFactorWidth(), getFactorHeight());
		Polygon hex = HexagonRenderer.buildHexagon(rect);
		g.setColor(getFactorColor(AppPreferences.TAG_COLOR_STRATEGY));
		g.fillPolygon(hex);
		g.setColor(Color.BLACK);
		g.drawPolygon(hex);
	}

	Color getFactorColor(String tag)
	{
		return EAM.getMainWindow().getColorPreference(tag);
	}

	protected int getFactorHeight()
	{
		return getIconHeight() / 5;
	}

	protected int getFactorWidth()
	{
		return getIconWidth() / 3;
	}

}
