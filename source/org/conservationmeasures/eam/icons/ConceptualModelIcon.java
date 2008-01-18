/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/
package org.conservationmeasures.eam.icons;

import java.awt.Component;
import java.awt.Graphics;


public class ConceptualModelIcon extends AbstractDiagramIcon
{
	public void paintIcon(Component c, Graphics g, int x, int y)
	{
		int[] columns = new int[] {x, x + getFactorWidth(), x + getFactorWidth()*2,};
		int[] rows = new int[] {y, y + getIconHeight()/2-2, y + getIconHeight() - getFactorHeight() -2,};
		
		drawStrategy(g, columns[0], rows[1]);
		drawStrategy(g, columns[0], rows[2]);
		
		drawThreat(g, columns[1], y + getFactorHeight());
		drawThreat(g, columns[1], y + getIconHeight() - getFactorHeight() - 4);

		drawTarget(g, columns[2], rows[0]);
		drawTarget(g, columns[2], rows[1]);
	}
}
