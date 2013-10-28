package org.example.mapviewsample;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends MapActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		MapView mapView = (MapView)findViewById(R.id.MapView01);
		
		mapView.setClickable(true);
		
		mapView.setBuiltInZoomControls(true);
		
		ConcreteOverlay overlay = new ConcreteOverlay(this);
		
		List<Overlay> overlayList = mapView.getOverlays();
		overlayList.add(overlay);
	}

	protected boolean isRouteDisplayed()
	{
		return false;
	}
	
	private class ConcreteOverlay extends Overlay implements OnClickListener
	{
		// 円の半径
		private static final int CIRCLE_RADIUS = 16;
		
		// タップされた位置
		GeoPoint mGeoPoint;
		
		// 色情報
		Paint mCirclePaint;
		
		// 緯度経度から住所に変換するオブジェクト
		Geocoder mGeocoder;
		
		public ConcreteOverlay(Context context) {
			// TODO Auto-generated constructor stub
			mGeoPoint = null;
			mCirclePaint = new Paint();
			mCirclePaint.setStyle(Paint.Style.FILL);
			mCirclePaint.setARGB(255, 255, 0, 0);
			
			mGeocoder = new Geocoder(context, Locale.JAPAN);
			
			Button button = (Button)findViewById(R.id.Button01);
			button.setOnClickListener(this);
		}
		
		// ボタンが押された
		public void onClick(View v)
		{
			switch (v.getId())
			{
			case R.id.Button01:
				EditText editText = (EditText)findViewById(R.id.EditText01);
				String text = editText.getText().toString();
				
				try {
					List<Address> addressList = mGeocoder.getFromLocationName(text, 1);
					if (addressList.size() > 0) {
						Address address = addressList.get(0);
						
						setTapPoint(new GeoPoint((int)(address.getLatitude()*1E6), (int)(address.getLongitude()*1e6)));
						
						MapView mapView = (MapView)findViewById(R.id.MapView01);
						mapView.getController().setCenter(mGeoPoint);
						mapView.getController().setZoom(15);
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				break;

			default:
				break;
			}
		}

		public boolean onTap(GeoPoint point, MapView mapView)
		{
			setTapPoint(point);
			return super.onTap(point, mapView);
		}
		
		public void setTapPoint(GeoPoint point)
		{
			mGeoPoint = point;
			
			try {
				TextView textView = (TextView)findViewById(R.id.TextView01);
				
				// 市区町村まで取得できたか
				boolean success = false;
				
				// 緯度経度から住所
				List<Address> addressList = mGeocoder.getFromLocation(point.getLatitudeE6()/1E6, point.getLongitudeE6()/1E6, 5);
				
				for (Iterator<Address> it=addressList.iterator(); it.hasNext();)
				{
					Address address = it.next();
					
					// 国名
					String country = address.getCountryName();
					
					// 都道府県
					String admin = address.getAdminArea();
					
					// 市区町村
					String locality = address.getLocality();
					
					if (country != null && admin != null && locality != null)
					{
						textView.setText(country + admin + locality);
						success = true;
						break;
					}
				}
				
				// エラーならテキストビューに表示
				if (!success) {
					textView.setText("Error");
				}
				
				textView.invalidate();
			} catch (Exception e) {
				// TODO: handle exception
			}
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
