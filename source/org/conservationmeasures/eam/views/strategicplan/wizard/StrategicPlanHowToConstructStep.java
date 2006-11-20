/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan.wizard;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.views.umbrella.WizardPanel;
import org.conservationmeasures.eam.views.umbrella.WizardStep;

public class StrategicPlanHowToConstructStep extends WizardStep
{
	public StrategicPlanHowToConstructStep(WizardPanel wizardToUse)
	{
		super(wizardToUse);
	}
	
	public String getResourceFileName()
	{
		return HTML_FILE_NAME;
	}
	
	public void linkClicked(String linkDescription)
	{
		//TODO definitions need to be central.  
		//right now the same def appears in another class.  
		if(linkDescription.equals("Definition:StrategicPlan"))
		{
			EAM.okDialog("Definition:StrategicPlan", new String[] {
				"Strategic plan -- An outline of how the project team proposes to" +
				" change the world that contains a project's goals, objectives," +
				" and strategies." });
		}
		if(linkDescription.equals("Definition:Goal"))
		{
			EAM.okDialog("Definition:Goals", new String[] {
				"Goal -- A formal statement detailing a desired impact of a project.  " +
				"In conservation projects, it is the desired future status of a target." });
		}
		if(linkDescription.equals("Definition:Objective"))
		{
			EAM.okDialog("Definition:Objective", new String[] {
				"Objective -- A formal statement detailing a desired " +
				"outcome of a project, such as reducing a critical threat." });
		}
		if(linkDescription.equals("Definition:Strategies"))
		{
			EAM.okDialog("Definition:Strategies", new String[] {
				"Strategies -- A broad course of action designed to restore natural systems, " +
				"reduce threats, and/or develop capacity.  A strategy is typically " +
				"used as an umbrella term to describe a set of specific " +
				"conservation actions." });
		}
	}
	
	private static final String HTML_FILE_NAME = "HowToConstructStratPlan.html";
}
