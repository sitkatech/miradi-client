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
package org.miradi.objects;

import org.miradi.ids.BaseId;
import org.miradi.objectdata.IntegerData;
import org.miradi.objectdata.TagListData;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.utils.EnhancedJsonObject;
import org.miradi.utils.StringMapData;

public class TableSettings extends BaseObject
{
	public TableSettings(ObjectManager objectManager, BaseId idToUse)
	{
		super(objectManager, idToUse);
		clear();
	}
		
	public TableSettings(ObjectManager objectManager, int idAsInt, EnhancedJsonObject json) throws Exception
	{
		super(objectManager, new BaseId(idAsInt), json);
	}
	
	public int getType()
	{
		return getObjectType();
	}
	
	public String getTypeName()
	{
		return OBJECT_NAME;
	}

	public static int getObjectType()
	{
		return ObjectType.TABLE_SETTINGS;
	}
	
	public static boolean is(ORef ref)
	{
		return is(ref.getObjectType());
	}
	
	public static boolean is(int objectType)
	{
		return objectType == getObjectType();
	}
	
	public static TableSettings find(ObjectManager objectManager, ORef tableSettingsRef)
	{
		return (TableSettings) objectManager.findObject(tableSettingsRef);
	}
	
	public static TableSettings find(Project project, ORef tableSettingsRef)
	{
		return find(project.getObjectManager(), tableSettingsRef);
	}
	
	void clear()
	{
		super.clear();
		
		columnSequenceCodes = new TagListData(TAG_COLUMN_SEQUENCE_CODES);
		columnWidths = new StringMapData(TAG_COLUMN_WIDTHS);
		rowHeights = new IntegerData(TAG_ROW_HEIGHTS);
		
		addField(TAG_COLUMN_SEQUENCE_CODES, columnSequenceCodes);
		addField(TAG_COLUMN_WIDTHS, columnWidths);
		addField(TAG_ROW_HEIGHTS, rowHeights);
	}
	
	public static final String OBJECT_NAME = "TableSettings";
	
	public static final String TAG_COLUMN_SEQUENCE_CODES = "ColumnSequenceCodes";
	public static final String TAG_COLUMN_WIDTHS = "ColumnWidths";
	public static final String TAG_ROW_HEIGHTS = "RowHeights";
	
	private TagListData columnSequenceCodes;
	private StringMapData columnWidths;
	private IntegerData rowHeights;
}
