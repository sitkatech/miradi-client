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
import org.conservationmeasures.eam.icons.IndicatorIcon;
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
			detailStatusCode = object.getData(Goal.TAG_DESIRED_STATUS);
		}
		else if (object.getType() == ObjectType.INDICATOR)
		{
			measurementSummary = object.getData(Indicator.TAG_MEASUREMENT_SUMMARY);
			measurementStatusCode = object.getData(Indicator.TAG_MEASUREMENT_STATUS);
		}

		
		//TODO: checking to make sure fields come over correctly
		model.setValueAt("", 1, 0);
		model.setValueAt("", 1, 1);
		model.setValueAt("", 1, 2);
		model.setValueAt("", 1, 3);
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
	}
	
	class TableCellRenderer extends DefaultTableCellRenderer
	{
		public Component getTableCellRendererComponent(JTable tableToUse, Object value,
				boolean isSelected, boolean hasFocus, int row, int column)
		{
			if (row!=1) 
				return this;

			if (validCode(detailStatusCode) && Integer.parseInt(detailStatusCode)-1 == column)
			{
				return new JLabel((String)value, new IndicatorIcon(), JLabel.LEFT);
			}
			else if (validCode(measurementStatusCode)  && Integer.parseInt(measurementStatusCode)-1 == column)
			{
				return new JLabel((String)value, new GoalIcon(), JLabel.LEFT);
			}
			return this;
		}
		
		private boolean validCode(String code)
		{
			try
			{
				Integer.parseInt(code);
			}
			catch (Exception e)
			{
				return false;
			}
			return true;
		}
	}
	
	String measurementStatusCode;
	String measurementSummary;
	String detailStatusCode;
	String detailSummary;
	ChoiceQuestion question;
	DefaultTableModel model;

}
