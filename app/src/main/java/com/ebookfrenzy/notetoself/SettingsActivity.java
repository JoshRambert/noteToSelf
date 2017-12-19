package com.ebookfrenzy.notetoself;

import android.content.SharedPreferences;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class SettingsActivity extends AppCompatActivity {
    //the intent so that the Settings activity is opened when clicked


    //add some member variables for the Class -- that will represent what is in the settings menu
    private SharedPreferences mPrefs;
    private SharedPreferences.Editor mEditor;

    private boolean mSound;

    public static final int FAST = 0;
    public static final int SLOW = 1;
    public static final int NONE = 2;

    private int mAnimaOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //now initialize the mPrefs and the mEditor -- for saving and loading data in the App
        mPrefs = getSharedPreferences("NoteToSelf", MODE_PRIVATE);
        mEditor = mPrefs.edit();

        //get a reference to the checkBox for sound
        mSound = mPrefs.getBoolean("sounds", true);
        CheckBox checkBoxSound = (CheckBox)findViewById(R.id.soundCheckBox);

        if(mSound){
            checkBoxSound.setChecked(true);
        }else{
            checkBoxSound.setChecked(false);
        }

        //create an anonymous class to listen for clicks on the Sound CheckBox
        checkBoxSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.i("sound = ", "" + mSound);
                Log.i("isChecked = ", "" + isChecked);

                //if msound is true make it False
                //if mSound is false make it true
                mSound = ! mSound;
                mEditor.putBoolean("sound", mSound);
            }
        });

        //Now for the RadioButtons
        mAnimaOption = mPrefs.getInt("anim option", FAST);

        RadioGroup radioGroup = (RadioGroup)findViewById(R.id.radioGroup);

        //deselect all of the buttons
        radioGroup.clearCheck();

        //Which Radio button should be selected
        switch(mAnimaOption){
            case FAST:
                radioGroup.check(R.id.radioFast);
                break;
            case SLOW:
                radioGroup.check(R.id.radioSlow);
                break;
            case NONE:
                radioGroup.check(R.id.radioNone);
                break;

        }

        //Create the On click listeners for the Radio Buttons
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                //reference the radio Buttons in the Layout
                RadioButton rb = (RadioButton)group.findViewById(checkedId);
                if (null != rb && checkedId > -1){
                    switch (rb.getId()){

                        case R.id.radioFast:
                            mAnimaOption = FAST;
                            break;
                        case R.id.radioSlow:
                            mAnimaOption = SLOW;
                            break;
                        case R.id.radioNone:
                            mAnimaOption = NONE;
                            break;
                    }
                    //store the information
                    mEditor.putInt("anim option", mAnimaOption);
                }
            }
        });
    }

    //add the onPause method to commit the information we just stored --
    //so that whenever the user leaves the settings page it is automatically saved
    @Override
    protected void onPause(){
        super.onPause();

        //save the settings here
        mEditor.commit();
    }
}
