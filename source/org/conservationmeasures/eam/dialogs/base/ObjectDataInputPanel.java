/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.base;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.conservationmeasures.eam.actions.EAMAction;
import org.conservationmeasures.eam.dialogfields.ObjectDataInputField;
import org.conservationmeasures.eam.dialogs.fieldComponents.PanelFieldLabel;
import org.conservationmeasures.eam.dialogs.fieldComponents.PanelTitleLabel;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.layout.OneRowPanel;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.project.Project;
import org.martus.swing.UiButton;
import org.martus.swing.UiLabel;

import com.jhlabs.awt.Alignment;
import com.jhlabs.awt.GridLayoutPlus;

abstract public class ObjectDataInputPanel extends AbstractObjectDataInputPanel
{
	
	public ObjectDataInputPanel(Project projectToUse, int objectType, BaseId idToUse)
	{
		this(projectToUse, new ORef[] {new ORef(objectType, idToUse)});
	}
	
	
	public ObjectDataInputPanel(Project projectToUse, ORef orefToUse)
	{
		this(projectToUse, new ORef[] {orefToUse});
	}
	
	
	public ObjectDataInputPanel(Project projectToUse, ORef[] orefsToUse)
	{
		super(projectToUse, orefsToUse);
		GridLayoutPlus layout = new GridLayoutPlus(0, 2, HGAP, VGAP);
		layout.setColAlignment(0, Alignment.NORTHEAST);
		setLayout(layout);
	}
	
	public void addLine(Component field1, Component field2)
	{
		add(field1);
		add(field2);
	}
	
	public ObjectDataInputField addField(ObjectDataInputField field)
	{
		super.addField(field);
		addLabel(field.getObjectType(), field.getTag());
		addFieldComponent(field.getComponent());
		return field;
	}
	
	public ObjectDataInputField addFieldWithCustomLabel(ObjectDataInputField field, UiLabel label)
	{
		super.addField(field);
		addLabel(label);
		addFieldComponent(field.getComponent());
		return field;
	}
	
	public ObjectDataInputField addFieldWithCustomLabelAndHint(ObjectDataInputField field, String hint)
	{
		super.addField(field);
		addLabel(field.getObjectType(), field.getTag());
		Box box = Box.createHorizontalBox();
		box.add(field.getComponent());
		box.add(Box.createHorizontalStrut(20));
		box.add(new PanelTitleLabel(hint));
		addFieldComponent(box);
		return field;
	}

	protected void addFieldsOnOneLine(String label, ObjectDataInputField[] fields)
	{
		JPanel fieldPanel = new OneRowPanel();
		for(int i = 0; i < fields.length; ++i)
		{
			super.addField(fields[i]);
			fieldPanel.add(new PanelFieldLabel(fields[i].getObjectType(), fields[i].getTag()));
			fieldPanel.add(fields[i].getComponent());
		}
		addLabel(label);
		add(fieldPanel);
	}

	protected void addFieldWithEditButton(String label, ObjectDataInputField field, EAMAction action)
	{
		super.addField(field);
		JPanel fieldPanel = new OneRowPanel();
		fieldPanel.add(field.getComponent());
		UiButton button = new UiButton(action);
		fieldPanel.add(button);

		addLabel(label);
		add(fieldPanel);
	}


	public void addLabel(String translatedLabelText)
	{
		addLabel(getORef(0).getObjectType(), translatedLabelText);
	}
	
	public void addLabel(int objectType, String translatedLabelText)
	{
		UiLabel label = new PanelFieldLabel(objectType, translatedLabelText);
		addLabel(label);
	}


	public void addLabel(UiLabel label)
	{
		label.setVerticalAlignment(SwingConstants.TOP);
		add(label);
	}
	
	public void addFieldComponent(Component component)
	{
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(component, BorderLayout.BEFORE_LINE_BEGINS);
		add(panel);
	}
	
	static final int HGAP = 3;
	static final int VGAP = 3;
}
