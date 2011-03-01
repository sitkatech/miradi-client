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
package org.miradi.dialogs.diagram;

import org.miradi.actions.ActionEditStrategyObjectiveRelevancyList;
import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.main.EAM;
import org.miradi.objects.Strategy;
import org.miradi.project.Project;
import org.miradi.utils.ObjectsActionButton;

public class RelatedItemsSubpanel extends ObjectDataInputPanel
{
	public RelatedItemsSubpanel(Project projectToUse, int objectType) throws Exception
	{
		super(projectToUse, objectType);
		
		addField(createReadonlyTextField(Strategy.PSEUDO_TAG_TARGETS));
		addField(createReadonlyTextField(Strategy.PSEUDO_TAG_DIRECT_THREATS));
		ObjectsActionButton actionsButton = createObjectsActionButton(getMainWindow().getActions().getObjectsAction(ActionEditStrategyObjectiveRelevancyList.class), getPicker());
		ObjectDataInputField readonlyField = createReadOnlyObjectList(Strategy.getObjectType(), Strategy.PSEUDO_TAG_RELEVANT_OBJECTIVE_REFS);
		addFieldWithEditButton(EAM.text("Objectives"), readonlyField, actionsButton);
	}

	@Override
	public String getPanelDescription()
	{
		return EAM.text("Related Items");
	}

}
