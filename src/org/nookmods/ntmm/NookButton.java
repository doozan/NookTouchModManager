package org.nookmods.ntmm;

import android.content.ContentResolver;
import android.provider.Settings;

public final class NookButton {
  public final String name;
  public final String prefName;

  public NookButton(String name, String prefName) {
    this.name = name;
    this.prefName = prefName;
  }

  public void setValue( ButtonPrefs context, String value )
  {
    ContentResolver resolver = context.getApplicationContext().getContentResolver();

    Command item = new Command(value);

    if (item.type == Command.PACKAGE)
    {
      Settings.System.putString(resolver, "mod."+this.name+".cmd",  "PACKAGE:" + item.command);
      Settings.System.putString(resolver, "mod."+this.name+".icon",  item.command);
      Settings.System.putString(resolver, "mod."+this.name+".label", item.label);
    }
    else if (item.type == Command.ACTION && item.command != null && item.command.length() > 0 )
    {
      Settings.System.putString(resolver, "mod."+this.name+".cmd",  item.command);
      Settings.System.putString(resolver, "mod."+this.name+".icon",  ""+item.iconId);
      Settings.System.putString(resolver, "mod."+this.name+".label", item.label);
    }
    else
    {
      Settings.System.putString(resolver, "mod."+this.name+".cmd",   null);
      Settings.System.putString(resolver, "mod."+this.name+".icon",  null);
      Settings.System.putString(resolver, "mod."+this.name+".label", null);
    }
  }
}

