/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.summary;

import javax.swing.Icon;

import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.icons.LocationIcon;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.ProjectMetadata;
import org.miradi.project.Project;
import org.miradi.questions.CountriesQuestion;

public class SummaryLocationPanel extends ObjectDataInputPanel
{
	public SummaryLocationPanel(Project projectToUse, ORef orefToUse)
	{
		super(projectToUse, orefToUse);
		
		addFieldWithCustomLabelAndHint(createNumericField(ProjectMetadata.TAG_PROJECT_LATITUDE, 10), "(Latitude must be -90.0000 to +90.0000)");
		addFieldWithCustomLabelAndHint(createNumericField(ProjectMetadata.TAG_PROJECT_LONGITUDE, 10), "(Longitude must be -180.0000 to +180.0000)");
		
		addField(createCodeListField(ProjectMetadata.getObjectType(), ProjectMetadata.TAG_COUNTRIES, new CountriesQuestion(), 1));
		addField(createStringField(ProjectMetadata.TAG_STATE_AND_PROVINCES));
		addField(createStringField(ProjectMetadata.TAG_MUNICIPALITIES));
		addField(createStringField(ProjectMetadata.TAG_LEGISLATIVE_DISTRICTS));
		addField(createMultilineField(ProjectMetadata.TAG_LOCATION_DETAIL));
		addField(createStringField(ProjectMetadata.TAG_SITE_MAP_REFERENCE));
		addField(createMultilineField(ProjectMetadata.TAG_LOCATION_COMMENTS));
		
		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Location");
	}
	
	@Override
	public Icon getIcon()
	{
		return new LocationIcon();
	}
}
