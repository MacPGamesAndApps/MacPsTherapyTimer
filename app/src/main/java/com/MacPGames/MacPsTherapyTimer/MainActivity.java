package com.MacPGames.MacPsTherapyTimer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.os.Handler;
import android.media.MediaPlayer;
import static android.text.TextUtils.isEmpty;

public class MainActivity extends AppCompatActivity {

    private int stretchTimes;
    private int holdTime;
    private boolean timerRunning = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void buttonStart_onClick(View view)
    {
        EditText editTextHoldTime = (EditText) findViewById(R.id.editTextHoldTime);
        EditText editTextTotalRepetitions = (EditText) findViewById(R.id.editTextTotalRepetitions);
        boolean canStart = (!isEmpty(editTextHoldTime.getText().toString()))
                            && (!isEmpty(editTextTotalRepetitions.getText().toString()));
        if(canStart) {
            ClearAllSelectionHighlight();
            ((Button) findViewById(R.id.buttonPause)).setY(view.getY());
            ((Button) findViewById(R.id.buttonPause)).setVisibility(View.VISIBLE);
            view.setVisibility(View.INVISIBLE);
            view.setEnabled(false);
            editTextHoldTime.setEnabled(false);
            editTextTotalRepetitions.setEnabled(false);
            tmrStart_Run(); //tmrStart.Enabled = true
        }
    }

    public void buttonPause_onClick(View view)
    {
        String pauseText = getResources().getString(R.string.pause);

        if (((Button)view).getText().equals(pauseText))
        {
            timerRunning = false;
            ((Button)view).setText(getResources().getString(R.string.resume));
        }
        else
        {
            timerRunning = true;
            ((Button) view).setText(pauseText);
            tmrTimer_Run();
        }
    }

    private void tmrStart_Run() // 2000 msec, disabled on loading
    {
        final Handler timerStart = new Handler();
        timerStart.postDelayed(new Runnable() {
            @Override
            public void run() {
                stretchTimes = Integer.parseInt(((EditText) findViewById(R.id.editTextTotalRepetitions)).getText().toString());
                ((TextView) findViewById(R.id.textViewCountDown)).setText(((EditText) findViewById(R.id.editTextHoldTime)).getText());
                ((TextView) findViewById(R.id.textViewRepetitions)).setText(Integer.toString(stretchTimes));
                tmrTimer_Run(); //tmrTimer.Enabled = true
                ((Button) findViewById(R.id.buttonPause)).setEnabled(true);
            }
        }, 2000);
    }

    private void tmrTimer_Run() //1000 msec, disabled on loading
    {
        final Handler timerMain = new Handler();
        timerMain.postDelayed(new Runnable() {
            @Override
            public void run()
            {
                TextView txtCountDown = (TextView) findViewById(R.id.textViewCountDown);

                holdTime = Integer.parseInt(txtCountDown.getText().toString());
                holdTime--;
                if (holdTime < 0)
                {
                    //Finished one repetition
                    tmrController_Run();//tmrController.Enabled = true;
                    if(((CheckBox)findViewById(R.id.checkBoxSoundNotification)).isChecked())
                    {
                        MediaPlayer mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.chime_1a);
                        mediaPlayer.start();
                    }
                }
                else
                {
                    if(timerRunning)
                    {
                        txtCountDown.setText(Integer.toString(holdTime));
                        timerMain.postDelayed(this, 1000);
                    }
                }
            }
        }, 1000);
    }

    private void tmrController_Run() //3000 msec, disabled on loading
    {
        final Handler timerController = new Handler();
        timerController.postDelayed(new Runnable() {
            @Override
            public void run() {
                TextView txtCountDown = (TextView) findViewById(R.id.textViewCountDown);
                TextView txtRemainingTimes = (TextView) findViewById(R.id.textViewRepetitions);
                EditText txtHoldTime = (EditText) findViewById(R.id.editTextHoldTime);

                stretchTimes--;
                if (stretchTimes == 0)
                {
                    txtCountDown.setText("Finished");
                    txtRemainingTimes.setText("0"); //textViewRepetitions
                    Button btnStart = ((Button) findViewById(R.id.buttonStart));
                    btnStart.setEnabled(true);
                    btnStart.setVisibility(View.VISIBLE);
                    Button btnPause = ((Button) findViewById(R.id.buttonPause));
                    btnPause.setVisibility(View.INVISIBLE);
                    btnPause.setEnabled(false);
                    txtHoldTime.setEnabled(true);
                    ((EditText) findViewById(R.id.editTextTotalRepetitions)).setEnabled(true);
                }
                else
                {
                    tmrTimer_Run();
                    txtCountDown.setText(txtHoldTime.getText());
                    txtRemainingTimes.setText(Integer.toString(stretchTimes));
                }
            }
        }, 3000);
    }

    private void ClearAllSelectionHighlight()
    {
        ((EditText) findViewById(R.id.editTextHoldTime)).setSelection(0);
        ((EditText) findViewById(R.id.editTextTotalRepetitions)).setSelection(0);
    }
}
