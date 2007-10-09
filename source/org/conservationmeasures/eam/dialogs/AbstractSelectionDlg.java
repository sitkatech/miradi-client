/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.conservationmeasures.eam.dialogs.fieldComponents.PanelButton;
import org.conservationmeasures.eam.dialogs.fieldComponents.PanelTitleLabel;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.utils.FastScrollPane;
import org.martus.swing.Utilities;

abstract public class AbstractSelectionDlg extends EAMDialog implements ListSelectionListener
{
	public AbstractSelectionDlg(MainWindow mainWindow, String title, ObjectTablePanel poolTable)
	{
		super(mainWindow);
		list = poolTable;
		list.getTable().addListSelectionListener(this);
		Box box = Box.createVerticalBox();
		box.add(getPanelTitleInstructions());
		box.add(list, BorderLayout.AFTER_LAST_LINE);
		
		setTitle(title);
		JComponent buttonBar = createButtonBar();
		Container contents = getContentPane();
		contents.setLayout(new BorderLayout());
		contents.add(new FastScrollPane(box), BorderLayout.CENTER);
		contents.add(buttonBar, BorderLayout.AFTER_LAST_LINE);
		setModal(true);
		setPreferredSize(new Dimension(900,400));
		Utilities.centerDlg(this);
	}
	
	public BaseObject getSelectedAnnotaton()
	{
		return objectSelected;
	}
	
	public void dispose()
	{
		super.dispose();
		list.dispose();
	}

	private Box createButtonBar()
	{
		PanelButton cancelButton = new PanelButton(new CancelAction());
		customButton = createCustomButton();
		customButton.setEnabled(false);
		getRootPane().setDefaultButton(cancelButton);
		Box buttonBar = Box.createHorizontalBox();
		Component[] components = new Component[] {Box.createHorizontalGlue(), customButton,  Box.createHorizontalStrut(10), cancelButton};
		Utilities.addComponentsRespectingOrientation(buttonBar, components);
		return buttonBar;
	}
	
	public void valueChanged(ListSelectionEvent e)
	{
		customButton.setEnabled(true);
	}
	
	class CancelAction extends AbstractAction
	{
		public CancelAction()
		{
			super(EAM.text("Cancel"));
		}

		public void actionPerformed(ActionEvent arg0)
		{
			dispose();
		}
	}
	
	abstract protected PanelButton createCustomButton();
	
	abstract protected PanelTitleLabel getPanelTitleInstructions();
	
	private PanelButton customButton;
	protected ObjectTablePanel list;
	protected BaseObject objectSelected;
}

