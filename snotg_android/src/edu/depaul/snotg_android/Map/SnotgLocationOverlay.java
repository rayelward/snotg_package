package edu.depaul.snotg_android.Map;

import android.content.Context;

import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

public class SnotgLocationOverlay extends MyLocationOverlay {
		
	private Context context;
	
	public SnotgLocationOverlay(Context context, MapView mapView) {
		super(context, mapView);
		this.context = context;
	}
	
	@Override
	protected boolean dispatchTap(){
		return true;
	}

}
