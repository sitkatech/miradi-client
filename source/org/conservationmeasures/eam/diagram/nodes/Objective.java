/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.diagram.nodes;

import java.awt.Color;

import org.conservationmeasures.eam.main.EAM;

public class Objective 
{
	public Objective()
	{
		objective = OBJECTIVE_NONE_STRING;
	}
	
	public Objective(String objectiveToUse)
	{
		objective = objectiveToUse;
	}
	
	public boolean hasObjective()
	{
		return !objective.equals(OBJECTIVE_NONE_STRING);
	}


	public String toString()
	{
		return String.valueOf(objective).toString();
	}
		
	public boolean equals(Object obj) 
	{
		if(!(obj instanceof Objective))
			return false;
		return ((Objective)obj).objective.equals(objective);
	}
	
	public String getLabel()
	{
		return objective;
	}
	
	public void setLabel(String value)
	{
		objective = value;
	}
	
	public Color getColor()
	{
		return LIGHT_BLUE;
	}
	
	public static final String OBJECTIVE_NONE_STRING = EAM.text("None");
	private static final Color LIGHT_BLUE = Color.getHSBColor((float)0.73,(float)0.80,1);
	String objective;

}
