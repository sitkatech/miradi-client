/* 
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
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

package org.miradi.forms.objects;

import org.miradi.forms.FieldPanelSpec;
import org.miradi.objects.MiradiShareProjectData;
import org.miradi.schemas.MiradiShareProjectDataSchema;

public class MiradiShareProjectDataDetailsForm extends FieldPanelSpec
{
	public MiradiShareProjectDataDetailsForm()
	{
		addLabelAndReadOnlySingleLineField(MiradiShareProjectDataSchema.getObjectType(), MiradiShareProjectData.TAG_PROJECT_ID);
		addLabelAndReadOnlySingleLineField(MiradiShareProjectDataSchema.getObjectType(), MiradiShareProjectData.TAG_PROJECT_URL);
		addLabelAndReadOnlySingleLineField(MiradiShareProjectDataSchema.getObjectType(), MiradiShareProjectData.TAG_PROGRAM_ID);
		addLabelAndReadOnlySingleLineField(MiradiShareProjectDataSchema.getObjectType(), MiradiShareProjectData.TAG_PROGRAM_NAME);
		addLabelAndReadOnlySingleLineField(MiradiShareProjectDataSchema.getObjectType(), MiradiShareProjectData.TAG_PROGRAM_URL);
		addLabelAndReadOnlySingleLineField(MiradiShareProjectDataSchema.getObjectType(), MiradiShareProjectData.TAG_PROJECT_TEMPLATE_ID);
		addLabelAndReadOnlySingleLineField(MiradiShareProjectDataSchema.getObjectType(), MiradiShareProjectData.TAG_PROJECT_TEMPLATE_NAME);
		addLabelAndReadOnlySingleLineField(MiradiShareProjectDataSchema.getObjectType(), MiradiShareProjectData.TAG_PROJECT_VERSION);
		addLabelAndReadOnlySingleLineField(MiradiShareProjectDataSchema.getObjectType(), MiradiShareProjectData.TAG_PROGRAM_TAXONOMY_SET_NAME);
		addLabelAndReadOnlySingleLineField(MiradiShareProjectDataSchema.getObjectType(), MiradiShareProjectData.TAG_PROGRAM_TAXONOMY_SET_VERSION_ID);
		addLabelAndReadOnlySingleLineField(MiradiShareProjectDataSchema.getObjectType(), MiradiShareProjectData.TAG_PROGRAM_TAXONOMY_SET_VERSION);
	}
}
