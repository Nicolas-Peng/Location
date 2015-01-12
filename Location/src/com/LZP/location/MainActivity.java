package com.LZP.location;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.os.Build;

public class MainActivity extends ActionBarActivity {
	private TextView tv1;
	private Button bt1;
	private Button bt2;
	Double lat;
	Double lng;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		tv1 = (TextView) findViewById(R.id.textView1);
		bt1 = (Button) findViewById(R.id.button1);
		bt2 = (Button) findViewById(R.id.button2);
		final String key = "ZQzCHzkFnRr65yGoUO1vn3F1";
		final LocationManager locationManager = (LocationManager) getSystemService(this.LOCATION_SERVICE);
		final String provider = LocationManager.GPS_PROVIDER;
		
		LocationListener locationListener = new LocationListener() {

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onLocationChanged(Location location) {
				// TODO Auto-generated method stub
				if(location!=null){
				lat = location.getLatitude();
				Log.e("22222222", "2222222222");
				lng = location.getLongitude();
				tv1.setText(lat.toString() + "," + lng.toString());
				}
				else{
					tv1.setText("无法获取地理信息");
				}
			}
		};

		locationManager.requestLocationUpdates(provider, 1000, 0,
				locationListener);

		bt1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Location loc = locationManager.getLastKnownLocation(provider);
				
				
				lat = loc.getLatitude();
				
				lng = loc.getLongitude();
				tv1.setText("你的当前纬度是：\n" + "经度：" + lat.toString() + "\n"
						+ "纬度：" + lng.toString());

			}

			// locationManager.requestLocationUpdates(provider, 2000, 10,
			// locationListener);
		});

		bt2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new Thread(new GetPosition()).start();
			}
		});
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	// 子线程
	class GetPosition implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			String url = "http://maps.google.com/maps/api/geocode/json?latlng="
					+ lat + "," + lng + "&language=zh-CN&sensor=false";
			HttpClient client = new DefaultHttpClient();
			// 发送get请求
			HttpGet httpGet = new HttpGet(url);
			StringBuilder sb = new StringBuilder();
			try {
				// 发送请求
				HttpResponse response = client.execute(httpGet);
				HttpEntity entity = response.getEntity();
				InputStream stream = entity.getContent();

				int len = 0;// 表示成功读取的字节数的个数
				byte[] b = new byte[1024];
				// 表示从输入流当中最多将length个字节的数据读取到一个byte数据当中
				while (-1 != (len = stream.read(b, 0, b.length))) {
					sb.append(new String(b, 0, len));
				}
				JSONObject jsonObj = new JSONObject(sb.toString());
				final String result = jsonObj.getJSONArray("results")
						.getJSONObject(0).getString("formatted_address");
				tv1.post(new Runnable() {
					public void run() {
						tv1.setText("你的当前地址是：\n"+result);
					}
				});
				// 获取服务器响应的字符串
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

}
