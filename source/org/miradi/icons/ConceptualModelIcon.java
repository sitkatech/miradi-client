/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/
package org.miradi.icons;

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
