/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;


public interface EAMObject
{
	public BaseId getId();
	public int getType();
	public void setData(String fieldTag, String dataValue) throws Exception;
	public String getData(String fieldTag);
	public CreateObjectParameter getCreationExtraInfo();
	public EnhancedJsonObject toJson();
	public ORef getRef();
	public CommandSetObjectData[] createCommandsToClear();
}
