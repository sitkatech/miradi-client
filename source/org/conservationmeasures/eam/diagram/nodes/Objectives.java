/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.diagram.nodes;

import java.awt.Color;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Vector;

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
	
	public Color getColor()
	{
		return LIGHT_BLUE;
	}
	
	public boolean hasObjectives()
	{
		return hasAnnotation();
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
			objectives.add(new Objective("1a"));
			objectives.add(new Objective("1b"));
			objectives.add(new Objective("2"));
		}
		else if (node.isIndirectFactor())
		{
			objectives.add(new Objective("A"));
			objectives.add(new Objective("B"));
			objectives.add(new Objective("C"));
			objectives.add(new Objective("D"));
		}
		else if (node.isStress())
		{
			objectives.add(new Objective("1"));
			objectives.add(new Objective("2"));
			objectives.add(new Objective("3"));
		}
		else if (node.isIntervention())
		{
			objectives.add(new Objective("a"));
			objectives.add(new Objective("b"));
			objectives.add(new Objective("c"));
		}
		return objectives;
	}
	private static final Color LIGHT_BLUE = new Color(204,238,255);
}
