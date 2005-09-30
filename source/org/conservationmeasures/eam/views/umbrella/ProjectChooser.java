/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.umbrella;

import java.awt.Component;
import java.awt.Container;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FilenameFilter;

import javax.swing.Box;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.conservationmeasures.eam.main.EAM;
import org.martus.swing.UiButton;
import org.martus.swing.UiLabel;
import org.martus.swing.UiList;
import org.martus.swing.UiScrollPane;
import org.martus.swing.UiTextField;
import org.martus.swing.UiVBox;
import org.martus.swing.Utilities;

public class ProjectChooser extends JDialog implements ActionListener, ListSelectionListener
{
	public ProjectChooser(JFrame parent) throws HeadlessException
	{
		super(parent);
		setModal(true);
		setResizable(true);
		
		existingProjectList = createExistingProjectList();
		projectNameField = createTextArea();
		
		UiVBox bigBox = new UiVBox();
		bigBox.add(new UiLabel("Existing Projects:"));
		bigBox.add(new UiScrollPane(existingProjectList));
		bigBox.addSpace();
		bigBox.add(createProjectNameBar());
		bigBox.addSpace();
		bigBox.add(createButtonBar());

		Container contents = getContentPane();
		contents.add(bigBox);

		getRootPane().setDefaultButton(okButton);
	}
	
	public boolean showOpenDialog()
	{
		setTitle(EAM.text("Title|Open Existing Project")); 
		okButton.setText(EAM.text("Button|Open"));
		return showDialog();
	}
	
	public boolean showCreateDialog()
	{
		setTitle(EAM.text("Title|Create New Project")); 
		okButton.setText(EAM.text("Button|Create"));
		return showDialog();
	}
	
	private boolean showDialog()
	{
		pack();
		setVisible(true);
		return getResult();
	}
	
	public File getSelectedFile()
	{
		return new File(getHomeDirectory(), projectNameField.getText());
	}
	
	private UiList createExistingProjectList()
	{
		File home = getHomeDirectory();
		UiList list = new UiList(home.list(new DirectoryFilter()));
		list.addListSelectionListener(this);
		list.addMouseListener(new DoubleClickHandler(this));
		return list;
	}

	private File getHomeDirectory()
	{
		File home = new File(System.getProperty("user.home"));
		return home;
	}
	
	static class DoubleClickHandler extends MouseAdapter
	{
		public DoubleClickHandler(ProjectChooser dialogToControl)
		{
			dialog = dialogToControl;
		}
		
		public void mouseClicked(MouseEvent e) 
		{
			if (e.getClickCount() != 2)
				return;

			dialog.ok();
		}
		ProjectChooser dialog;
	}
	
	static class DirectoryFilter implements FilenameFilter
	{
		public boolean accept(File directory, String name)
		{
			if(name.startsWith("."))
				return false;
			
			File projectDirectory = new File(directory, name);
			if(!projectDirectory.isDirectory())
				return false;
			
			File script = new File(projectDirectory, name + ".script");
			return script.exists();
		}
	}
	
	private UiTextField createTextArea()
	{
		UiTextField textField = new UiTextField(40);
		textField.requestFocus(true);
		textField.selectAll();
		return textField;
	}
	
	private Box createProjectNameBar()
	{
		Box nameBar = Box.createHorizontalBox();
		nameBar.add(new UiLabel(EAM.text("Project Name: ")));
		nameBar.add(projectNameField);
		return nameBar;
	}

	private Box createButtonBar()
	{
		okButton = new UiButton("");
		okButton.addActionListener(this);
		getRootPane().setDefaultButton(okButton);
		cancelButton = new UiButton(EAM.text("Button|Cancel"));
		cancelButton.addActionListener(this);

		Box buttonBar = Box.createHorizontalBox();
		Component[] components = new Component[] {Box.createHorizontalGlue(), okButton, cancelButton};
		Utilities.addComponentsRespectingOrientation(buttonBar, components);
		return buttonBar;
	}

	public boolean getResult()
	{
		return result;
	}
	
	public void actionPerformed(ActionEvent event)
	{
		if(event.getSource() == okButton)
			ok();
		else
			cancel();
	}
	
	public void ok()
	{
		result = true;
		dispose();
	}
	
	public void cancel()
	{
		result = false;
		dispose();
	}
	
	public void valueChanged(ListSelectionEvent arg0)
	{
		projectNameField.setText((String)existingProjectList.getSelectedValue());
	}

	boolean result;
	UiList existingProjectList;
	UiTextField projectNameField;
	UiButton okButton;
	UiButton cancelButton;
}
