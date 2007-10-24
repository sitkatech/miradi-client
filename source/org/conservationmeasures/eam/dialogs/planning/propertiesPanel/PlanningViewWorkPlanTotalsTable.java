/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning.propertiesPanel;

import javax.swing.table.TableModel;

public class PlanningViewWorkPlanTotalsTable extends PlanningViewAbstractTable
{
	public PlanningViewWorkPlanTotalsTable(TableModel model)
	{
		super(model);
	}
	
	public String getUniqueTableIdentifier()
	{
		return UNIQUE_IDENTIFIER;
	}
	
	public static final String UNIQUE_IDENTIFIER = "PlanningViewWorkPlanTotalsTable";
}
