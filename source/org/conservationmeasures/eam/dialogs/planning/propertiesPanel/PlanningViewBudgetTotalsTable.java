/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning.propertiesPanel;

import java.awt.Dimension;

import javax.swing.table.TableModel;

public class PlanningViewBudgetTotalsTable extends PlanningViewAbstractTable
{
	public PlanningViewBudgetTotalsTable(TableModel model)
	{
		super(model);
	}
	
	public Dimension getPreferredScrollableViewportSize()
	{
		Dimension maximumDimension = super.getPreferredScrollableViewportSize();
		return new Dimension(50, maximumDimension.height);
	}

}