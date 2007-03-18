/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogfields;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.conservationmeasures.eam.icons.GoalIcon;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.ChoiceQuestion;

public class ViabilityRatingsTableField extends ObjectStringMapTableField
{

	public ViabilityRatingsTableField(Project projectToUse, int objectType, BaseId objectId, ChoiceQuestion questionToUse)
	{
		super(projectToUse, objectType, objectId, questionToUse);
		model = (DefaultTableModel)table.getModel();
		model.insertRow(1, new String[]{});
		question = questionToUse;
		table.setDefaultRenderer(Object.class, new TableCellRenderer());
	}

	private void setIconRow(ORef oref)
	{	EAMObject object = getProject().findObject(oref);
		if (object.getType() == ObjectType.GOAL)
		{
			detailSummary = object.getData(Goal.TAG_DESIRED_SUMMARY);
			String detailStatusCode = object.getData(Goal.TAG_DESIRED_STATUS);
			detailStatus = question.findChoiceByCode(detailStatusCode).getLabel();
		}
		else if (object.getType() == ObjectType.INDICATOR)
		{
			measurementSummary = object.getData(Indicator.TAG_MEASUREMENT_SUMMARY);
			String measurementStatusCode = object.getData(Indicator.TAG_MEASUREMENT_STATUS);
			measurementStatus = question.findChoiceByCode(measurementStatusCode).getLabel();
		}

		
		//TODO: checking to make sure fields come over correctly
		model.setValueAt(detailStatus, 1, 0);
		model.setValueAt(detailSummary, 1, 1);
		model.setValueAt(measurementSummary, 1, 2);
		model.setValueAt(measurementStatus, 1, 3);
	}
	

	public void setIconRowObject(ORef oref)
	{
		if (oref==null)
			clearIconRow();
		else
			setIconRow(oref);
	}
	
	private void clearIconRow()
	{
		model.setValueAt("", 1, 0);
		model.setValueAt("", 1, 1);
		model.setValueAt("", 1, 2);
		model.setValueAt("", 1, 3);
		measurementStatus = "";
		measurementSummary = "";
		detailStatus = "";
		detailSummary = "";
	}
	
	class TableCellRenderer extends DefaultTableCellRenderer
	{
		public Component getTableCellRendererComponent(JTable tableToUse, Object value,
				boolean isSelected, boolean hasFocus, int row, int column)
		{
			if (row==1) 
			{
				JLabel label = new JLabel();
				label.setIcon(new GoalIcon());
				label.setText((String)value);
				return label;
			}
			return this;
		}
	}
	
	String measurementStatus;
	String measurementSummary;
	String detailStatus;
	String detailSummary;
	ChoiceQuestion question;
	DefaultTableModel model;

}
