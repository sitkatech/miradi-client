/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning.propertiesPanel;

import java.awt.Color;

import org.conservationmeasures.eam.diagram.renderers.FactorRenderer;
import org.conservationmeasures.eam.utils.TableWithTreeTableNodes;

public class PlanningViewFutureStatusTable extends TableWithTreeTableNodes
{
	public PlanningViewFutureStatusTable(PlanningViewFutureStatusTableModel model)
	{
		super(model);	
	}
	
	public Color getColumnBackGroundColor(int columnCount, int column)
	{
		return FactorRenderer.ANNOTATIONS_COLOR;
	}
	
	public String getUniqueTableIdentifier()
	{
		return UNIQUE_IDENTIFIER;
	}
	
	public static final String UNIQUE_IDENTIFIER = "PlanningViewFutureStatusTable";
}
