/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.summary;

import javax.swing.Icon;

import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.icons.ProjectScopeIcon;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.ProjectMetadata;
import org.miradi.project.Project;
import org.miradi.questions.ProtectedAreaCategoryQuestion;

public class SummaryScopePanel extends ObjectDataInputPanel
{
	public SummaryScopePanel(Project projectToUse, ORef orefToUse)
	{
		super(projectToUse, orefToUse);
		
		addField(createStringField(ProjectMetadata.TAG_SHORT_PROJECT_SCOPE));
		addField(createMultilineField(ProjectMetadata.TAG_PROJECT_SCOPE));
		addField(createMultilineField(ProjectMetadata.TAG_PROJECT_VISION));
		
		ObjectDataInputField projectAreaField = createNumericField(ProjectMetadata.TAG_PROJECT_AREA);
		ObjectDataInputField projectAreaNotesField = createMultilineField(ProjectMetadata.TAG_PROJECT_AREA_NOTES);
		addFieldsOnOneLine(EAM.text("Label|Biodiversity Area (ha)"), new ObjectDataInputField[]{projectAreaField, projectAreaNotesField});


		ObjectDataInputField protectedAreaStatusField = createMultiCodeField(ProjectMetadata.TAG_PROTECTED_AREA_CATEGORIES, new ProtectedAreaCategoryQuestion(), 1);
		ObjectDataInputField protectedAreaStatusNotesField = createMultilineField(ProjectMetadata.getObjectType(), ProjectMetadata.TAG_PROTECTED_AREA_CATEGORY_NOTES, 25);
		addFieldsOnOneLine(EAM.text("Label|Protected Area Categories"), new ObjectDataInputField[]{protectedAreaStatusField, protectedAreaStatusNotesField});
		
		addField(createStringField(ProjectMetadata.TAG_RED_LIST_SPECIES));
		addField(createStringField(ProjectMetadata.TAG_OTHER_NOTABLE_SPECIES));
		
		ObjectDataInputField humanPopulationField = createNumericField(ProjectMetadata.TAG_HUMAN_POPULATION);
		ObjectDataInputField humanPopulationNotesField = createMultilineField(ProjectMetadata.TAG_HUMAN_POPULATION_NOTES);
		addFieldsOnOneLine(EAM.text("Label|Human Stakeholder Pop Size"), new ObjectDataInputField[]{humanPopulationField, humanPopulationNotesField});
		
		addField(createMultilineField(ProjectMetadata.TAG_SOCIAL_CONTEXT));
		
		addField(createMultilineField(ProjectMetadata.TAG_SCOPE_COMMENTS));
		
		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Scope");
	}
	
	@Override
	public Icon getIcon()
	{
		return new ProjectScopeIcon();
	}
}
