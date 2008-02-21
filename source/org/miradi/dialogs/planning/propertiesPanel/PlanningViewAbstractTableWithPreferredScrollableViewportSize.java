/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.planning.propertiesPanel;

import java.awt.Dimension;

import org.miradi.dialogs.base.EditableObjectTableModel;

abstract public class PlanningViewAbstractTableWithPreferredScrollableViewportSize extends PlanningViewAbstractTableWithColoredColumns
{
	public PlanningViewAbstractTableWithPreferredScrollableViewportSize(EditableObjectTableModel modelToUse)
	{
		super(modelToUse);
	}
	
	@Override
	protected void addRowHeightSaver()
	{
		// NOTE: Override and do nothing to prevent user from changing row height
	}
	
	public Dimension getPreferredScrollableViewportSize()
	{
		return getPreferredSize();
	}
}
