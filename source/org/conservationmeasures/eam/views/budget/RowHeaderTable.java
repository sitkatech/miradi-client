/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.budget;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.ProjectResource;
import org.conservationmeasures.eam.project.Project;
import org.martus.swing.UiComboBox;
import org.martus.swing.UiTable;

public class RowHeaderTable extends UiTable
{
	public RowHeaderTable(Project projectToUse)
	{
		project = projectToUse;
		rebuild();
	}
	
	private void rebuild()
	{
	}
	
	Project project;
	UiComboBox resourceCombo;
	ProjectResource[] projectResources;
	RowHeaderTableModel rowHeaderModel;
	static final String COLUMN_HEADER_TITLE = EAM.text("Resource Names");
}

