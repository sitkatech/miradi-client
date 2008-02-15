/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.summary;

import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.main.EAM;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.WcpaProjectData;
import org.miradi.project.Project;
import org.miradi.questions.ProtectedAreaCategoryQuestion;

public class WcpaSummaryPanel extends ObjectDataInputPanel
{
	public WcpaSummaryPanel(Project projectToUse)
	{
		super(projectToUse, projectToUse.getSingletonObjectRef(WcpaProjectData.getObjectType()));

		// NOTE: Mirror from Scope panel
		ObjectDataInputField protectedAreaStatusField = createMultiCodeField(ProjectMetadata.getObjectType(), ProjectMetadata.TAG_PROTECTED_AREA_CATEGORIES, new ProtectedAreaCategoryQuestion(), 1);
		ObjectDataInputField protectedAreaStatusNotesField = createMultilineField(ProjectMetadata.getObjectType(), ProjectMetadata.TAG_PROTECTED_AREA_CATEGORY_NOTES, 25);
		addFieldsOnOneLine(EAM.text("Label|Protected Area Categories"), new ObjectDataInputField[]{protectedAreaStatusField, protectedAreaStatusNotesField});
		
		addField(createMultilineField(WcpaProjectData.TAG_LEGAL_STATUS));
		addField(createMultilineField(WcpaProjectData.TAG_LEGISLATIVE));
		addField(createMultilineField(WcpaProjectData.TAG_PHYSICAL_DESCRIPTION));
		addField(createMultilineField(WcpaProjectData.TAG_BIOLOGICAL_DESCRIPTION));
		addField(createMultilineField(WcpaProjectData.TAG_SOCIO_ECONOMIC_INFORMATION));
		addField(createMultilineField(WcpaProjectData.TAG_HISTORICAL_DESCRIPTION));
		addField(createMultilineField(WcpaProjectData.TAG_CULTURAL_DESCRIPTION));
		addField(createMultilineField(WcpaProjectData.TAG_ACCESS_INFORMATION));
		addField(createMultilineField(WcpaProjectData.TAG_VISITATION_INFORMATION));
		addField(createMultilineField(WcpaProjectData.TAG_CURRENT_LAND_USES));
		addField(createMultilineField(WcpaProjectData.TAG_MANAGEMENT_RESOURCES));				
						
		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Label|PAs");
	}
}
