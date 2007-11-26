/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.threatstressrating.properties;

import java.awt.Component;

import org.conservationmeasures.eam.dialogs.base.ObjectDataInputPanel;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.Project;

import com.jhlabs.awt.BasicGridLayout;

public class ThreatStressRatingPropertiesPanel extends ObjectDataInputPanel
{
	public ThreatStressRatingPropertiesPanel(Project projectToUse) throws Exception
	{
		super(projectToUse, ObjectType.THREAT_STRESS_RATING, BaseId.INVALID);
		setLayout(new BasicGridLayout(2, 1));
		
		threatStressRatingFieldPanel = new ThreatStressRatingFieldPanel(projectToUse, ORef.INVALID); 
		editorComponent = new ThreatStressRatingEditorComponent(projectToUse);
		add(threatStressRatingFieldPanel);
		add(editorComponent);
		
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
		
		if (threatStressRatingFieldPanel != null)
		{
			threatStressRatingFieldPanel.dispose();
			threatStressRatingFieldPanel = null;
		}
	}
	
	public void setObjectRefs(ORef[] hierarchyToSelectedRef)
	{
		threatStressRatingFieldPanel.setObjectRefs(hierarchyToSelectedRef);
		editorComponent.setObjectRefs(hierarchyToSelectedRef);
	}
	
	public void setObjectRefs(ORefList[] hierarchiesToSelectedRefs)
	{
		if (hierarchiesToSelectedRefs.length == 0)
			setObjectRefs(new ORef[0]);
		else
			setObjectRefs(hierarchiesToSelectedRefs[0].toArray());
	}
	
	public void dataWasChanged()
	{
		editorComponent.dataWasChanged();
	}
	
	public String getPanelDescription()
	{
		return EAM.text("Title|Stress-Based Threat Rating");
	}

	public void addFieldComponent(Component component)
	{
		add(component);
	}
	
	private ThreatStressRatingEditorComponent editorComponent;
	private ThreatStressRatingFieldPanel threatStressRatingFieldPanel;
}
