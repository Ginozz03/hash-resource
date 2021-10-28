package operazioniBit;

public class OperazioniBit {
    /**
     * converte l' array di numeri ascii in stringhe binarie e le formatta a 8 bit di lunghezza
     * @param messaggio l'array con i valori ascii dei caratteri
     * @return l'array con i valori binari dei caratteri ascii formattati
     */
    public static String[] asciiToBinario(int[] messaggio){
        String[] messaggioBinario = new String[messaggio.length];
        for(int i = 0; i < messaggio.length; i++){
            messaggioBinario[i] = aggiungiZeroASinistra(numeroToBinario(messaggio[i]), 8);
        }
        return messaggioBinario;
    }

    /**
     * converte un numero intero in binario
     * @param num il numero intero
     * @return la sua stringa binaria
     */
    public static String numeroToBinario(int num){
        String divisioni = "", risulato = "";

        while(num > 0){
            divisioni += num % 2;
            num /= 2;
        }

        for(int i = divisioni.length() - 1; i >= 0; i--){
            risulato += divisioni.charAt(i);
        }
        return risulato;
    }

    /**
     * aggiunge il numero di zeri a sinistra necessario per raggiungere la lunghezza richiesta
     * @param s la stringa di partenza
     * @param lunghezza la lunghezza da raggiungere
     * @return la stringa formattata secondo la lunghezza richiesta
     */
    public static String aggiungiZeroASinistra(String s, int lunghezza){
        int differenza = lunghezza - s.length();
        String zeri = "";

        for(int i = 0; i < differenza; i++){
            zeri += '0';
        }
        return zeri + s;
    }

    /**
     * effettua l' operazione bit xor tra due stringhe binarie
     * @param s1 la prima stringa
     * @param s2 la seconda stringa
     * @return la stringa risultante dallo xor binario
     */
    public static String xor(String s1, String s2){
        String xor = "";
        for(int i = 0; i < s1.length(); i++){
            if((s1.charAt(i) == '0' && s2.charAt(i) == '1') || (s1.charAt(i) == '1' && s2.charAt(i) == '0')){
                xor += '1';
            }else{
                xor += '0';
            }
        }
        return xor;
    }

    /**
     * effettua l' operazione bit and tra due stringhe binarie
     * @param s1 la prima stringa
     * @param s2 la seconda stringa
     * @return la stringa risultante dall'and binario
     */
    public static String and(String s1, String s2){
        String and = "";
        for(int i = 0; i < s1.length(); i++){
            and += Integer.parseInt("" + s1.charAt(i)) * Integer.parseInt("" + s2.charAt(i));
        }
        return and;
    }

    /**
     * effettua l' operazione bit or tra due stringhe binarie
     * @param s1 la prima stringa
     * @param s2 la seconda stringa
     * @return la stringa risultante dall'or binario
     */
    public static String or(String s1, String s2){
        String or = "";
        for(int i = 0; i < s1.length(); i++){
            if(s1.charAt(i) == '1' || s2.charAt(i) == '1'){
                or += '1';
            }else {
                or += '0';
            }
        }
        return or;
    }

    /**
     * effettua l' operazione bit not di una stringa binaria
     * @param s la stringa di partenza
     * @return la sua negazione
     */
    public static String not(String s){
        String not = "";
        for(int i = 0; i < s.length(); i++){
            if(s.charAt(i) == '1'){
                not += '0';
            }else{
                not += '1';
            }
        }
        return not;
    }

    /**
     * esegue la rotazione binaria verso sinistra di una posizione
     * @param s1 la stringa di partenza
     * @return la stringa ruotata verso sinistra di uno
     */
    public static String ruotaDiUno(String s1){
        String ruotata = "";
        for(int i = 1; i < s1.length(); i++){
            ruotata += s1.charAt(i);
        }
        ruotata += s1.charAt(0);
        return ruotata;
    }

    /**
     * effettua la somma binaria tra due stringhe binarie
     * @param s1 la prima stringa
     * @param s2 la seconda stringa
     * @return la somma binaria delle due stringhe
     */
    public static String sommaBinaria(String s1, String s2){
        if(s1.length() > s2.length()){
            s2 = aggiungiZeroASinistra(s2, s1.length());
        }else if(s2.length() > s1.length()){
            s1 = aggiungiZeroASinistra(s1, s2.length());
        }

        String somma = "";
        int riporto = 0, bit1, bit2;
        for(int i = s1.length() - 1; i >= 0; i--){
            bit1 = Integer.parseInt("" + s1.charAt(i));
            bit2 = Integer.parseInt("" + s2.charAt(i));
            if(bit1 + bit2 + riporto == 2){
                somma += 0;
                riporto = 1;
            }else if(bit1 + bit2 + riporto == 3){
                somma += 1;
                riporto = 1;
            }
            else {
                somma += (bit1 + bit2 + riporto);
                riporto = 0;
            }
        }
        if(riporto != 0){
            somma += riporto;
        }

        return invertiStringa(somma);
    }

    /**
     * inverte le lettere della stringa in input
     * @param s la stringa di partenza
     * @return la stringa invertita
     */
    private static String invertiStringa(String s){
        String invertita = "";
        for(int i = s.length() - 1; i >= 0; i--){
            invertita += s.charAt(i);
        }

        return invertita;
    }

    /**
     * tronca una stringa alla lunghezza indicata
     * @param s1 la stringa di partenza
     * @param lunghezza la lunghezza a cui troncare
     * @return la stringa troncata alla lunghezza richiesta
     */
    public static String tronca(String s1, int lunghezza){
        String troncata = "";
        for(int i = 0; i < lunghezza; i++){
            troncata += s1.charAt(i);
        }
        return troncata;
    }

    /**
     * esegue la rotazione binaria verso sinistra di un numero n di posizioni
     * @param s1 la stringa di partenza
     * @param nVolte il numero di volte che è necessario effettuare la rotazione
     * @return la stringa ruotata risultante
     */
    public static String ruotaASinistra(String s1, int nVolte){
        String ruotata = s1;
        for(int i = 0; i < nVolte; i++){
            ruotata = ruotaDiUno(ruotata);
        }
        return ruotata;
    }
}
