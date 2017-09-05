package com.pixbits.lib.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.swing.ActionMap;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.DocumentFilter.FilterBypass;

public class ConsolePanel extends JPanel implements KeyListener
{
  private final JTextArea console;
  private final AbstractDocument document;
  private int startCommandPosition;
  
  private Supplier<String> prompt = () -> "> ";
  private Consumer<String> parser = s -> {};

  public ConsolePanel(int rows, int cols)
  {
    console = new JTextArea(rows, cols);
    document = (AbstractDocument)console.getDocument();
    JScrollPane pane = new JScrollPane(console);
    pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    
    console.setFont(new Font("Monaco", Font.BOLD, 12));
    console.setBackground(Color.BLACK);
    console.setForeground(new Color(177,242,27));
    console.getCaret().setVisible(true);
    
    ActionMap actionMap = console.getActionMap();
    actionMap.get("caret-down").setEnabled(false);
    actionMap.get("caret-up").setEnabled(false);
    
    this.setLayout(new BorderLayout());
    this.add(pane, BorderLayout.CENTER);
    
    document.setDocumentFilter(new Filter());
    console.addKeyListener(this);
  }
  
  public void setPrompt(Supplier<String> prompt) { this.prompt = prompt; }
  public void setParser(Consumer<String> parser) { this.parser = parser; };
  
  public void appendPrompt()
  {
    /*if (console.getText().length() != 0 && console.getText().charAt(console.getText().length()-1) != '\n');
      console.append("\n");*/
    
    console.append(prompt.get());
    console.setCaretPosition(console.getText().length());
    startCommandPosition = console.getText().length();
  }

  public void keyPressed(KeyEvent k)
  {

  }
  
  public void keyReleased(KeyEvent k)
  {
    if (k.getKeyChar() == KeyEvent.VK_ENTER)
    {
      try
      {
        String command = console.getText(startCommandPosition, console.getText().length() - 1 - startCommandPosition);
        parser.accept(command);
      }
      /*catch (ParserException e)
      {
        //ParseErrorDetails details = e.getErrorDetails();
        syntaxError(e.getMessage());

      }*/
      catch (Exception e)
      {
        e.printStackTrace();
      }
      finally
      {
        appendPrompt();
      }
    }
    else if (k.getKeyChar() == KeyEvent.VK_TAB)
    {
      k.consume();
    }
    else if (k.getKeyChar() == KeyEvent.VK_UP)
    {
      
    }
  }
  
  public void keyTyped(KeyEvent k)
  {
    
  }
  
  public void append(String string, Object... args)
  {
    console.append(String.format(string, args));
  }
  
  public void appendln(String string, Object... args)
  {
    console.append(String.format(string+"\n", args));
  }
  
  private class Filter extends DocumentFilter
  {
    @Override
    public void remove(FilterBypass fb, int offset, int length) throws BadLocationException
    {
      if (offset < startCommandPosition) ;
      else
        super.remove(fb, offset, length);
    }
    
    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException
    {
      if (length > 0) ;
      else if (text.equals("\t")) ;
      else
        super.replace(fb, offset, length, text, attrs);
    }
    
    @Override
    public void insertString(DocumentFilter.FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException
    {
      super.insertString(fb, offset, string, attr);
    }
  }
}
