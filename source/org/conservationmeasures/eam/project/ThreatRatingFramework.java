/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.project;

import java.awt.Color;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.ThreatRatingCriterion;
import org.conservationmeasures.eam.objects.ThreatRatingValueOption;


public class ThreatRatingFramework
{
	public ThreatRatingFramework(IdAssigner idAssignerToUse)
	{
		idAssigner = idAssignerToUse;
		
		criteria = new ThreatRatingCriterion[] {
			new ThreatRatingCriterion(idAssigner.takeNextId(), "Scope"), 
			new ThreatRatingCriterion(idAssigner.takeNextId(), "Severity"),
			new ThreatRatingCriterion(idAssigner.takeNextId(), "Urgency"),
			new ThreatRatingCriterion(idAssigner.takeNextId(), "Custom"),
		};
				
		options =  new ThreatRatingValueOption[] {
			new ThreatRatingValueOption(idAssigner.takeNextId(), EAM.text("Label|Very High"), Color.RED),
			new ThreatRatingValueOption(idAssigner.takeNextId(), EAM.text("Label|High"), Color.ORANGE),
			new ThreatRatingValueOption(idAssigner.takeNextId(), EAM.text("Label|Medium"), Color.YELLOW),
			new ThreatRatingValueOption(idAssigner.takeNextId(), EAM.text("Label|Low"), Color.GREEN),
			new ThreatRatingValueOption(idAssigner.takeNextId(), EAM.text("Label|None"), Color.WHITE),
		};
		
	}

	public ThreatRatingCriterion[] getCriteria()
	{
		return criteria;
	}
	
	public ThreatRatingValueOption[] getValueOptions()
	{
		return options;
	}
	
	private IdAssigner idAssigner;
	private ThreatRatingCriterion[] criteria;
	private ThreatRatingValueOption[] options;
}
