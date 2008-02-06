/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogfields;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.project.Project;

public class ObjectStringMapInputField extends ObjectStringInputField
{
	public ObjectStringMapInputField(Project projectToUse, int objectTypeToUse, BaseId objectIdToUse, String tagToUse, String codeToUse, int columnsToUse)
	{
		super(projectToUse, objectTypeToUse, objectIdToUse, tagToUse, columnsToUse);
		
		code = codeToUse;
	}
	
	//FIXME finish setText and getText
	@Override
	public String getText()
	{
		return super.getText();
	}
	
	@Override
	public void setText(String newValue)
	{
		//BaseObject object = getProject().findObject(getORef());
		//String data = object.getData(getTag());
		//StringMapData stringMap = new StringMapData(getTag(), data);
		//StringMapData
		super.setText(newValue);
	}
	
	String code;
}
