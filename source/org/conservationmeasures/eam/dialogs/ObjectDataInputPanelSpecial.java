/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.Box;
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
	
	public Box createColumn(ObjectDataInputField field)
	{
		Box box = Box.createVerticalBox();
		box.add(createLabel(field));
		box.add(field.getComponent(), BorderLayout.BEFORE_LINE_BEGINS);
		return box;
	}

	public JPanel createColumn(ObjectDataInputField field, Dimension dim)
	{
		JPanel box = createGridLayoutPanel(2,1);
		field.getComponent().setMaximumSize(new Dimension(dim.width, field.getComponent().getPreferredSize().height));
		box.add(createLabel(field));
		box.add(field.getComponent(), BorderLayout.BEFORE_LINE_BEGINS);
		return box;
	}
	
	

	public JPanel createGridLayoutPanel(int row, int columns)
	{
		JPanel panel = new JPanel();
		GridLayoutPlus layout = new GridLayoutPlus(0, columns);
		layout.setColAlignment(0, Alignment.NORTHEAST);
		panel.setLayout(layout);
		return panel;
	}
	
	
}
