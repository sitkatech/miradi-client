/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.viability;


import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.TreeSelectionModel;

import org.conservationmeasures.eam.dialogs.tablerenderers.BasicTableCellRenderer;
import org.conservationmeasures.eam.dialogs.tablerenderers.ChoiceItemTableCellRenderer;
import org.conservationmeasures.eam.dialogs.tablerenderers.FontForObjectTypeProvider;
import org.conservationmeasures.eam.dialogs.tablerenderers.RowColumnBaseObjectProvider;
import org.conservationmeasures.eam.dialogs.tablerenderers.TableCellRendererForObjects;
import org.conservationmeasures.eam.dialogs.tablerenderers.ViabilityViewFontProvider;
import org.conservationmeasures.eam.dialogs.treetables.TreeTableNode;
import org.conservationmeasures.eam.dialogs.treetables.TreeTableWithColumnWidthSaving;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.KeyEcologicalAttribute;
import org.conservationmeasures.eam.objects.Measurement;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.ChoiceItem;
import org.conservationmeasures.eam.questions.StatusQuestion;

public class TargetViabilityTreeTable extends TreeTableWithColumnWidthSaving implements RowColumnBaseObjectProvider 
{
	public TargetViabilityTreeTable(Project projectToUse, GenericViabilityTreeModel targetViabilityModelToUse)
	{
		super(projectToUse, targetViabilityModelToUse);
		FontForObjectTypeProvider fontProvider = new ViabilityViewFontProvider();
		setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		getTree().setShowsRootHandles(true);
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		getTree().setCellRenderer(new ViabilityTreeCellRenderer());
		setColumnHeaderRenderers();
		measurementValueRenderer = new MeasurementValueRenderer(this, fontProvider);
		otherRenderer = new TableCellRendererForObjects(this, fontProvider);
		statusQuestionRenderer = new ChoiceItemTableCellRenderer(this, fontProvider);
		rebuildTableCompletely();
	}
	
	public String getUniqueTableIdentifier()
	{
		return UNIQUE_IDENTIFIER;
	}
	
	public BaseObject getBaseObjectForRowColumn(int row, int column)
	{
		return getNodeForRow(row).getObject();
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
		boolean isFutureStatusNode = node.getType() == Goal.getObjectType() && node.getType() == Indicator.getObjectType();
		boolean isValueColumn = getViabilityModel().isChoiceItemColumn(columnTag);
		if((isMeasurementNode || isIndicatorNode || isFutureStatusNode) && isValueColumn)
		{
			measurementValueRenderer.setColumnTag(columnTag);
			return measurementValueRenderer;
		}
		
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
	private void setColumnHeaderRenderers()
	{
		ColumnHeaderRenderer headerRenderer = new ColumnHeaderRenderer();
		for (int i = 0; i < getModel().getColumnCount(); ++i)
		{
			getTableHeader().setDefaultRenderer(headerRenderer);
		}
	}

	public static class ColumnHeaderRenderer extends DefaultTableCellRenderer
	{
		public ColumnHeaderRenderer()
		{
			statusQuestion = new StatusQuestion("");
		}

		 public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) 
		 {
			 JLabel renderer = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			 renderer.setBorder(UIManager.getBorder("TableHeader.cellBorder"));
			 renderer.setHorizontalAlignment(JLabel.CENTER);
			 renderer.setBackground(table.getTableHeader().getBackground());
			 renderer.setForeground(table.getTableHeader().getForeground());
			
			 ChoiceItem[] choices = statusQuestion.getChoices();
			 for (int i = 0; i <  choices.length; ++i)
			 {
				 if (renderer.getText().equals(choices[i].getLabel()))
				 {
					 renderer.setBackground(choices[i].getColor());
					 renderer.setForeground(Color.BLACK);
				 }
			 }

			 return renderer;
		 }

		 private StatusQuestion statusQuestion;
	}
	
	public class ViabilityTreeCellRenderer extends Renderer
	{
		public ViabilityTreeCellRenderer()
		{
			super();
			indicatorRenderer.setFont(getPlainFont());
		}
	}
	
	private GenericViabilityTreeModel getViabilityModel()
	{
		return (GenericViabilityTreeModel)getTreeTableModel();
	}
	
	public static final String UNIQUE_IDENTIFIER = "TargetViabilityTree";

	private MeasurementValueRenderer measurementValueRenderer;
	private BasicTableCellRenderer otherRenderer;
	private ChoiceItemTableCellRenderer statusQuestionRenderer;
}
