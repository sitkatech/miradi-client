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
package org.miradi.views.diagram;

import java.util.Comparator;

import org.jgraph.graph.DefaultGraphCell;
import org.miradi.diagram.cells.EAMGraphCell;
import org.miradi.diagram.cells.FactorCell;

public class LayerSorter implements Comparator<DefaultGraphCell>
{
	public int compare(DefaultGraphCell c1, DefaultGraphCell c2)
	{
		if (!(c1 instanceof EAMGraphCell))
			return -1;
		
		if (!(c2 instanceof EAMGraphCell))
			return -1;
		
		EAMGraphCell cell1 = (EAMGraphCell) c1;
		EAMGraphCell cell2 = (EAMGraphCell) c2;
		String layer1 = getLayer(cell1);
		String layer2 = getLayer(cell2);
		
		return layer1.compareTo(layer2);
	}

	private String getLayer(EAMGraphCell cell)
	{
		if (cell.isProjectScope())
			return LAYER_1;
		
		if (cell.isFactorLink())
			return LAYER_5;
		
		FactorCell factorCell = (FactorCell) cell;
		if (factorCell.isGroupBox())
			return LAYER_2;
		
		if (factorCell.isStrategy())
			return LAYER_6;
		
		return LAYER_3; 
	}
	
	private static final String LAYER_1 = "Layer1";
	private static final String LAYER_2 = "Layer2";
	private static final String LAYER_3 = "Layer3";
	
	private static final String LAYER_5 = "Layer5";
	private static final String LAYER_6 = "Layer6";
}
