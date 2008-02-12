/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.summary;

import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objectpools.WwfProjectDataPool;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.WwfProjectData;
import org.miradi.project.Project;
import org.miradi.questions.WwfEcoRegionsQuestion;
import org.miradi.questions.WwfManagingOfficesQuestion;
import org.miradi.questions.WwfRegionsQuestion;

public class WWFSummaryPanel extends ObjectDataInputPanel
{
	
	public WWFSummaryPanel(Project projectToUse, ProjectMetadata metaDataToUse)
	{
		super(projectToUse, ORef.INVALID);

		addField(createMultiCodeField(WwfProjectData.getObjectType(), WwfProjectData.TAG_MANAGING_OFFICES, new WwfManagingOfficesQuestion(), 1));
		addField(createMultiCodeField(WwfProjectData.getObjectType(), WwfProjectData.TAG_REGIONS, new WwfRegionsQuestion(), 1));
		addField(createMultiCodeField(WwfProjectData.getObjectType(), WwfProjectData.TAG_ECOREGIONS, new WwfEcoRegionsQuestion(), 1));		
		
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
