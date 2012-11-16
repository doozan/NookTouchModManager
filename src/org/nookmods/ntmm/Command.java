package org.nookmods.ntmm;

public final class Command implements Comparable<Command> {

  public final static Integer PACKAGE = 1;
  public final static Integer ACTION  = 2;

  public final String label;
  public final String command;
  public final int    type;
  public final int    iconId;

  public Command(String label, String packageName) {
    this.label = label;
    this.command = packageName;
    this.type = PACKAGE;
    this.iconId = 0;
  }

  public Command(Action action) {
    this.label = action.label;
    this.command = action.command;
    this.type = ACTION;
    this.iconId = action.iconId;
  }

  public Command(String serializedValue)
  {
    String[] params = serializedValue.split("::");
    label   = (params.length > 0 && params[0] != null && params[0].length() > 0) ? params[0] : "";
    command = (params.length > 1 && params[1] != null && params[1].length() > 0) ? params[1] : "";
    type    = (params.length > 2 && params[2] != null && params[2].length() > 0) ? Integer.parseInt(params[2]) : PACKAGE;
    iconId  = (params.length > 3 && params[3] != null && params[3].length() > 0) ? Integer.parseInt(params[3]) : 0;
  }

  public int compareTo(Command compare) {
    return String.CASE_INSENSITIVE_ORDER.compare(this.label, compare.label);
  }

  public String asValue()
  {
    return label + "::" + command + "::" + type + "::" + iconId;
  }
}
