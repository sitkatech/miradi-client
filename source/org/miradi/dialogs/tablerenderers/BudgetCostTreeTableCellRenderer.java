/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.dialogs.tablerenderers;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;

import org.miradi.icons.AllocatedCostIcon;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Task;

public class BudgetCostTreeTableCellRenderer extends NumericTableCellRenderer
{
	public BudgetCostTreeTableCellRenderer(RowColumnBaseObjectProvider providerToUse, FontForObjectTypeProvider fontProviderToUse)
	{
		super(providerToUse, fontProviderToUse);
		allocatedIcon = new AllocatedCostIcon();
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int tableColumn)
	{
		JLabel renderer = (JLabel)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, tableColumn);
		String text = annotateIfOverride(row, tableColumn, renderer, value); 
		annotateIfAllocated(row, tableColumn, renderer, text);
		
		return renderer;
	}

	private void annotateIfAllocated(int row, int tableColumn, JLabel labelComponent, String text)
	{
		if(text == null)
			text = "";
		
		labelComponent.setIcon(null);
		labelComponent.setText(text);
		
		if(labelComponent.getText().length() == 0)
			return;
		
		BaseObject node = getBaseObjectForRow(row, tableColumn);
		if(node.getType() != Task.getObjectType())
			return;
		
		double nodeCostAlloctionProportion = calculateAllocationProportion((Task)node);
		if (Double.compare(nodeCostAlloctionProportion, 1.0) < 0)
			labelComponent.setIcon(allocatedIcon);
	}
	
	public double calculateAllocationProportion(Task task)
	{
		ORefList referrers = task.findObjectsThatReferToUs();
		return (1.0 / referrers.size());
	}
	
	private String annotateIfOverride(int row, int tableColumn, JLabel labelComponent, Object value)
	{
		if(value == null)
			return null;
		
		String baseText = value.toString();
		BaseObject object = getBaseObjectForRow(row, tableColumn);
		if(object == null)
			return baseText;
		
		if(object.isBudgetOverrideMode())
			return "~ " + baseText;
		
		return baseText;
	}
	
	Icon allocatedIcon;
}
