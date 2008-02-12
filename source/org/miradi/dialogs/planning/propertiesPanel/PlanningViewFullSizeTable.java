/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.planning.propertiesPanel;

import java.awt.Dimension;

import org.miradi.dialogs.base.EditableObjectTableModel;

abstract public class PlanningViewFullSizeTable extends	PlanningViewAbstractTableWithColoredColumns
{
	public PlanningViewFullSizeTable(EditableObjectTableModel modelToUse)
	{
		super(modelToUse);
	}
	
	public Dimension getPreferredScrollableViewportSize()
	{
		return getPreferredSize();
	}
}
