/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.conservationmeasures.eam.dialogfields.ObjectDataInputPanel;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.views.summary.ObjectListTable;
import org.conservationmeasures.eam.views.summary.ObjectListTableModel;

abstract public class ObjectListTablePanel extends JPanel implements ListSelectionListener
{
	public ObjectListTablePanel(ObjectListTableModel model)
	{
		super(new BorderLayout());
		table = new ObjectListTable(model);
		table.addListSelectionListener(this);
		add(table, BorderLayout.CENTER);

	}

	
	public Indicator getSelectedObject()
	{
		EAMObject[] selected = table.getSelectedObjects();
		if(selected.length == 0)
			return null;
		return (Indicator)selected[0];
	}
	
	public void valueChanged(ListSelectionEvent event)
	{
		try
		{
			BaseId selectedId = BaseId.INVALID;
			int row = table.getSelectedRow();
			if(row >= 0)
			{
				EAMObject selectedObject = table.getObjectListTableModel().getObjectFromRow(row);
				selectedId = selectedObject.getId();
			}
			propertiesPanel.setObjectId(selectedId);
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}
	
	public void setPropertiesPanel(ObjectDataInputPanel panel)
	{
		propertiesPanel = panel;
	}


	ObjectListTable table;
	ObjectDataInputPanel propertiesPanel;
}
