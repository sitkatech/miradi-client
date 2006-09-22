package org.conservationmeasures.eam.views.strategicplan;

import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.project.Project;

public class GoalTableModel extends AnnotationTableModel
{
	GoalTableModel(Project projectToUse)
	{
		super(projectToUse.getGoalPool(), goalColumnTags);
		project = projectToUse;
	}

	public Object getValueAt(int rowIndex, int columnIndex)
	{
		
		return super.getValueAt(rowIndex, columnIndex);
	}

	static final String[] goalColumnTags = {
		Indicator.TAG_SHORT_LABEL, 
		Indicator.TAG_LABEL,
		};

	Project project;

}
