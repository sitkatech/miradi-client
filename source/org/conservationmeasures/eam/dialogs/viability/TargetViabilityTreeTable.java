/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.viability;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.TreeSelectionModel;

import org.conservationmeasures.eam.dialogs.treetables.ChoiceItemRenderer;
import org.conservationmeasures.eam.dialogs.treetables.TreeTableCellRendererWithColor;
import org.conservationmeasures.eam.dialogs.treetables.TreeTableNode;
import org.conservationmeasures.eam.dialogs.treetables.TreeTableWithColumnWidthSaving;
import org.conservationmeasures.eam.dialogs.treetables.TreeTableWithIcons;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.KeyEcologicalAttribute;
import org.conservationmeasures.eam.objects.Measurement;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.ChoiceItem;

public class TargetViabilityTreeTable extends TreeTableWithColumnWidthSaving 
{
	public TargetViabilityTreeTable(Project projectToUse, ViabilityTreeModel targetViabilityModelToUse)
	{
		super(projectToUse, targetViabilityModelToUse);
		setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		getTree().setShowsRootHandles(true);
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		measurementValueRenderer = new MeasurementValueRenderer(this);
		otherRenderer = new TreeTableCellRendererWithColor(this);
		statusQuestionRenderer = new ChoiceItemRenderer(this);
		rebuildTableCompletely();
	}
	
	public String getUniqueTableIdentifier()
	{
		return UNIQUE_IDENTIFIER;
	}
	
	public TableCellRenderer getCellRenderer(int row, int tableColumn)
	{
		if(tableColumn == 0)
			return super.getCellRenderer(row, tableColumn);
		TreeTableNode node = (TreeTableNode)getObjectForRow(row);
		int modelColumn = convertColumnIndexToModel(tableColumn);
		String columnTag = getViabilityModel().getColumnTag(modelColumn);
		boolean isMeasurementNode = node.getType() == Measurement.getObjectType();
		boolean isIndicatorNode = node.getType() == Indicator.getObjectType();
		boolean isFutureStatusNode = node.getType() == Goal.getObjectType() && node.getObject().getType() == Indicator.getObjectType();
		boolean isValueColumn = getViabilityModel().isValueColumn(columnTag);
		if((isMeasurementNode || isIndicatorNode || isFutureStatusNode) && isValueColumn)
			return measurementValueRenderer;
		
		boolean isChoiceItemColumn = columnTag == Indicator.TAG_STATUS || 
									 columnTag == Target.PSEUDO_TAG_TARGET_VIABILITY_VALUE || 
									 columnTag == KeyEcologicalAttribute.PSEUDO_TAG_VIABILITY_STATUS_VALUE || 
									 columnTag == Measurement.TAG_STATUS;
		if (isChoiceItemColumn)
			return statusQuestionRenderer;
		
		return otherRenderer;
	}

	class MeasurementValueRenderer extends TreeTableCellRendererWithColor
	{
		public MeasurementValueRenderer(TreeTableWithIcons treeTableToUse)
		{
			super(treeTableToUse);
		}

		public Component getTableCellRendererComponent(JTable table, Object value, boolean arg2, boolean arg3, int row, int tableColumn)
		{
			Component renderer = super.getTableCellRendererComponent(table, value, arg2, arg3, row, tableColumn);
			String columnTag = getColumnTag(tableColumn);
			Color color = getBackgroundColor(columnTag);
			if(value != null && ((String)value).trim().length() > 0)
			{
				ChoiceItem choice = getViabilityModel().getValueColumnChoice(getColumnTag(tableColumn));
				color = choice.getColor();
			}
			renderer.setBackground(color);
			return renderer;
		}
	}
	
	public String getColumnTag(int tableColumn)
	{
		int modelColumn = convertColumnIndexToModel(tableColumn);
		return getViabilityModel().getColumnTag(modelColumn);
	}

	private ViabilityTreeModel getViabilityModel()
	{
		return (ViabilityTreeModel)getTreeTableModel();
	}
	
	public static final String UNIQUE_IDENTIFIER = "TargetViabilityTree";

	private TableCellRenderer measurementValueRenderer;
	private TreeTableCellRendererWithColor otherRenderer;
	private ChoiceItemRenderer statusQuestionRenderer;
}
