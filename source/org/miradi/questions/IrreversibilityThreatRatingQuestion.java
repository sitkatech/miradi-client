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

public class IrreversibilityThreatRatingQuestion extends ThreatRatingQuestion
{
	public IrreversibilityThreatRatingQuestion()
	{
		super(getDescription());
	}

	@Override
	protected String getLowRatingChoiceItemDescription()
	{
		return EAM.text("<html><b>Low:</b> The effects of the threat are easily reversible and the target can be easily restored at a relatively low cost and/or within 0-5 years (e.g., off-road vehicles trespassing in wetland).</html>");
	}
	
	@Override
	protected String getMediumRatingChoiceItemDescription()
	{
		return EAM.text("<html><b>Medium:</b> The effects of the threat can be reversed and the target restored with a reasonable commitment of resources and/or within 6-20 years (e.g., ditching and draining of wetland).</html>");
	}

	@Override
	protected String getHighRatingChoiceItemDescription()
	{
		return EAM.text("<html><b>High:</b> The effects of the threat can technically be reversed and the target restored, but it is not practically affordable and/or it would take 21-100 years to achieve this (e.g., wetland converted to agriculture).</html>");
	}

	@Override
	protected String getVeryHighRatingChoiceItemDescription()
	{
		return EAM.text("<html><b>Very High:</b> The effects of the threat cannot be reversed and it is very unlikely the target can be restored, and/or it would take more than 100 years to achieve this (e.g., wetlands converted to a shopping center).</html>");
	}
	
	private static String getDescription()
	{
		return EAM.text("<html><strong>Irreversibility (Permanence) - </strong>The degree to which the effects of a " +
						"threat can be reversed and the target affected by the threat restored.</html>");
	}
}
