/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.annotations;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Vector;

import org.conservationmeasures.eam.diagram.nodes.DiagramNode;


public class Objectives extends NodeAnnotations
{
	public Objectives()
	{
		super();
	}
	
	public Objectives(DataInputStream dataIn) throws IOException
	{
		super(dataIn);
	}

	public void readDataFrom(DataInputStream dataIn) throws IOException
	{
		int size = dataIn.readInt();
		for(int i = 0; i < size; ++i)
		{
			Objective objective = new Objective(dataIn.readUTF());
			add(objective);
		}
	}
	
	public Objective get(int i)
	{
		return (Objective)(getAnnotation(i));
	}
	
	public void setObjectives(Objective objective)
	{
		setAnnotations(objective);
	}

	public void setObjectives(Vector objectivesToUse)
	{
		setAnnotations(objectivesToUse);
	}

	public boolean equals(Object obj) 
	{
		if(!(obj instanceof Objectives))
			return false;
		if(size() != ((Objectives)obj).size())
			return false;
		for(int i = 0; i < size(); ++i)
		{
			if(!get(i).equals(((Objectives)obj).get(i)))
				return false;
		}
		return true;
	}
	
	public static Objectives getAllObjectives(DiagramNode node)
	{
		//TODO: These will be replaced by real user entered data from a wizard
		Objectives objectives = new Objectives();
		objectives.add(new Objective(Objective.ANNOTATION_NONE_STRING));
		if(node.isDirectThreat())
		{
			objectives.add(new Objective("Obj 1"));
			objectives.add(new Objective("Obj 2"));
			objectives.add(new Objective("Obj 3"));
			objectives.add(new Objective("Obj 4"));
			objectives.add(new Objective("Obj 5"));
		}
		else
		{
			objectives.add(new Objective("Obj A"));
			objectives.add(new Objective("Obj B"));
			objectives.add(new Objective("Obj C"));
			objectives.add(new Objective("Obj D"));
			objectives.add(new Objective("Obj E"));
		}
		return objectives;
	}
}
