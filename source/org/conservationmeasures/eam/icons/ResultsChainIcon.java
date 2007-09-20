package org.conservationmeasures.eam.icons;

import java.awt.Component;
import java.awt.Graphics;

public class ResultsChainIcon extends AbstractDiagramIcon
{
	public void paintIcon(Component c, Graphics g, int x, int y)
	{
		int[] columns = new int[] {x, x + getFactorWidth(), x + getFactorWidth()*2,};
		int[] rows = new int[] {y, y + getFactorHeight(), y + getFactorHeight() * 2,};
		
		drawStrategy(g, columns[0], rows[0]);
		
		drawThreatReductionResult(g, columns[1], rows[1]);

		drawTarget(g, columns[2], rows[2]);
	}
}
