/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.summary;

import javax.swing.Icon;

import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.icons.MiradiApplicationIcon;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.ProjectMetadata;
import org.miradi.project.Project;

public class SummaryProjectPanel extends ObjectDataInputPanel
{
	public SummaryProjectPanel(Project projectToUse, ORef refToUse)
	{
		super(projectToUse, refToUse);
		
		addField(createStringField(ProjectMetadata.TAG_PROJECT_NAME));
		addField(createDateChooserField(ProjectMetadata.TAG_DATA_EFFECTIVE_DATE));
		addField(createReadonlyTextField(ProjectMetadata.PSEUDO_TAG_PROJECT_FILENAME));
		addBlankHorizontalLine();
		
		addField(createShortStringField(ProjectMetadata.TAG_OTHER_ORG_PROJECT_NUMBER));
		addField(createStringField(ProjectMetadata.TAG_OTHER_ORG_RELATED_PROJECTS));
		addField(createStringField(ProjectMetadata.TAG_PROJECT_URL));
		addField(createMultilineField(ProjectMetadata.TAG_PROJECT_DESCRIPTION));
		
		addField(createMultilineField(ProjectMetadata.TAG_PROJECT_STATUS));
		addField(createMultilineField(ProjectMetadata.TAG_NEXT_STEPS));
		
		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Project");
	}
	
	@Override
	public Icon getIcon()
	{
		return new MiradiApplicationIcon();
	}
}
