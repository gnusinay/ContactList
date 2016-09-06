package com.mercury.gnusin.contactlist;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class ContactListActivity extends AppCompatActivity implements WorkStartDialog.WorkStartDialogListener {

    private static final String WORK_START_DIALOG_TAG = "WSDialog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.a_contact_list);

        DialogFragment dialogFragment = new WorkStartDialog();
        dialogFragment.show(getSupportFragmentManager(), WORK_START_DIALOG_TAG);
    }


    @Override
    public void onDialogPositiveClick(DialogFragment dialogFragment) {
        Fragment fragment = new ContactListFragment();

        FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
        tran.replace(android.R.id.content, fragment);
        //tran.addToBackStack(null);
        tran.commit();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialogFragment) {
        LayoutInflater.from(this).inflate(R.layout.v_no_contacts, (ViewGroup) findViewById(android.R.id.content));
        //findViewById(R.id.noContactsText).setVisibility(View.VISIBLE);
    }
}
