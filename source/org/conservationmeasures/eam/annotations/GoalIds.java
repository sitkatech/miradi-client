/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.annotations;

import java.io.DataInputStream;
import java.io.IOException;

import org.conservationmeasures.eam.objects.IdList;



public class GoalIds extends NodeAnnotationIds 
{
	public GoalIds() 
	{
		super();
	}
	
	public GoalIds(IdList ids)
	{
		super(ids);
	}

	public GoalIds(DataInputStream dataIn) throws IOException 
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
	
	public void setGoalId(int id)
	{
		setAnnotationId(id);
	}
}
