/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.summary;

import org.conservationmeasures.eam.dialogs.ObjectDataInputPanel;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objectpools.WwfProjectDataPool;
import org.conservationmeasures.eam.objects.ProjectMetadata;
import org.conservationmeasures.eam.objects.WwfProjectData;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.WwfCountriesQuestion;
import org.conservationmeasures.eam.questions.WwfEcoRegionsQuestion;
import org.conservationmeasures.eam.questions.WwfLinkToGlobalTargetsQuestion;
import org.conservationmeasures.eam.questions.WwfManagingOfficesQuestion;
import org.conservationmeasures.eam.questions.WwfRegionsQuestion;

public class WWFSummaryPanel extends ObjectDataInputPanel
{
	
	public WWFSummaryPanel(Project projectToUse, ProjectMetadata metaDataToUse)
	{
		super(projectToUse, ORef.INVALID);

		addField(createStringField(ProjectMetadata.getObjectType(), ProjectMetadata.TAG_PROJECT_NAME));
		addField(createDateChooserField(ProjectMetadata.getObjectType(), ProjectMetadata.TAG_START_DATE));
		addField(createDateChooserField(ProjectMetadata.getObjectType(), ProjectMetadata.TAG_EXPECTED_END_DATE));
		addField(createStringField(WwfProjectData.getObjectType(), WwfProjectData.TAG_RELATED_PROJECTS));
		addField(createStringField(WwfProjectData.getObjectType(), WwfProjectData.TAG_PROJECT_NUMBER, 20));
		addField(createMultilineField(ProjectMetadata.getObjectType(), ProjectMetadata.TAG_PROJECT_VISION));
		addField(createNumericField(ProjectMetadata.getObjectType(), ProjectMetadata.TAG_TOTAL_BUDGET_FOR_FUNDING));
		addField(createNumericField(ProjectMetadata.getObjectType(), ProjectMetadata.TAG_BUDGET_SECURED_PERCENT));
		addField(createReadOnlyObjectList(ProjectMetadata.getObjectType(), ProjectMetadata.PSEUDO_TAG_PROJECT_TEAM_MEMBERS));
		
		addField(createMultiCodeField(WwfProjectData.getObjectType(), new WwfManagingOfficesQuestion(WwfProjectData.TAG_MANAGING_OFFICES)));
		addField(createMultiCodeField(WwfProjectData.getObjectType(), new WwfRegionsQuestion(WwfProjectData.TAG_REGIONS)));
		addField(createMultiCodeField(WwfProjectData.getObjectType(), new WwfCountriesQuestion(WwfProjectData.TAG_COUNTRIES)));
		addField(createMultiCodeField(WwfProjectData.getObjectType(), new WwfEcoRegionsQuestion(WwfProjectData.TAG_ECOREGIONS)));
		addField(createMultiCodeField(WwfProjectData.getObjectType(), new WwfLinkToGlobalTargetsQuestion(WwfProjectData.TAG_LINK_TO_GLOBAL_TARGETS)));
		
		
		setObjectRefs(new ORef[] {metaDataToUse.getRef(), getWwfProjectDataRef()});
	}
	
	private ORef getWwfProjectDataRef()
	{
		WwfProjectDataPool pool = getProject().getWwfProjectDataPool();
		ORefList wwfProjectDataRefs = pool.getORefList();
		if (wwfProjectDataRefs.size() == 0)
			return ORef.INVALID;
		
		return wwfProjectDataRefs.get(0);
	}

	public String getPanelDescription()
	{
		return EAM.text("Label|WWF");
	}

}
