/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.planning.upperPanel;

import java.awt.Color;

import javax.swing.table.TableCellRenderer;

import org.miradi.diagram.renderers.FactorRenderer;
import org.miradi.dialogs.tablerenderers.FontForObjectTypeProvider;
import org.miradi.dialogs.tablerenderers.TableCellRendererForObjects;
import org.miradi.utils.TableWithTreeTableNodes;

public class PlanningViewFutureStatusTable extends TableWithTreeTableNodes
{
	public PlanningViewFutureStatusTable(PlanningViewFutureStatusTableModel model, FontForObjectTypeProvider fontProvider)
	{
		super(model);
		renderer = new TableCellRendererForObjects(model, fontProvider);
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

	private TableCellRendererForObjects renderer;
}
