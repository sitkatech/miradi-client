/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.objects;

import java.io.DataInputStream;
import java.io.IOException;

import org.conservationmeasures.eam.annotations.NodeAnnotationIds;



public class ObjectiveIds extends NodeAnnotationIds
{
	public ObjectiveIds()
	{
		super();
	}
	
	public ObjectiveIds(DataInputStream dataIn) throws IOException
	{
		super(dataIn);
	}
	
	public void readDataFrom(DataInputStream dataIn) throws IOException
	{
		int size = readCount(dataIn);
		for(int i = 0; i < size; ++i)
		{
			addId(dataIn.readInt());
		}
	}
	
	public void setObjectives(Objective objective)
	{
		setAnnotationId(objective.getId());
	}
}
