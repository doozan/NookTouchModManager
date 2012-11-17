package org.nookmods.ntmm;

import java.util.ArrayList;

import org.nookmods.ntmm.R;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;

public class ButtonPrefs extends PreferenceActivity implements OnSharedPreferenceChangeListener {

  public static final String TAG = "NTMM.ButtonPrefs";

  private static ArrayList<NookButton> buttons;
  private static ArrayList<Action> actions;

  private void initActions(boolean bHasGlowlight)
  {
    actions = new ArrayList<Action>();

    Resources sres =  Resources.getSystem();
    Resources res  =  getResources();

    actions.add( new Action("",
                res.getString (R.string.action_default ),
                0,
                ""));

    actions.add( new Action("back",
                res.getString( R.string.action_back ),
                sres.getIdentifier("btn_square_back_normal", "drawable", "android"),
                "KEY:" + KeyEvent.KEYCODE_BACK ) );

    actions.add( new Action("home",
                res.getString( R.string.action_home ),
                res.getIdentifier("btn_square_home_normal", "drawable", "android"),
                "ACTIVITY:android.intent.action.MAIN;CATEGORY:android.intent.category.HOME;FLAGS:" + Intent.FLAG_ACTIVITY_NEW_TASK ) );

    actions.add( new Action("menu",
                res.getString( R.string.action_menu ),
                res.getIdentifier("ic_menu_agenda", "drawable", "android"),
                "KEY:" + KeyEvent.KEYCODE_MENU) );

    actions.add( new Action("search",
                res.getString( R.string.action_search ),
                res.getIdentifier("ic_menu_search", "drawable", "android"),
                "KEY:" + KeyEvent.KEYCODE_SEARCH) );

    actions.add( new Action("search_long",
                res.getString( R.string.action_search_long ),
                res.getIdentifier("ic_menu_search", "drawable", "android"),
                "BROADCAST:android.intent.action.SEARCH_LONG_PRESS") );

    actions.add( new Action("quicknav",
                res.getString( R.string.action_quicknav ),
                res.getIdentifier("c_quicknav_home_normal_sm", "drawable", "android"),
                "QUICKNAV") );

    if ( ! bHasGlowlight ) return;

    actions.add( new Action("glowlight",
                res.getString( R.string.action_glowlight ),
                res.getIdentifier("setting_light_on", "drawable", "android"),
                "GLOWLIGHT") );

    actions.add( new Action("glowlight_control",
                res.getString( R.string.action_glowlight_control ),
                res.getIdentifier("setting_light_on", "drawable", "android"),
                "ACTIVITY:com.android.server.status.GlowLight;FLAGS:" + Intent.FLAG_ACTIVITY_NEW_TASK ) );
  }

  private void initButtons() {
    buttons = new ArrayList<NookButton>();
    buttons.add( new NookButton("key.home",             "pref_n_press") );
    buttons.add( new NookButton("key.home_long",        "pref_n_long") );
    buttons.add( new NookButton("quicknav.1",           "pref_quicknav_1") );
    buttons.add( new NookButton("quicknav.2",           "pref_quicknav_2") );
    buttons.add( new NookButton("quicknav.3",           "pref_quicknav_3") );
    buttons.add( new NookButton("quicknav.4",           "pref_quicknav_4") );
    buttons.add( new NookButton("quicknav.5",           "pref_quicknav_5") );
    buttons.add( new NookButton("quicknav.6",           "pref_quicknav_6") );
    buttons.add( new NookButton("statusbar.readnow",    "pref_statusbar_readnow") );
    buttons.add( new NookButton("statusbar.back",       "pref_statusbar_back") );
    buttons.add( new NookButton("statusbar.menu",       "pref_statusbar_menu") );
    buttons.add( new NookButton("statusbar.glowlight",  "pref_statusbar_glowlight") );
  }

  public NookButton getButton( String prefName )
  {
    for ( NookButton button : buttons)
      if ( button.prefName.equals(prefName) )
        return button;
    return null;
  }
  public Drawable getActionIconDrawable(String actionId)
  {
    Action action = getAction( actionId );
    return (action.iconId > 0) ?  getResources().getDrawable( action.iconId ) : null;
  }
  public Action getAction( String actionId )
  {
    for ( Action action : actions)
      if ( action.id.equals(actionId) )
        return action;
    return null;
  }
  public ArrayList<Action> getActions()
  {
    return actions;
  }

  public ArrayList<NookButton> getButtons()
  {
    return buttons;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ContentResolver resolver = getApplicationContext().getContentResolver();
    Boolean bHasGlowlight = ( Integer.parseInt(Settings.System.getString(resolver, "has_glowlight")) > 0);

    if (bHasGlowlight)
      addPreferencesFromResource(R.xml.buttons_glowlight);
    else
      addPreferencesFromResource(R.xml.buttons);

    initActions(bHasGlowlight);
    initButtons();

    getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
  }

  @Override
  protected void onResume() {
    super.onResume();
    getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

    for (String key : getPreferenceScreen().getSharedPreferences().getAll().keySet() )
      setSummary( (ListPreference) getPreferenceScreen().findPreference( key ) );
  }

  @Override
  protected void onPause() {
    super.onPause();
    getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
  }

  public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    //Log.v(TAG, "Pref changed " + key);
    String value = sharedPreferences.getString(key, "");

    setSummary( (ListPreference)getPreferenceScreen().findPreference( key ), value );

    NookButton button = getButton( key );
    if ( button != null)
      button.setValue( this, value );
  }

  private void setSummary(ListPreference pref, String value)
  {
    if (pref == null) return;
    if (value == null || value.length() <= 0) return;

    Command item = new Command(value);
    pref.setSummary( item.label );
  }

  private void setSummary(ListPreference pref)
  {
    if (pref == null) {
      Log.v("NTM", "Pref is null");
      return;
    }
    setSummary(pref, pref.getValue());
  }

  public String parseValue(String id, String prefix)
  {
    if (id.startsWith(prefix)) {
      int start = id.indexOf(prefix) + prefix.length();
      int end = id.indexOf(";", start);
      if (end>0)
        return id.substring(start, end);
      else
        return id.substring(start);
    }
    return null;
  }

}