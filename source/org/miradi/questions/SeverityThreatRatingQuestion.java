/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 

package org.miradi.questions;

import org.miradi.main.EAM;

public class SeverityThreatRatingQuestion extends ThreatRatingQuestion
{
	public SeverityThreatRatingQuestion()
	{
		super(getDescription());
	}

	@Override
	protected String getLowRatingChoiceItemDescription()
	{
		return EAM.text("<html><b>Low:</b> Within the scope, the threat is likely to only <b>slightly degrade/reduce</b> the target or reduce its population by <b>1-10%</b> within ten years or three generations.</html>");
	}
	
	@Override
	protected String getMediumRatingChoiceItemDescription()
	{
		return EAM.text("<html><b>Medium:</b> Within the scope, the threat is likely to <b>moderately degrade/reduce</b> the target or reduce its population by <b>11-30%</b> within ten years or three generations.</html>");
	}

	@Override
	protected String getHighRatingChoiceItemDescription()
	{
		return EAM.text("<html><b>High:</b> Within the scope, the threat is likely to <b>seriously degrade/reduce</b> the target or reduce its population by <b>31-70%</b> within ten years or three generations.</html>");
	}

	@Override
	protected String getVeryHighRatingChoiceItemDescription()
	{
		return EAM.text("<html><b>Very High:</b> Within the scope, the threat is likely to <b>destroy or eliminate</b> the target, or reduce its population by <b>71-100%</b> within ten years or three generations.</html>");
	}
	
	private static String getDescription()
	{
		String description = EAM.text("<strong>Severity - </strong>Within the scope, the level of damage to the target from " +
						"the threat that can reasonably be expected given the continuation of current circumstances " +
						"and trends. For ecosystems and ecological communities, typically measured as the degree " +
						"of destruction or degradation of the target within the scope. For species, usually measured " +
						"as the degree of reduction of the target population within the scope.");
		
		//NOTE: The thml table tag exists to ensure max width of 750.  Text was getting cut off.
		return EAM.substitute("<html><table><tr><td width='750'> %s </td></tr></table></html>", description);
	}
}
