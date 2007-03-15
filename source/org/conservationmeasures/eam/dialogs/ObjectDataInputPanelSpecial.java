/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.conservationmeasures.eam.dialogfields.ObjectDataInputField;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.CommandExecutedListener;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.project.Project;
import org.martus.swing.UiLabel;

import com.jhlabs.awt.Alignment;
import com.jhlabs.awt.GridLayoutPlus;

abstract public class ObjectDataInputPanelSpecial extends AbstractObjectDataInputPanel implements CommandExecutedListener
{
	
	public ObjectDataInputPanelSpecial(Project projectToUse, int objectType, BaseId idToUse)
	{
		this(projectToUse, new Vector(Arrays.asList(new ORef[] {new ORef(objectType, idToUse)})));
	}
	
	
	public ObjectDataInputPanelSpecial(Project projectToUse, ORef orefToUse)
	{
		this(projectToUse, new Vector(Arrays.asList(new ORef[] {orefToUse})));
	}
	
	
	public ObjectDataInputPanelSpecial(Project projectToUse, Vector orefsToUse)
	{
		super(projectToUse, orefsToUse);
		setLayout(new BorderLayout());
	}
	
	public UiLabel createLabel(ObjectDataInputField field)
	{
		UiLabel label = new UiLabel(EAM.fieldLabel(field.getObjectType(), field.getTag()));
		label.setVerticalAlignment(SwingConstants.TOP);
		return label;
	}
	
	public void addFieldComponent(Component component)
	{
		add(component);
	}
	
	public JPanel createRowBox(ObjectDataInputField[] fields, int columns)
	{
		JPanel panel = createGridLayoutPanel(columns);
		for (int i=0; i<fields.length; ++i)
		{
			createRow(panel, fields[i]);
		}
		return panel;
	}
	
	private void createRow(JPanel box, ObjectDataInputField field)
	{
		JPanel panel = new JPanel(new BorderLayout());
		box.add(createLabel(field));
		panel.add(field.getComponent(), BorderLayout.BEFORE_LINE_BEGINS);
		box.add(panel);
	}
	
	public JPanel createColumnBox(ObjectDataInputField[] fields, int columns)
	{
		JPanel panel = createGridLayoutPanel(columns);
		for (int i=0; i<fields.length; ++i)
		{
			createColumn(panel, fields[i], columns, i);
		}
		return panel;
	}


	private void createColumn(JPanel box, ObjectDataInputField field, int columns, int column)
	{
		JPanel panel = new JPanel(new GridLayoutPlus(1,1));
		if (field==null)
		{
			panel.add(new JPanel());
		}
		else
		{
			if (column<columns) panel.add(createLabel(field));
			panel.add(field.getComponent(), BorderLayout.BEFORE_LINE_BEGINS);
		}
		box.add(panel);
	}
	
	

	private JPanel createGridLayoutPanel(int columns)
	{
		JPanel panel = new JPanel();
		GridLayoutPlus layout = new GridLayoutPlus(0, columns);
		layout.setColAlignment(0, Alignment.NORTHEAST);
		panel.setLayout(layout);
		return panel;
	}
	
	
}
