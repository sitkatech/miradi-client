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
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.project.Project;

public class ThreatRatingCommentsSubpanel extends ObjectDataInputPanel
{
	public ThreatRatingCommentsSubpanel(Project projectToUse, Actions actions) throws Exception
	{
		super(projectToUse, ORef.INVALID);
		
		commentsField = new ThreatRatingCommentsEditorComponent(getProject());
		add(commentsField.getComponent());
		
		updateFieldsFromProject();
	}
	
	@Override
	public void commandExecuted(CommandExecutedEvent event)
	{
		super.commandExecuted(event);
		
		//FIXME threat stress rating - need to restrict this more
		setObjectRefs(getSelectedRefs());
	}
	
	@Override
	public void setObjectRefs(ORef[] orefsToUse)
	{
		commentsField.setObjectRefs(new ORefList(orefsToUse));
		
		super.setObjectRefs(orefsToUse);
	}


	public String getPanelDescription()
	{
		return "ThreatRatingCommentsSubpanel";
	}
	
	private ThreatRatingCommentsEditorComponent commentsField;
}
