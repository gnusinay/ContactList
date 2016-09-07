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
    private static final String LIST_IS_LOADED_PREFERENCE_KEY = "listIsLoaded";
    private static final String FRAGMENT_TAG = "list_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getPreferences(MODE_PRIVATE).getBoolean(LIST_IS_LOADED_PREFERENCE_KEY, false)) {
            createFragment();
        } else {
            DialogFragment dialogFragment = new WorkStartDialog();
            dialogFragment.show(getSupportFragmentManager(), WORK_START_DIALOG_TAG);
        }
    }

    private void createFragment() {

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(ContactListFragment.TAG);
        if (fragment == null) {
            fragment = new ContactListFragment();
        }

        FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
        tran.replace(android.R.id.content, fragment, ContactListFragment.TAG);
        //tran.addToBackStack(null);
        tran.commit();
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialogFragment) {
        getPreferences(MODE_PRIVATE).edit().putBoolean(LIST_IS_LOADED_PREFERENCE_KEY, true).commit();
        createFragment();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialogFragment) {
        LayoutInflater.from(this).inflate(R.layout.v_no_contacts, (ViewGroup) findViewById(android.R.id.content));
    }
}
