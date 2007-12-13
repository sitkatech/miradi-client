/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.viability;


import javax.swing.JTable;
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
		//FIXME wating for nicks feeback if we want colored headers or not
		//setColumnHeaderRenderers();
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

//FIXME wating for nicks feeback if we want colored headers or not
//	private void setColumnHeaderRenderers()
//	{
//		ColumnHeaderRenderer headerRenderer = new ColumnHeaderRenderer();
//		for (int i = 0; i < getModel().getColumnCount(); ++i)
//		{
//			getTableHeader().setDefaultRenderer(headerRenderer);
//		}
//	}
//
//	public static class ColumnHeaderRenderer extends DefaultTableCellRenderer
//	{
//		public ColumnHeaderRenderer()
//		{
//			statusQuestion = new StatusQuestion("");
//		}
//
//		 public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) 
//		 {
//			 JLabel renderer = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
//			 renderer.setBorder(UIManager.getBorder("TableHeader.cellBorder"));
//			 renderer.setHorizontalAlignment(JLabel.CENTER);
//			 renderer.setForeground(Color.BLACK);
//			 renderer.setBackground(table.getTableHeader().getBackground());
//			 
//			 if (renderer.getText().equals(statusQuestion.getChoices()[1].getLabel()))
//				 renderer.setBackground(statusQuestion.getChoices()[1].getColor());
//			 
//			 if (renderer.getText().equals(statusQuestion.getChoices()[2].getLabel()))
//				 renderer.setBackground(statusQuestion.getChoices()[2].getColor());
//			 
//			 if (renderer.getText().equals(statusQuestion.getChoices()[3].getLabel()))
//				 renderer.setBackground(statusQuestion.getChoices()[3].getColor());
//			 
//			 if (renderer.getText().equals(statusQuestion.getChoices()[4].getLabel()))
//				 renderer.setBackground(statusQuestion.getChoices()[4].getColor());
//			 
//			 return renderer;
//		 }
//
//		 private StatusQuestion statusQuestion;
//	}
	
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
