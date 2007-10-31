/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning.propertiesPanel;

import java.awt.Color;

public class PlanningViewMeasurementTable extends PlanningViewAbstractTableWithSizedColumns
{
	public PlanningViewMeasurementTable(PlanningViewMeasurementTableModel model)
	{
		super(model);	
	}
	
	public Color getColumnBackGroundColor(int columnCount, int column)
	{
		return Color.PINK;
	}
	
	public String getUniqueTableIdentifier()
	{
		return UNIQUE_IDENTIFIER;
	}
	
	public static final String UNIQUE_IDENTIFIER = "PlanningViewMeasurementTable";
}
