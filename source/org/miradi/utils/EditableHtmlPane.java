/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 

package org.miradi.utils;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.StringReader;

import javax.swing.JEditorPane;
import javax.swing.JPopupMenu;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.undo.UndoManager;

import net.atlanticbb.tantlinger.ui.text.CompoundUndoManager;
import net.atlanticbb.tantlinger.ui.text.HTMLUtils;
import net.atlanticbb.tantlinger.ui.text.WysiwygHTMLEditorKit;
import net.atlanticbb.tantlinger.ui.text.actions.HTMLEditorActionFactory;
import net.atlanticbb.tantlinger.ui.text.actions.HTMLTextEditAction;

import org.bushe.swing.action.ActionList;
import org.bushe.swing.action.ActionUIFactory;
import org.miradi.main.MainWindow;

public class EditableHtmlPane extends MiradiTextPane
{
	private static final long serialVersionUID = 1L;
	private static final String INVALID_TAGS[] = {"html", "head", "body", "title"};

	private JEditorPane focusedEditor;
	private JPopupMenu wysPopupMenu;
	private ActionList actionList;

	private FocusListener focusHandler = new FocusHandler(); 
	private CaretListener caretHandler = new CaretHandler();
	private MouseListener popupHandler = new PopupHandler();

	public EditableHtmlPane(MainWindow mainWindow, int fixedApproximateColumnCount, int initialApproximateRowCount) throws Exception
	{
		super(mainWindow, fixedApproximateColumnCount, initialApproximateRowCount);
		
		createWysiwygEditor();
		createEditorActions();
	}

	private void createEditorActions()
	{        
		actionList = new ActionList("editor-actions");
		actionList.addAll(HTMLEditorActionFactory.createFontSizeActionList());        
		actionList.addAll(HTMLEditorActionFactory.createInlineActionList());
		actionList.addAll(HTMLEditorActionFactory.createBlockElementActionList());
		actionList.addAll(HTMLEditorActionFactory.createListElementActionList());
		actionList.addAll(HTMLEditorActionFactory.createAlignActionList());        
		actionList.addAll(HTMLEditorActionFactory.createInsertTableElementActionList());
		actionList.addAll(HTMLEditorActionFactory.createDeleteTableElementActionList());
		
		wysPopupMenu = ActionUIFactory.getInstance().createPopupMenu(actionList);
	}

	
	private void createWysiwygEditor()
	{
		setEditorKitForContentType("text/html", new WysiwygHTMLEditorKit());
		setContentType("text/html"); 
		insertHTML("", 0);        

		addCaretListener(caretHandler);
		addFocusListener(focusHandler);
		addMouseListener(popupHandler);


		HTMLDocument document = (HTMLDocument)getDocument();
		CompoundUndoManager cuh = new CompoundUndoManager(document, new UndoManager());
		document.addUndoableEditListener(cuh);
	}

	private void insertHTML(String html, int location) 
	{       
		try 
		{
			HTMLEditorKit kit = (HTMLEditorKit) getEditorKit();
			Document doc = getDocument();
			System.out.println("BEFORE=" + html);
			final String jEditorPaneizeHTML = HTMLUtils.jEditorPaneizeHTML(html);
			System.out.println("AFTERR=" + jEditorPaneizeHTML);
			StringReader reader = new StringReader(jEditorPaneizeHTML);
			kit.read(reader, doc, location);
		} 
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	@Override
	public void setText(String text)
	{
		//`System.out.println("setttingText=" +  text);
		String topText = removeInvalidTags(text);  
		super.setText("");
		insertHTML(topText, 0);            
		CompoundUndoManager.discardAllEdits(getDocument());
	}

	@Override
	public String getText()
	{
		return removeInvalidTags(super.getText());          
	}

	private String removeInvalidTags(String html)
	{
		for(int i = 0; i < INVALID_TAGS.length; i++)
		{
			html = deleteOccurance(html, '<' + INVALID_TAGS[i] + '>');
			html = deleteOccurance(html, "</" + INVALID_TAGS[i] + '>');
		}

		return html.trim();
	}

	private String deleteOccurance(String text, String word)
	{
		StringBuffer sb = new StringBuffer(text);       
		int p;
		while((p = sb.toString().toLowerCase().indexOf(word.toLowerCase())) != -1)
		{           
			sb.delete(p, p + word.length());            
		}
		return sb.toString();
	}

	private void updateState()
	{
		actionList.putContextValueForAll(HTMLTextEditAction.EDITOR, focusedEditor);
		actionList.updateEnabledForAll();
	}

	private class CaretHandler implements CaretListener
	{
		public void caretUpdate(CaretEvent e)
		{            
			updateState();
		}        
	}

	private class PopupHandler extends MouseAdapter
	{
		@Override
		public void mousePressed(MouseEvent e)
		{ 
			checkForPopupTrigger(e); 
		}

		@Override
		public void mouseReleased(MouseEvent e)
		{ 
			checkForPopupTrigger(e); 
		}

		private void checkForPopupTrigger(MouseEvent e)
		{
			if(e.isPopupTrigger())
			{                    
				wysPopupMenu.show(e.getComponent(), e.getX(), e.getY());
			}
		}
	}

	private class FocusHandler implements FocusListener
	{
		public void focusGained(FocusEvent e)
		{
			if(e.getComponent() instanceof JEditorPane)
			{
				JEditorPane ed = (JEditorPane)e.getComponent();
				CompoundUndoManager.updateUndo(ed.getDocument());
				focusedEditor = ed;

				updateState();
			}
		}

		public void focusLost(FocusEvent e)
		{
			if(e.getComponent() instanceof JEditorPane)
			{
				//focusedEditor = null;
				//wysiwygUpdated();
			}
		}
	}

}
