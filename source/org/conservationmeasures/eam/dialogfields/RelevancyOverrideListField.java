/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogfields;

import java.awt.Dimension;
import java.text.ParseException;

import javax.swing.JComponent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.RelevancyOverride;
import org.conservationmeasures.eam.objecthelpers.RelevancyOverrideSet;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.ChoiceQuestion;
import org.conservationmeasures.eam.utils.FastScrollPane;

public class RelevancyOverrideListField extends ObjectDataInputField implements ListSelectionListener
{
	public RelevancyOverrideListField(Project projectToUse, int objectTypeToUse, BaseId objectIdToUse, ChoiceQuestion questionToUse, String defaultListTagToUse, String tagToUse)
	{
		super(projectToUse, objectTypeToUse, objectIdToUse, tagToUse);
		
		tag = tagToUse;
		defaultListTag = defaultListTagToUse;
		refListEditor = new RefListComponent(questionToUse, 1, this);
		refListScroller = new FastScrollPane(refListEditor);
		Dimension preferredSize = refListScroller.getPreferredSize();
		final int ARBITRARY_REASONABLE_MAX_WIDTH = 800;
		final int ARBITRARY_REASONABLE_MAX_HEIGHT = 200;
		int width = Math.min(preferredSize.width, ARBITRARY_REASONABLE_MAX_WIDTH);
		int height = Math.min(preferredSize.height, ARBITRARY_REASONABLE_MAX_HEIGHT);
		refListScroller.getViewport().setPreferredSize(new Dimension(width, height));
	}
	
	public JComponent getComponent()
	{
		return refListScroller;
	}

	public String getText()
	{
		return refListEditor.getText();
	}

	public void setText(String codes)
	{
		try
		{
			ORefList relevantRefList = new ORefList(getProject().getObjectData(getORef(), defaultListTag));
			RelevancyOverrideSet relevantOverrides = new RelevancyOverrideSet(getProject().getObjectData(getORef(), tag));
			for(RelevancyOverride override : relevantOverrides)
			{
				if (override.isOverride())
					relevantRefList.add(override.getRef());
				else
					relevantRefList.remove(override.getRef());
			}
			
			refListEditor.setText(relevantRefList.toString());
		}
		catch(ParseException e)
		{
			//TODO do something else with this exception
			EAM.logException(e);
		}	
	}
	
	public void updateEditableState()
	{
		refListEditor.setEnabled(isValidObject());
	}
	
	public void valueChanged(ListSelectionEvent arg0)
	{
		setNeedsSave();
		saveIfNeeded();
	}
	
	private RefListComponent refListEditor;
	private FastScrollPane refListScroller;
	String tag;
	String defaultListTag;
}
