/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.json.JSONObject;


public interface EAMObject
{
	public int getId();
	public int getType();
	public void setData(String fieldTag, Object dataValue) throws Exception;
	public String getData(String fieldTag);
	public JSONObject toJson();
}
