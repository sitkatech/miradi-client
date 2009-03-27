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
import org.miradi.objects.DiagramFactor;

public class LayerSorter implements Comparator<DefaultGraphCell>
{
	public int compare(DefaultGraphCell c1, DefaultGraphCell c2)
	{
		if (!(c1 instanceof EAMGraphCell))
			return 0;
		
		if (!(c2 instanceof EAMGraphCell))
			return 0;
		
		EAMGraphCell cell1 = (EAMGraphCell) c1;
		EAMGraphCell cell2 = (EAMGraphCell) c2;
		String layer1 = getLayer(cell1);
		String layer2 = getLayer(cell2);
		
		return layer1.compareTo(layer2);
	}

	private String getLayer(EAMGraphCell cell)
	{
		if (cell.isFactorLink())
			return LINK_LAYER;
		
		if (cell.isProjectScopeBox() )
			return SCOPE_BOX_GROUP_BOX_LAYER;
		
		FactorCell factorCell = (FactorCell) cell;
		if (factorCell.isTextBox())
			return getTextBoxLayer(factorCell.getDiagramFactor());
		
		if (factorCell.isGroupBox())
			return SCOPE_BOX_GROUP_BOX_LAYER;
			
		if (factorCell.isActivity() || factorCell.isStress())
			return ACTIVITY_STRESS_LAYER;
		
		return DEFAULT_LAYER; 
	}
	
	private String getTextBoxLayer(DiagramFactor diagramFactor)
	{
		if (diagramFactor.isDefaultZOrder())
			return FRONT_TEXT_BOX_LAYER;
		
		return BACK_TEXT_BOX_LAYER;
	}

	private static final String FRONT_TEXT_BOX_LAYER = "Layer1";
	private static final String SCOPE_BOX_GROUP_BOX_LAYER = "Layer2";
	private static final String DEFAULT_LAYER = "Layer3";
	private static final String LINK_LAYER = "Layer4";
	private static final String ACTIVITY_STRESS_LAYER = "Layer5";
	private static final String BACK_TEXT_BOX_LAYER = "Layer6";
}
