/* 
Copyright 2005-2021, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

package org.miradi.dialogs.indicator;

import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.dialogs.base.ObjectDataInputPanelWithSections;
import org.miradi.icons.IconManager;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objects.Method;
import org.miradi.schemas.MethodSchema;

public class MethodPropertiesPanel extends ObjectDataInputPanelWithSections
{
	public MethodPropertiesPanel(MainWindow mainWindow) throws Exception
	{
		super(mainWindow.getProject(), MethodSchema.getObjectType());

		createSingleSection(EAM.text("Summary"));
		ObjectDataInputField shortLabelField = createStringField(MethodSchema.getObjectType(), Method.TAG_SHORT_LABEL, 10);
		ObjectDataInputField labelField = createExpandableField(MethodSchema.getObjectType(), Method.TAG_LABEL);
		addFieldsOnOneLine(EAM.text("Method"), IconManager.getMethodIcon(), new ObjectDataInputField[]{shortLabelField, labelField,});

		addField(createMultilineField(MethodSchema.getObjectType(), Method.TAG_DETAILS));

		addTaxonomyFields(MethodSchema.getObjectType());

		addField(createMultilineField(MethodSchema.getObjectType(), Method.TAG_COMMENTS));

		addField(createStringField(MethodSchema.getObjectType(), Method.TAG_URL));

		updateFieldsFromProject();
	}

	@Override
	public String getPanelDescription()
	{
		return EAM.text("Title|Method Properties");
	}
}
