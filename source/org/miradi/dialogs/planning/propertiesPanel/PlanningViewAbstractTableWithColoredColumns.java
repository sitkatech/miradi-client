/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.planning.propertiesPanel;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JTable;

import org.miradi.dialogs.base.EditableObjectTable;
import org.miradi.dialogs.base.EditableObjectTableModel;
import org.miradi.dialogs.treetables.TreeTableWithIcons;

abstract public class PlanningViewAbstractTableWithColoredColumns extends EditableObjectTable
{
	public PlanningViewAbstractTableWithColoredColumns(EditableObjectTableModel modelToUse)
	{
		super(modelToUse);
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	}
		
	public Color getColumnBackGroundColor(int tableColumn)
	{
		return getBackground();
	}
	
	public Font getRowFont(int row)
	{
		return TreeTableWithIcons.Renderer.getPlainFont();
	}
	
}
