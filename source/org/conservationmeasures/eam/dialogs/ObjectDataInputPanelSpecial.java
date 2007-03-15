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

import javax.swing.SwingConstants;

import org.conservationmeasures.eam.dialogfields.ObjectDataInputField;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.CommandExecutedListener;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.project.Project;
import org.martus.swing.UiLabel;

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
}
