/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import java.text.ParseException;

import javax.swing.table.AbstractTableModel;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.project.Project;

public class ObjectListTableModel extends AbstractTableModel
{
	public ObjectListTableModel(Project projectToUse, int objectType, BaseId objectId, 
			String idListFieldTag, int listedItemType, String tableColumnTag)
	{
		project = projectToUse;
		type = objectType;
		id = objectId;
		tagOfIdList = idListFieldTag;
		rowObjectType = listedItemType;
		columnTag = tableColumnTag;
	}
	
	public int getColumnCount()
	{
		return 1;
	}

	public String getColumnName(int column)
	{
		return EAM.fieldLabel(rowObjectType, columnTag);
	}

	public int getRowCount()
	{
		try
		{
			return getIdList().size();
		}
		catch(ParseException e)
		{
			EAM.logException(e);
			throw new RuntimeException("Parse error reading IdList " + tagOfIdList);
		}
	}

	private IdList getIdList() throws ParseException
	{
		return new IdList(getObject().getData(tagOfIdList));
	}

	private EAMObject getObject()
	{
		return project.findObject(type, id);
	}

	public Object getValueAt(int row, int column)
	{
		try
		{
			EAMObject rowObject;
			rowObject = getObjectFromRow(row);
			return rowObject.getData(columnTag);
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return "(Error)";
		}
	}

	public EAMObject getObjectFromRow(int row) throws RuntimeException
	{
		try
		{
			BaseId rowObjectId = getIdList().get(row);
			EAMObject rowObject = project.findObject(rowObjectType, rowObjectId);
			return rowObject;
		}
		catch(Exception e)
		{
			EAM.logException(e);
			throw new RuntimeException("TeamModel.getObjectFromRow error");
		}
	}

	Project project;
	int type;
	BaseId id;
	String tagOfIdList;
	int rowObjectType;
	String columnTag;
}
