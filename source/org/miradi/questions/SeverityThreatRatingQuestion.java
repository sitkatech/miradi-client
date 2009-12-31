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
		return EAM.text("<html><b>Low:</b> Within the scope, the threat is likely to only slightly degrade/reduce the target or reduce its population by 1-10% within ten years or three generations.</html>");
	}
	
	@Override
	protected String getMediumRatingChoiceItemDescription()
	{
		return EAM.text("<html><b>Medium:</b> Within the scope, the threat is likely to moderately degrade/reduce the target or reduce its population by 11-30% within ten years or three generations.</html>");
	}

	@Override
	protected String getHighRatingChoiceItemDescription()
	{
		return EAM.text("<html><b>High:</b> Within the scope, the threat is likely to seriously degrade/reduce the target or reduce its population by 31-70% within ten years or three generations.</html>");
	}

	@Override
	protected String getVeryHighRatingChoiceItemDescription()
	{
		return EAM.text("<html><b>Very High:</b> Within the scope, the threat is likely to destroy or eliminate the target, or reduce its population by 71-100% within ten years or three generations.</html>");
	}
	
	private static String getDescription()
	{
		return EAM.text("<html><strong>Severity - </strong>Within the scope, the level of damage to the target from " +
						"the threat that can reasonably be expected given the continuation of current circumstances " +
						"and trends. For ecosystems and ecological communities, typically measured as the degree " +
						"of destruction or degradation of the target within the scope. For species, usually measured " +
						"as the degree of reduction of the target population within the scope.</html>");
	}
}
