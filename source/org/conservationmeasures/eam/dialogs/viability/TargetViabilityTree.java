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

import org.conservationmeasures.eam.objects.Measurement;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.ChoiceItem;
import org.conservationmeasures.eam.questions.StatusQuestion;
import org.conservationmeasures.eam.views.GenericTreeTableModel;
import org.conservationmeasures.eam.views.TreeTableNode;
import org.conservationmeasures.eam.views.TreeTableWithColumnWidthSaving;

public class TargetViabilityTree extends TreeTableWithColumnWidthSaving 
{
	public TargetViabilityTree(Project projectToUse, GenericTreeTableModel targetViabilityModelToUse)
	{
		super(projectToUse, targetViabilityModelToUse);
		setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		getTree().setShowsRootHandles(true);
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		statusQuestion = new StatusQuestion("");
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
		String columnTag = getColumnTag(tableColumn);
		if(node.getType() == Measurement.getObjectType() && isValueColumn(columnTag))
			return measurementValueRenderer;
		return super.getCellRenderer(row, tableColumn);
	}

	private String getColumnTag(int tableColumn)
	{
		int modelColumn = convertColumnIndexToModel(tableColumn);
		String columnTag = getTreeTableModel().getColumnTag(modelColumn);
		return columnTag;
	}

	boolean isValueColumn(String columnTag)
	{
		return (statusQuestion.findChoiceByCode(columnTag) != null);
	}

	class MeasurementValueRenderer extends DefaultTableCellRenderer
	{
		public Component getTableCellRendererComponent(JTable table, Object value, boolean arg2, boolean arg3, int row, int tableColumn)
		{
			Component renderer = super.getTableCellRendererComponent(table, value, arg2, arg3, row, tableColumn);
			if(value != null && ((String)value).trim().length() > 0)
				setAppropriateBackgroundColor(renderer, getColumnTag(tableColumn));
			else
				renderer.setBackground(Color.WHITE);
			return renderer;
		}

		private void setAppropriateBackgroundColor(Component renderer, String columnTag)
		{
			ChoiceItem choice = statusQuestion.findChoiceByCode(columnTag);
			renderer.setBackground(choice.getColor());
		}
		
	}
	
	public static final String UNIQUE_IDENTIFIER = "TargetViabilityTree";

	StatusQuestion statusQuestion;
	TableCellRenderer measurementValueRenderer;
}
