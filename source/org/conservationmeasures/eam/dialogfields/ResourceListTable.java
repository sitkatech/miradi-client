/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogfields;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;

import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.views.strategicplan.ObjectPicker;
import org.martus.swing.UiTable;

public class ResourceListTable extends UiTable implements ObjectPicker
{
	public ResourceListTable(ResourceListModel modelToUse)
	{
		super(modelToUse);
		setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
	}
	
	public ResourceListModel getResourceListModel()
	{
		return (ResourceListModel)getModel();
	}
	
	public EAMObject[] getSelectedObjects()
	{
		int[] rows = getSelectedRows();
		EAMObject[] objects = new EAMObject[rows.length];
		for(int i = 0; i < objects.length; ++i)
			objects[i] = getResourceListModel().getObjectFromRow(rows[i]);
		return objects;
	}

	public void addListSelectionListener(ListSelectionListener listener)
	{
		getSelectionModel().addListSelectionListener(listener);
	}
}