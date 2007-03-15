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

abstract public class ObjectDataInputPanel extends AbstractObjectDataInputPanel implements CommandExecutedListener
{
	
	public ObjectDataInputPanel(Project projectToUse, int objectType, BaseId idToUse)
	{
		this(projectToUse, new Vector(Arrays.asList(new ORef[] {new ORef(objectType, idToUse)})));
	}
	
	
	public ObjectDataInputPanel(Project projectToUse, ORef orefToUse)
	{
		this(projectToUse, new Vector(Arrays.asList(new ORef[] {orefToUse})));
	}
	
	
	public ObjectDataInputPanel(Project projectToUse, Vector orefsToUse)
	{
		super(projectToUse, orefsToUse);
		GridLayoutPlus layout = new GridLayoutPlus(0, 2);
		layout.setColAlignment(0, Alignment.NORTHEAST);
		setLayout(layout);
	}
	
	public ObjectDataInputField addField(ObjectDataInputField field)
	{
		super.addField(field);
		addLabel(field.getObjectType(), field.getTag());
		addFieldComponent(field.getComponent());
		return field;
	}

	public void addLabel(String translatedLabelText)
	{
		addLabel(getORef(0).getObjectType(), translatedLabelText);
	}
	
	public void addLabel(int objectType, String translatedLabelText)
	{
		UiLabel label = new UiLabel(EAM.fieldLabel(objectType, translatedLabelText));
		label.setVerticalAlignment(SwingConstants.TOP);
		add(label);
	}
	
	public void addFieldComponent(Component component)
	{
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(component, BorderLayout.BEFORE_LINE_BEGINS);
		add(panel);
	}
}
