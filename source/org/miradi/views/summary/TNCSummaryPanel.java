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
package org.miradi.views.summary;

import javax.swing.Icon;

import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.forms.summary.TncTabForm;
import org.miradi.icons.TncIcon;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.TncProjectData;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.TncOrganizationalPrioritiesQuestion;
import org.miradi.questions.TncProjectTypePlacesQuestion;
import org.miradi.questions.TncFreshwaterEcoRegionQuestion;
import org.miradi.questions.TncMarineEcoRegionQuestion;
import org.miradi.questions.TncOperatingUnitsQuestion;
import org.miradi.questions.TncProjectSharingQuestion;
import org.miradi.questions.TncTerrestrialEcoRegionQuestion;
import org.miradi.rtf.RtfFormExporter;
import org.miradi.rtf.RtfWriter;

public class TNCSummaryPanel extends ObjectDataInputPanel
{
	public TNCSummaryPanel(Project projectToUse, ProjectMetadata metadata) throws Exception
	{
		super(projectToUse, metadata.getType(), metadata.getId());

		addField(createReadonlyTextField(metadata.TAG_TNC_DATABASE_DOWNLOAD_DATE));
		addField(createConproProjectIdField(ProjectMetadata.getObjectType(), ProjectMetadata.TAG_XENODATA_STRING_REF_MAP));
		
		ChoiceQuestion tncProjectSharingQuestion = getProject().getQuestion(TncProjectSharingQuestion.class);
		addRadioButtonFieldWithCustomLabel(TncProjectData.getObjectType(), TncProjectData.TAG_PROJECT_SHARING_CODE, tncProjectSharingQuestion, EAM.text("Share Outside of TNC?"));
		
		addField(createStringField(ProjectMetadata.getObjectType(), ProjectMetadata.TAG_OTHER_ORG_RELATED_PROJECTS));
		addField(createReadonlyTextField(TncProjectData.getObjectType(), TncProjectData.LEGACY_TAG_ORGANIZATIONAL_PRIORITY));
		addField(createSingleColumnCodeListField(TncProjectData.getObjectType(), TncProjectData.TAG_ORGANIZATIONAL_PRIORITIES, getProject().getQuestion(TncOrganizationalPrioritiesQuestion.class)));
		addField(createSingleColumnCodeListField(TncProjectData.getObjectType(), TncProjectData.TAG_PROJECT_PLACE_TYPES, getProject().getQuestion(TncProjectTypePlacesQuestion.class)));

		addField(createReadonlyTextField(metadata.TAG_TNC_PLANNING_TEAM_COMMENT));

		addField(createSingleColumnCodeListField(ProjectMetadata.getObjectType(), ProjectMetadata.TAG_TNC_OPERATING_UNITS, new TncOperatingUnitsQuestion()));

		addField(createSingleColumnCodeListField(ProjectMetadata.getObjectType(), ProjectMetadata.TAG_TNC_TERRESTRIAL_ECO_REGION, new TncTerrestrialEcoRegionQuestion()));
		addField(createSingleColumnCodeListField(ProjectMetadata.getObjectType(), ProjectMetadata.TAG_TNC_MARINE_ECO_REGION, new TncMarineEcoRegionQuestion()));
		addField(createSingleColumnCodeListField(ProjectMetadata.getObjectType(), ProjectMetadata.TAG_TNC_FRESHWATER_ECO_REGION, new TncFreshwaterEcoRegionQuestion()));
		addField(createMultilineField(ProjectMetadata.getObjectType(), ProjectMetadata.TAG_TNC_LESSONS_LEARNED));
		
		setObjectRefs(new ORef[]{metadata.getRef(), getProject().getSingletonObjectRef(TncProjectData.getObjectType())});
	}
	
	public String getPanelDescription()
	{
		return PANEL_DESCRIPTION;
	}
	
	public Icon getIcon()
	{
		return new TncIcon();
	}
	
	@Override
	public boolean isRtfExportable()
	{
		return true;
	}
	
	@Override
	public void exportRtf(RtfWriter writer) throws Exception
	{
		RtfFormExporter rtfFormExporter = new RtfFormExporter(getProject(), writer, getSelectedRefs());
		rtfFormExporter.exportForm(new TncTabForm());
	}
	
	public static final String PANEL_DESCRIPTION = EAM.text("TNC");
}
