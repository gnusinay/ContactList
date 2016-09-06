package com.mercury.gnusin.contactlist;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * Created by gnusin on 01.09.2016.
 */
public class WorkStartDialog extends DialogFragment {

    public interface WorkStartDialogListener {
        void onDialogPositiveClick(DialogFragment dialogFragment);
        void onDialogNegativeClick(DialogFragment dialogFragment);
    }

    private WorkStartDialogListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (WorkStartDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement WorkStartDialogListener");
        }
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder =  new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.work_start_dialog_title);
        builder.setMessage(R.string.work_start_dialog_message);
        builder.setPositiveButton(R.string.work_start_dialog_button_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.onDialogPositiveClick(WorkStartDialog.this);
            }
        });
        builder.setNegativeButton(R.string.work_start_dialog_button_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.onDialogNegativeClick(WorkStartDialog.this);
            }
        });
        return builder.create();
    }
}

