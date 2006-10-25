/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.summary;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JPanel;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.InvalidDateException;
import org.conservationmeasures.eam.utils.InvalidNumberException;
import org.martus.swing.UiTextField;

import com.jhlabs.awt.BasicGridLayout;

public class MetadataEditingPanel extends JPanel
{
	public MetadataEditingPanel(MainWindow mainWindowToUse)
	{
		super(new BasicGridLayout(0, 2));
		mainWindow = mainWindowToUse;
	}
	
	Project getProject()
	{
		return mainWindow.getProject();
	}
	
	protected UiTextField createFieldComponent(String tag, int length)
	{
		UiTextField fieldComponent = new UiTextField(length);
		fieldComponent.setText(getProject().getMetadata().getData(tag));
		fieldComponent.addFocusListener(new FocusHandler(tag, fieldComponent));
		return fieldComponent;
	}
	
	void save(String tag, UiTextField field)
	{
		String newValue = field.getText();
		String existing = getProject().getMetadata().getData(tag);
		try
		{
			if(!existing.equals(newValue))
			{
				getProject().setMetadata(tag, newValue);
			}
		}
		catch (InvalidDateException e)
		{
			EAM.errorDialog(EAM.text("Text|Dates must be in YYYY-MM-DD format"));
			field.setText(existing);
			field.requestFocus();
		}
		catch (InvalidNumberException e)
		{
			EAM.errorDialog(EAM.text("Text|Must be numeric"));
			field.setText(existing);
			field.requestFocus();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			EAM.errorDialog(EAM.text("Text|Error prevented saving"));
		}
	}

	public class FocusHandler implements FocusListener
	{
		public FocusHandler(String tagToUse, UiTextField componentToUse)
		{
			tag = tagToUse;
			component = componentToUse;
		}
		
		public void focusGained(FocusEvent event)
		{
		}

		public void focusLost(FocusEvent event)
		{
			save(tag, component);
			component.setText(getProject().getMetadata().getData(tag));
		}
		
		String tag;
		UiTextField component;
	}
	
	MainWindow mainWindow;
}
