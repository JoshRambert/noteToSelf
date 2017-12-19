package com.ebookfrenzy.notetoself;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Rambo on 9/9/17.
 */

public class DialogShowNote extends DialogFragment {
    //create a variable for the Note Class that we have
    private Note mNote;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        //Declare and Initialize an instance of AlertDialog.builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //Create the layout inflater and inflate the Dialog Show Note Resource File
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_show_note, null);

        //Reference all of the UI elements within the Layout
        TextView txtTitle = (TextView)dialogView.findViewById(R.id.txtTitle);
        TextView txtDescription = (TextView)dialogView.findViewById(R.id.txtDescription);
        //set the text for the Title and Description
        txtTitle.setText(mNote.getmTitle());
        txtDescription.setText(mNote.getmDescription());

        ImageView ivTodo = (ImageView)dialogView.findViewById(R.id.imageViewTodo);
        ImageView ivIdea = (ImageView)dialogView.findViewById(R.id.imageViewIdea);
        ImageView ivImportant = (ImageView)dialogView.findViewById(R.id.imageViewImportant);

        //Check whether the Note being shown is important, an idea or is a to-do
        //then display the image if it is
        if (!mNote.ismImportant()){
            ivImportant.setVisibility(View.GONE);
        }

        if (!mNote.ismTodo()){
            ivTodo.setVisibility(View.GONE);
        }

        if (!mNote.ismIdea()){
            ivIdea.setVisibility(View.GONE);
        }

        Button btnOK = (Button)dialogView.findViewById(R.id.btnOK);
        builder.setView(dialogView).setMessage("Your Note"); //create a dialogView that shows the Message your note
        //then dismiss the dialog window
        btnOK.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                dismiss();
            }
        });
        return builder.create();
    }

    //Receive a not from the MainActivity -- Initialize mNote
    public void sendNoteSelected(Note noteSelected){
        mNote = noteSelected;
    }
}
