/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.diagram.nodes;

import java.util.Vector;

public class Objectives 
{
	public Objectives()
	{
		objectives = new Vector();
	}
	
	private void add(Objective objectiveToAdd)
	{
		objectives.add(objectiveToAdd);
	}
	
	public int getSize()
	{
		return objectives.size();
	}
	
	public Objective get(int i)
	{
		return (Objective)objectives.get(i);
	}
	
	public static Objectives getAllObjectives(DiagramNode node)
	{
		//TODO: These will be replaced by real user entered data from a wizard
		Objectives objectives = new Objectives();
		if(node.isDirectThreat())
		{
			objectives.add(new Objective());
			objectives.add(new Objective("1a"));
			objectives.add(new Objective("1b"));
			objectives.add(new Objective("2"));
		}
		else if (node.isIndirectFactor())
		{
			objectives.add(new Objective());
			objectives.add(new Objective("A"));
			objectives.add(new Objective("B"));
			objectives.add(new Objective("C"));
			objectives.add(new Objective("D"));
		}
		else if (node.isStress())
		{
			objectives.add(new Objective());
			objectives.add(new Objective("1"));
			objectives.add(new Objective("2"));
			objectives.add(new Objective("3"));
		}
		else if (node.isIntervention())
		{
			objectives.add(new Objective());
			objectives.add(new Objective("a"));
			objectives.add(new Objective("b"));
			objectives.add(new Objective("c"));
		}
		return objectives;
	}
	Vector objectives;
}
