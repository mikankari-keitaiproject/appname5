package com.example.appname5;

import java.io.IOException;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class GameView extends View {
    
	Context context = null;
    Bitmap background = null;
    Bitmap player = null;
    Bitmap player2 = null;
    Bitmap[] enemies = null;
    MediaPlayer[] enemies_se = null;

    int playerX = 0;
    int playerY = 0;
    boolean playerisAtack = false;
    int[] enemiesX = null;
    int[] enemiesY = null;
    int[] enemiesV = null;
    int screenWidth = 0;
    int screenHeight = 0;
    int playerWidth_half = 0;
    int playerHeight_half = 0;
    int enemyWidth_half = 0;
    int enemyHeight_half = 0;
    int score_counter = 0;
    int draw_counter = 0;
    boolean isdraw = false;
    final int enemies_count = 20;
    final int draw_count = 60 * 30;	// ïb * ï`âÊë“ã@éûä‘
    final boolean debugmode = true;

    public GameView(Context context) {
        super(context);
        this.context = context;

        initView(context);
    }
    
    private void initView(Context context){
    	
    	Resources resources = this.getContext().getResources();
        background = BitmapFactory.decodeResource(resources, R.drawable.background);
        player = BitmapFactory.decodeResource(resources, R.drawable.player);
//        player2 = BitmapFactory.decodeResource(resources, R.drawable.player2); 
        enemies = new Bitmap[enemies_count];
        enemies_se = new MediaPlayer[enemies_count];
        for(int i = 0; i < enemies.length; i++){
        	enemies[i] = BitmapFactory.decodeResource(resources, R.drawable.enemy);
        	enemies_se[i] = MediaPlayer.create(context, R.raw.se);

        }
    	
    	WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        Display disp = wm.getDefaultDisplay();
        screenWidth = disp.getWidth();
        screenHeight = disp.getHeight();        
        playerWidth_half = player.getWidth() / 2;
        playerHeight_half = player.getHeight() / 2;
        enemyWidth_half = enemies[0].getWidth() / 2;
        enemyHeight_half = enemies[0].getHeight() / 2;
        playerX = screenWidth / 2 - playerWidth_half;
        playerY = (int)(screenHeight * 0.8);
        playerisAtack = false;
        enemiesX = new int[enemies_count];
        enemiesY = new int[enemies_count];
        enemiesV = new int[enemies_count];
        for(int i = 0; i < enemiesX.length; i++){
        	enemiesX[i] = screenWidth + 100;
        	enemiesY[i] = screenHeight + 100;
        	enemiesV[i] = 1;
        }
        score_counter = 0;
        draw_counter = 0;
        isdraw = true;
        
    }
        
    @Override
    public void onDraw(Canvas canvas) {
    	
        Paint paint = new Paint();
    	for(int i = 0; i < screenWidth; i += background.getWidth()){
    		for(int j = 0; j < screenHeight; j += background.getHeight()){
        		canvas.drawBitmap(background, i, j, paint);    			
    		}
    	}
//        paint.setColor(Color.rgb(255, 255, 255));
//        canvas.drawRect(playerX - playerWidth_half - 50, playerY - playerHeight_half - 50, playerX + playerWidth_half + 50, playerY + playerHeight_half + 50, paint);
        canvas.drawBitmap(player, playerX - playerWidth_half, playerY - playerHeight_half, paint);
        if(playerisAtack){
        	for(int i = 0; i < enemies.length; i++){
        		if(playerX > enemiesX[i] - enemyWidth_half && 
        				playerX < enemiesX[i] + enemyWidth_half &&
        				playerY > enemiesY[i] &&
        				0 < enemiesY[i]){
        			enemiesY[i] = screenHeight + 100;
        			enemies_se[i].start(); 
        			score_counter++;
        		}
        	}
        	
        	paint.setColor(Color.rgb(255, 255, 127));
        	paint.setStrokeWidth(3);
        	canvas.drawLine(playerX, 0, playerX, playerY, paint);
        }
        
        for(int i = 0; i < enemies.length; i++){
        	if(enemiesY[i] > screenHeight){
        		enemiesY[i] = (int)(-screenHeight * Math.random());
        		enemiesX[i] = (int)(screenWidth * Math.random());
        		enemiesV[i] = (int)(Math.random() * 5 + 1);
        	}
        	enemiesY[i] += enemiesV[i];
        			
        	canvas.drawBitmap(enemies[i], enemiesX[i] - enemyWidth_half , enemiesY[i] - enemyHeight_half, paint);        	
        }
        
        if(debugmode){
	        paint.setColor(Color.rgb(255, 255, 255));
	        canvas.drawText("DrawCount: " + draw_counter + " / " + draw_count, 0, 20, paint);
	        canvas.drawText("Score: " + score_counter, 0, 40, paint);
        }
	        
        
        if(isdraw){
        	invalidate();
        }
        draw_counter++;
        if(draw_counter > draw_count && isdraw){
        	isdraw = false;
        	for(int i = 0; i < enemies_se.length; i++){
        		enemies_se[i].reset();
        	}
        	((GameActivity)context).end(score_counter);
        }
        
        try {
            Thread.sleep(30);
        } catch (InterruptedException e) {
        }
    }
    
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
        	if(event.getX() > playerX - playerWidth_half - 50 &&
        			event.getX() < playerX + playerWidth_half + 50 &&
        			event.getY() > playerY - playerHeight_half + 25 &&
        			event.getY() < playerY + playerHeight_half + 125){
//            	playerX = (int)event.getX();
//            	playerY = (int)event.getY() - 75;
//            	playerisAtack = true;
        	}
        }
        if(event.getAction() == MotionEvent.ACTION_UP){
//        	playerisAtack = false;
        	playerisAtack = !playerisAtack;
        }

        return true;
    }
    
    public void freeResources(){
    	background.recycle();
    	player.recycle();
    	for(int i = 0; i < enemies.length; i++){
    		enemies[i].recycle();
    		enemies_se[i].release();
    	}
    }
    
    public void acc(float x, float y, float z){
        if(x > 1){ //ç∂Ç…åXÇØÇΩÇ∆Ç´
        	playerX = playerX - 20;
        }
        if(x < -1){ //âEÇ…åXÇØÇΩÇ∆Ç´
        	playerX = playerX + 20;
        }
        if(playerX < 0){
        	playerX = 0;
        }
        if(playerX > screenWidth){
        	playerX = screenWidth;
        }
        
        if(y > 2){ //è„Ç…åXÇØÇΩÇ∆Ç´
        	playerY = playerY + 20;
        }
        if(y < 5){ //â∫Ç…åXÇØÇΩÇ∆Ç´
        	playerY = playerY - 20;
        }
        if(playerY < 0){
        	playerY = 0;
        }
        if(playerY > screenHeight - 50){
        	playerY = screenHeight - 50;
        }
    }
    
}
