/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;


public interface EAMObject
{
	public BaseId getId();
	public int getType();
	public void setData(String fieldTag, String dataValue) throws Exception;
	public String getData(String fieldTag);
	public CreateObjectParameter getCreationExtraInfo();
	public EnhancedJsonObject toJson();
}
