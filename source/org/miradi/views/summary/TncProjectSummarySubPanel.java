/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
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

package org.miradi.views.summary;

import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.TncProjectData;
import org.miradi.project.Project;
import org.miradi.questions.*;
import org.miradi.schemas.ProjectMetadataSchema;
import org.miradi.schemas.TncProjectDataSchema;

public class TncProjectSummarySubPanel extends ObjectDataInputPanel
{
	public TncProjectSummarySubPanel(Project projectToUse, ORef[] refs) throws Exception
	{
		super(projectToUse, refs);
		
		addField(createReadonlyTextField(ProjectMetadata.TAG_TNC_DATABASE_DOWNLOAD_DATE));
		addFieldWithCustomLabel(createSingleStringToRefField(ProjectMetadata.TAG_XENODATA_STRING_REF_MAP, CONPRO_CONTEXT), EAM.text("ConPro Project Number"));
		addField(createStringField(ProjectMetadataSchema.getObjectType(), ProjectMetadata.TAG_OTHER_ORG_RELATED_PROJECTS));
		addField(createRadioButtonEditorField(TncProjectDataSchema.getObjectType(), TncProjectData.TAG_PROJECT_FOCUS, StaticQuestionManager.getQuestion(ProjectFocusQuestion.class)));
		addField(createRadioButtonEditorField(TncProjectDataSchema.getObjectType(), TncProjectData.TAG_PROJECT_SCALE, StaticQuestionManager.getQuestion(ProjectScaleQuestion.class)));
		ChoiceQuestion tncProjectSharingQuestion = StaticQuestionManager.getQuestion(ProjectSharingQuestion.class);
		addRadioButtonFieldWithCustomLabelAndLink(TncProjectDataSchema.getObjectType(), TncProjectData.TAG_PROJECT_SHARING_CODE, tncProjectSharingQuestion, "", "ProjectSharingMessage.html");

		addField(createReadonlyTextField(TncProjectDataSchema.getObjectType(), TncProjectData.TAG_CON_PRO_PARENT_CHILD_PROJECT_TEXT));

		addField(createSingleColumnCodeListField(ProjectMetadataSchema.getObjectType(), ProjectMetadata.TAG_TNC_OPERATING_UNITS, new TncOperatingUnitsQuestion()));

		addField(createQuestionFieldWithDescriptionPanel(ProjectMetadataSchema.getObjectType(), ProjectMetadata.TAG_TNC_TERRESTRIAL_ECO_REGION, new TncTerrestrialEcoRegionQuestion()));
		addField(createSingleColumnCodeListField(ProjectMetadataSchema.getObjectType(), ProjectMetadata.TAG_TNC_MARINE_ECO_REGION, new TncMarineEcoRegionQuestion()));
		addField(createSingleColumnCodeListField(ProjectMetadataSchema.getObjectType(), ProjectMetadata.TAG_TNC_FRESHWATER_ECO_REGION, new TncFreshwaterEcoRegionQuestion()));
		
		updateFieldsFromProject();
	}

	@Override
	public String getPanelDescription()
	{
		return PANEL_DESCRIPTION;
	}
	
	public static final String PANEL_DESCRIPTION = EAM.text("Label|Project Info");
	public static final String CONPRO_CONTEXT = "ConPro";
}
