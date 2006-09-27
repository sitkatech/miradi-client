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

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objectpools.EAMObjectPool;
import org.conservationmeasures.eam.objects.EAMObject;
import org.martus.swing.UiTableModel;

class ObjectManagerTableModel extends UiTableModel
{
	public ObjectManagerTableModel(EAMObjectPool resourcePool, String[] columnTagsToUse)
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
		EAMObject object = getObjectFromRow(rowIndex);
		return object.getData(columnTags[columnIndex]);
	}

	public EAMObject getObjectFromRow(int rowIndex)
	{
		BaseId objectId = pool.getIds()[rowIndex];
		return (EAMObject)pool.getRawObject(objectId);
	}

	public String getColumnName(int column)
	{
		return EAM.fieldLabel(pool.getObjectType(), columnTags[column]);
	}
	
	EAMObjectPool pool;
	String[] columnTags;
}