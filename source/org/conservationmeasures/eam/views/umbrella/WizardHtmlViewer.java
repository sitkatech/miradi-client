/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.umbrella;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.text.html.StyleSheet;

import org.conservationmeasures.eam.actions.ActionCopy;
import org.conservationmeasures.eam.actions.ActionCut;
import org.conservationmeasures.eam.actions.ActionDelete;
import org.conservationmeasures.eam.actions.ActionPaste;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.martus.swing.HtmlViewer;
import org.martus.swing.HyperlinkHandler;

public class WizardHtmlViewer extends HtmlViewer implements MouseListener
{
	public WizardHtmlViewer(HyperlinkHandler hyperLinkHandler)
	{
		super("", hyperLinkHandler);
		addMouseListener(this);
	}

	public void customizeStyleSheet(StyleSheet style)
	{
		super.customizeStyleSheet(style);
		for(int i = 0; i < rules.length; ++i)			
			style.addRule(makeSureRuleHasRightPrefix(rules[i]));
	}
	
	private String makeSureRuleHasRightPrefix(String rule)
	{
		if (cssDotPrefixWorksCorrectly())
			return rule;

		return replaceDotWithPoundSign(rule);
	}
	
	private String replaceDotWithPoundSign(String rule)
	{
		if (rule.trim().startsWith("."))
			return rule.trim().replaceFirst(".", "#");

		return rule;
	}

	private boolean cssDotPrefixWorksCorrectly()
	{
		String javaVersion = EAM.getJavaVersion();
		if (javaVersion.startsWith("1.4"))
			return false;
		return true;
	}
	
	
	public JPopupMenu getRightClickMenu(Actions actions)
	{
		JPopupMenu menu = new JPopupMenu();
		
		JMenuItem menuItemCopy = new JMenuItem(actions.get(ActionCopy.class));
		menuItemCopy.setEnabled(false);
		menu.add(menuItemCopy);
		
		JMenuItem menuItemCut = new JMenuItem(actions.get(ActionCut.class));
		menuItemCut.setEnabled(false);
		menu.add(menuItemCut);
		
		JMenuItem menuItemPaste = new JMenuItem(actions.get(ActionPaste.class));
		menuItemPaste.setEnabled(false);
		menu.add(menuItemPaste);
		
		JMenuItem menuItemDelete = new JMenuItem(actions.get(ActionDelete.class));
		menuItemDelete.setEnabled(false);
		menu.add(menuItemDelete);
		
		return menu;
	}
	
	public void mouseClicked(MouseEvent e)
	{
	}

	public void mouseEntered(MouseEvent e)
	{
	}

	public void mouseExited(MouseEvent e)
	{
	}

	public void mousePressed(MouseEvent e)
	{
		if(e.isPopupTrigger())
			fireRightClick(e);
	}

	public void mouseReleased(MouseEvent e)
	{
		if(e.isPopupTrigger())
			fireRightClick(e);
	}
	
	void fireRightClick(MouseEvent e)
	{
		try
		{
		Actions actions = new Actions(new MainWindow());
		getRightClickMenu(actions).show(this, e.getX(), e.getY());
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	/*
	 * NOTE! In Java 1.4 the CSS class reverses the meanings of #xxx and .xxx
	 * so if you want to affect a _class_ use #xxx 
	 * and if you want to affect an _id_ use .xxx
	 * GRRRR!
	 */
	final static String[] rules = {
		"body {font-family: sans-serif, arial; }",
		"code {font-family: sans-serif; }",
		"  .viewname { font-size: 125%;  }",
		"  .processsection { font-size: 120%; }",
		"  .taskheading { font-size: 110%; font-weight: bold; font-style: italics; }",
		"  .nextsteps { font-weight: 700; }",
		"  .navigation { background-color: #eeeeee; " +
			"border-width: 1; border-color: black; font-size: small; }",
		"  .hintheading { font-style: italics; font-weight: bold; }",
		"  .task {font-weight: bold; color: #0000ff; }",
		"  .toolbarbutton {color: #0000ff; }",
		"  .hint {font-style: italics; }",
	};
}
