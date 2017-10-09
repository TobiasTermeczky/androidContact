package nl.yzaazy.contactslist.Interface;

import java.util.ArrayList;

import nl.yzaazy.contactslist.Model.Person;

public interface PersonInterface {
    void onGetPersonCompleted(ArrayList<Person> mPersonList);
}
