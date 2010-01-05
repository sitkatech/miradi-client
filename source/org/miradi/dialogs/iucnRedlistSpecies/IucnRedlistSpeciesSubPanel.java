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

package org.miradi.dialogs.iucnRedlistSpecies;

import javax.swing.JPanel;

import org.miradi.actions.ActionCreateIucnRedlistSpecies;
import org.miradi.actions.ActionDeleteIucnRedlistSpecies;
import org.miradi.dialogs.base.EditableObjectTableSubPanel;
import org.miradi.layout.OneRowPanel;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.objects.IucnRedlistSpecies;
import org.miradi.project.Project;
import org.miradi.views.umbrella.ObjectPicker;

public class IucnRedlistSpeciesSubPanel extends EditableObjectTableSubPanel
{
	public IucnRedlistSpeciesSubPanel(Project projectToUse, ObjectPicker objectPickerToUse) throws Exception
	{
		super(projectToUse, objectPickerToUse, getObjectType());
	}
	
	@Override
	protected void createTable() throws Exception
	{
		objectTableModel = new IucnRedlistSpeciesEditablePoolTableModel(getProject());
		objectTable = new IucnRedlistSpeciesEditablePoolTable(getMainWindow(), objectTableModel);
	}
	
	@Override
	protected JPanel createButtonBar()
	{
		OneRowPanel box = new OneRowPanel();
		box.setBackground(AppPreferences.getDataPanelBackgroundColor());
		box.setGaps(3);
		box.add(createObjectsActionButton(getActions().getObjectsAction(ActionCreateIucnRedlistSpecies.class), objectPicker));
		box.add(createObjectsActionButton(getActions().getObjectsAction(ActionDeleteIucnRedlistSpecies.class), objectTable));
		
		return box;
	}
	
	@Override
	public String getPanelDescription()
	{
		return EAM.text("Title|IUCN Redlist Species");
	}
	
	@Override
	protected int getEditableObjectType()
	{
		return getObjectType();
	}

	private static int getObjectType()
	{
		return IucnRedlistSpecies.getObjectType();
	}

	@Override
	protected String getTagForRefListFieldBeingEdited()
	{
		//FIXME urgent IUCN related - either fix this method or the caller
		return "";
	}
}
