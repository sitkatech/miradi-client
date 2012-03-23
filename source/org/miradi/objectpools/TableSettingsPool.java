/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.objectpools;

import org.miradi.ids.BaseId;
import org.miradi.ids.IdAssigner;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.objects.TableSettings;
import org.miradi.project.ObjectManager;
import org.miradi.questions.SortDirectionQuestion;
import org.miradi.questions.WorkPlanVisibleRowsQuestion;
import org.miradi.schemas.BaseObjectSchema;

public class TableSettingsPool extends EAMNormalObjectPool
{
	public TableSettingsPool(IdAssigner idAssignerToUse)
	{
		super(idAssignerToUse, ObjectType.TABLE_SETTINGS);
	}
	
	public void put(TableSettings tableSettings) throws Exception
	{
		put(tableSettings.getId(), tableSettings);
	}
	
	public TableSettings find(BaseId id)
	{
		return (TableSettings) getRawObject(id);
	}

	@Override
	BaseObject createRawObject(ObjectManager objectManager, BaseId actualId)
	{
		return new TableSettings(objectManager, actualId);
	}
	
	@Override
	public BaseObjectSchema createSchema()
	{
		final BaseObjectSchema schema = super.createSchema();
		
		schema.createFieldSchemaCodeField(TableSettings.TAG_TABLE_IDENTIFIER);
		schema.createFieldSchemaIntegerField(TableSettings.TAG_ROW_HEIGHT);
		schema.createFieldSchemaDateUnitListField(TableSettings.TAG_DATE_UNIT_LIST_DATA);
		schema.createFieldSchemaCodeToCodeListMapField(TableSettings.TAG_TABLE_SETTINGS_MAP);
		schema.createFieldSchemaChoice(TableSettings.TAG_WORK_PLAN_VISIBLE_NODES_CODE, getQuestion(WorkPlanVisibleRowsQuestion.class));
		
		schema.createFieldSchemaRefListList(TableSettings.TAG_TREE_EXPANSION_LIST);
		schema.setNonUserField(TableSettings.TAG_TREE_EXPANSION_LIST);
		schema.createFieldSchemaTagList(TableSettings.TAG_COLUMN_SEQUENCE_CODES);
		schema.setNonUserField(TableSettings.TAG_COLUMN_SEQUENCE_CODES);
		schema.createFieldSchemaCodeToCodeMapField(TableSettings.TAG_COLUMN_WIDTHS);
		schema.setNonUserField(TableSettings.TAG_COLUMN_WIDTHS);
		schema.createFieldSchemaCodeField(TableSettings.TAG_COLUMN_SORT_TAG);
		schema.setNonUserField(TableSettings.TAG_COLUMN_SORT_TAG);
		schema.createFieldSchemaChoice(TableSettings.TAG_COLUMN_SORT_DIRECTION, getQuestion(SortDirectionQuestion.class));
		schema.setNonUserField(TableSettings.TAG_COLUMN_SORT_DIRECTION);
		
		return schema;
	}
}
