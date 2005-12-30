/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.annotations;

import org.conservationmeasures.eam.project.IdAssigner;
import org.conservationmeasures.eam.project.ObjectPool;

public class ObjectivePool extends ObjectPool
{
	public void put(Objective objective)
	{
		put(objective.getId(), objective);
	}
	
	public Objective find(int id)
	{
		return (Objective)getRawObject(id);
	}

	public static ObjectivePool createSampleObjectives(IdAssigner assigner)
	{
		//TODO: These will be replaced by real user entered data from a wizard
		ObjectivePool objectives = new ObjectivePool();
		
		objectives.put(new Objective(-1, Objective.ANNOTATION_NONE_STRING));
		objectives.put(new Objective(assigner.takeNextId(), "Obj 1"));
		objectives.put(new Objective(assigner.takeNextId(), "Obj 2"));
		objectives.put(new Objective(assigner.takeNextId(), "Obj 3"));
		objectives.put(new Objective(assigner.takeNextId(), "Obj A"));
		objectives.put(new Objective(assigner.takeNextId(), "Obj B"));
		objectives.put(new Objective(assigner.takeNextId(), "Obj C"));
		return objectives;
	}
}
