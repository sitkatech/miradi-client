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
package org.miradi.views.summary;

import javax.swing.Icon;
import javax.swing.JComponent;

import org.miradi.dialogfields.RadioButtonsField;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.forms.summary.TncTabForm;
import org.miradi.icons.TncIcon;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.TncProjectData;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceQuestion;
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
		RadioButtonsField projectSharingField = createRadioButtonsField(TncProjectData.getObjectType(), TncProjectData.TAG_PROJECT_SHARING_CODE, tncProjectSharingQuestion);
		JComponent noRadioButton = projectSharingField.getComponent(tncProjectSharingQuestion.findIndexByCode(TncProjectSharingQuestion.SHARE_TNC_ONLY));
		JComponent yesRadioButton = projectSharingField.getComponent(tncProjectSharingQuestion.findIndexByCode(TncProjectSharingQuestion.SHARE_WITH_ANYONE));
		addRadioButtonFieldWithCustomLabel(projectSharingField, EAM.text("Share Outside of TNC?"), new JComponent[]{noRadioButton, yesRadioButton, });
		
		addField(createStringField(ProjectMetadata.getObjectType(), ProjectMetadata.TAG_OTHER_ORG_RELATED_PROJECTS));
		addField(createStringField(TncProjectData.getObjectType(), TncProjectData.TAG_ORGANIZATIONAL_PRIORITY));

		addField(createReadonlyTextField(metadata.TAG_TNC_PLANNING_TEAM_COMMENT));

		addField(createCodeListField(ProjectMetadata.getObjectType(), ProjectMetadata.TAG_TNC_OPERATING_UNITS, new TncOperatingUnitsQuestion(), 1));

		addField(createCodeListField(ProjectMetadata.getObjectType(), ProjectMetadata.TAG_TNC_TERRESTRIAL_ECO_REGION, new TncTerrestrialEcoRegionQuestion(), 1));
		addField(createCodeListField(ProjectMetadata.getObjectType(), ProjectMetadata.TAG_TNC_MARINE_ECO_REGION, new TncMarineEcoRegionQuestion(), 1));
		addField(createCodeListField(ProjectMetadata.getObjectType(), ProjectMetadata.TAG_TNC_FRESHWATER_ECO_REGION, new TncFreshwaterEcoRegionQuestion(), 1));
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
