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
	// �J�n���ꂽ
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		
        view = new GameView(this);
        FrameLayout layout = (FrameLayout)findViewById(R.id.container);
        layout.addView(view);
        
        player = MediaPlayer.create(this, R.raw.bgm);
        player.setLooping(true);
        
        //�Z���T�[�}�l�[�W�����l������
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);        
        // �}�l�[�W����������x�Z���T�[�I�u�W�F�N�g���擾
        mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        String help = getResources().getString(R.string.help);
        Toast.makeText(this, help, Toast.LENGTH_SHORT).show();
	}

	
	// onCreate�̌�A�܂��͉�ʂ��畜�A�����Ƃ�
	protected void onStart(){
		super.onStart();
		
	}
	
	// onStart�̌�A�܂��͑��̃A�N�e�B�r�e�B���畜�A�����Ƃ�
    protected void onResume() {
        super.onResume();
    
		player.start();
        // 200ms�Ɉ�xSensorEvent���ϑ����郊�X�i��o�^
        mSensorManager.registerListener(this, mAccelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
   }
    
    @Override
    // ���̃A�N�e�B�r�e�B���O�ʂɌ��ꂽ�Ƃ�
    protected void onPause() {
         super.onPause();
     
 		player.pause();
         // ��A�N�e�B�u����SensorEvent���Ƃ�Ȃ��悤�Ƀ��X�i�̓o�^����
         mSensorManager.unregisterListener(this);
    }
   
    
	// onPause�̌�A�܂��͉�ʂ���������Ƃ�
	protected void onStop(){
		super.onStop();
	}

	// onStop�̌�
	protected void onDestroy(){
		super.onDestroy();
	}
	
	// �^�C���I�[�o�[�̂Ƃ�
	public void end(int score){
		finish();
		Intent intent = new Intent(GameActivity.this, ResultActivity.class);
		intent.putExtra("score", score);
		startActivity(intent);
	}
    
	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
        //To change body of implemented methods use File | Settings | File Templates.
        // �����x�Z���T�̏ꍇ�A�ȉ��̏��������s
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            StringBuilder builder = new StringBuilder();
    
            // ���l�̒P�ʂ�m/s^2
            // X��
            float x = event.values[0];
            // Y��
            float y = event.values[1];
            // Z��
            float z = event.values[2];
    
            builder.append("X : " + (x) + "\n");
            builder.append("Y : " + (y) + "\n");
            builder.append("Z : " + (z) + "\n");
            
            view.acc(x,y,z);
        }		
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
        //To change body of implemented methods use File | Settings | File Templates.

	}

}
