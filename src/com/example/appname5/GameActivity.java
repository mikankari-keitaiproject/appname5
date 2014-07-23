package com.example.appname5;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

public class GameActivity extends Activity implements SensorEventListener{
	
	MediaPlayer player;
	GameView view;
	SensorManager mSensorManager;
	Sensor mAccelerometerSensor;

	@Override
	// 開始された
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		
        view = new GameView(this);
        FrameLayout layout = (FrameLayout)findViewById(R.id.container);
        layout.addView(view);
        
        player = MediaPlayer.create(this, R.raw.bgm);
        player.setLooping(true);
        
        //センサーマネージャを獲得する
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);        
        // マネージャから加速度センサーオブジェクトを取得
        mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        String help = getResources().getString(R.string.help);
        Toast.makeText(this, help, Toast.LENGTH_SHORT).show();
	}

	
	// onCreateの後、または画面から復帰したとき
	protected void onStart(){
		super.onStart();
		
	}
	
	// onStartの後、または他のアクティビティから復帰したとき
    protected void onResume() {
        super.onResume();
    
		player.start();
        // 200msに一度SensorEventを観測するリスナを登録
        mSensorManager.registerListener(this, mAccelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
   }
    
    @Override
    // 他のアクティビティが前面に現れたとき
    protected void onPause() {
         super.onPause();
     
 		player.pause();
         // 非アクティブ時にSensorEventをとらないようにリスナの登録解除
         mSensorManager.unregisterListener(this);
    }
   
    
	// onPauseの後、または画面から消えたとき
	protected void onStop(){
		super.onStop();
	}

	// onStopの後
	protected void onDestroy(){
		super.onDestroy();
	}
	
	// タイムオーバーのとき
	public void end(int score){
		finish();
		Intent intent = new Intent(GameActivity.this, ResultActivity.class);
		intent.putExtra("score", score);
		startActivity(intent);
	}
    
	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO 自動生成されたメソッド・スタブ
        //To change body of implemented methods use File | Settings | File Templates.
        // 加速度センサの場合、以下の処理を実行
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            StringBuilder builder = new StringBuilder();
    
            // 数値の単位はm/s^2
            // X軸
            float x = event.values[0];
            // Y軸
            float y = event.values[1];
            // Z軸
            float z = event.values[2];
    
            builder.append("X : " + (x) + "\n");
            builder.append("Y : " + (y) + "\n");
            builder.append("Z : " + (z) + "\n");
            
            view.acc(x,y,z);
        }		
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO 自動生成されたメソッド・スタブ
        //To change body of implemented methods use File | Settings | File Templates.

	}

}
