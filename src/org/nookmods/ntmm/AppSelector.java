package org.nookmods.ntmm;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.preference.ListPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.widget.ListAdapter;

public class AppSelector extends ListPreference
{	
	private ButtonPrefs mContext;

	public AppSelector(Context context, AttributeSet attrs) {
        super(context, attrs);
    	mContext = (ButtonPrefs) context;
    }

	
    @Override
    protected void onPrepareDialogBuilder(Builder builder) {

        ArrayList<Command> list = new ArrayList<Command>();

        /* Get list of built-in actions */
        for (Action action : mContext.getActions() )
        	list.add( new Command( action ) );
        
        /* Get list of runnable packages */
    	ArrayList<Command> packages = new ArrayList<Command>();
        PackageManager pm = mContext.getPackageManager();

        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);
        for(ResolveInfo info : resolveInfos) {
            ApplicationInfo applicationInfo = info.activityInfo.applicationInfo;
            packages.add( new Command(applicationInfo.loadLabel(pm).toString(), applicationInfo.packageName ) );
        }
        java.util.Collections.sort(packages /*, String.CASE_INSENSITIVE_ORDER*/ );

		list.addAll(packages);
        
		List<String> values = new ArrayList<String>();
		for (Command item : list)
		    values.add(item != null ? item.asValue() : null);
        ListAdapter adapter = (ListAdapter) new AppAdapter(mContext, this, values, pm);
        builder.setAdapter(adapter, this);
        super.onPrepareDialogBuilder(builder);
    }

	public void setResult(String value)
	{
		if(this.callChangeListener(value))
		{
			Editor edit = PreferenceManager.getDefaultSharedPreferences(mContext).edit();
			edit.putString(this.getKey(), value);
			edit.commit();
		}
		this.getDialog().dismiss();
	}
}