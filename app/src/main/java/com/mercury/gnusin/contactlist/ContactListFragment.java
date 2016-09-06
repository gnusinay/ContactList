package com.mercury.gnusin.contactlist;

import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;


/**
 * Created by gnusin on 01.09.2016.
 */
public class ContactListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private ContactAdapter contactAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        contactAdapter = new ContactAdapter(this.getContext());

        setListAdapter(contactAdapter);

        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.v_contact_list, null);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        String[] projection = {
                ContactsContract.Data.CONTACT_ID,
                ContactsContract.Data.DISPLAY_NAME,
                ContactsContract.Data.PHOTO_ID,
                ContactsContract.Data.MIMETYPE,
                ContactsContract.Data.DATA1,
                ContactsContract.Data.DATA14
        };

        String selectionClause = ContactsContract.Contacts.Entity.MIMETYPE + " in ( ?, ?, ?)";

        String[] selectionArgs = {ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE,
                                  ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE,
                                  ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE};

        return new CursorLoader(
                getActivity(),
                ContactsContract.Data.CONTENT_URI,
                projection,
                selectionClause,
                selectionArgs,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        HashMap<Integer, Contact> contactMap = new HashMap<>();

        while (data.moveToNext()) {

            int id = data.getInt(data.getColumnIndex(ContactsContract.Data.CONTACT_ID));

            Contact contact = contactMap.get(id);
            if (contact == null) {
                contact = new Contact(id);
                contactMap.put(id, contact);
            }

            String type = data.getString(data.getColumnIndex(ContactsContract.Data.MIMETYPE));
            switch (type) {
                case ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE:
                    contact.addEmail(data.getString(data.getColumnIndex(ContactsContract.Data.DATA1)));
                    break;
                case ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE:
                    contact.setName(data.getString(data.getColumnIndex(ContactsContract.Data.DISPLAY_NAME)));
                    break;
                case ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE:
                    int i = data.getColumnIndex(ContactsContract.Data.DATA14);
                    if (i > -1) {
                        int photoPointer = data.getInt(i);
                        Uri photoUri = ContentUris.withAppendedId(ContactsContract.DisplayPhoto.CONTENT_URI, photoPointer);
                        try {
                            AssetFileDescriptor fd = getContext().getContentResolver().openAssetFileDescriptor(photoUri, "r");
                            InputStream in = fd.createInputStream();
                            byte[] imageArray = new byte[(int) fd.getLength()];
                            in.read(imageArray);
                            contact.setPhoto(BitmapFactory.decodeByteArray(imageArray, 0, imageArray.length));
                        } catch (IOException e) {

                        }
                    }
                    break;
            }
        }

        List<Contact> contactList = new ArrayList<>(contactMap.values());
        Collections.sort(contactList, new Comparator<Contact>() {
            @Override
            public int compare(Contact lhs, Contact rhs) {
                return lhs.getName().compareTo(rhs.getName());
            }
        });

        contactAdapter.changeData(contactList);
        contactAdapter.notifyDataSetChanged();

        getView().findViewById(R.id.loadIndicator).setVisibility(View.INVISIBLE);
        if (contactAdapter.getCount() > 0) {
            getView().findViewById(R.id.emptyText).setVisibility(View.INVISIBLE);
        } else {
            getView().findViewById(R.id.emptyText).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        contactAdapter.changeData(null);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        //super.onListItemClick(l, v, position, id);
        Contact contact = (Contact) l.getAdapter().getItem(position);
        if (contact.getEmails() != null) {
            if (contact.getEmails().size() > 1) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getString(R.string.choose_email_dialog_title));
                builder.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.select_dialog_item, contact.getEmails().toArray()), null);
                builder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        createEmailForm((String) parent.getItemAtPosition(position));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                builder.create().show();
            } else {
                createEmailForm(contact.getEmails().get(0));
            }
        } else {
            Toast toast = Toast.makeText(getActivity(), getString(R.string.no_email_message), Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }
    }

    private void createEmailForm(String emailTo) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, emailTo);
        i.putExtra(Intent.EXTRA_SUBJECT, getContext().getString(R.string.default_email_subject));
        i.putExtra(Intent.EXTRA_TEXT, getContext().getString(R.string.default_email_body));
        getActivity().startActivity(i);
    }
}
