package com.mercury.gnusin.contactlist;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gnusin on 05.09.2016.
 */
public class Contact {

    private int id;
    private String name;
    private List<String> emails;
    private Bitmap photo;

    public Contact(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getEmails() {
        return emails;
    }

    public void addEmail(String email) {
        if (emails == null) {
            emails = new ArrayList<>();
        }
        emails.add(email);

        /*

        if (emails == null) {
            emails = new LinkedList<>();
            emails.add(email);
        } else {
            int i = 0;
            while (i < emails.size()) {
                if (email.compareToIgnoreCase(emails.get(i)) < 0) {
                    emails.add(i, email);
                    break;
                }
                i++;
            }
            if (i == emails.size()) {
                emails.add(email);
            }
        }*/
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    public boolean hasPhoto() {
        return photo != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        return  id != ((Contact) o).id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
