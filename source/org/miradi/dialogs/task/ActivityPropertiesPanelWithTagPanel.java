/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.dialogs.task;

import org.miradi.actions.ActionManageFactorTags;
import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.dialogs.diagram.FactorSummaryCorePanel;
import org.miradi.main.MainWindow;
import org.miradi.objects.Factor;
import org.miradi.objects.Task;
import org.miradi.utils.ObjectsActionButton;
import org.miradi.views.umbrella.ObjectPicker;

public class ActivityPropertiesPanelWithTagPanel extends ActivityPropertiesPanelWithoutBudgetPanels
{
	public ActivityPropertiesPanelWithTagPanel(MainWindow mainWindow, ObjectPicker objectPickerToUse) throws Exception
	{
		super(mainWindow, objectPickerToUse);
		
		createSingleSection(FactorSummaryCorePanel.getTagsLabel());
		ObjectsActionButton chooseTagForFactorButton = createObjectsActionButton(getMainWindow().getActions().getObjectsAction(ActionManageFactorTags.class), objectPickerToUse);
		ObjectDataInputField readOnlyTaggedObjects = createReadOnlyObjectList(Task.getObjectType(), Factor.PSEUDO_TAG_REFERRING_TAG_REFS);
		addFieldWithEditButton(FactorSummaryCorePanel.getTagsLabel(), readOnlyTaggedObjects, chooseTagForFactorButton);
	}
}
