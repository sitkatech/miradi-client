/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.base;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.conservationmeasures.eam.actions.ObjectsAction;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.CommandExecutedListener;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.FastScrollPane;
import org.conservationmeasures.eam.views.umbrella.ObjectPicker;
import org.martus.swing.UiButton;

import com.jhlabs.awt.GridLayoutPlus;

abstract public class ObjectCollectionPanel extends DisposablePanel implements CommandExecutedListener
{
	public ObjectCollectionPanel(Project projectToUse, ObjectPicker componentToUse)
	{
		super(new BorderLayout());
		project = projectToUse;
		component = componentToUse;
		add(new FastScrollPane((JComponent)component), BorderLayout.CENTER);
		GridLayoutPlus layout = new GridLayoutPlus(0, 1);
		buttons = new JPanel(layout);
		add(buttons, BorderLayout.AFTER_LINE_ENDS);
		setFocusCycleRoot(true);
		project.addCommandExecutedListener(this);
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
	private JPanel buttons;
	private ObjectPicker component;
	private AbstractObjectDataInputPanel propertiesPanel;

}
