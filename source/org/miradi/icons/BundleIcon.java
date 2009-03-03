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

package org.miradi.icons;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ThreatTargetVirtualLink;
import org.miradi.objects.FactorLink;
import org.miradi.project.Project;
import org.miradi.project.threatrating.SimpleThreatRatingFramework;
import org.miradi.project.threatrating.ThreatRatingBundle;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ThreatRatingQuestion;

public class BundleIcon extends AbstractMiradiIcon
{
	public BundleIcon(AppPreferences preferencesToUse)
	{
		preferences = preferencesToUse;
	}
	
	public void setLink(FactorLink linkToUse)
	{
		link = linkToUse;
		threatTargetVirualLink = new ThreatTargetVirtualLink(getProject());
	}

	public void paintIcon(Component c, Graphics g, int x, int y)
	{
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		int summaryX = 0;
		if(preferences.getIsCellRatingsVisible())
			summaryX = getSummaryX();
		
		ThreatRatingQuestion question = getThreatRatingQuestion();
		try
		{
			int value = threatTargetVirualLink.calculateThreatRatingBundleValue(link.getUpstreamThreatRef(), link.getDownstreamTargetRef());
			g.setColor(question.findChoiceByNumericValue(value).getColor());
			g.fillRect(summaryX, 0, getIconWidth() - summaryX, getIconHeight());
			
			if(preferences.getIsCellRatingsVisible())
				drawIndividualRatings(g);
		}
		catch(Exception e)
		{
			EAM.logException(e);
		}
	}

	private ThreatRatingQuestion getThreatRatingQuestion()
	{
		return (ThreatRatingQuestion)getProject().getQuestion(ThreatRatingQuestion.class);
	}

	private void drawIndividualRatings(Graphics g) throws Exception
	{
		ThreatRatingQuestion question = getThreatRatingQuestion();
		ThreatRatingBundle bundle = getBundle();
		ChoiceItem scope = question.findChoiceByNumericValue(getFramework().getScopeNumericValue(bundle));
		ChoiceItem severity = question.findChoiceByNumericValue(getFramework().getSeverityNumericValue(bundle));
		ChoiceItem irreversibility = question.findChoiceByNumericValue(getFramework().getIrreversibilityNumericValue(bundle));

		drawCriterionRectangle(g, 0, scope);
		drawCriterionRectangle(g, 1, severity);
		drawCriterionRectangle(g, 2, irreversibility);
	}

	private int getSummaryX()
	{
		return getIconWidth() / 3;
	}

	private void drawCriterionRectangle(Graphics g, int index, ChoiceItem choice)
	{
		int height = getIconHeight() / 3;
		int top = height * index;
		g.setColor(choice.getColor());
		g.fillRect(0, top, getSummaryX(), height);
		g.setColor(Color.BLACK);
		g.drawRect(0, top, getSummaryX(), height);
	}

	private ThreatRatingBundle getBundle() throws Exception
	{
		ORef threatRef = link.getUpstreamThreatRef();
		ORef targetRef = link.getDownstreamTargetRef();
		return getFramework().getBundle(threatRef, targetRef);
	}

	private SimpleThreatRatingFramework getFramework()
	{
		return getProject().getSimpleThreatRatingFramework();
	}
	
	private Project getProject()
	{
		return link.getProject();
	}

	private FactorLink link;
	private AppPreferences preferences;
	private ThreatTargetVirtualLink threatTargetVirualLink;
}
