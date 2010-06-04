/*
 *  JOrtho
 *
 *  Copyright (C) 2005-2008 by i-net software
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License as 
 *  published by the Free Software Foundation; either version 2 of the
 *  License, or (at your option) any later version. 
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *  General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 *  USA.
 *  
 *  Created on 05.12.2007
 */

/* 
Portions (identified below) were modified by or written by the Miradi team, 
working for Foundations of Success, Bethesda, Maryland (on behalf of 
the Conservation Measures Partnership, "CMP") and Beneficent Technology, 
Inc. ("Benetech"), Palo Alto, California.

These portions may be released under the same terms as the original 
file, specifically either under the GNU GPL, or by i-net software 
under their commercial license.
*/ 

package com.inet.jortho;

import java.awt.Point;
import java.util.Locale;

import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.Utilities;

public class SpellCheckedWord
{
	private JTextComponent jText;

	private int wordStartOffset;

	private String word;

	private boolean isWordValid;

	private boolean needCapitalization;

	private Dictionary dictionary;

	public SpellCheckedWord(JTextComponent jTextComponent, SpellCheckerOptions options, Locale locale) throws BadLocationException
	{
		jText = jTextComponent;

		if(options == null)
			options = SpellChecker.getOptions();
		dictionary = SpellChecker.getCurrentDictionary();
		locale = SpellChecker.getCurrentLocale();

		wordStartOffset = getOffsetOfStartOfWordAtCursor();
		if(wordStartOffset < 0)
		{
			// there is nothing under the mouse pointer
			return;
		}

		Tokenizer tokenizer = new Tokenizer(jText, dictionary, locale, wordStartOffset, options);
		String invalidWord = findFirstInvalidWordFromOffset(tokenizer, wordStartOffset);
		word = extractWord();
		isWordValid = (!word.equals(invalidWord));
		needCapitalization = tokenizer.isFirstWordInSentence() && Utils.isFirstCapitalized( word );
	}

	public boolean hasWord()
	{
		return (word != null);
	}
	
	public boolean isWordValid()
	{
		return isWordValid;
	}
	
	public String getWord()
	{
		return word;
	}
	
	public JTextComponent getTextComponent()
	{
		return jText;
	}

	public int getWordStartOffset()
	{
		return wordStartOffset;
	}

	public boolean needCapitalization()
	{
		return needCapitalization;
	}

	private int getOffsetOfStartOfWordAtCursor() throws BadLocationException
	{
		Caret caret = jText.getCaret();
		int offs = Math.min(caret.getDot(), caret.getMark());
		Point p = jText.getMousePosition();
		if(p != null)
		{
			// use position from mouse click and not from editor cursor position
			offs = jText.viewToModel(p);
		}
		Document doc = jText.getDocument();
		if(offs > 0
				&& (offs >= doc.getLength() || Character.isWhitespace(doc
						.getText(offs, 1).charAt(0))))
		{
			// if the next character is a white space then use the word on the
			// left site
			offs--;
		}

		if(offs < 0)
			return offs;

		return Utilities.getWordStart(jText, offs);
	}

	private String findFirstInvalidWordFromOffset(Tokenizer tokenizer,
			int begOffs)
	{
		String invalidWord;
		do
		{
			invalidWord = tokenizer.nextInvalidWord();
		} while(tokenizer.getWordOffset() < begOffs);
		return invalidWord;
	}

	private String extractWord() throws BadLocationException
	{
		int wordEndOffset = Utilities.getWordEnd(jText, wordStartOffset);
		return jText.getText(wordStartOffset, wordEndOffset - wordStartOffset);
	}

}
