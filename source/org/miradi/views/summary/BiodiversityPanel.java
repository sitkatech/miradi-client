/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.summary;

import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.ProjectMetadata;
import org.miradi.project.Project;

public class BiodiversityPanel extends ObjectDataInputPanel
{
	public BiodiversityPanel(Project projectToUse, ORef orefToUse)
	{
		super(projectToUse, orefToUse);
		
		ObjectDataInputField projectAreaField = createNumericField(ProjectMetadata.TAG_PROJECT_AREA);
		ObjectDataInputField projectAreaNotesField = createMultilineField(ProjectMetadata.TAG_PROJECT_AREA_NOTES);
		addFieldsOnOneLine(EAM.text("Label|Biodiversity Area (hectares)"), new ObjectDataInputField[]{projectAreaField, projectAreaNotesField});

		addField(createStringField(ProjectMetadata.TAG_RED_LIST_SPECIES));
		addField(createStringField(ProjectMetadata.TAG_OTHER_NOTABLE_SPECIES));
		
	}

	@Override
	public String getPanelDescription()
	{
		return EAM.text("Biodiversity Features");
	}

}
