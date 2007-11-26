/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning.propertiesPanel;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JTable;
import javax.swing.table.TableModel;

import org.conservationmeasures.eam.dialogs.base.EditableObjectTable;
import org.conservationmeasures.eam.dialogs.treetables.TreeTableWithIcons;

abstract public class PlanningViewAbstractTableWithColoredColumns extends EditableObjectTable
{
	public PlanningViewAbstractTableWithColoredColumns(TableModel modelToUse)
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
