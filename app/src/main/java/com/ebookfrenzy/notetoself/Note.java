package com.ebookfrenzy.notetoself;

import android.content.Context;
import android.support.annotation.RequiresPermission;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rambo on 9/9/17.
 * This class will be used to hold the note data for the User -- Also the Handler for the Data that the App requires
 */

public class Note {

    //Start making some private variables for the class -- precede their names with the letter "m"
    private String mTitle;
    private boolean mTodo;
    private boolean mImportant;
    private String mDescription;
    private boolean mIdea;

    //add some more members that will act as the Key in key-value pairs
    private static final String JSON_TITLE = "title";
    private static final String JSON_DESCRIPTION = "description";
    private static final String JSON_IDEA = "idea";
    private static final String JSON_TODO = "todo";
    private static final String JSON_IMPORTANT = "important";

    //Now add a constructor
    //only used when new is called with a jsonObject
    public Note(JSONObject jo) throws JSONException{

        //Initialize all of the the variables at the top of the Class as the "Keys"
        mTitle = jo.getString(JSON_TITLE);
        mDescription = jo.getString(JSON_DESCRIPTION);
        mImportant = jo.getBoolean(JSON_IMPORTANT);
        mTodo = jo.getBoolean(JSON_TODO);
        mIdea = jo.getBoolean(JSON_IDEA);
    }

    //Now add the empty Constructor class
    public Note(){
    }

    //Now we are going to convert all of the Variables into JSONObjects so that they can be stored into JSONArrays...
    //Then parsed onto the ListView
    public JSONObject convertToJSON() throws JSONException{
        JSONObject jo = new JSONObject();

        jo.put(JSON_TITLE, mTitle);
        jo.put(JSON_DESCRIPTION, mDescription);
        jo.put(JSON_IDEA, mIdea);
        jo.put(JSON_TODO, mTodo);
        jo.put(JSON_IMPORTANT, mImportant);

        return jo;
    }

    //Generate the Getters and Setters for each
    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }


    public boolean ismIdea() {
        return mIdea;
    }

    public void setmIdea(boolean mIdea) {
        this.mIdea = mIdea;
    }


    public boolean ismTodo() {
        return mTodo;
    }

    public void setmTodo(boolean mTodo) {
        this.mTodo = mTodo;
    }

    public boolean ismImportant() {
        return mImportant;
    }

    public void setmImportant(boolean mImportant) {
        this.mImportant = mImportant;
    }


    /**
     * Create a Json serializer Class that will...
     * -- convert the Notes to JSON Objects
     * -- Store the JSON Objects in a JSON array
     * -- Take the JSON array convert to a string object and write it to a file // private disk space in our app
     * -- Then open that Same file and Parse the JSON Array
     */
    public static class JSONSerializer {
        //create some variables for the Parsing
        private String mFileName;
        private Context mContext;

        //Create a Constructor to initialize the Variables
        public JSONSerializer(String fn, Context con){
            mFileName = fn;
            mContext = con;
        }

        //Create the Method that will write the data to an Actual File
        public void save(List<Note> notes) throws JSONException, IOException {
            //Make an array JSON Format
            JSONArray jArray = new JSONArray();

            //And load it with the notes
            for (Note n : notes){
                jArray.put(n.convertToJSON());
            }

            //Now write it to the Private disk space of our App -- Apps have Private disk space now
            Writer writer = null;
            try{
                OutputStream out = mContext.openFileOutput
                        (mFileName, mContext.MODE_PRIVATE);

                writer = new OutputStreamWriter(out);
                writer.write(jArray.toString());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally{
                if (writer != null){
                    writer.close();
                }
            }
        }

        //Create the Method that will do the Parsing of the ArrayList file -- that we just created
        public ArrayList<Note> parse() throws IOException, JSONException{
            ArrayList<Note> noteList = new ArrayList<Note>();
            BufferedReader reader = null;
            try{
                //Gain access to the JSON File -- similar to the Code inside of Quovie
                InputStream in = mContext.openFileInput(mFileName);
                reader = new BufferedReader(new InputStreamReader(in));

                StringBuilder jsonString = new StringBuilder();
                String line = null;

                while((line = reader.readLine()) != null){
                    jsonString.append(line);
                }
            } catch(FileNotFoundException e){
                //We will ignore this one, since it happens
                //When we start fresh. You could add a log here
            } finally{
                //this will always run
                if (reader != null){
                    reader.close();
                }
                return noteList;
            }
        }
    }
}


