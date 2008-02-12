/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.planning.propertiesPanel;

import java.awt.Color;

import javax.swing.table.TableCellRenderer;

import org.miradi.dialogs.tablerenderers.BasicTableCellRenderer;
import org.miradi.dialogs.tablerenderers.FontForObjectTypeProvider;
import org.miradi.main.AppPreferences;
import org.miradi.utils.TableWithTreeTableNodes;

public class PlanningViewMeasurementTable extends TableWithTreeTableNodes
{
	public PlanningViewMeasurementTable(PlanningViewMeasurementTableModel model, FontForObjectTypeProvider fontProvider)
	{
		super(model);	
		renderer = new BasicTableCellRenderer();
	}
	
	public TableCellRenderer getCellRenderer(int row, int tableColumn)
	{
		renderer.setCellBackgroundColor(getColumnBackGroundColor(tableColumn));
		return renderer;
	}

	public Color getColumnBackGroundColor(int column)
	{
		return AppPreferences.MEASUREMENT_COLOR_BACKGROUND;
	}
	
	public String getUniqueTableIdentifier()
	{
		return UNIQUE_IDENTIFIER;
	}
	
	public static final String UNIQUE_IDENTIFIER = "PlanningViewMeasurementTable";

	private BasicTableCellRenderer renderer;
}
