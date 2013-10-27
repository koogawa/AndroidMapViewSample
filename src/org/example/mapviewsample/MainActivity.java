package org.example.mapviewsample;

import java.util.List;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

import android.R.integer;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.text.NoCopySpan.Concrete;

public class MainActivity extends MapActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		MapView mapView = (MapView)findViewById(R.id.MapView01);
		
		mapView.setClickable(true);
		
		mapView.setBuiltInZoomControls(true);
		
		ConcreteOverlay overlay = new ConcreteOverlay();
		
		List<Overlay> overlayList = mapView.getOverlays();
		overlayList.add(overlay);
	}

	protected boolean isRouteDisplayed()
	{
		return false;
	}
	
	private class ConcreteOverlay extends Overlay
	{
		// 円の半径
		private static final int CIRCLE_RADIUS = 16;
		
		// タップされた位置
		GeoPoint mGeoPoint;
		
		// 色情報
		Paint mCirclePaint;
		
		public ConcreteOverlay() {
			// TODO Auto-generated constructor stub
			mGeoPoint = null;
			mCirclePaint = new Paint();
			mCirclePaint.setStyle(Paint.Style.FILL);
			mCirclePaint.setARGB(255, 255, 0, 0);
		}
		
		public boolean onTap(GeoPoint point, MapView mapView)
		{
			mGeoPoint = point;
			
			return super.onTap(point, mapView);
		}
		
		public void draw(Canvas canvas, MapView mapView, boolean shadow)
		{
			super.draw(canvas, mapView, shadow);
			
			if (!shadow)
			{
				if (mGeoPoint != null)
				{
					// 緯度経度から座標へ変換
					Projection projection = mapView.getProjection();
					Point point = new Point();
					projection.toPixels(mGeoPoint, point);
					
					// 円を描く
					canvas.drawCircle(point.x, point.y, CIRCLE_RADIUS, mCirclePaint);
				}
			}
		}
	}
}
