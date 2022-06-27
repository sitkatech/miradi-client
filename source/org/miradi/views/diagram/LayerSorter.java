/* 
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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
package org.miradi.views.diagram;

import java.util.Comparator;

import org.jgraph.graph.DefaultGraphCell;
import org.miradi.diagram.cells.EAMGraphCell;
import org.miradi.diagram.cells.FactorCell;
import org.miradi.diagram.cells.LinkCell;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramLink;

public class LayerSorter implements Comparator<DefaultGraphCell>
{
	public int compare(DefaultGraphCell c1, DefaultGraphCell c2)
	{
		//NOTE: We do not know why Default ports are showing up in the roots.
		boolean isC1Sortable = c1 instanceof EAMGraphCell;
		boolean isC2Sortable = c2 instanceof EAMGraphCell;
		if (!isC1Sortable && !isC2Sortable)
			return 0;
		
		if (!isC1Sortable)
			return 1;
		
		if (!isC2Sortable)
			return -1;
		
		EAMGraphCell cell1 = (EAMGraphCell) c1;
		EAMGraphCell cell2 = (EAMGraphCell) c2;
		int zIndex1 = getZIndex(cell1);
		int zIndex2 = getZIndex(cell2);

		return Integer.compare(zIndex1, zIndex2);
	}

	private int getZIndex(EAMGraphCell cell)
	{
		if (cell.isFactorLink())
		{
			LinkCell linkCell = (LinkCell) cell;
			DiagramLink diagramLink = linkCell.getDiagramLink();
			return diagramLink.getZIndex();
		}

		FactorCell factorCell = (FactorCell) cell;
		DiagramFactor diagramFactor =  factorCell.getDiagramFactor();
		return diagramFactor.getZIndex();
	}
}
