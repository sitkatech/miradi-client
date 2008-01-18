/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.summary;

import org.conservationmeasures.eam.dialogs.base.ObjectDataInputPanel;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objectpools.WwfProjectDataPool;
import org.conservationmeasures.eam.objects.ProjectMetadata;
import org.conservationmeasures.eam.objects.WwfProjectData;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.WwfEcoRegionsQuestion;
import org.conservationmeasures.eam.questions.WwfManagingOfficesQuestion;
import org.conservationmeasures.eam.questions.WwfRegionsQuestion;

public class WWFSummaryPanel extends ObjectDataInputPanel
{
	
	public WWFSummaryPanel(Project projectToUse, ProjectMetadata metaDataToUse)
	{
		super(projectToUse, ORef.INVALID);

		addField(createStringField(WwfProjectData.getObjectType(), WwfProjectData.TAG_RELATED_PROJECTS));
		addField(createStringField(WwfProjectData.getObjectType(), WwfProjectData.TAG_PROJECT_NUMBER, 20));
		
		addField(createMultiCodeField(WwfProjectData.getObjectType(), new WwfManagingOfficesQuestion(WwfProjectData.TAG_MANAGING_OFFICES), 1));
		addField(createMultiCodeField(WwfProjectData.getObjectType(), new WwfRegionsQuestion(WwfProjectData.TAG_REGIONS), 1));
		addField(createMultiCodeField(WwfProjectData.getObjectType(), new WwfEcoRegionsQuestion(WwfProjectData.TAG_ECOREGIONS), 1));		
		
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
