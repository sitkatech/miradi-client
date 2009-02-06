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

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Hashtable;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;

import org.martus.swing.UiLabel;
import org.miradi.actions.EAMAction;
import org.miradi.actions.ObjectsAction;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.dialogs.base.DisposablePanel;
import org.miradi.dialogs.fieldComponents.PanelButton;
import org.miradi.dialogs.fieldComponents.PanelCheckBox;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.ViewData;
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
		checkBoxes = new Hashtable();
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
	
	protected void saveSettingsToProject(String tag)
	{
		try
		{
			ViewData data = getProject().getCurrentViewData();
			CommandSetObjectData setLegendSettingsCommand = new CommandSetObjectData(data.getRef(), tag, getLegendSettings().toString());
			getProject().executeCommand(setLegendSettingsCommand);
		}
		catch(Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog("Unable to update project legend settings:" + e.getMessage());
		}
	}
	
	protected CodeList getLegendSettings(String tag)
	{
		try
		{
			ViewData data = getProject().getCurrentViewData();
			return new CodeList(data.getData(tag));
		}
		catch(Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog("Unable to read project settings:" + e.getMessage());
			return new CodeList();
		}
	}
	
	protected Project getProject()
	{
		return project;
	}

	protected JCheckBox findCheckBox(Object property)
	{
		return (JCheckBox)checkBoxes.get(property);
	}

	public boolean isSelected(String property)
	{
		JCheckBox checkBox = findCheckBox(property);
		if (checkBox == null)
			return false;
		
		return checkBox.isSelected();
	}

	public void disableAllCheckBoxes(boolean enableAll)
	{
		Object[] keys = checkBoxes.keySet().toArray();
		for (int i = 0; i < keys.length; ++i)
		{
			findCheckBox(keys[i]).setEnabled(enableAll);
		}
	}
	
	public void enableAllCheckBoxes()
	{
		Object[] keys = checkBoxes.keySet().toArray();
		for (int i = 0; i < keys.length; ++i)
		{
			findCheckBox(keys[i]).setEnabled(true);
		}
	}

	public void unselectAllCheckBoxes()
	{
		Object[] keys = checkBoxes.keySet().toArray();
		for (int i = 0; i < keys.length; ++i)
		{
			findCheckBox(keys[i]).setSelected(false);
		}
	}
	
	protected void selectAllCheckBoxes()
	{
		setAllCheckboxes(true);
	}

	protected void setAllCheckboxes(boolean newSetting)
	{
		Object[] keys = checkBoxes.keySet().toArray();
		for (int i = 0; i < keys.length; ++i)
		{
			String property = ((JCheckBox)checkBoxes.get(keys[i])).getClientProperty(LAYER).toString();
			JCheckBox checkBox = findCheckBox(property);
			checkBox.setSelected(newSetting);
		}
	}
	
	public UiLabel createTitleBar(String text)
	{
		UiLabel title = new PanelTitleLabel(text);
		title.setFont(title.getFont().deriveFont(Font.BOLD));
		title.setBorder(new LineBorder(Color.BLACK, 2));
		title.setHorizontalAlignment(UiLabel.CENTER);
		title.setBackground(getBackground());
		
		return title;
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

	protected void addCheckBoxLine(JComponent panel, String objectName)
	{
		JCheckBox foundCheckBox = findCheckBox(objectName);
		String foundLabel = EAM.fieldLabel(ObjectType.FAKE, objectName);
		addIconLine(panel, foundLabel, null, foundCheckBox);
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

	protected void addPickerButtonLineWithCheckBox(JComponent panel, int objectType, String objectName, ObjectsAction action, ObjectPicker picker)
	{
		ObjectsActionButton button = new ObjectsActionButton(action, picker);
		buttonsToDispose.add(button);
		button.setText(EAM.fieldLabel(objectType, objectName));
		
		panel.add(findCheckBox(objectName));
		panel.add(button);
	}

	protected void addIconLine(JComponent panel, String text, Icon icon, JCheckBox checkbox)
	{
		panel.add(checkbox);
		panel.add(new PanelTitleLabel(text, icon));
	}
	
	protected void addSeparator(JComponent panel)
	{
		panel.add(new JLabel(" "));
		panel.add(new JLabel(" "));
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

	abstract public CodeList getLegendSettings();
	abstract public void actionPerformed(ActionEvent event);
	
	protected static final String LAYER = "LAYER";
	Project project;
	protected Hashtable checkBoxes;
	private HashSet<ObjectsActionButton> buttonsToDispose;
}