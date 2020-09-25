package br.com.victoor.treinamentos.session;

import android.net.Uri;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import br.com.victoor.treinamentos.model.User;

public class DataUser {
    private static String id;
    private static String personName;
    private static String personGivenName;
    private static String personFamilyName;
    private static String personEmail;
    private static String personId;
    private static Uri personPhoto;

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        DataUser.user = user;
    }

    private static User user;

    public static boolean isAdmin() {
        return admin;

    }

    public static void setAdmin(boolean admin) {
        DataUser.admin = admin;
    }

    private static boolean admin;

    public DataUser(GoogleSignInAccount account){
        personName = account.getDisplayName();
        personGivenName = account.getGivenName();
        personFamilyName = account.getFamilyName();
        personEmail = account.getEmail();
        personId = account.getId();
        personPhoto = account.getPhotoUrl();
        user = new User();
    }

    public static String getPersonName() {
        return personName;
    }

    public static void setPersonName(String personName) {
        DataUser.personName = personName;
    }

    public static String getPersonGivenName() {
        return personGivenName;
    }

    public static void setPersonGivenName(String personGivenName) {
        DataUser.personGivenName = personGivenName;
    }

    public static String getPersonFamilyName() {
        return personFamilyName;
    }

    public static void setPersonFamilyName(String personFamilyName) {
        DataUser.personFamilyName = personFamilyName;
    }

    public static String getPersonEmail() {
        return personEmail;
    }

    public static void setPersonEmail(String personEmail) {
        DataUser.personEmail = personEmail;
    }

    public static String getPersonId() {
        return personId;
    }

    public static void setPersonId(String personId) {
        DataUser.personId = personId;
    }

    public static Uri getPersonPhoto() {
        return personPhoto;
    }

    public static void setPersonPhoto(Uri personPhoto) {
        DataUser.personPhoto = personPhoto;
    }

    public static String getId() {
        return id;
    }

    public static void setId(String id) {
        DataUser.id = id;
    }
}
