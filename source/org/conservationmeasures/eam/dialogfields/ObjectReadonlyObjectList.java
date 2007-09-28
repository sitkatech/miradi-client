/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogfields;

import javax.swing.JComponent;
import javax.swing.JTextArea;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.project.Project;

public class ObjectReadonlyObjectList extends ObjectDataInputField
{
	public ObjectReadonlyObjectList(Project projectToUse, int objectTypeToUse, BaseId idToUse, String tagToUse)
	{
		super(projectToUse, objectTypeToUse, idToUse, tagToUse);
		
		textArea = new JTextArea();
	}

	private void updateComponent()
	{
		try
		{
			BaseObject foundObject = getProject().findObject(new ORef(getObjectType(), getObjectId()));
			ORefList orefList = new ORefList(foundObject.getData(getTag()));
			
			String names = "";
			for (int i = 0; i < orefList.size(); ++i)
			{
				BaseObject object = project.findObject(orefList.get(i)); 
				names += object.toString() + "\n";
			}
			textArea.setText(names);
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}

	public void updateFromObject()
	{
		super.updateFromObject();
		updateComponent();
	}
	
	public JComponent getComponent()
	{
		return textArea;
	}

	public String getText()
	{
		return null;
	}

	public void setText(String newValue)
	{
	}

	private JTextArea textArea;
}
