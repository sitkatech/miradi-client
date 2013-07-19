/* 
Copyright 2005-2013, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.forms.objects;

import org.miradi.forms.FieldPanelSpec;
import org.miradi.main.EAM;
import org.miradi.objects.MiradiShareProjectData;
import org.miradi.objects.ProjectMetadata;
import org.miradi.schemas.MiradiShareProjectDataSchema;
import org.miradi.schemas.ProjectMetadataSchema;

public class MiradiShareProjectDataForm extends FieldPanelSpec
{
	public MiradiShareProjectDataForm()
	{
		addLabelAndReadOnlySingeLineField(MiradiShareProjectDataSchema.getObjectType(), MiradiShareProjectData.TAG_PROJECT_ID);
		addLabelAndReadOnlySingeLineField(MiradiShareProjectDataSchema.getObjectType(), MiradiShareProjectData.TAG_PROJECT_URL);
		addLabelAndReadOnlySingeLineField(MiradiShareProjectDataSchema.getObjectType(), MiradiShareProjectData.TAG_PROGRAM_ID);
		addLabelAndReadOnlySingeLineField(MiradiShareProjectDataSchema.getObjectType(), MiradiShareProjectData.TAG_PROGRAM_NAME);
		addLabelAndReadOnlySingeLineField(MiradiShareProjectDataSchema.getObjectType(), MiradiShareProjectData.TAG_PROGRAM_URL);
		addLabelAndReadOnlySingeLineField(MiradiShareProjectDataSchema.getObjectType(), MiradiShareProjectData.TAG_PROJECT_TEMPLATE_ID);
		addLabelAndReadOnlySingeLineField(MiradiShareProjectDataSchema.getObjectType(), MiradiShareProjectData.TAG_PROJECT_TEMPLATE_NAME);
		addLabelAndReadOnlySingeLineField(MiradiShareProjectDataSchema.getObjectType(), MiradiShareProjectData.TAG_PROJECT_VERSION);
		addLabelAndReadOnlySingeLineField(MiradiShareProjectDataSchema.getObjectType(), MiradiShareProjectData.TAG_PROGRAM_TAXONOMY_SET_NAME);
		addLabelAndReadOnlySingeLineField(MiradiShareProjectDataSchema.getObjectType(), MiradiShareProjectData.TAG_PROGRAM_TAXONOMY_SET_VERSION_ID);
		addLabelAndReadOnlySingeLineField(MiradiShareProjectDataSchema.getObjectType(), MiradiShareProjectData.TAG_PROGRAM_TAXONOMY_SET_VERSION);
		addLabelAndExternalProjectIdField(ProjectMetadataSchema.getObjectType(), ProjectMetadata.TAG_XENODATA_STRING_REF_MAP, "Tanager", EAM.text("Miradi Share Project Id"));
		addMultipleTaxonomyWithEditButtonFields(MiradiShareProjectDataSchema.getObjectType(), MiradiShareProjectData.TAG_TAXONOMY_CLASSIFICATION_CONTAINER);
	}
}
