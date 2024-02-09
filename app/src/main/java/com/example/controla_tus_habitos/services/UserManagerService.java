package com.example.controla_tus_habitos.services;

import androidx.annotation.NonNull;

import com.example.controla_tus_habitos.utils.AuthCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserManagerService {

    static boolean registrado;
    static boolean logeado;
    public static FirebaseUser user;

    static FirebaseAuth firebaseAuth =  FirebaseAuth.getInstance();

    /**
     * metodo para registrar un usuario con  el ervicio de firebase
     * @param email
     * @param password
     * @return hace un callback para gestionarlo de manera sincrona
     */
    public static void register(String email, String password, AuthCallback callback) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onAuthSuccess();
                    } else {
                        callback.onAuthFailed();
                    }
                });
    }

    /**
     * metodo para logear a un usuario con  el ervicio de firebase
     * @param email
     * @param password
     * @return hace un callback para gestionarlo de manera sincrona
     */
    public static void login(String email, String password, AuthCallback callback) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onAuthSuccess();
                    } else {
                        callback.onAuthFailed();
                    }
                });
    }

}
