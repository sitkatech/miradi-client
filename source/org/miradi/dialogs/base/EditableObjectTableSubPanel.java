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

package org.miradi.dialogs.base;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import org.miradi.actions.Actions;
import org.miradi.layout.OneColumnGridLayout;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.project.Project;
import org.miradi.utils.MiradiScrollPane;
import org.miradi.views.umbrella.ObjectPicker;

abstract public class EditableObjectTableSubPanel extends ObjectDataInputPanel
{
	public EditableObjectTableSubPanel(Project projectToUse, ObjectPicker objectPickerToUse, int ObjectType) throws Exception
	{
		super(projectToUse, ORef.createInvalidWithType(ObjectType));
		
		setLayout(new OneColumnGridLayout());
		
		objectPicker = objectPickerToUse;
		createTable();
		addComponents();
		updateFieldsFromProject();
	}
	
	@Override
	public void dispose()
	{
		super.dispose();
		objectTable.dispose();
	}
	
	@Override
	public void becomeActive()
	{
		super.becomeActive();
		
		objectTable.becomeActive();
	}
	
	@Override
	public void becomeInactive()
	{
		objectTable.becomeInactive();

		super.becomeInactive();
	}
	
	public void refreshModel()
	{
		ORefList[] selectedHierarchies = objectPicker.getSelectedHierarchies();
		if (selectedHierarchies.length > 0)
			setObjectRefs(selectedHierarchies[0].toArray());
	}
	
	@Override
	public void setObjectRefs(ORef[] hierarchyToSelectedRef)
	{
		objectTable.stopCellEditing();
		objectTableModel.setObjectRefs(hierarchyToSelectedRef);
		objectTableModel.fireTableDataChanged();
	}
	
	protected Actions getActions()
	{
		return getMainWindow().getActions();
	}
	
	private void addComponents()
	{
		MiradiScrollPane scroller = new MiradiScrollPane(objectTable);
		add(createButtonBar(), BorderLayout.PAGE_START);
		add(scroller, BorderLayout.CENTER);
	}
	
	@Override
	public void commandExecuted(CommandExecutedEvent event)
	{
		super.commandExecuted(event);
		
		if (event.isSetDataCommandWithThisType(getEditableObjectType()))
			objectTableModel.fireTableDataChanged();
		
		if (event.isSetDataCommandWithThisTag(getTagForRefListFieldBeingEdited()))
			refreshModel();
	}
	
	abstract protected int getEditableObjectType();
	
	abstract protected String getTagForRefListFieldBeingEdited();
	
	abstract protected JPanel createButtonBar();
	
	abstract protected void createTable() throws Exception;
	
	protected ObjectPicker objectPicker;
	protected EditableObjectRefsTableModel objectTableModel;
	protected EditableRefsTable objectTable;
}
