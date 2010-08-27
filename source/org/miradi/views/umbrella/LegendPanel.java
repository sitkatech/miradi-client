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
package org.miradi.views.umbrella;

import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Hashtable;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;

import org.miradi.actions.EAMAction;
import org.miradi.dialogs.base.DisposablePanel;
import org.miradi.dialogs.fieldComponents.PanelButton;
import org.miradi.dialogs.fieldComponents.PanelCheckBox;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.project.Project;
import org.miradi.utils.CodeList;
import org.miradi.utils.LocationHolder;
import org.miradi.utils.ObjectsActionButton;

import com.jhlabs.awt.BasicGridLayout;

abstract public class LegendPanel extends DisposablePanel implements ActionListener
{
	public LegendPanel(Project projectToUse)
	{
		super(new BasicGridLayout(0, 1));
	
		project = projectToUse;
		checkBoxes = new Hashtable<String, JCheckBox>();
		buttonsToDispose = new HashSet<ObjectsActionButton>();
		setBackground(AppPreferences.getControlPanelBackgroundColor());
	}
	
	@Override
	public void dispose()
	{
		super.dispose();
		for(ObjectsActionButton button : buttonsToDispose)
		{
			button.dispose();
		}
	}
	
	protected Project getProject()
	{
		return project;
	}

	protected JCheckBox findCheckBox(Object property)
	{
		return checkBoxes.get(property);
	}

	public boolean isSelected(String property)
	{
		JCheckBox checkBox = findCheckBox(property);
		if (checkBox == null)
			return false;
		
		return checkBox.isSelected();
	}

	protected void createCheckboxes(CodeList checkBoxCodes)
	{
		for (int i = 0; i < checkBoxCodes.size(); ++i)
		{
			createCheckBox(checkBoxCodes.get(i));
		}
	}
	
	protected void createCheckBox(String objectName)
	{
		JCheckBox component = new PanelCheckBox();
		component.setBackground(AppPreferences.getControlPanelBackgroundColor());
		checkBoxes.put(objectName, component);
		
		component.putClientProperty(LAYER, new String(objectName));
		component.addActionListener(this);
	}

	protected void addIconLineWithCheckBox(JComponent panel, int objectType, String objectName, Icon icon)
	{
		JCheckBox foundCheckBox = findCheckBox(objectName);
		String foundLabel = EAM.fieldLabel(objectType, objectName);
		addIconLine(panel, foundLabel, icon, foundCheckBox);
	}

	protected void addButtonLineWithCheckBox(JComponent panel, int objectType, String objectName, EAMAction action)
	{
		JButton button = new LocationButton(action);

		panel.add(findCheckBox(objectName));
		panel.add(button);
	}

	protected void addIconLine(JComponent panel, String text, Icon icon, JCheckBox checkbox)
	{
		panel.add(checkbox);
		panel.add(new PanelTitleLabel(text, icon));
	}
	
	protected static class LocationButton extends PanelButton implements LocationHolder
	{
		public LocationButton(EAMAction action)
		{
			super(action);
		}
		
		public boolean hasLocation()
		{
			return false;
		}
	}

	protected static final String LAYER = "LAYER";
	Project project;
	protected Hashtable<String, JCheckBox> checkBoxes;
	private HashSet<ObjectsActionButton> buttonsToDispose;
}