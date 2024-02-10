package com.example.controla_tus_habitos.utils;

/**
 * Esta interfaz sirve para gestionar las operaciones asincronas de firebase de forma sincrona,
 * ya que mi implementacion protege la separacion de responsabilidades dejandola logica del servicio
 * fuera de la de la UI
 */
public interface AuthCallback {
    void onAuthSuccess();
    void onAuthFailed();
}
