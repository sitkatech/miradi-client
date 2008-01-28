/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.summary;

import org.conservationmeasures.eam.dialogs.base.ObjectDataInputPanel;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.ProjectMetadata;
import org.conservationmeasures.eam.objects.TncProjectData;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.TncFreshwaterEcoRegionQuestion;
import org.conservationmeasures.eam.questions.TncMarineEcoRegionQuestion;
import org.conservationmeasures.eam.questions.TncOperatingUnitsQuestion;
import org.conservationmeasures.eam.questions.TncTerrestrialEcoRegionQuestion;

public class TNCSummaryPanel extends ObjectDataInputPanel
{
	public TNCSummaryPanel(Project projectToUse, ProjectMetadata metadata)
	{
		super(projectToUse, metadata.getType(), metadata.getId());

		addField(createReadonlyTextField(metadata.TAG_TNC_WORKBOOK_VERSION_NUMBER));
		addField(createReadonlyTextField(metadata.TAG_TNC_WORKBOOK_VERSION_DATE));
		addField(createReadonlyTextField(metadata.TAG_TNC_DATABASE_DOWNLOAD_DATE));
		addField(createMultilineField(metadata.TAG_TNC_LESSONS_LEARNED));
		addField(createMultilineField(metadata.TAG_TNC_PLANNING_TEAM_COMMENT));
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
