package org.nookmods.ntmm;

import org.nookmods.ntmm.R;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

  public static String  versionServerJar = null;
  public static String  versionPolicyJar = null;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Resources res  =  getResources();
    TextView t = (TextView) findViewById(R.id.text_jar_version);
    try {
      versionServerJar = (String) Class.forName("com.android.server.status.ModUtils").getMethod("getVersion").invoke(null);
      versionPolicyJar = (String) Class.forName("com.android.internal.policy.impl.ModUtils").getMethod("getVersion").invoke(null);
      t.setText( res.getString( R.string.detected_jars ) + ":\nserver.jar = mod-" + versionServerJar + "\nanrdoid.policy.jar = mod-" + versionPolicyJar);
    } catch (Exception e) {
      Log.v("NTM", "getVersion failed " + e.toString() + "\n" + e.getCause() );
      t.setText( res.getString( R.string.no_jars ) );
      findViewById(R.id.button_modify_buttons).setVisibility(View.GONE);
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.activity_main, menu);
    return true;
  }

  public void doModConfig(View view) {
    Intent intent = new Intent(this, ModPrefs.class);
    startActivity(intent);
  }

  public void doButtonConfig(View view) {
    Intent intent = new Intent(this, ButtonPrefs.class);
    startActivity(intent);
  }
}
