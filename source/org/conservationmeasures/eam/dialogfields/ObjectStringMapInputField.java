/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogfields;

import java.text.ParseException;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.StringMap;
import org.conservationmeasures.eam.project.Project;

public class ObjectStringMapInputField extends ObjectStringInputField
{
	public ObjectStringMapInputField(Project projectToUse, int objectTypeToUse, BaseId objectIdToUse, String tagToUse, String codeToUse, int columnsToUse)
	{
		super(projectToUse, objectTypeToUse, objectIdToUse, tagToUse, columnsToUse);
		
		code = codeToUse;
	}
	
	@Override
	public String getText()
	{
		if (getORef().isInvalid())
			return "";
						
		try
		{
			String data = getProject().getObjectData(getORef(), getTag());
			StringMap stringMap = new StringMap(data);
			stringMap.add(code, super.getText());
			
			return stringMap.toString();
		}
		catch(ParseException e)
		{
			EAM.logException(e);
			return "";
		}
	}
	
	@Override
	public void setText(String newValue)
	{
		try
		{
			StringMap stringMap = new StringMap(newValue);
			String value = stringMap.get(code);
			super.setText(value);
		}
		catch (Exception e)
		{
			//FIXME when fixing other setText fixmes, fix this as well
			EAM.logException(e);
		}
	}
	
	private String code;
}
