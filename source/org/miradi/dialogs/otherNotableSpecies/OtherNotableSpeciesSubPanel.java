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

package org.miradi.dialogs.otherNotableSpecies;

import java.util.LinkedHashMap;

import org.miradi.actions.ActionCreateOtherNotableSpecies;
import org.miradi.actions.ActionDeleteOtherNotableSpecies;
import org.miradi.dialogs.base.EditableObjectPoolTableSubPanel;
import org.miradi.main.EAM;
import org.miradi.objects.OtherNotableSpecies;
import org.miradi.project.Project;
import org.miradi.views.umbrella.ObjectPicker;

public class OtherNotableSpeciesSubPanel extends EditableObjectPoolTableSubPanel
{
	public OtherNotableSpeciesSubPanel(Project projectToUse, ObjectPicker objectPickerToUse) throws Exception
	{
		super(projectToUse, objectPickerToUse, getObjectType());
	}
	
	@Override
	protected void createTable() throws Exception
	{
		objectTableModel = new OtherNotableSpeciesEditablePoolTableModel(getProject());
		objectTable = new OtherNotableSpeciesEditablePoolTable(getMainWindow(), objectTableModel);
	}
		
	@Override
	protected LinkedHashMap<Class, ObjectPicker> getButtonsActionsPickerMap()
	{
		LinkedHashMap<Class, ObjectPicker> buttonsMap = new LinkedHashMap<Class, ObjectPicker>();
		buttonsMap.put(ActionCreateOtherNotableSpecies.class, objectPicker);
		buttonsMap.put(ActionDeleteOtherNotableSpecies.class, objectTable);
		
		return buttonsMap;
	}
	
	@Override
	public String getPanelDescription()
	{
		return EAM.text("Title|Other Notable Species");
	}
	
	@Override
	protected int getEditableObjectType()
	{
		return getObjectType();
	}

	private static int getObjectType()
	{
		return OtherNotableSpecies.getObjectType();
	}
}
