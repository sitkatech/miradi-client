/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.dialogs.threatstressrating.properties;

import java.awt.Component;

import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.ids.BaseId;
import org.miradi.layout.OneColumnGridLayout;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.FactorLink;
import org.miradi.objects.Stress;
import org.miradi.objects.ThreatStressRating;
import org.miradi.views.umbrella.ObjectPicker;

public class ThreatStressRatingPropertiesPanel extends ObjectDataInputPanel
{
	public ThreatStressRatingPropertiesPanel(MainWindow mainWindowToUse, ObjectPicker objectPickerToUse) throws Exception
	{
		super(mainWindowToUse.getProject(), ObjectType.THREAT_STRESS_RATING, BaseId.INVALID);
		setLayout(new OneColumnGridLayout());
		
		threatStressRatingFieldPanel = new ThreatStressRatingFieldPanel(mainWindowToUse.getProject(), mainWindowToUse.getActions()); 
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
			event.isSetDataCommandWithThisType(ThreatStressRating.getObjectType()) || 
			event.isSetDataCommandWithThisType(Stress.getObjectType()))
			editorComponent.updateModelBasedOnPickerList();
	}
	
	private ThreatStressRatingEditorComponent editorComponent;
	private ThreatStressRatingFieldPanel threatStressRatingFieldPanel;
}
