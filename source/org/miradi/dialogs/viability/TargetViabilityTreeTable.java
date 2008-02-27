/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.viability;


import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.TreeSelectionModel;

import org.miradi.dialogs.tablerenderers.BasicTableCellRenderer;
import org.miradi.dialogs.tablerenderers.ChoiceItemTableCellRenderer;
import org.miradi.dialogs.tablerenderers.FontForObjectTypeProvider;
import org.miradi.dialogs.tablerenderers.RowColumnBaseObjectProvider;
import org.miradi.dialogs.tablerenderers.TableCellRendererForObjects;
import org.miradi.dialogs.tablerenderers.ViabilityViewFontProvider;
import org.miradi.dialogs.treetables.TreeTableNode;
import org.miradi.dialogs.treetables.TreeTableWithColumnWidthSaving;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Goal;
import org.miradi.objects.Indicator;
import org.miradi.objects.KeyEcologicalAttribute;
import org.miradi.objects.Measurement;
import org.miradi.objects.Target;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.StatusQuestion;

public class TargetViabilityTreeTable extends TreeTableWithColumnWidthSaving implements RowColumnBaseObjectProvider 
{
	public TargetViabilityTreeTable(Project projectToUse, GenericViabilityTreeModel targetViabilityModelToUse) throws Exception
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
		TreeTableNode node = (TreeTableNode)getRawObjectForRow(row);
		int modelColumn = convertColumnIndexToModel(tableColumn);
		String columnTag = getViabilityModel().getColumnTag(modelColumn);
		boolean isMeasurementNode = node.getType() == Measurement.getObjectType();
		boolean isFutureStatusNode = node.getType() == Goal.getObjectType();
		boolean isValueColumn = getViabilityModel().isChoiceItemColumn(columnTag);
		if((isMeasurementNode || isFutureStatusNode) && isValueColumn)
		{
			measurementValueRenderer.setColumnTag(columnTag);
			return measurementValueRenderer;
		}
		
		boolean isChoiceItemColumn =
			columnTag == Target.TAG_VIABILITY_MODE || 
			columnTag == Indicator.TAG_STATUS ||
			columnTag == Indicator.PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE ||
			columnTag == Target.PSEUDO_TAG_TARGET_VIABILITY || 
			columnTag == KeyEcologicalAttribute.PSEUDO_TAG_VIABILITY_STATUS || 
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
			statusQuestion = new StatusQuestion();
		}

		 public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) 
		 {
			 JLabel renderer = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			 renderer.setBorder(UIManager.getBorder("TableHeader.cellBorder"));
			 renderer.setHorizontalAlignment(JLabel.CENTER);
			 renderer.setBackground(table.getTableHeader().getBackground());
			 renderer.setForeground(table.getTableHeader().getForeground());
			
			 ChoiceItem choice = statusQuestion.findChoiceByLabel(renderer.getText());
			 if (choice != null)
			 {
				renderer.setBackground(choice.getColor());
			 	renderer.setForeground(Color.BLACK);
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
			goalRenderer.setFont(getPlainFont());
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
