package com.faridcodeur.letschat.utiles;

import com.faridcodeur.letschat.entities.Contact;

import java.util.ArrayList;
import java.util.List;

public class Global {
    public static List<Contact> contacts = new ArrayList<>();

    public static String getSurveysCollectionPath() { return "surveys";}

    public static String getAnswerCollectionPath() { return "surveys";}


}
