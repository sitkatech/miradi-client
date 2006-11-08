package org.conservationmeasures.eam.views.strategicplan;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.umbrella.AnnotationTableModel;

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
		Goal.TAG_SHORT_LABEL, 
		Goal.TAG_LABEL,
		Goal.TAG_FULL_TEXT,
		};
	
	public String getTableCellDisplayString(int rowIndex, int columnIndex, BaseId indicatorId, String columnTag)
	{
		return super.getValueAt(rowIndex, columnIndex).toString();
	}
	
	Project project;

}
