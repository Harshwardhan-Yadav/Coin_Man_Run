package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import java.util.*;

public class CoinMan extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] man;
	Rectangle manRectangle;
	int frameLooper=0;
	int frameDelay=0;
	int manY=0;
	float gravity=1f;
	float velocity=0;
	ArrayList<Float> coinXs=new ArrayList<>();
	ArrayList<Float> coinYs=new ArrayList<>();
	ArrayList<Rectangle> coinRectangles=new ArrayList<>();
	int coinCount=0;
	Texture coin;
	ArrayList<Float> bombXs=new ArrayList<>();
	ArrayList<Float> bombYs=new ArrayList<>();
	ArrayList<Rectangle> bombRectangles=new ArrayList<>();
	int bombCount=0;
	Texture bomb;
	int score=0;
	int gameState=0;
	BitmapFont font;
	Texture dizzy;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		man=new Texture[4];
		man[0]=new Texture("frame-1.png");
		man[1]=new Texture("frame-2.png");
		man[2]=new Texture("frame-3.png");
		man[3]=new Texture("frame-4.png");
		manY=Gdx.graphics.getHeight()/2;
		coin=new Texture("coin.png");
		bomb=new Texture("bomb.png");
		manRectangle=new Rectangle(0,manY,man[frameLooper].getWidth(),man[frameLooper].getHeight());
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);
		dizzy = new Texture("dizzy-1.png");
	}

	public void makeCoin(){
		Random random=new Random();
		coinYs.add(random.nextFloat()*Gdx.graphics.getHeight());
		coinXs.add((float)Gdx.graphics.getWidth());
		coinRectangles.add(new Rectangle(coinXs.get(coinXs.size()-1),coinYs.get(coinYs.size()-1),coin.getWidth(),coin.getHeight()));
	}

	public void makeBomb(){
		Random random=new Random();
		bombXs.add((float)Gdx.graphics.getWidth());
		bombYs.add(random.nextFloat()*Gdx.graphics.getHeight());
		bombRectangles.add(new Rectangle(bombXs.get(bombXs.size()-1),bombYs.get(bombYs.size()-1),bomb.getWidth(),bomb.getHeight()));
	}

	@Override
	public void render () {
		batch.begin();
		batch.draw(background,0,0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		font.draw(batch,""+score,Gdx.graphics.getWidth()-150,Gdx.graphics.getHeight()-150);
		if(gameState==0){//waiting to start
			batch.draw(man[frameLooper], 0, manY);
			if(Gdx.input.justTouched()){
				gameState=1;
			}
		} else if(gameState==1) { //running
			for (int i = 0; i < coinXs.size(); i++) {
				batch.draw(coin, coinXs.get(i), coinYs.get(i));
				coinXs.set(i, coinXs.get(i) - 5);
				coinRectangles.set(i, new Rectangle(coinXs.get(i), coinYs.get(i), coin.getWidth(), coin.getHeight()));
			}
			for (int i = 0; i < bombXs.size(); i++) {
				batch.draw(bomb, bombXs.get(i), bombYs.get(i));
				bombXs.set(i, bombXs.get(i) - 5);
				bombRectangles.set(i, new Rectangle(bombXs.get(i), bombYs.get(i), bomb.getWidth(), bomb.getHeight()));
			}
			batch.draw(man[frameLooper], 0, manY);
			manRectangle = new Rectangle(0, manY, man[frameLooper].getWidth(), man[frameLooper].getHeight());
			for (int i = 0; i < coinRectangles.size(); i++) {
				if (Intersector.overlaps(manRectangle, coinRectangles.get(i))) {
					System.out.println("Coin!");
					score++;
					coinXs.remove(i);
					coinYs.remove(i);
					coinRectangles.remove(i);
				}
			}
			for (int i = 0; i < bombRectangles.size(); i++) {
				if (Intersector.overlaps(manRectangle, bombRectangles.get(i))) {
					System.out.println("Bomb!");
					gameState=2;
				}
			}
			velocity += gravity;
			manY -= velocity;
			if (manY < 0) {
				manY = 0;
			}
			if (manY > Gdx.graphics.getHeight()) {
				manY = Gdx.graphics.getHeight();
			}
			if (Gdx.input.justTouched()) {
				velocity = 0;
				manY += 250;
			}
			if (coinCount < 100) {
				coinCount++;
			} else {
				makeCoin();
				coinCount = 0;
			}
			if (bombCount < 200) {
				bombCount++;
			} else {
				makeBomb();
				bombCount = 0;
			}
			if (frameDelay < 5) frameDelay++;
			else {
				frameLooper = (++frameLooper) % 4;
				frameDelay = 0;
			}
		} else{//end
			batch.draw(dizzy,0,manY);
			font.draw(batch,""+score,Gdx.graphics.getWidth()-150,Gdx.graphics.getHeight()-150);
			coinXs.clear();
			coinYs.clear();
			coinRectangles.clear();
			coinCount=0;
			bombXs.clear();
			bombYs.clear();
			bombRectangles.clear();
			bombCount=0;
			velocity=0;
			frameLooper=0;
			frameDelay=0;
			if(Gdx.input.justTouched()){
				manY=Gdx.graphics.getHeight()/2;
				score=0;
				gameState=1;
			}
		}
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
