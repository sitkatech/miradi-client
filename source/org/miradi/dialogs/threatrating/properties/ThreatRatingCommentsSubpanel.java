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

package org.miradi.dialogs.threatrating.properties;

import org.miradi.actions.Actions;
import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.FactorLink;
import org.miradi.objects.ProjectMetadata;
import org.miradi.project.Project;

public class ThreatRatingCommentsSubpanel extends ObjectDataInputPanel
{
	public ThreatRatingCommentsSubpanel(Project projectToUse, Actions actions) throws Exception
	{
		super(projectToUse, ORef.INVALID);
		
		commentsField = createMultilineField(FactorLink.getObjectType(), FactorLink.getCommentTagForMode(getProject()));
		addField(commentsField);
		
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
	public void setObjectRefs(ORef[] orefsToUse)
	{
		updateFieldLabels(orefsToUse);
		super.setObjectRefs(orefsToUse);
	}

	private void updateFieldLabels(ORef[] orefsToUse)
	{
		ORefList refs = new ORefList(orefsToUse);
		
		ORef linkRef = refs.getRefForType(FactorLink.getObjectType());
		if(linkRef.isInvalid())
		{
			return;
		}
		
		try
		{
			commentsField.setTag(FactorLink.getCommentTagForMode(getProject()));	
		}
		catch(Exception e)
		{
			EAM.panic(e);
		}
	}

	public String getPanelDescription()
	{
		return "ThreatRatingCommentsSubpanel";
	}
	
	private ObjectDataInputField commentsField;
}
