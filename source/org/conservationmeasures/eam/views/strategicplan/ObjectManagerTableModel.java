/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.project.ObjectPool;
import org.martus.swing.UiTableModel;

class ObjectManagerTableModel extends UiTableModel
{
	public ObjectManagerTableModel(ObjectPool resourcePool, String[] columnTagsToUse)
	{
		pool = resourcePool;
		columnTags = columnTagsToUse;
	}
	
	public boolean isEnabled(int row)
	{
		return false;
	}

	public int getColumnCount()
	{
		return columnTags.length;
	}

	public int getRowCount()
	{
		return pool.size();
	}

	public Object getValueAt(int rowIndex, int columnIndex)
	{
		int objectId = pool.getIds()[rowIndex];
		EAMObject object = (EAMObject)pool.getRawObject(objectId);
		return object.getData(columnTags[columnIndex]);
	}

	public String getColumnName(int column)
	{
		return columnTags[column];
	}
	
	ObjectPool pool;
	String[] columnTags;
}