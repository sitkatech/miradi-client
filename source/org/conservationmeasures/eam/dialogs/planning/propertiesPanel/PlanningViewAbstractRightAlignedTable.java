/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning.propertiesPanel;

import javax.swing.JLabel;
import javax.swing.table.TableModel;

abstract public class PlanningViewAbstractRightAlignedTable extends	PlanningViewAbstractTableWithSizedColumns
{
	public PlanningViewAbstractRightAlignedTable(TableModel modelToUse)
	{
		super(modelToUse);
	}
	
	public int getColumnAlignment()
	{
		return JLabel.RIGHT;
	}
	
}
