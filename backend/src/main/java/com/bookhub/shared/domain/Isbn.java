package com.bookhub.shared.domain;


import java.util.Objects;

/***
 *
 * deux objets meme valeurs sont consideres eqaux
 * et considere comme ValueObject (son identité est definie par sa valeur)
 * et pas une entite
 */
public final class Isbn{
    private final String identifiant;


    public Isbn(String identifiant) {
        this.identifiant = Objects.requireNonNull(identifiant);
    }

    public String getIdentifiant() {
        return identifiant;
    }

    @Override
    public boolean equals(Object id) {
        if(id!=null && (id instanceof Isbn) ){
            return identifiant.equals(((Isbn) id).identifiant);
    }
        return false;
    }

    /***
     * le bucket regroupe tous les objets qui ont meme le hashcode
     * et ensuite on utilise equals pour distinguer lobjet exacte
     * *** si nous avons 3 objets dans le meme buket et qui ont le meme hashcode on parle de collision ,
     * et pour remedier a ce probleme on utilise equals
     * hashMap et hashSet se sont des tables de hashage , ses tables peuvent mal fonctionner
     * si equals et hashcode sont mal definie dans la classe de l'objet lui meme
     * En deux etape;
     * Hashmap on localise un bucket puis avec equals on trouve lobjet exacte a linterieur,
     * hashmap chaque cle est unique mais peuvent avoir meme valeur
     * hashmap sert a stocket des cle et des valeurs
     * HashSet fonction pareil , garde seulement une seule occurence de chaque objet
     * Haset pas ce key value , lutilité est de garder une collection sans doublons ,
     * s'appuie de hashcode et equals pour fontionner corretctement
     *
     * @return
     */
    @Override
    public int hashCode() {
        return identifiant.hashCode();
    }
}
