package com.pixbits.lib.lang;

import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class Reflection
{
  public static enum AccessModifier
  {
    PRIVATE,
    DEFAULT,
    PROTECTED,
    PUBLIC
    ;
    
    public boolean accessibleToSubclasses() { return this == PROTECTED || this == PUBLIC; }
  };
  
  public static AccessModifier accessModifier(Executable executable)
  {
    int modifiers = executable.getModifiers() & Modifier.methodModifiers();
    
    if ((modifiers & Modifier.PRIVATE) == Modifier.PRIVATE)
      return AccessModifier.PRIVATE;
    else if ((modifiers & Modifier.PROTECTED) == Modifier.PROTECTED)
      return AccessModifier.PROTECTED;
    else if ((modifiers & Modifier.PUBLIC) == Modifier.PUBLIC)
      return AccessModifier.PUBLIC;
    else if (modifiers == 0)
      return AccessModifier.DEFAULT;
    else
      throw new RuntimeException("Invalid modifiers for method "+executable.toGenericString());
  }
  
  public static void requireDefaultConstructible(Object object) { requireDefaultConstructible(object.getClass(), AccessModifier.PUBLIC); }
  public static void requireDefaultConstructible(Class<?> type, AccessModifier modifier)
  { 
    try
    {
      if ((type.getModifiers() & Modifier.ABSTRACT) != 0)
        throw new InstantiationException("Class "+type.getName()+" is abstract but it is required to be default constructible");
      
      Constructor<?> constructor = type.getDeclaredConstructor();
      
      if (accessModifier(constructor).ordinal() < modifier.ordinal())
        throw new InstantiationException("Class "+type.getName()+" is required to be default constructible ("+modifier+")");
    } 
    catch (Exception e)
    {
      throw new RuntimeException("Class "+type.getName()+" is required to be default constructible", e);
    }
  }
  
  public static void requireStaticFieldOfType(Class<?> target, Class<?> type, String name)
  {
    AccessModifier modifier = AccessModifier.PUBLIC;

    try
    {
      Field field = target.getField(name);
      
      if (field.getType() != type)
        throw new InstantiationException(String.format("Field of type %s required but found %s", type.getName(), field.getType().getName()));
    } 
    catch (Exception e)
    {
      throw new RuntimeException("Class "+target.getName()+" is required to have a "+ modifier + " static field named "+name+" of type "+type.getName(), e);
    }
  }
  
  public static Class<?> getGenericTypeForField(Field field)
  {
    Type type = field.getGenericType();
    ParameterizedType ptype = (ParameterizedType)type;

    return (Class<?>)ptype.getActualTypeArguments()[0];
  }
}
