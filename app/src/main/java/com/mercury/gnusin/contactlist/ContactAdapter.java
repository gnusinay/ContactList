package com.mercury.gnusin.contactlist;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

/**
 * Created by gnusin on 05.09.2016.
 */
public class ContactAdapter extends BaseAdapter {

    private Context context;
    private List<Contact> contacts;
    private LayoutInflater inflater;

    public ContactAdapter(Context context) {
        super();
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void changeData(List<Contact> data) {
        contacts = data;
    }

    @Override
    public int getCount() {
        if (contacts == null) {
            return 0;
        } else {
            return contacts.size();
        }
    }

    @Override
    public Object getItem(int position) {
        return contacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return contacts.get(position).getId();
    }

    public Context getContext() {
        return context;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Contact contact = (Contact) getItem(position);
        if (view == null) {
            view = inflater.inflate(R.layout.v_contact_item, parent, false);
        }

        if (contact.hasPhoto()) {
            ImageView photo = (ImageView) view.findViewById(R.id.photo);
            photo.setImageBitmap(contact.getPhoto());
        }

        TextView name = (TextView) view.findViewById(R.id.name);
        name.setText(contact.getName());

        Spinner emails = (Spinner) view.findViewById(R.id.emails);



        if (contact.getEmails() != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, contact.getEmails());
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            emails.setAdapter(adapter);
            //emails.setSelection(-1, true);
            emails.setVisibility(View.VISIBLE);
        } else {
            emails.setAdapter(null);
            emails.setVisibility(View.INVISIBLE);
        }


        /*emails.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String email = (String) parent.getSelectedItem();
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL, email);
                i.putExtra(Intent.EXTRA_SUBJECT, getContext().getString(R.string.default_email_subject));
                i.putExtra(Intent.EXTRA_TEXT, getContext().getString(R.string.default_email_body));
                getContext().startActivity(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        return view;
    }


}
