/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.viability;

import java.awt.Color;
import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.TreeSelectionModel;

import org.conservationmeasures.eam.dialogs.fieldComponents.TreeNodeForRowProvider;
import org.conservationmeasures.eam.dialogs.treetables.ChoiceItemRenderer;
import org.conservationmeasures.eam.dialogs.treetables.TableCellRendererWithColor;
import org.conservationmeasures.eam.dialogs.treetables.TreeTableNode;
import org.conservationmeasures.eam.dialogs.treetables.TreeTableWithColumnWidthSaving;
import org.conservationmeasures.eam.icons.GoalIcon;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.KeyEcologicalAttribute;
import org.conservationmeasures.eam.objects.Measurement;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.ChoiceItem;
import org.conservationmeasures.eam.questions.TrendQuestion;

public class TargetViabilityTreeTable extends TreeTableWithColumnWidthSaving 
{
	public TargetViabilityTreeTable(Project projectToUse, ViabilityTreeModel targetViabilityModelToUse)
	{
		super(projectToUse, targetViabilityModelToUse);
		setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		getTree().setShowsRootHandles(true);
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		getTree().setCellRenderer(new ViabilityTreeCellRenderer());
		measurementValueRenderer = new MeasurementValueRenderer(this);
		otherRenderer = new TableCellRendererWithColor(this);
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
		
		boolean isChoiceItemColumn =
			columnTag == Target.TAG_VIABILITY_MODE || 
			columnTag == Indicator.TAG_STATUS || 
			columnTag == Target.PSEUDO_TAG_TARGET_VIABILITY_VALUE || 
			columnTag == KeyEcologicalAttribute.PSEUDO_TAG_VIABILITY_STATUS_VALUE || 
			columnTag == Measurement.TAG_STATUS_CONFIDENCE;
		if (isChoiceItemColumn)
			return statusQuestionRenderer;
		
		return otherRenderer;
	}

	class MeasurementValueRenderer extends TableCellRendererWithColor
	{
		public MeasurementValueRenderer(TreeNodeForRowProvider providerToUse)
		{
			super(providerToUse);
		}

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int tableColumn)
		{
			JLabel renderer = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, tableColumn);
			String columnTag = nodeProvider.getColumnTag(tableColumn);
			Color color = getBackgroundColor(columnTag);
			renderer.setIcon(null);
			if(value != null && !value.equals(""))
			{
				ChoiceItem choice = getViabilityModel().getValueColumnChoice(columnTag);
				color = choice.getColor();
				renderer.setIcon(getCellIcon(row, choice));
			}
			renderer.setBackground(color);
			if (isSelected)
				setBackground(table.getSelectionBackground());
			
			return renderer;
		}

		public Icon getCellIcon(int row, ChoiceItem choice)
		{
			TreeTableNode node = nodeProvider.getNodeForRow(row);
			if (node.getType() == Goal.getObjectType())
				return new GoalIcon();
			
			if (node.getType() != Measurement.getObjectType())
				return null;
			
			String trendData = node.getObject().getData(Measurement.TAG_TREND);
			return getTrendIcon(trendData);
		}
		
		public Icon getTrendIcon(String measurementTrendCode)
		{
			TrendQuestion trendQuestion = new TrendQuestion(Measurement.TAG_TREND);
			ChoiceItem findChoiceByCode = trendQuestion.findChoiceByCode(measurementTrendCode);
			
			return findChoiceByCode.getIcon();
		}

	}
	
	public class ViabilityTreeCellRenderer extends Renderer
	{
		public ViabilityTreeCellRenderer()
		{
			super();
			indicatorRenderer.setFont(getPlainFont());
		}
	}
	
	private ViabilityTreeModel getViabilityModel()
	{
		return (ViabilityTreeModel)getTreeTableModel();
	}
	
	public static final String UNIQUE_IDENTIFIER = "TargetViabilityTree";

	private TableCellRenderer measurementValueRenderer;
	private TableCellRendererWithColor otherRenderer;
	private ChoiceItemRenderer statusQuestionRenderer;
}
