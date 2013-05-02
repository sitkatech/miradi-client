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

package org.miradi.views.summary;

import javax.swing.Icon;

import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.icons.MiradiShareIcon;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.MiradiShareProjectData;
import org.miradi.objects.ProjectMetadata;
import org.miradi.project.Project;
import org.miradi.schemas.MiradiShareProjectDataSchema;

public class SummaryMiradiSharePanel extends ObjectDataInputPanel
{
	public SummaryMiradiSharePanel(Project projectToUse) throws Exception
	{
		super(projectToUse, getSingletonRefs(projectToUse));
		
		addField(createReadonlyTextField(MiradiShareProjectDataSchema.getObjectType(), MiradiShareProjectData.TAG_PROJECT_ID));
		addField(createReadonlyTextField(MiradiShareProjectDataSchema.getObjectType(), MiradiShareProjectData.TAG_PROJECT_URL));
		addField(createReadonlyTextField(MiradiShareProjectDataSchema.getObjectType(), MiradiShareProjectData.TAG_PROGRAM_ID));
		addField(createReadonlyTextField(MiradiShareProjectDataSchema.getObjectType(), MiradiShareProjectData.TAG_PROGRAM_NAME));
		addField(createReadonlyTextField(MiradiShareProjectDataSchema.getObjectType(), MiradiShareProjectData.TAG_PROGRAM_URL));
		addField(createReadonlyTextField(MiradiShareProjectDataSchema.getObjectType(), MiradiShareProjectData.TAG_PROJECT_TEMPLATE_ID));
		addField(createReadonlyTextField(MiradiShareProjectDataSchema.getObjectType(), MiradiShareProjectData.TAG_PROJECT_TEMPLATE_NAME));
		addField(createReadonlyTextField(MiradiShareProjectDataSchema.getObjectType(), MiradiShareProjectData.TAG_PROJECT_VERSION));
		addField(createReadonlyTextField(MiradiShareProjectDataSchema.getObjectType(), MiradiShareProjectData.TAG_PROGRAM_TAXONOMY_SET_NAME));
		addField(createReadonlyTextField(MiradiShareProjectDataSchema.getObjectType(), MiradiShareProjectData.TAG_PROGRAM_TAXONOMY_SET_VERSION_ID));
		addField(createReadonlyTextField(MiradiShareProjectDataSchema.getObjectType(), MiradiShareProjectData.TAG_PROGRAM_TAXONOMY_SET_VERSION));
		addFieldWithCustomLabel(createSingleStringToRefField(ProjectMetadata.TAG_XENODATA_STRING_REF_MAP, "Tanager"), EAM.text("External Project Id"));

		updateFieldsFromProject();
	}

	private static ORef[] getSingletonRefs(Project projectToUse)
	{
		ORef miradiShareProjectDataRef = projectToUse.getSafeSingleObjectRef(MiradiShareProjectDataSchema.getObjectType());
		ORef metadataRef = projectToUse.getMetadata().getRef();
		
		return new ORef[]{metadataRef, miradiShareProjectDataRef, };
	}

	@Override
	public String getPanelDescription()
	{
		return getPlanningPanelDescription();
	}

	public static String getPlanningPanelDescription()
	{
		return EAM.text("Label|Miradi Share");
	}
	
	@Override
	public Icon getIcon()
	{
		return new MiradiShareIcon();
	}	
}
