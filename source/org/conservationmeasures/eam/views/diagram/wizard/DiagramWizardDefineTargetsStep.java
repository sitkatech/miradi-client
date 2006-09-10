/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram.wizard;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.views.umbrella.WizardPanel;
import org.conservationmeasures.eam.views.umbrella.WizardStep;

public class DiagramWizardDefineTargetsStep extends WizardStep
{

	public DiagramWizardDefineTargetsStep(WizardPanel panelToUse)
	{
		super(panelToUse);
	}

	public void linkClicked(String linkDescription)
	{
		if(linkDescription.equals("Definition:ConservationTargets"))
		{
			EAM.okDialog("Definition: Conservation Targets", new String[] 
			        {"Targets - A limited suite of species, communities, and ecological systems " +
					"that are chosen to represent and encompass the full array of " +
					"biodiversity found in a project area.  They are the basis for " +
					"setting goals, carrying out conservation actions, and measuring " +
					"conservation effectiveness.  In theory – and hopefully in practice " +
					"– conservation of the focal targets will ensure the conservation of " +
					"all native biodiversity within functional landscapes. " +
					" \n\n" +
					"Ecosystems –The ecological systems that characterize the " +
					"terrestrial, aquatic, and marine biodiversity of the project " +
					"site. " +
					" \n\n" +
					"Focal Species – These include species endemic to the ecoregion, " +
					"area-sensitive (umbrella) species, commercially exploited species, " +
					"flagship species, keystone species, or imperiled species. Species " +
					"selected as focal targets are typically those that are not represented " +
					"by the key ecosystems because they require multiple habitats or have " +
					"special conservation requirements. " +
					" \n\n" +
					"Ecological Processes – Ecological processes that create and maintain " +
					"biodiversity. These could include pollination, seed dispersal, " +
					"dispersal of large mammals between protected areas, movements of " +
					"migratory fish, nursery and recruitment areas for coastal fisheries; " +
					"or altitudinal migrations by birds."});
		}
	}

	public String getResourceFileName()
	{
		return "ConservationTargetStep.html";
	}

}
