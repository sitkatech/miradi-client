/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogfields;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.JTextComponent;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.ChoiceQuestion;

public class ViabilityRatingsTableField extends ObjectStringMapTableField implements DocumentListener
{

	public ViabilityRatingsTableField(Project projectToUse, int objectType, BaseId objectId, ChoiceQuestion questionToUse, ObjectDataInputField measurementSummary, ObjectDataInputField desiredSummary)
	{
		super(projectToUse, objectType, objectId, questionToUse);
		//FIXME: there should be a way to get the core component even thou the input data field had to wrap it to make it work for display perpuses.
		// Maybe a getComponent and a getNativeComponent ..or somthing.
		JTable c =(JTable)((JViewport)  ((JScrollPane)getComponent()).getComponent(0)).getView();
		model = (DefaultTableModel)c.getModel();
		model.insertRow(1, getRowStatus());
		question = questionToUse;
		measurement = measurementSummary;
		desired = desiredSummary;
		//FIXME: shoule we do this like this or should we do a command listner?
		((JTextComponent)measurement.getComponent()).getDocument().addDocumentListener(this);
		((JTextComponent)desired.getComponent()).getDocument().addDocumentListener(this);
	}


	public void setText(String dataString)
	{
		super.setText(dataString);
		setIconRow();
	}
	
	private void setIconRow()
	{
		//TODO: set iconic row display based on summary fields.
		Object[] data = getRowStatus();
		model.setValueAt(data[0], 1, 0);
		model.setValueAt(data[1], 1, 1);
		model.setValueAt(data[2], 1, 2);
		model.setValueAt(data[3], 1, 3);
	}
	
	private Object[] getRowStatus()
	{
		return new String[] {"aa","b","c","aa"};
	}
	
	public void changedUpdate(DocumentEvent arg0)
	{	
	}


	public void insertUpdate(DocumentEvent arg0)
	{
	}


	public void removeUpdate(DocumentEvent arg0)
	{
	}

	
	ChoiceQuestion question;
	ObjectDataInputField measurement;
	ObjectDataInputField desired;
	DefaultTableModel model;

}
