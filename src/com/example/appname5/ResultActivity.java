package com.example.appname5;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ResultActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);

		
		Button button1 = (Button)findViewById(R.id.button1);
		button1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自動生成されたメソッド・スタブ
				finish();
			}
		});
		
		Intent intent = getIntent();
		int score = intent.getIntExtra("score", 0);
		TextView textview1 = (TextView)findViewById(R.id.textview1);
		textview1.setText(score + " のエネミーを倒した！");

	}
	
}
