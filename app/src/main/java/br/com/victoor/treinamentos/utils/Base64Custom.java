package br.com.victoor.treinamentos.utils;

import android.util.Base64;

public class Base64Custom {

    public static String codificaBase64(String texto){
        return Base64.encodeToString(texto.getBytes(),Base64.DEFAULT).replaceAll("(\\n|\\r)","");
    }

    public static String decodificaBase64(String textoCodificado){
        return new String(Base64.decode(textoCodificado,Base64.DEFAULT));
    }


}

