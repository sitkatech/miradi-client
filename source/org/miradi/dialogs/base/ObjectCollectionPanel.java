/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
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
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;
import org.miradi.utils.MiradiScrollPane;
import org.miradi.views.umbrella.ObjectPicker;

abstract public class ObjectCollectionPanel extends DisposablePanel implements CommandExecutedListener
{
	public ObjectCollectionPanel(Project projectToUse, ObjectPicker componentToUse)
	{
		super(new BorderLayout());
		project = projectToUse;
		component = componentToUse;
		add(new MiradiScrollPane((JComponent)component), BorderLayout.CENTER);
		buttons = new OneRowPanel();
		buttons.setGaps(3);
		buttons.setBackground(AppPreferences.getDataPanelBackgroundColor());

		add(buttons, BorderLayout.AFTER_LAST_LINE);
		setFocusCycleRoot(true);
		project.addCommandExecutedListener(this);

		setBackground(AppPreferences.getDataPanelBackgroundColor());
		setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
	}
	
	
	public void dispose()
	{
		project.removeCommandExecutedListener(this);
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

	
	public void addButton(ObjectsAction action)
	{
		addButton(createObjectsActionButton(action, component));
	}
	
	public void addButton(UiButton button)
	{
		buttons.add(button);
	}
	
	public Project getProject()
	{
		return project;
	}
	
	public ObjectPicker getPicker()
	{
		return component;
	}
	
	abstract public BaseObject getSelectedObject();
	
	abstract public void commandExecuted(CommandExecutedEvent event);
	
	private Project project;
	private OneRowPanel buttons;
	private ObjectPicker component;
	private AbstractObjectDataInputPanel propertiesPanel;

}
