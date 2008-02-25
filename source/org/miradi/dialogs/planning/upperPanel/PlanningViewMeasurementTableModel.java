/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.planning.upperPanel;

import org.miradi.dialogs.planning.propertiesPanel.PlanningViewAbstractTreeTableSyncedTableModel;
import org.miradi.dialogs.treetables.TreeTableNode;
import org.miradi.main.EAM;
import org.miradi.objects.Measurement;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.StatusConfidenceQuestion;
import org.miradi.questions.TrendQuestion;

import com.java.sun.jtreetable.TreeTableModelAdapter;

public class PlanningViewMeasurementTableModel extends PlanningViewAbstractTreeTableSyncedTableModel
{
	public PlanningViewMeasurementTableModel(Project projectToUse, TreeTableModelAdapter adapterToUse) throws Exception
	{
		super(projectToUse, adapterToUse);
	}

	public int getColumnCount()
	{
		return columnTags.length;
	}
	
	@Override
	public String getColumnTag(int column)
	{
		return columnTags[column];
	}
	
	public String getColumnName(int column)
	{
		return EAM.fieldLabel(Measurement.getObjectType(), getColumnTag(column));
	}
	
	public Object getValueAt(int row, int column)
	{
		TreeTableNode node = getNodeForRow(row);
		if (node.getType() != Measurement.getObjectType())
			return "";
		
		String columnTag = getColumnTag(column);
		String data = node.getObject().getData(columnTag);
		
		ChoiceQuestion question = getColumnQuestion(column);
		if(question != null)
			return question.findChoiceByCode(data);
		
		return data;
	}
	
	public boolean isChoiceItemColumn(int column)
	{
		return (getColumnQuestion(column) != null);
	}
	
	public ChoiceQuestion getColumnQuestion(int column)
	{
		String columnTag = getColumnTag(column);
		if(columnTag.equals(Measurement.TAG_TREND))
			return new TrendQuestion();
		
		if(columnTag.equals(Measurement.TAG_STATUS_CONFIDENCE))
			return new StatusConfidenceQuestion();
		
		return null;
	}
	
	public final static String[] columnTags = {
		Measurement.TAG_DATE, 
		Measurement.TAG_SUMMARY, 
		Measurement.TAG_TREND, 
		Measurement.TAG_STATUS_CONFIDENCE
		};
}
