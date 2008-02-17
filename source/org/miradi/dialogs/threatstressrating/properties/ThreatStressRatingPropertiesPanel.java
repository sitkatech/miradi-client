/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.threatstressrating.properties;

import java.awt.Component;

import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.ids.BaseId;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.FactorLink;
import org.miradi.objects.Stress;
import org.miradi.views.umbrella.ObjectPicker;

import com.jhlabs.awt.BasicGridLayout;

public class ThreatStressRatingPropertiesPanel extends ObjectDataInputPanel
{
	public ThreatStressRatingPropertiesPanel(MainWindow mainWindowToUse, ObjectPicker objectPickerToUse) throws Exception
	{
		super(mainWindowToUse.getProject(), ObjectType.THREAT_STRESS_RATING, BaseId.INVALID);
		setLayout(new BasicGridLayout(2, 1));
		
		threatStressRatingFieldPanel = new ThreatStressRatingFieldPanel(mainWindowToUse.getProject()); 
		editorComponent = new ThreatStressRatingEditorComponent(mainWindowToUse, objectPickerToUse);
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
	
	public String getPanelDescription()
	{
		return EAM.text("Title|Stress-Based Threat Rating");
	}

	public void addFieldComponent(Component component)
	{
		add(component);
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		super.commandExecuted(event);
		
		if (event.isSetDataCommandWithThisTypeAndTag(FactorLink.getObjectType(), FactorLink.TAG_THREAT_STRESS_RATING_REFS) || 
			event.isSetDataCommandWithThisType(Stress.getObjectType()))
			editorComponent.updateModelBasedOnPickerList();
	}
	
	private ThreatStressRatingEditorComponent editorComponent;
	private ThreatStressRatingFieldPanel threatStressRatingFieldPanel;
}
