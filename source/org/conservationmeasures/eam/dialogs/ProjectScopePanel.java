/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.ProjectMetadata;
import org.conservationmeasures.eam.views.summary.MetadataEditingPanel;
import org.martus.swing.UiLabel;
import org.martus.swing.UiTextField;

public class ProjectScopePanel extends MetadataEditingPanel
{
	public ProjectScopePanel(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
		createFields();
	}
	
	private void createFields()
	{
		add(new UiLabel(EAM.text("Label|Project Scope:")));
		projectScope = createFieldComponent(ProjectMetadata.TAG_PROJECT_SCOPE, 50);
		add(projectScope);
		
		add(new UiLabel(EAM.text("Label|Short Project Scope:")));
		shortProjectScope = createFieldComponent(ProjectMetadata.TAG_SHORT_PROJECT_SCOPE, 50);
		add(shortProjectScope);
		
		add(new UiLabel(EAM.text("Label|Project Vision:")));
		projectVision = createFieldComponent(ProjectMetadata.TAG_PROJECT_VISION, 50);
		add(projectVision);
		
		add(new UiLabel(EAM.text("Label|Short Project Vision:")));
		shortProjectVision = createFieldComponent(ProjectMetadata.TAG_SHORT_PROJECT_VISION, 50);
		add(shortProjectVision);
	}

	UiTextField projectScope;
	UiTextField shortProjectScope;
	UiTextField projectVision;
	UiTextField shortProjectVision;
}
