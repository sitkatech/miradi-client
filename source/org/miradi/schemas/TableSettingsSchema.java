/* 
Copyright 2005-2012, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.schemas;

import org.miradi.objects.TableSettings;
import org.miradi.questions.SortDirectionQuestion;
import org.miradi.questions.WorkPlanVisibleRowsQuestion;

public class TableSettingsSchema extends BaseObjectSchema
{
	public TableSettingsSchema()
	{
		super();
	}
	
	@Override
	public void fillFieldSchemas()
	{
		createFieldSchemaCode(TableSettings.TAG_TABLE_IDENTIFIER);
		createFieldSchemaInteger(TableSettings.TAG_ROW_HEIGHT);
		createFieldSchemaDateUnitList(TableSettings.TAG_DATE_UNIT_LIST_DATA);
		createFieldSchemaCodeToCodeListMap(TableSettings.TAG_TABLE_SETTINGS_MAP);
		createFieldSchemaChoice(TableSettings.TAG_WORK_PLAN_VISIBLE_NODES_CODE, getQuestion(WorkPlanVisibleRowsQuestion.class));
		
		createFieldSchemaRefListList(TableSettings.TAG_TREE_EXPANSION_LIST).setNavigationField();
		createFieldSchemaTagList(TableSettings.TAG_COLUMN_SEQUENCE_CODES).setNavigationField();
		createFieldSchemaCodeToCodeMap(TableSettings.TAG_COLUMN_WIDTHS).setNavigationField();
		createFieldSchemaCode(TableSettings.TAG_COLUMN_SORT_TAG).setNavigationField();
		createFieldSchemaChoice(TableSettings.TAG_COLUMN_SORT_DIRECTION, getQuestion(SortDirectionQuestion.class)).setNavigationField();
	}
}
