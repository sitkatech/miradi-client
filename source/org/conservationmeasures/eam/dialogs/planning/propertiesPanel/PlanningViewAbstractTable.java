/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning.propertiesPanel;

import java.awt.Dimension;

import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;

import org.conservationmeasures.eam.utils.TableWithColumnWidthSaver;

public class PlanningViewAbstractTable extends TableWithColumnWidthSaver
{
	public PlanningViewAbstractTable(TableModel  modelToUse)
	{
		super(modelToUse);

		//TODO planning table - find better solution - check the other tables two planning tables too
		setRowHeight(getRowHeight() + 10);
	}

	public Dimension getPreferredScrollableViewportSize()
	{
		int preferredHeight = getPreferredScrollableViewportHeight();
		int preferredWidth = getPreferredScrollableViewportWidth();
		return new Dimension(preferredWidth, preferredHeight);
	}

	int getPreferredScrollableViewportWidth()
	{
		return 250;
	}

	int getPreferredScrollableViewportHeight()
	{
		return getRowHeight() * 5;
	}
	
	public void stopCellEditing()
	{
		TableCellEditor editor = getCellEditor();
		if (editor == null)
			return;

		editor.stopCellEditing();
	}
}
