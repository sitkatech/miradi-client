/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.viability;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.TreeSelectionModel;

import org.conservationmeasures.eam.dialogs.treetables.TreeTableNode;
import org.conservationmeasures.eam.dialogs.treetables.TreeTableWithColumnWidthSaving;
import org.conservationmeasures.eam.objects.Measurement;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.ChoiceItem;

public class TargetViabilityTree extends TreeTableWithColumnWidthSaving 
{
	public TargetViabilityTree(Project projectToUse, ViabilityTreeModel targetViabilityModelToUse)
	{
		super(projectToUse, targetViabilityModelToUse);
		setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		getTree().setShowsRootHandles(true);
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		measurementValueRenderer = new MeasurementValueRenderer();
		rebuildTableCompletely();
	}
	
	public String getUniqueTableIdentifier()
	{
		return UNIQUE_IDENTIFIER;
	}
	
	public TableCellRenderer getCellRenderer(int row, int tableColumn)
	{
		TreeTableNode node = (TreeTableNode)getObjectForRow(row);
		int modelColumn = convertColumnIndexToModel(tableColumn);
		String columnTag = getViabilityModel().getColumnTag(modelColumn);
		if(node.getType() == Measurement.getObjectType() && getViabilityModel().isValueColumn(columnTag))
			return measurementValueRenderer;
		return super.getCellRenderer(row, tableColumn);
	}

	class MeasurementValueRenderer extends DefaultTableCellRenderer
	{
		public Component getTableCellRendererComponent(JTable table, Object value, boolean arg2, boolean arg3, int row, int tableColumn)
		{
			Component renderer = super.getTableCellRendererComponent(table, value, arg2, arg3, row, tableColumn);
			if(value != null && ((String)value).trim().length() > 0)
			{
				setAppropriateBackgroundColor(renderer, getColumnTag(tableColumn));
			}
			else
			{
				renderer.setBackground(Color.WHITE);
			}
			return renderer;
		}

		private void setAppropriateBackgroundColor(Component renderer, String columnTag)
		{
			ChoiceItem choice = getViabilityModel().getValueColumnChoice(columnTag);
			renderer.setBackground(choice.getColor());
		}
		
	}
	
	private String getColumnTag(int tableColumn)
	{
		int modelColumn = convertColumnIndexToModel(tableColumn);
		return getViabilityModel().getColumnTag(modelColumn);
	}

	private ViabilityTreeModel getViabilityModel()
	{
		return (ViabilityTreeModel)getTreeTableModel();
	}
	
	public static final String UNIQUE_IDENTIFIER = "TargetViabilityTree";

	TableCellRenderer measurementValueRenderer;
}
