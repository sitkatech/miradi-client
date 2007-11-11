/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning.propertiesPanel;

import java.awt.Color;

import javax.swing.table.TableCellRenderer;

import org.conservationmeasures.eam.diagram.renderers.FactorRenderer;
import org.conservationmeasures.eam.dialogs.tablerenderers.BasicTableCellRenderer;
import org.conservationmeasures.eam.utils.TableWithTreeTableNodes;

public class PlanningViewFutureStatusTable extends TableWithTreeTableNodes
{
	public PlanningViewFutureStatusTable(PlanningViewFutureStatusTableModel model)
	{
		super(model);
		renderer = new BasicTableCellRenderer();
	}
	
	public TableCellRenderer getCellRenderer(int row, int tableColumn)
	{
		renderer.setCellBackgroundColor(getColumnBackGroundColor(tableColumn));
		return renderer;
	}

	public Color getColumnBackGroundColor(int tableColumn)
	{
		return FactorRenderer.ANNOTATIONS_COLOR;
	}
	
	public String getUniqueTableIdentifier()
	{
		return UNIQUE_IDENTIFIER;
	}
	
	public static final String UNIQUE_IDENTIFIER = "PlanningViewFutureStatusTable";

	private BasicTableCellRenderer renderer;
}
