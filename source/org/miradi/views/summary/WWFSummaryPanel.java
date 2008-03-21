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

		addField(createCodeListField(WwfProjectData.getObjectType(), WwfProjectData.TAG_MANAGING_OFFICES, new WwfManagingOfficesQuestion(), 1));
		addField(createCodeListField(WwfProjectData.getObjectType(), WwfProjectData.TAG_REGIONS, new WwfRegionsQuestion(), 1));
		addField(createCodeListField(WwfProjectData.getObjectType(), WwfProjectData.TAG_ECOREGIONS, new WwfEcoRegionsQuestion(), 1));		
		
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
