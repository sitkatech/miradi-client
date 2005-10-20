/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.diagram.nodes;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Vector;

public class Objectives 
{
	public Objectives()
	{
		objectives = new Vector();
	}
	
	public Objectives(DataInputStream dataIn) throws IOException
	{
		int size = dataIn.readInt();
		objectives = new Vector(size);
		for(int i = 0; i < size; ++i)
		{
			Objective objective = new Objective(dataIn.readUTF());
			add(objective);
		}
	}
	
	public static Objectives createObjectivesNone()
	{
		Objectives objectives = new Objectives();
		objectives.add(new Objective());
		return objectives;
	}

	public void writeDataTo(DataOutputStream dataOut) throws IOException
	{
		dataOut.writeInt(size());
		for(int i = 0; i < size(); ++i)
		{
			dataOut.writeUTF(get(i).getLabel());
		}
	}
	
	public void add(Objective objectiveToAdd)
	{
		objectives.add(objectiveToAdd);
	}
	
	public void setObjectives(Objective objectiveToUse)
	{
		Vector objective = new Vector();
		objective.add(objectiveToUse);
		objectives = objective;
	}

	public void setObjectives(Vector objectivesToUse)
	{
		objectives = objectivesToUse;
	}

	public int size()
	{
		return objectives.size();
	}
	
	public boolean hasObjective()
	{
		for(int i = 0 ; i < size(); ++i)
		{
			if(get(i).hasObjective())
				return true;
		}
		return false;
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
	
	
	public Objective get(int i)
	{
		return (Objective)objectives.get(i);
	}
	
	public static Objectives getAllObjectives(DiagramNode node)
	{
		//TODO: These will be replaced by real user entered data from a wizard
		Objectives objectives = new Objectives();
		objectives.add(new Objective()); //Add the "NONE" objective
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
	Vector objectives;
}
