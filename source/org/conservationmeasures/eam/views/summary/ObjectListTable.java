/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.summary;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;

import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.views.strategicplan.ObjectPicker;
import org.martus.swing.UiTable;

public class ObjectListTable extends UiTable implements ObjectPicker
{
	public ObjectListTable(ObjectListTableModel modelToUse)
	{
		super(modelToUse);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		resizeTable(4);
	}
	
	public ObjectListTableModel getObjectListTableModel()
	{
		return (ObjectListTableModel)getModel();
	}
	
	public EAMObject[] getSelectedObjects()
	{
		int[] rows = getSelectedRows();
		EAMObject[] objects = new EAMObject[rows.length];
		for(int i = 0; i < objects.length; ++i)
			objects[i] = getObjectListTableModel().getObjectFromRow(rows[i]);
		return objects;
	}

	public void addListSelectionListener(ListSelectionListener listener)
	{
		getSelectionModel().addListSelectionListener(listener);
	}
}