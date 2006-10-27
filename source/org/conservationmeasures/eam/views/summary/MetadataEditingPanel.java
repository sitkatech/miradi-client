/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.summary;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.ProjectMetadata;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.InvalidDateException;
import org.conservationmeasures.eam.utils.InvalidNumberException;
import org.martus.swing.UiTextField;

abstract public class MetadataEditingPanel extends FieldEditingPanel
{
	public MetadataEditingPanel(MainWindow mainWindowToUse)
	{
		mainWindow = mainWindowToUse;
	}
	
	Project getProject()
	{
		return mainWindow.getProject();
	}
	
	public void refreshField(String tag)
	{
		
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
		ProjectMetadata metadata = getProject().getMetadata();
		String existing = metadata.getData(tag);
		try
		{
			if(!existing.equals(newValue))
			{
				CommandSetObjectData cmd = new CommandSetObjectData(metadata.getType(), metadata.getId(), tag, newValue);
				getProject().executeCommand(cmd);
			}
		}
		catch(CommandFailedException outer)
		{
			try
			{
				throw(outer.getCause());
			}
			catch (InvalidDateException ide)
			{
				EAM.errorDialog(EAM.text("Text|Dates must be in YYYY-MM-DD format"));
				field.setText(existing);
				field.requestFocus();
			}
			catch (InvalidNumberException ine)
			{
				EAM.errorDialog(EAM.text("Text|Must be numeric"));
				field.setText(existing);
				field.requestFocus();
			}
			catch(Throwable inner)
			{
				inner.printStackTrace();
				EAM.errorDialog(EAM.text("Text|Error prevented saving"));
			}
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
