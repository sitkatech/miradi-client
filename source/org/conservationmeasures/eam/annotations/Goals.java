/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.annotations;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Vector;


public class Goals extends NodeAnnotations 
{
	public Goals() 
	{
		super();
	}

	public Goals(DataInputStream dataIn) throws IOException 
	{
		super(dataIn);
	}

	public void readDataFrom(DataInputStream dataIn) throws IOException
	{
		int size = dataIn.readInt();
		for(int i = 0; i < size; ++i)
		{
			Goal goal = new Goal(dataIn.readUTF());
			add(goal);
		}
	}
	
	public Goal get(int i)
	{
		return (Goal)(getAnnotation(i));
	}
	
	public void setGoals(Goal goal)
	{
		setAnnotations(goal);
	}

	public void setGoals(Vector goals)
	{
		setAnnotations(goals);
	}

	public boolean equals(Object obj) 
	{
		if(!(obj instanceof Goals))
			return false;
		if(size() != ((Goals)obj).size())
			return false;
		for(int i = 0; i < size(); ++i)
		{
			if(!get(i).equals(((Goals)obj).get(i)))
				return false;
		}
		return true;
	}
	
	public static Goals getAllGoals()
	{
		//TODO: These will be replaced by real user entered data from a wizard
		Goals goals = new Goals();
		goals.add(new Goal("Goal 1"));
		goals.add(new Goal("Goal 2"));
		goals.add(new Goal("Goal 3"));
		return goals;
	}
}
