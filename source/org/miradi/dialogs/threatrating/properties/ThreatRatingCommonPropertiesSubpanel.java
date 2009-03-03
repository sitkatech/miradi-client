/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.dialogs.threatrating.properties;

import org.miradi.actions.Actions;
import org.miradi.dialogfields.ThreatStressRatingValueReadonlyComponent;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.ProjectMetadata;
import org.miradi.project.Project;

public class ThreatRatingCommonPropertiesSubpanel extends ObjectDataInputPanel
{
	public ThreatRatingCommonPropertiesSubpanel(Project projectToUse, Actions actions) throws Exception
	{
		super(projectToUse, ORef.INVALID);
		
		rollupField = new ThreatStressRatingValueReadonlyComponent(getProject());
		PanelTitleLabel rollupLabel = new PanelTitleLabel(EAM.text("Target-Threat Rating"));
		add(rollupLabel);
		add(rollupField.getComponent());
		
		updateFieldsFromProject();
	}
	
	@Override
	public void commandExecuted(CommandExecutedEvent event)
	{
		super.commandExecuted(event);
		if(event.isSetDataCommandWithThisTypeAndTag(ProjectMetadata.getObjectType(), ProjectMetadata.TAG_THREAT_RATING_MODE))
		{
			setObjectRefs(getSelectedRefs());
		}
	}
	
	@Override
	public void updateFieldsFromProject()
	{
		super.updateFieldsFromProject();
		
		rollupField.setObjectRefs(getSelectedRefs());
	}
	
	public String getPanelDescription()
	{
		return "ThreatRatingCommonPropertiesSubpanel";
	}
	
	private ThreatStressRatingValueReadonlyComponent rollupField;
}
