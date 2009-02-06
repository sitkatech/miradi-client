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

import javax.swing.BorderFactory;
import javax.swing.JComponent;

import org.martus.swing.UiButton;
import org.miradi.actions.ObjectsAction;
import org.miradi.layout.OneRowPanel;
import org.miradi.main.AppPreferences;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.CommandExecutedListener;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;
import org.miradi.utils.MiradiScrollPane;
import org.miradi.views.umbrella.ObjectPicker;

abstract public class ObjectCollectionPanel extends DisposablePanel implements CommandExecutedListener
{
	public ObjectCollectionPanel(MainWindow mainWindowToUse, ObjectPicker componentToUse)
	{
		super(new BorderLayout());
		mainWindow = mainWindowToUse;
		component = componentToUse;
		MiradiScrollPane tableScrollPane = new MiradiScrollPane((JComponent)component);
		tableScrollPane.setBackground(AppPreferences.getDataPanelBackgroundColor());
		tableScrollPane.getViewport().setBackground(AppPreferences.getDataPanelBackgroundColor());
		add(tableScrollPane, BorderLayout.CENTER);
		buttons = new OneRowPanel();
		buttons.setGaps(3);
		buttons.setBackground(AppPreferences.getDataPanelBackgroundColor());

		add(buttons, BorderLayout.AFTER_LAST_LINE);
		setFocusCycleRoot(true);
		getProject().addCommandExecutedListener(this);

		setBackground(AppPreferences.getDataPanelBackgroundColor());
		setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
	}
	
	
	public void dispose()
	{
		getProject().removeCommandExecutedListener(this);
		super.dispose();
	}

	
	public void setPropertiesPanel(AbstractObjectDataInputPanel panel)
	{
		propertiesPanel = panel;
		ORefList selectionHierarchy = component.getSelectionHierarchy();
		if(selectionHierarchy == null)
			selectionHierarchy = new ORefList();
		propertiesPanel.setObjectRefs(selectionHierarchy.toArray());
	}

	public AbstractObjectDataInputPanel getPropertiesPanel()
	{
		return propertiesPanel;
	}

	public void addAboveTable(JComponent componentToAdd)
	{
		add(componentToAdd, BorderLayout.BEFORE_FIRST_LINE);
		validate();
	}
	
	public void addButton(ObjectsAction action)
	{
		addButton(createObjectsActionButton(action, component));
	}
	
	public void addButton(UiButton button)
	{
		buttons.add(button);
	}
	
	public MainWindow getMainWindow()
	{
		return mainWindow;
	}
	
	public Project getProject()
	{
		return getMainWindow().getProject();
	}
	
	public ObjectPicker getPicker()
	{
		return component;
	}
	
	abstract public BaseObject getSelectedObject();
	
	abstract public void commandExecuted(CommandExecutedEvent event);
	
	private MainWindow mainWindow;
	private OneRowPanel buttons;
	private ObjectPicker component;
	private AbstractObjectDataInputPanel propertiesPanel;

}
