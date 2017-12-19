package com.ebookfrenzy.notetoself;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

/**
 * Created by Rambo on 9/9/17.
 * This is used for when the user is creating a New note
 */

public class DialogNewNote extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        //this is the Dialog Window
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //Inflate the Layout for the new Dialog
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_new_note, null); //This is the reference for the Layout resource File

        //get a reference to each of the UI elements
        final EditText editTitle = (EditText)dialogView.findViewById(R.id.editTitle);
        final EditText editDescription = (EditText)dialogView.findViewById(R.id.editDescription);
        final CheckBox checkBoxIdea = (CheckBox)dialogView.findViewById(R.id.checkBoxIdea);
        final CheckBox checkBoxTodo = (CheckBox)dialogView.findViewById(R.id.checkBoxTodo);
        final CheckBox checkBoxImportant = (CheckBox)dialogView.findViewById(R.id.checkBoxImportant);

        Button btnCancel = (Button)dialogView.findViewById(R.id.btnCancel);
        Button btnOK = (Button)dialogView.findViewById(R.id.btnOK);

        //create the Dilog windo that will ask the User to enter a new Note or Cancel
        builder.setView(dialogView).setMessage("Add new Note");

        //handle the Cancel Button
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dismiss the window
                dismiss();
            }
        });

        //handle the Okay button -- we use the Getter and Setter Java Class called Note that we created earlier
        //then we set each of the members to the appropriate form for the Note
        btnOK.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //create a New Note
                Note newNote = new Note();

                //set its variables to match the users entries on the form
                newNote.setmTitle(editTitle.getText().toString());
                newNote.setmDescription(editDescription.getText().toString());
                newNote.setmIdea(checkBoxIdea.isChecked());
                newNote.setmTodo(checkBoxTodo.isChecked());
                newNote.setmImportant(checkBoxImportant.isChecked());

                //get a reference to the Main Activity -- so that we can call the createNewNote
                MainActivity callingActivity = (MainActivity) getActivity();

                //Pass newNote back to MainActivity
                callingActivity.createNewNote(newNote);

                //quit the Dialog
                dismiss();
            }
        });
        //
        return builder.create();
    }
}
