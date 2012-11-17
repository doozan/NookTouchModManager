package org.nookmods.ntmm;

import org.nookmods.ntmm.R;

import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.provider.Settings;
import android.util.Log;

public class ModPrefs extends PreferenceActivity implements OnSharedPreferenceChangeListener {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ContentResolver resolver = getApplicationContext().getContentResolver();
    Boolean bHasGlowlight = ( Integer.parseInt(Settings.System.getString(resolver, "has_glowlight")) > 0);

    if (bHasGlowlight)
      addPreferencesFromResource(R.xml.mods_glowlight);
    else
      addPreferencesFromResource(R.xml.mods);
    
    getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
  }

  @Override
  protected void onResume() {
    super.onResume();
    getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
  }

  @Override
  protected void onPause() {
    super.onPause();
    getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
  }

  public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    //Log.v("NTM", "Pref " + key + " changed");

    ContentResolver resolver = getApplicationContext().getContentResolver();
    String value = sharedPreferences.getBoolean(key, false) ? "1" : "0";
    Settings.System.putString(resolver, "mod.option." + key, value);
  }

}