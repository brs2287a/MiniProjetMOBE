package com.m2dl.ballgame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MotionEventCompat;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import static java.lang.Thread.sleep;

public class MainActivity extends Activity implements View.OnTouchListener, SensorEventListener {
    private GameView gameView;

    public static SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;

    private SensorManager sensorManager = null;
    private Sensor light;

    private int background_color = Color.WHITE;
    private int ball_color = Color.BLACK;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putInt("BackgroundColor", background_color);
        editor.putInt("BallColor", ball_color);
        editor.apply();

        gameView = new GameView(this);
        gameView.setOnTouchListener(this);
        setContentView(gameView);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        int action = MotionEventCompat.getActionMasked(event);
        switch (action) {
            case (MotionEvent.ACTION_DOWN):
                switch (gameView.direction) {
                    case HAUT:
                        gameView.direction = gameView.randomDirection(gameView.direction);
                        break;
                    case BAS:
                        gameView.direction = gameView.randomDirection(gameView.direction);
                        break;
                    case DROITE:
                        gameView.direction = gameView.randomDirection(gameView.direction);
                        break;
                    case GAUCHE:
                        gameView.direction = gameView.randomDirection(gameView.direction);

                        break;
                }
                break;
        }
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        sensorManager.unregisterListener(this, light);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, light, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            float light_value = event.values[0];
            System.out.println(light_value);
            if ( 0 <= light_value && light_value < 5) {
                background_color = Color.YELLOW;
                ball_color = Color.MAGENTA;
            } else if (5 <= light_value && light_value < 7) {
                background_color = Color.MAGENTA;
                ball_color = Color.GREEN;
            } else if (7 <= light_value && light_value < 10) {
                background_color = Color.GREEN;
                ball_color = Color.BLUE;
            } else {
                background_color = Color.BLUE;
                ball_color = Color.RED;
            }
            editor.putInt("BackgroundColor", background_color);
            editor.putInt("BallColor", ball_color);
            editor.apply();
        }
    }

}