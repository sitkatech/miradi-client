/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.tablerenderers;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;

import org.conservationmeasures.eam.dialogs.planning.treenodes.PlanningTreeTaskNode;
import org.conservationmeasures.eam.dialogs.treetables.TreeTableNode;
import org.conservationmeasures.eam.icons.AllocatedCostIcon;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Task;

import com.java.sun.jtreetable.TreeTableModelAdapter;

public class BudgetCostTreeTableCellRenderer extends NumericTableCellRenderer
{
	public BudgetCostTreeTableCellRenderer(RowColumnBaseObjectProvider providerToUse, TreeTableModelAdapter adapterToUse, FontForObjectTypeProvider fontProviderToUse)
	{
		super(providerToUse, fontProviderToUse);
		allocatedIcon = new AllocatedCostIcon();
		adapter = adapterToUse;
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int tableColumn)
	{
		JLabel renderer = (JLabel)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, tableColumn);
		String text = annotateIfOverride(row, renderer, value); 
		annotateIfAllocated(row, renderer, text);
		
		return renderer;
	}

	private void annotateIfAllocated(int row, JLabel labelComponent, String text)
	{
		if(text == null)
			text = "";
		
		labelComponent.setIcon(null);
		labelComponent.setText(text);
		
		if(labelComponent.getText().length() == 0)
			return;
		
		TreeTableNode node = getNodeForRow(row);
		if(node.getType() != Task.getObjectType())
			return;
		
		PlanningTreeTaskNode taskNode = (PlanningTreeTaskNode) node;
		double nodeCostAlloctionProportion = taskNode.getCostAllocationProportion();
		
		if (Double.compare(nodeCostAlloctionProportion, 1.0) < 0)
			labelComponent.setIcon(allocatedIcon);
		
	}
	
	private String annotateIfOverride(int row, JLabel labelComponent, Object value)
	{
		if(value == null)
			return null;
		
		String baseText = value.toString();
		
		TreeTableNode node = getNodeForRow(row);
		BaseObject object = node.getObject();
		if(object == null)
			return baseText;
		
		if(object.isBudgetOverrideMode())
			return "~ " + baseText;
		
		return baseText;
	}
	
	protected TreeTableNode getNodeForRow(int row)
	{
		return (TreeTableNode)adapter.nodeForRow(row);
	}
	
	TreeTableModelAdapter adapter;
	Icon allocatedIcon;
}
