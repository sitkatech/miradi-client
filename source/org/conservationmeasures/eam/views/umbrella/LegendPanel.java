/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.umbrella;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import org.conservationmeasures.eam.actions.EAMAction;
import org.conservationmeasures.eam.actions.ObjectsAction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.dialogs.fieldComponents.PanelButton;
import org.conservationmeasures.eam.dialogs.fieldComponents.PanelCheckBox;
import org.conservationmeasures.eam.dialogs.fieldComponents.PanelTitleLabel;
import org.conservationmeasures.eam.main.AppPreferences;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.CodeList;
import org.conservationmeasures.eam.utils.LocationHolder;
import org.conservationmeasures.eam.utils.ObjectsActionButton;
import org.martus.swing.UiLabel;

import com.jhlabs.awt.BasicGridLayout;

abstract public class LegendPanel extends JPanel implements ActionListener
{
	public LegendPanel(Project projectToUse)
	{
		super(new BasicGridLayout(0, 1));
		project = projectToUse;
		checkBoxes = new Hashtable();
		setBackground(AppPreferences.CONTROL_PANEL_BACKGROUND);
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
	
	protected void addIconLine(JComponent panel, String text, Icon icon, JComponent component)
	{
		panel.add(new JLabel(icon));
		panel.add(new PanelTitleLabel(EAM.text(text)));
		panel.add(component);
	}
	
	protected void addSeparator(JComponent panel)
	{
		panel.add(new JLabel(" "));
		panel.add(new JLabel(" "));
		panel.add(new JLabel(" "));
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
	
	protected void addIconLineWithoutCheckBox(JComponent panel, int objectType, String objectName, Icon icon)
	{
		addIconLine(panel, EAM.fieldLabel(objectType, objectName), icon, new UiLabel(""));
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
		panel.add(button);
		panel.add(new PanelTitleLabel(EAM.fieldLabel(objectType, objectName)));
		panel.add(findCheckBox(objectName));
	}

	protected void addPickerButtonLineWithCheckBox(JComponent panel, int objectType, String objectName, ObjectsAction action, ObjectPicker picker)
	{
		ObjectsActionButton button = new ObjectsActionButton(action, picker);
		
		panel.add(button);
		panel.add(new PanelTitleLabel(EAM.fieldLabel(objectType, objectName)));
		panel.add(findCheckBox(objectName));
	}

	protected void addButtonLineWithoutCheckBox(JComponent panel, int objectType, String objectName, EAMAction action)
	{
		JButton button = new LocationButton(action);
		panel.add(button);
		panel.add(new PanelTitleLabel(EAM.fieldLabel(objectType, objectName)));
		panel.add(new UiLabel(""));
	}
	
	protected static class LocationButton extends PanelButton implements LocationHolder
	{
		public LocationButton(EAMAction action)
		{
			super(action);
			setText(null);
			setMargin(new Insets(0, 0, 0 ,0));
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
}