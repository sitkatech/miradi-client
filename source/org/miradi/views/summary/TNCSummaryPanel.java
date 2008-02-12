/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.summary;

import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.TncProjectData;
import org.miradi.project.Project;
import org.miradi.questions.TncFreshwaterEcoRegionQuestion;
import org.miradi.questions.TncMarineEcoRegionQuestion;
import org.miradi.questions.TncOperatingUnitsQuestion;
import org.miradi.questions.TncTerrestrialEcoRegionQuestion;

public class TNCSummaryPanel extends ObjectDataInputPanel
{
	public TNCSummaryPanel(Project projectToUse, ProjectMetadata metadata)
	{
		super(projectToUse, metadata.getType(), metadata.getId());

		addField(createReadonlyTextField(metadata.TAG_TNC_DATABASE_DOWNLOAD_DATE));
		addField(createReadonlyTextField(TncProjectData.getObjectType(), TncProjectData.TAG_CONPRO_PROJECT_NUMBER));
		addField(createStringField(ProjectMetadata.getObjectType(), ProjectMetadata.TAG_OTHER_ORG_RELATED_PROJECTS));
		addField(createStringField(TncProjectData.getObjectType(), TncProjectData.TAG_ORGANIZATIONAL_PRIORITY));
		addField(createStringField(metadata.TAG_TNC_PLANNING_TEAM_COMMENT));
		addField(createReadonlyTextField(metadata.TAG_TNC_ECOREGION));
		addField(createMultiCodeField(ProjectMetadata.getObjectType(), ProjectMetadata.TAG_TNC_TERRESTRIAL_ECO_REGION, new TncTerrestrialEcoRegionQuestion(), 1));
		addField(createMultiCodeField(ProjectMetadata.getObjectType(), ProjectMetadata.TAG_TNC_MARINE_ECO_REGION, new TncMarineEcoRegionQuestion(), 1));
		addField(createMultiCodeField(ProjectMetadata.getObjectType(), ProjectMetadata.TAG_TNC_FRESHWATER_ECO_REGION, new TncFreshwaterEcoRegionQuestion(), 1));
		
		addField(createReadonlyTextField(metadata.TAG_TNC_COUNTRY));
		addField(createReadonlyTextField(metadata.LEGACY_TAG_TNC_OPERATING_UNITS));
		addField(createMultiCodeField(ProjectMetadata.getObjectType(), ProjectMetadata.TAG_TNC_OPERATING_UNITS, new TncOperatingUnitsQuestion(), 1));

		setObjectRefs(new ORef[]{metadata.getRef(), getProject().getSingletonObjectRef(TncProjectData.getObjectType())});
	}
	
	public String getPanelDescription()
	{
		return EAM.text("TNC");
	}
	
}
