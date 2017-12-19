package com.ebookfrenzy.notetoself;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.support.design.widget.FloatingActionButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //create animation variables for the animations we just created
    Animation mAnimFlash;
    Animation mFadeIn;

    //Declare a NoteAdapter object as a member so that we can access it throughout the class
    private NoteAdapter mNoteAdapter;

    //add some member variables that will be used to load stored information within the app
    private boolean mSound;
    private int mAnimOption;
    private SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize mNoteAdapter -- get a reference to the ListView, and bind them together
        mNoteAdapter = new NoteAdapter();
        ListView listNote = (ListView)findViewById(R.id.listView);
        listNote.setAdapter(mNoteAdapter);

        //Handle clicks on the ListView
        listNote.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int whichItem, long id) {
                /*
                Create a temporary note
                Which is a reference to the Note
                That has just been clicked
                 */
                Note mtempNote = mNoteAdapter.getItem(whichItem);
                //Create a new Dialog Window for the ShowNote
                DialogShowNote dialog = new DialogShowNote();
                //Send in a reference to the Note to be shown
                dialog.sendNoteSelected(mtempNote);

                //show the dialog window with the note in it
                dialog.show(getFragmentManager(), "");
            }
        });

        //initialize the toSettings button and create onCLick listener to open the Settings menu \
        Button mtoSettings = (Button)findViewById(R.id.settingsButton);
        mtoSettings.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Create the intent for the ButtonClick
                Intent toSettings = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(toSettings);
                finish();
            }
        });

        //prepare the listView to listen for LongClicks
        // -- so that we can longClick
        listNote.setLongClickable(true);

        //now to detect long clicks and delete the note
        listNote.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int whichItem, long id) {
                //ask the adapter to Delete this entry
                mNoteAdapter.deleteNote(whichItem);
                return true;
            }
        });
    }

    //Call the onResume that will be used to Load the settings when the app start
    @Override
    protected void onResume(){
        super.onResume();

        //initialize the Animation Variables
        mAnimFlash = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.flash);
        mFadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);

        //set the rate of the flash based on the settings
        if (mAnimOption == SettingsActivity.FAST){

            mAnimFlash.setDuration(100);
            Log.i("anim = ", "" + mAnimOption);
        }else if (mAnimOption == SettingsActivity.SLOW){
            Log.i("anim = ", "" + mAnimOption);
            mAnimFlash.setDuration(1000);
        }

        //initialize the variables for loading data
        mPrefs = getSharedPreferences("NoteToSelf", MODE_PRIVATE);
        mSound = mPrefs.getBoolean("sound", true);
        mAnimOption = mPrefs.getInt("anim option", SettingsActivity.FAST);

        //notify the NoteAdapter of the changes
        mNoteAdapter.notifyDataSetChanged();
    }

    //Now override the onPause method to Save the Notes to the disl space by calling the method Down Below
    @Override
    protected  void onPause(){
        super.onPause();

        //Call the method
        mNoteAdapter.saveNotes();
    }

    //create the createNewNote method initialize the variable we just made
    public void createNewNote(Note n){
        //Add the call to the addNote Method that we just wrote inside the noteAdapter class
        mNoteAdapter.addNote(n);

    }


    //Create a method that will Open the NewNoteDialog class when the fab button is clicked
    public void openNewNote(View v){
        //create an Instance of the NewNote class
        DialogNewNote dialogNewNote = new DialogNewNote();
        //create the dialog window for the New Note Class
        dialogNewNote.show(getFragmentManager(), "");
    }

    //create a new Class for the NoteAdapter -- This class will place the notes in a ListView
    public class NoteAdapter extends BaseAdapter {

        //Create the Array List that we will store the users Notes inside of
        List<Note> noteList = new ArrayList<Note>();
        private Note.JSONSerializer mSerializer;

        //Now create a Constructor to Initialize the New Variables
        public NoteAdapter(){
            mSerializer = new Note.JSONSerializer("NoteToSelf.json", MainActivity.this.getApplicationContext());

            try{
                noteList = mSerializer.parse();
            } catch (Exception e){
                noteList = new ArrayList<Note>();
                Log.e("Error loading notes: ", "", e);
            }
        }

        //Create another method for saving the data -- by calling the save method from the Note Class
        public void saveNotes(){
            try{
                mSerializer.save(noteList);
            } catch (Exception e){
                Log.e("Error saving Notes", "", e);
            }
        }

        @Override
        public int getCount() {
            return noteList.size();
        }

        @Override
        public Note getItem(int whichItem) {
            return noteList.get(whichItem);
        }

        @Override
        public long getItemId(int whichItem) {
            return whichItem;
        }

        @Override
        public View getView(int whichItem, View view, ViewGroup viewGroup) {
            //implement this method next
            //has the view been inflated?
            if(view == null){

                //if not, do so here
                //First create a layoutInflater
                LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                // this inflates the list_item view that we created
                view = inflater.inflate(R.layout.list_item, viewGroup, false);
                //the false parameter is necessary
                //because of the way that we went to use listItem
            } // end if

            //Grab a reference to all our TextView and ImageView widgets
            TextView txtTitle = (TextView)view.findViewById(R.id.txtTitle);
            TextView txtDescription = (TextView)view.findViewById(R.id.txtDescription);

            ImageView ivImportant = (ImageView)view.findViewById(R.id.imageViewImportant);
            ImageView ivTodo = (ImageView)view.findViewById(R.id.imageViewTodo);
            ImageView ivIdea = (ImageView)view.findViewById(R.id.imageViewIdea);

            //Hide any imageView widgets that are not relevant
            Note tempNote = noteList.get(whichItem);

            //To animate or not to animate -- this will make sure that the right animations are on the right notes
            if (tempNote.ismImportant() && mAnimOption != SettingsActivity.NONE){
                view.setAnimation(mAnimFlash);
            }else{
                view.setAnimation(mFadeIn);
            }

            if(!tempNote.ismImportant()){
                ivImportant.setVisibility(View.GONE);
            }
            if(!tempNote.ismTodo()){
                ivTodo.setVisibility(View.GONE);
            }
            if(!tempNote.ismIdea()){
                ivIdea.setVisibility(View.GONE);
            }

            //Add the text to the heading and the Description
            txtTitle.setText(tempNote.getmTitle());
            txtDescription.setText(tempNote.getmDescription());

            return view;
        }

        //Add the addNote Method to the Class -- this class will be called when we want tot add a note
        public void addNote(Note n){
            noteList.add(n);
            notifyDataSetChanged(); // this notifies the data has been changed and the listView needs to be updated
        }

        //Add a method to the class that will be used to delete notes using the LongClickListener
        public void deleteNote(int n){
            noteList.remove(n);
            notifyDataSetChanged(); // notify the adapter of the Change
        }
    }
}
