package com.pixbits.lib.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import javax.swing.ActionMap;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class ConsolePanel extends JPanel implements KeyListener, ConsoleInterface
{  
  public static interface Interface
  {
    public void setCaret(int position);
  }
  
  private final JTextArea console;
  private final AbstractDocument document;
  private final Filter filter;
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
    
    console.addKeyListener(this);
    
    filter = new Filter(this);
    document.setDocumentFilter(filter);
    
    filter.addDeleteRule(i -> i.offset < startCommandPosition);
    
    filter.addFullInsertRule(i -> {
      /* adjust caret to always be at end when text is going to be typed */
      i.area.setCaretPosition(i.area.getTextLength());
      return Optional.of(i.offset(i.area.getTextLength()));
    });
    
    filter.addInsertRule(i -> i.length > 0);
    //filter.addInsertRule(i -> i.text.equals("\t"));
  }
  
  public void addInsertRule(Function<Info, Optional<Info>> rule) { this.filter.addFullInsertRule(rule); }
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
        String command = getCurrentCommand();
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
  
  @Override public void setCaretPosition(int position) { console.setCaretPosition(position); }
  @Override public int getTextLength() { return console.getText().length(); }
  
  @Override public String getCurrentCommand()
  { 
    try
    {
      return console.getText(startCommandPosition, console.getText().length() - startCommandPosition);
    } catch (BadLocationException e)
    {
      e.printStackTrace();
      return "";
    }
  }
  
  @Override public void replaceCurrentCommand(String text)
  {
    console.replaceRange(text, startCommandPosition, console.getText().length());
  }

  public static class Info
  {
    public final ConsoleInterface area;
    public final String text;
    public final int offset;
    public final int length;
    public final AttributeSet attrs;
    
    public Info(ConsoleInterface area, String text, int offset, int length, AttributeSet attrs)
    {
      this.area = area;
      this.text = text;
      this.offset = offset;
      this.length = length;
      this.attrs = attrs;
    }
    
    public Info offset(int offset) { return new Info(area, text, offset, length, attrs); } 
  }
  
  private class Filter extends DocumentFilter
  {

    
    private final ConsoleInterface console;
    private final Set<Function<Info, Optional<Info>>> onDeleteRules;
    private final Set<Function<Info, Optional<Info>>> onInsertRules;
    
    public Filter(ConsoleInterface console)
    {
      this.console = console;
      onDeleteRules = new HashSet<>();
      onInsertRules = new HashSet<>();
    }
    
    public void addDeleteRule(Predicate<Info> rule) { addFullDeleteRule(i -> rule.test(i) ? Optional.empty() : Optional.of(i)); }
    public void addFullDeleteRule(Function<Info, Optional<Info>> rule) { onDeleteRules.add(rule); }
    
    public void addInsertRule(Predicate<Info> rule) { addFullInsertRule(i -> rule.test(i) ? Optional.empty() : Optional.of(i)); }
    public void addFullInsertRule(Function<Info, Optional<Info>> rule) { onInsertRules.add(rule); }
    
    @Override
    public void remove(FilterBypass fb, int offset, int length) throws BadLocationException
    {
      Optional<Info> info = Optional.of(new Info(console, null, offset, length, null));
      
      for (Function<Info, Optional<Info>> rule : onDeleteRules)
      {
        if (!info.isPresent())
          break;
        else
          info = rule.apply(info.get());
      }
 
      if (info.isPresent())
        super.remove(fb, info.get().offset, info.get().length);
    }
    
    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException
    {      
      Optional<Info> info = Optional.of(new Info(console, text, offset, length, attrs));
      
      for (Function<Info, Optional<Info>> rule : onInsertRules)
      {
        if (!info.isPresent())
          break;
        else
          info = rule.apply(info.get());
      }
 
      if (info.isPresent())
        super.replace(fb, info.get().offset, info.get().length, info.get().text, info.get().attrs);
    }
    
    @Override
    public void insertString(DocumentFilter.FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException
    {
      super.insertString(fb, offset, string, attr);
    }
  }
}
