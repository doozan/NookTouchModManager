package org.nookmods.ntmm;

import java.util.List;

import org.nookmods.ntmm.R;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class AppAdapter extends ArrayAdapter<String> implements OnClickListener {
	private final ButtonPrefs mContext;
	private final List<String> mValues;
    private AppSelector mSelector;
    private PackageManager mPacMan;
 
	public AppAdapter(Context context, AppSelector selector, List<String> values, PackageManager pacman) {
		super(context, R.layout.list_actions, values);
		mContext = (ButtonPrefs) context;
		mSelector = selector;
		mValues = values;
		mPacMan = pacman;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if(v == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            v = inflater.inflate(R.layout.applist, null);
        }
        
        v.setId(position);
      	v.setOnClickListener(this);

      	TextView tvAppName = (TextView)v.findViewById(R.id.tvAppName);
        ImageView ivAppIcon = (ImageView)v.findViewById(R.id.ivIcon);

      	Command item = new Command( mValues.get(position) );
      	tvAppName.setText( item.label );
    	
        if ( item.type == Command.ACTION && item.iconId > 0 )
    		ivAppIcon.setImageDrawable( mContext.getResources().getDrawable( item.iconId ) );
        else if (item.type == Command.PACKAGE) {
    		ApplicationInfo entry = null;
    		try {
    			entry = mPacMan.getApplicationInfo( item.command, 0);
    		} catch (NameNotFoundException e1) {
    			ivAppIcon.setImageDrawable( null );
    		}
    		if (entry != null)
    			ivAppIcon.setImageDrawable(entry.loadIcon(mPacMan));
        }
        else
            ivAppIcon.setImageDrawable(null);

        return v;
	}

//	@Override
	public void onClick(View v)
	{
		int position = v.getId();
		if (position < 0) return;

		mSelector.setResult( mValues.get(position) );
	}
}