/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.subTarget;

import org.conservationmeasures.eam.dialogs.base.ObjectDataInputPanel;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.SubTarget;
import org.conservationmeasures.eam.project.Project;

public class SubTargetPropertiesPanel extends ObjectDataInputPanel
{
	public SubTargetPropertiesPanel(Project projectToUse) throws Exception
	{
		super(projectToUse, ORef.INVALID);
		
		addField(createStringField(SubTarget.TAG_LABEL));
		
		updateFieldsFromProject();
	}
	
	public String getPanelDescription()
	{
		return EAM.text("Title|Nested Target Properties");
	}
}
