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
import org.miradi.objecthelpers.ThreatTargetVirtualLinkHelper;
import org.miradi.objects.Cause;
import org.miradi.objects.Target;
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
	
	public void setThreatTarget(Cause threatToUse, Target targetToUse)
	{
		threat = threatToUse;
		target = targetToUse;
		threatTargetVirualLink = new ThreatTargetVirtualLinkHelper(getProject());
	}
	
	public void paintIcon(Component c, Graphics g, int x, int y)
	{
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		try
		{
			paintSummaryColoredSection(g);
			if(preferences.areCellRatingsVisible())
				drawIndividualRatings(g);
		}
		catch(Exception e)
		{
			EAM.logException(e);
		}
	}

	private void paintSummaryColoredSection(Graphics g) throws Exception
	{
		int value = threatTargetVirualLink.calculateThreatRatingBundleValue(getThreatRef(), getTargetRef());
		ThreatRatingQuestion question = getThreatRatingQuestion();
		g.setColor(question.findChoiceByNumericValue(value).getColor());
		g.fillRect(getSummaryX(), 0, getSummaryWidth(), getSummaryHeight());
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

	private void drawCriterionRectangle(Graphics g, int criterionIndex, ChoiceItem choice)
	{
		int criterionHeight = getIconHeight() / 3;
		int top = criterionHeight * criterionIndex;
		g.setColor(choice.getColor());
		g.fillRect(0, top, getCriterionWidth(), criterionHeight);
		
		g.setColor(Color.BLACK);
		g.drawRect(0, top, getCriterionWidth(), criterionHeight);
	}
	
	public void setRowHeight(int rowHeightToUse)
	{
		rowHeight = rowHeightToUse;
	}
	
	@Override
	public int getIconHeight()
	{
		return rowHeight;
	}
	
	private int getSummaryHeight()
	{
		return getIconHeight();
	}
	
	private int getSummaryWidth()
	{
		return getCriterionWidth();
	}
	
	private int getCriterionWidth()
	{
		return 10;
	}
	
	private int getSummaryX()
	{
		if(preferences.areCellRatingsVisible())
			return getCriterionWidth();
		
		return 0;
	}

	private ThreatRatingBundle getBundle() throws Exception
	{
		ORef threatRef = getThreatRef();
		ORef targetRef = getTargetRef();
		return getFramework().getBundle(threatRef, targetRef);
	}

	private ORef getTargetRef() throws Exception
	{
		return target.getRef();
	}

	private ORef getThreatRef() throws Exception
	{
		return threat.getRef();
	}

	private SimpleThreatRatingFramework getFramework()
	{
		return getProject().getSimpleThreatRatingFramework();
	}
	
	private Project getProject()
	{
		return threat.getProject();
	}

	private Cause threat;
	private Target target;
	private AppPreferences preferences;
	private ThreatTargetVirtualLinkHelper threatTargetVirualLink;
	private int rowHeight;
}
