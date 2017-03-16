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

public class ConsolePanel extends JPanel implements KeyListener
{
  private final JTextArea console;
  private int startCommandPosition;
  
  private Supplier<String> prompt = () -> "> ";
  private Consumer<String> parser = s -> {};

  public ConsolePanel(int rows, int cols)
  {
    console = new JTextArea(rows, cols);
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
}
