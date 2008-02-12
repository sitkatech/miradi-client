/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.questions;

import org.miradi.objects.Cause;
import org.miradi.objects.FactorLink;
import org.miradi.objects.Goal;
import org.miradi.objects.Indicator;
import org.miradi.objects.IntermediateResult;
import org.miradi.objects.Objective;
import org.miradi.objects.Strategy;
import org.miradi.objects.Target;
import org.miradi.objects.TextBox;
import org.miradi.objects.ThreatReductionResult;
import org.miradi.views.diagram.DiagramLegendPanel;

public class DiagramLegendQuestion extends StaticChoiceQuestion
{
	public DiagramLegendQuestion()
	{
		super(getLegendChoices());
	}

	static ChoiceItem[] getLegendChoices()
	{
		return new ChoiceItem[] {
				new ChoiceItem(Strategy.OBJECT_NAME, Strategy.OBJECT_NAME),
				new ChoiceItem(Strategy.OBJECT_NAME_DRAFT, Strategy.OBJECT_NAME_DRAFT),
				new ChoiceItem(Cause.OBJECT_NAME_CONTRIBUTING_FACTOR, Cause.OBJECT_NAME_CONTRIBUTING_FACTOR),
				new ChoiceItem(Cause.OBJECT_NAME_THREAT, Cause.OBJECT_NAME_THREAT),
				new ChoiceItem(Target.OBJECT_NAME, Target.OBJECT_NAME),
				new ChoiceItem(FactorLink.OBJECT_NAME, FactorLink.OBJECT_NAME),
				new ChoiceItem(Goal.OBJECT_NAME, Goal.OBJECT_NAME),
				new ChoiceItem(Objective.OBJECT_NAME, Objective.OBJECT_NAME),
				new ChoiceItem(Indicator.OBJECT_NAME, Indicator.OBJECT_NAME),
				new ChoiceItem(TextBox.OBJECT_NAME, TextBox.OBJECT_NAME),
				new ChoiceItem(DiagramLegendPanel.SCOPE_BOX, DiagramLegendPanel.SCOPE_BOX),
				new ChoiceItem(FactorLink.OBJECT_NAME_TARGETLINK, FactorLink.OBJECT_NAME_TARGETLINK),
				new ChoiceItem(FactorLink.OBJECT_NAME_STRESS, FactorLink.OBJECT_NAME_STRESS),
				new ChoiceItem(IntermediateResult.OBJECT_NAME, IntermediateResult.OBJECT_NAME),
				new ChoiceItem(ThreatReductionResult.OBJECT_NAME, ThreatReductionResult.OBJECT_NAME),
		};
	}
}
