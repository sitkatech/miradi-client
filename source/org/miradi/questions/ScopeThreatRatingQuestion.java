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

public class ScopeThreatRatingQuestion extends ThreatRatingQuestion
{
	public ScopeThreatRatingQuestion()
	{
		super(getDescription());
	}

	@Override
	protected String getLowRatingChoiceItemDescription()
	{
		return EAM.text("<html><b>Low:</b> The threat is likely to be very narrow in its scope, affecting the target across a small proportion (1-10%) of its occurrence/population.</html>");
	}
	
	@Override
	protected String getMediumRatingChoiceItemDescription()
	{
		return EAM.text("<html><b>Medium:</b> The threat is likely to be restricted in its scope, affecting the target across some (11-30%) of its occurrence/population.</html>");
	}

	@Override
	protected String getHighRatingChoiceItemDescription()
	{
		return EAM.text("<html><b>High:</b> The threat is likely to be widespread in its scope, affecting the target across much (31-70%) of its occurrence/population.</html>");
	}

	@Override
	protected String getVeryHighRatingChoiceItemDescription()
	{
		return EAM.text("<html><b>Very High:</b> The threat is likely to be pervasive in its scope, affecting the target across all or most (71-100%) of its occurrence/population.</html>");
	}
	
	private static String getDescription()
	{
		return EAM.text("<html><strong>Scope - </strong>Most commonly defined spatially as the proportion of the target " +
				"that can reasonably be expected to be affected by the threat within ten years given " +
				"the continuation of current circumstances and trends. For ecosystems and ecological communities, " +
				"measured as the proportion of the target's occurrence. For species, measured as the proportion " +
				"of the target's population.</html>");
	}
}
