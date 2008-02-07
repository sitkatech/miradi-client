/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.summary;

import org.conservationmeasures.eam.dialogs.base.ObjectDataInputPanel;
import org.conservationmeasures.eam.layout.OneColumnGridLayout;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.WcpaProjectData;
import org.conservationmeasures.eam.project.Project;

public class WcpaSummaryPanel extends ObjectDataInputPanel
{
	public WcpaSummaryPanel(Project projectToUse)
	{
		super(projectToUse, projectToUse.getSingletonObjectRef(WcpaProjectData.getObjectType()));
		
		setLayout(new OneColumnGridLayout());
		
		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Label|WCPA");
	}
}
