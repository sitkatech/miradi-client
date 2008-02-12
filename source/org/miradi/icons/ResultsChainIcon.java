/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/
package org.miradi.icons;

import java.awt.Component;
import java.awt.Graphics;

public class ResultsChainIcon extends AbstractDiagramIcon
{
	public void paintIcon(Component c, Graphics g, int x, int y)
	{
		int[] columns = new int[] {x, x + getFactorWidth(), x + getFactorWidth()*2,};
		int[] rows = new int[] {y+4, y+4 + getFactorHeight(), y+4 + getFactorHeight() * 2,};
		
		drawStrategy(g, columns[0], rows[0]);
		
		drawThreatReductionResult(g, columns[1], rows[1]);

		drawTarget(g, columns[2], rows[2]);
	}
}
