/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.summary;

import org.conservationmeasures.eam.dialogs.base.ObjectDataInputPanel;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objectpools.RareProjectDataPool;
import org.conservationmeasures.eam.objects.RareProjectData;
import org.conservationmeasures.eam.project.Project;

public class RARESummaryPanel extends ObjectDataInputPanel
{
	public RARESummaryPanel(Project projectToUse)
	{
		super(projectToUse, getRareProjectDataRef(projectToUse));
		
		addField(createStringField(RareProjectData.TAG_FLAGSHIP_SPECIES_COMMON_NAME));
		addField(createStringField(RareProjectData.TAG_FLAGSHIP_SPECIES_SCIENTIFIC_NAME));
		addField(createMultilineField(RareProjectData.TAG_FLAGSHIP_SPECIES_DETAIL));
		addField(createStringField(RareProjectData.TAG_CAMPAIGN_SLOGAN));		
		addField(createStringField(RareProjectData.TAG_CAMPAIGN_THEORY_OF_CHANGE));
		addField(createStringField(RareProjectData.TAG_SUMMARY_OF_KEY_MESSAGES));
		addField(createStringField(RareProjectData.TAG_BIODIVERSITY_HOTSPOTS));
		addField(createStringField(RareProjectData.TAG_RELATED_PROJECTS));
		
		updateFieldsFromProject();
	}

	public static ORef getRareProjectDataRef(Project project)
	{
		RareProjectDataPool pool = project.getRareProjectDataPool();
		ORefList rareProjectDataRefs = pool.getORefList();
		if (rareProjectDataRefs.size() == 0)
			return new ORef(RareProjectData.getObjectType(), BaseId.INVALID);
		
		return rareProjectDataRefs.get(0);
	}
	
	public String getPanelDescription()
	{
		return EAM.text("Label|RARE");
	}
}
