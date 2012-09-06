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
package org.miradi.forms.summary;

import org.miradi.forms.FieldPanelSpec;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.TncProjectData;
import org.miradi.objects.Xenodata;
import org.miradi.questions.StaticQuestionManager;
import org.miradi.questions.TncFreshwaterEcoRegionQuestion;
import org.miradi.questions.TncMarineEcoRegionQuestion;
import org.miradi.questions.TncOperatingUnitsQuestion;
import org.miradi.questions.TncOrganizationalPrioritiesQuestion;
import org.miradi.questions.TncProjectPlaceTypeQuestion;
import org.miradi.questions.ProjectSharingQuestion;
import org.miradi.questions.TncTerrestrialEcoRegionQuestion;
import org.miradi.views.summary.TNCSummaryPanel;



public class TncTabForm extends FieldPanelSpec 
{
	public TncTabForm()
	{
		setTranslatedTitle(TNCSummaryPanel.getTncPanelDescription());
		
		int projectMetadataType = ProjectMetadata.getObjectType();
		addLabelAndField(projectMetadataType, ProjectMetadata.TAG_TNC_DATABASE_DOWNLOAD_DATE);
		addLabelAndField(Xenodata.getObjectType(), Xenodata.TAG_PROJECT_ID);
		addChoiceField(TncProjectData.getObjectType(), TncProjectData.TAG_PROJECT_SHARING_CODE, new ProjectSharingQuestion());
		addLabelAndField(projectMetadataType, ProjectMetadata.TAG_OTHER_ORG_RELATED_PROJECTS);
		addCodeListField(TncProjectData.getObjectType(), TncProjectData.TAG_PROJECT_PLACE_TYPES, StaticQuestionManager.getQuestion(TncProjectPlaceTypeQuestion.class));
		addCodeListField(TncProjectData.getObjectType(), TncProjectData.TAG_ORGANIZATIONAL_PRIORITIES, StaticQuestionManager.getQuestion(TncOrganizationalPrioritiesQuestion.class));
		addLabelAndField(projectMetadataType, ProjectMetadata.TAG_TNC_PLANNING_TEAM_COMMENTS);
		addLabelAndField(TncProjectData.getObjectType(), TncProjectData.TAG_CON_PRO_PARENT_CHILD_PROJECT_TEXT);
		addCodeListField(projectMetadataType, ProjectMetadata.TAG_TNC_OPERATING_UNITS, StaticQuestionManager.getQuestion(TncOperatingUnitsQuestion.class));
		addCodeListField(projectMetadataType, ProjectMetadata.TAG_TNC_TERRESTRIAL_ECO_REGION, StaticQuestionManager.getQuestion(TncTerrestrialEcoRegionQuestion.class));
		addCodeListField(projectMetadataType, ProjectMetadata.TAG_TNC_MARINE_ECO_REGION, StaticQuestionManager.getQuestion(TncMarineEcoRegionQuestion.class));
		addCodeListField(projectMetadataType, ProjectMetadata.TAG_TNC_FRESHWATER_ECO_REGION, StaticQuestionManager.getQuestion(TncFreshwaterEcoRegionQuestion.class));
		addLabelAndField(projectMetadataType, ProjectMetadata.TAG_TNC_LESSONS_LEARNED);
		addLabelAndField(TncProjectData.getObjectType(), TncProjectData.TAG_PROJECT_RESOURCES_SCORECARD);
		addLabelAndField(TncProjectData.getObjectType(), TncProjectData.TAG_PROJECT_LEVEL_COMMENTS);
		addLabelAndField(TncProjectData.getObjectType(), TncProjectData.TAG_PROJECT_CITATIONS);
		addLabelAndField(TncProjectData.getObjectType(), TncProjectData.TAG_CAP_STANDARDS_SCORECARD);
	}
}
