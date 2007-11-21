/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.threatstressrating.management.properties;

import org.conservationmeasures.eam.dialogs.base.ObjectDataInputPanel;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.Project;

public class ThreatStressRatingPropertiesPanel extends ObjectDataInputPanel
{
	public ThreatStressRatingPropertiesPanel(Project projectToUse) throws Exception
	{
		super(projectToUse, ObjectType.THREAT_STRESS_RATING, BaseId.INVALID);

		editorComponent = new ThreatStressRatingEditorComponent(projectToUse); 
		updateFieldsFromProject();
	}
	
	public void dispose()
	{
		super.dispose();
		if (editorComponent != null)
		{
			editorComponent.dispose();
			editorComponent = null;
		}
	}
	
	public void setObjectRefs(ORef[] hierarchyToSelectedRef)
	{
		editorComponent.setObjectRefs(hierarchyToSelectedRef);
	}
	
	public void dataWasChanged()
	{
		editorComponent.dataWasChanged();
	}
	
	public String getPanelDescription()
	{
		return EAM.text("Title|Stress-Based Threat Rating");
	}
	
	private ThreatStressRatingEditorComponent editorComponent;
}
