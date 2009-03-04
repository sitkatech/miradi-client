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

import javax.swing.Icon;

import org.miradi.objects.Assignment;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ConceptualModelDiagram;
import org.miradi.objects.Factor;
import org.miradi.objects.Goal;
import org.miradi.objects.Indicator;
import org.miradi.objects.KeyEcologicalAttribute;
import org.miradi.objects.Measurement;
import org.miradi.objects.Objective;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.ResultsChainDiagram;
import org.miradi.objects.Task;
 
public class IconManager
{
	//FIXME not all Icons are AbstractMiradiIcon.  But all icons should be.
	public static Icon getImage(BaseObject baseObject)
	{
		int type = baseObject.getType();

		if (Task.is(type))
			return getTaskIcon((Task) baseObject);
		
		if (Factor.isFactor(type))
			return getFactorIcon((Factor) baseObject);
		
		return getImage(type);
	}

	public static Icon getImage(int type)
	{
		if (ProjectMetadata.is(type))
			return getMiradiApplicationIcon();
		
		if (Indicator.is(type))
			return getIndicatorIcon();
		
		if (Measurement.is(type))
			return getMeasurementIcon();
		
		if (Goal.is(type))
			return getGoalIcon();
		
		if (Objective.is(type))
			return getObjectiveIcon();
		
		if (KeyEcologicalAttribute.is(type))
			return getKeyEcologicalAttributeIcon();

		if (ConceptualModelDiagram.is(type))
			return getConceptualModelIcon();
		
		if (ResultsChainDiagram.is(type))
			return getResultsChainIcon();
		
		if (Assignment.is(type))
			return new AssignmentIcon();
		
		throw new RuntimeException("Could not find icon for type:" + type);
	}

	private static AbstractMiradiIcon getFactorIcon(Factor factor)
	{
		if (factor.isDirectThreat())
			return getThreatIcon();
		
		if (factor.isContributingFactor())
			return new ContributingFactorIcon();
		
		if (factor.isStrategy())
			return new StrategyIcon();
		
		if (factor.isTarget())
			return new TargetIcon();
		
		if (factor.isStress())
			return new StressIcon();
		
		if (factor.isActivity())
			return new ActivityIcon();
		
		if (factor.isIntermediateResult())
			return new IntermediateResultIcon();
		
		if (factor.isThreatReductionResult())
			return new ThreatReductionResultIcon();
		
		if (factor.isTextBox())
			return new TextBoxIcon();
		
		if (factor.isGroupBox())
			return new GroupBoxIcon();

		throw new RuntimeException("type is factor but there is no icon for it was found:"  + factor.getType());
	}

	private static AbstractMiradiIcon getTaskIcon(Task task)
	{
		if (task.isMethod())
			return getMethodIcon();
		
		if (task.isActivity())
			return getActivityIcon();
		
		return getTaskIcon();
	}
	
	public static KeyEcologicalAttributeIcon getKeyEcologicalAttributeIcon()
	{
		return new KeyEcologicalAttributeIcon();
	}
	
	public static MeasurementIcon getMeasurementIcon()
	{
		return new MeasurementIcon();
	}
	
	public static GoalIcon getGoalIcon()
	{
		return new GoalIcon();
	}
	
	public static ObjectiveIcon getObjectiveIcon()
	{
		return new ObjectiveIcon();
	}
	
	public static IndicatorIcon getIndicatorIcon()
	{
		return new IndicatorIcon();
	}
	
	public static MiradiApplicationIcon getMiradiApplicationIcon()
	{
		return new MiradiApplicationIcon();
	}
	
	public static TargetIcon getTargetIcon()
	{
		return new TargetIcon();
	}

	public static StrategyIcon getStrategyIcon()
	{
		return new StrategyIcon();
	}
	
	public static DraftStrategyIcon getDraftStrategyIcon()
	{
		return new DraftStrategyIcon();
	}

	public static TaskIcon getTaskIcon()
	{
		return new TaskIcon();
	}

	public static MethodIcon getMethodIcon()
	{
		return new MethodIcon();
	}

	public static ActivityIcon getActivityIcon()
	{
		return new ActivityIcon();
	}
	
	public static ConceptualModelIcon getConceptualModelIcon()
	{
		return new ConceptualModelIcon();
	}
	
	public static ResultsChainIcon getResultsChainIcon()
	{
		return new ResultsChainIcon();
	}

	public static Icon getAllocatedCostIcon()
	{
		return new AllocatedCostIcon();
	}
	
	public static Icon getPlanningIcon()
	{
		return new PlanningIcon();
	}

	public static Icon getReportIcon()
	{
		return new ReportTemplateIcon();
	}

	public static AbstractMiradiIcon getThreatIcon()
	{
		return new DirectThreatIcon();
	}
}
