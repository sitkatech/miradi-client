/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JPanel;

import org.conservationmeasures.eam.dialogs.fieldComponents.PanelFieldLabel;
import org.conservationmeasures.eam.layout.OneRowPanel;
import org.conservationmeasures.eam.layout.TwoColumnPanel;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.ProjectMetadata;
import org.conservationmeasures.eam.questions.ChoiceItem;
import org.conservationmeasures.eam.questions.FiscalYearStartQuestion;
import org.martus.swing.UiButton;
import org.martus.swing.UiComboBox;

public class EditFiscalYearDialog extends JDialog
{
	public EditFiscalYearDialog(String fiscalYearChoiceCode)
	{
		setTitle(EAM.text("Title|Edit Fiscal Year"));
		setModal(true);

		UiButton saveButton = new UiButton(EAM.text("Button|Save"));
		saveButton.addActionListener(new SaveHandler());
		UiButton cancelButton = new UiButton(EAM.getCancelButtonText());
		cancelButton.addActionListener(new CancelHandler());
		combo = new UiComboBox(new FiscalYearStartQuestion("").getChoices());

		JPanel mainPanel = new TwoColumnPanel();
		mainPanel.add(new PanelFieldLabel(ProjectMetadata.getObjectType(), ProjectMetadata.TAG_FISCAL_YEAR_START));
		mainPanel.add(combo);
		
		JPanel buttonBar = new OneRowPanel();
		buttonBar.add(saveButton);
		buttonBar.add(cancelButton);

		setLayout(new BorderLayout());
		add(mainPanel, BorderLayout.CENTER);
		add(buttonBar, BorderLayout.AFTER_LAST_LINE);
	}
	
	public boolean getResult()
	{
		return result;
	}
	
	public String getFiscalYearChoiceCode()
	{
		ChoiceItem selected = (ChoiceItem)combo.getSelectedItem();
		if(selected == null)
			return "";
		return selected.getCode();
	}
	
	class SaveHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			result = true;
			dispose();
		}
	}
	
	class CancelHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			dispose();
		}
	}
	
	private boolean result;
	private UiComboBox combo;
}
