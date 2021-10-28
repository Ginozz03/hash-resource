package service.sha1;

import operazioniArray.OperazioniArray;
import operazioniBit.OperazioniBit;

public class SHA1 {
    // costanti utilizzate all'interno del metodo scorriParole
    private static String h0, h1, h2, h3, h4;
    private static String fasi;

    /**
     * metodo principale SHA-1, richiama tutte le sottofunzioni
     * @param messaggio la stringa di input
     * @return la stringa esadecimale finale
     */
    public static String converti(String messaggio){
        fasi = "<b>Fasi:</b><br>";
        int[] messaggioAscii = messaggioToAscii(messaggio);
        fasi += "<b>Messaggio ASCII</b><br>";
        fasi += stampaArray(messaggioAscii);
        fasi += "<br>---------------------<br>";
        String[] messaggioBinario = OperazioniBit.asciiToBinario(messaggioAscii);
        fasi += "<b>Messaggio binario</b><br>";
        fasi += stampaArray(messaggioBinario);
        fasi += "<br>---------------------<br>";
        String stringaBit = concatenaEAggiungiUno(messaggioBinario);
        fasi += "<b>Stringa concatenata e con 1 alla fine</b><br>";
        fasi += stringaBit;
        fasi += "<br>---------------------<br>";
        stringaBit = divisione512mod448(stringaBit);
        fasi += "<b>Stringa che soddisfa la condizione lunghezza % 512 == 448</b><br>";
        fasi += stringaBit;
        fasi += "<br>---------------------<br>";
        stringaBit += stringaLunghezza(messaggioBinario);
        fasi += "<b>Stringa concatenata con la sua lunghezza convertita in binario a 64 bit</b><br>";
        fasi += stringaBit;
        fasi += "<br>---------------------<br>";
        String[] blocchi = OperazioniArray.dividiInGruppi(stringaBit, 512);
        fasi += "<b>Stringa suddivisa in blocchi da 512 bit</b><br>";
        fasi += stampaArray(blocchi);
        fasi += "<br>---------------------<br>";
        String[][] parole = OperazioniArray.dividiInParole(blocchi);
        fasi += "<b>Blocchi suddivisi in parole da 32 bit</b><br>";
        fasi += stampaMatrice(parole);
        fasi += "<br>---------------------<br>";
        String[][] parole80 = parole80(parole);
        fasi += "<b>Aggiunta di stringhe binarie generate per arrivare a 80 parole per blocco</b><br>";
        fasi += stampaMatrice(parole80);
        fasi += "<br>---------------------<br>";
        scorriParole(parole80);
        fasi += "<b>Valori delle variabili h al termine dello scorrere della matrice di parole </b><br>";
        fasi += "h0: " + h0 + "<br>";
        fasi += "h1: " + h1 + "<br>";
        fasi += "h2: " + h2 + "<br>";
        fasi += "h3: " + h3 + "<br>";
        fasi += "h4: " + h4 ;
        fasi += "<br>---------------------<br>";
        fasi += "<b>Conversione in esadecimale</b>";
        return creaStringaEsadecimale();
    }

    /**
     * ritorna la stringa delle fasi dell'algoritmo
     * @return la stringa delle fasi
     */
    public static String getFasi() {
        return fasi;
    }

    /**
     * stampa un'array di interi
     * @param array l'array di interi
     * @return la stringa rappresentante l'array
     */
    private static String stampaArray(int[] array){
        String arrayStringa = "[";
        for(int i = 0; i < array.length; i++){
            arrayStringa += array[i];
            if(i < array.length - 1){
                arrayStringa += ", ";
            }
        }

        return arrayStringa + ']';
    }

    /**
     * stampa un'array di stringhe
     * @param array l'array di stringhe
     * @return la stringa rappresentante l'array
     */
    private static String stampaArray(String[] array){
        String arrayStringa = "[";
        for(int i = 0; i < array.length; i++){
            arrayStringa += array[i];
            if(i < array.length - 1){
                arrayStringa += ", ";
            }
        }

        return arrayStringa + ']';
    }

    /**
     * stampa una matrice di stringhe
     * @param matrice la matrice di stringhe
     * @return la stringa rappresentante la matrice
     */
    private static String stampaMatrice(String[][] matrice){
        String matriceStringa = "[";
        for(int i = 0; i < matrice.length; i++){
            matriceStringa += stampaArray(matrice[i]);
            if(i < matrice.length - 1){
                matriceStringa += ", ";
            }
        }

        return matriceStringa + ']';
    }

    /**
     * crea un array contentente i numeri ascii del messaggio
     * @param messaggio il messaggio in input
     * @return un'array con i valori ascii dei vari caratteri
     */
    private static int[] messaggioToAscii(String messaggio){
        int[] messaggioAscii = new int[messaggio.length()] ;
        for(int i = 0; i < messaggio.length(); i++){
            messaggioAscii[i] = messaggio.charAt(i);
        }

        return messaggioAscii;
    }

    /**
     * concatena i caratteri binari e alla fine concatena un ulteriore 1
     * @param messaggioBinario l'array di stringhe binarie
     * @return la stringa concatenata con 1 alla fine
     */
    private static String concatenaEAggiungiUno(String[] messaggioBinario){
        String messaggioConcatenato = "";
        for(int i = 0; i < messaggioBinario.length; i++){
            messaggioConcatenato += messaggioBinario[i];
        }
        messaggioConcatenato += '1';
        return messaggioConcatenato;
    }

    /**
     * aggiunge zero alla fine della stringa di bit per fare in modo che la lunghezza
     * della stringa divisa per 512 dia come risultato 448
     * @param messaggioConcatenato la stringa binarie concatenata
     * @return la stringa la cui lunghezza mod 512 dà 448
     */
    private static String divisione512mod448(String messaggioConcatenato){
        while ((messaggioConcatenato.length() % 512) != 448){
            messaggioConcatenato += '0';
        }
        return messaggioConcatenato;
    }

    /**
     * genera una stringa a partire dalla lunghezza del messaggio binario, la lunghezza viene poi
     * convertita in binario e vengono aggiunti zero a sinistra per renderla di lunghezza 64 bit
     * @param messaggioBinario l'array di stringhe binarie
     * @return la stringa binaria di 64 bit che rappresenta la lunghezza del messaggio
     */
    private static String stringaLunghezza(String[] messaggioBinario){
        int lunghezza = 8 * messaggioBinario.length;
        String lunghezzaInBinario = OperazioniBit.numeroToBinario(lunghezza);
        String stringaLunghezza = OperazioniBit.aggiungiZeroASinistra(lunghezzaInBinario, 64);
        return stringaLunghezza;
    }

    /**
     * aumenta il numero di gruppi di parole da 32 bit a 80 parole per ogni blocco
     * @param parole la matrice di blocchi suddivisi in parole binarie
     * @return una matrice che per ogni blocco possiede 80 parole binarie
     */
    private static String[][] parole80(String[][] parole){
        String[][] parole80 = new String[parole.length][80];
        for(int i = 0; i < parole.length; i++){
            for(int j = 0; j < parole[i].length; j++){
                parole80[i][j] = parole[i][j];
            }
        }
        String parolaA, parolaB, parolaC, parolaD, nuovaParola;
        String xorA, xorB, xorC;
        for(int i = 0; i < parole80.length; i++){
            for(int j = 16; j <= 79; j++){
                parolaA = parole80[i][j - 3];
                parolaB = parole80[i][j - 8];
                parolaC = parole80[i][j - 14];
                parolaD = parole80[i][j - 16];

                xorA = OperazioniBit.xor(parolaA, parolaB);
                xorB = OperazioniBit.xor(xorA, parolaC);
                xorC = OperazioniBit.xor(xorB, parolaD);

                nuovaParola = OperazioniBit.ruotaASinistra(xorC, 1);
                parole80[i] = OperazioniArray.push(parole80[i], nuovaParola);
            }
        }
        return parole80;
    }

    /**
     * cambia i valori delle costanti h definite all'inizio della classe
     * scorrendo la matrice parole effettuando operazioni sui bit
     * @param parole la matrice di blocchi da 80 parole
     */
    private static void scorriParole(String[][] parole){
        h0 = "01100111010001010010001100000001";
        h1 = "11101111110011011010101110001001";
        h2 = "10011000101110101101110011111110";
        h3 = "00010000001100100101010001110110";
        h4 = "11000011110100101110000111110000";
        String a = h0;
        String b = h1;
        String c = h2;
        String d = h3;
        String e = h4;
        String f, k;
        String bAndC, notB, bXorC, bAndD, cAndD, bAndCorBAndD;
        String parola, tempA, tempB, tempC, temp;
        for(int i = 0; i < parole.length; i++){
            for(int j = 0; j < 80; j++){
                if(j < 20){
                    bAndC = OperazioniBit.and(b, c);
                    notB = OperazioniBit.not(b);
                    f = OperazioniBit.or(bAndC, notB);
                    k = "01011010100000100111100110011001";
                } else if(j < 40){
                    bXorC = OperazioniBit.xor(b, c);
                    f = OperazioniBit.xor(bXorC, d);
                    k = "01101110110110011110101110100001";
                } else if(j < 60){
                    bAndC = OperazioniBit.and(b, c);
                    bAndD = OperazioniBit.and(b, d);
                    cAndD = OperazioniBit.and(c, d);
                    bAndCorBAndD = OperazioniBit.or(bAndC, bAndD);
                    f = OperazioniBit.or(bAndCorBAndD, cAndD);
                    k = "10001111000110111011110011011100";
                }else{
                    bXorC = OperazioniBit.xor(b, c);
                    f = OperazioniBit.xor(bXorC, d);
                    k = "11001010011000101100000111010110";
                }

                parola = parole[i][j];
                tempA = OperazioniBit.sommaBinaria(OperazioniBit.ruotaASinistra(a, 5), f);
                tempB = OperazioniBit.sommaBinaria(tempA, e);
                tempC = OperazioniBit.sommaBinaria(tempB, k);
                temp = OperazioniBit.sommaBinaria(tempC, parola);

                temp = OperazioniBit.tronca(temp, 32);
                e = d;
                d = c;
                c = OperazioniBit.ruotaASinistra(b, 30);
                b = a;
                a = temp;
            }
            h0 = OperazioniBit.tronca(OperazioniBit.sommaBinaria(h0, a), 32);
            h1 = OperazioniBit.tronca(OperazioniBit.sommaBinaria(h1, b), 32);
            h2 = OperazioniBit.tronca(OperazioniBit.sommaBinaria(h2, c), 32);
            h3 = OperazioniBit.tronca(OperazioniBit.sommaBinaria(h3, d), 32);
            h4 = OperazioniBit.tronca(OperazioniBit.sommaBinaria(h4, e), 32);
        }
    }

    /**
     * converte i valori delle costanti h in esadecimale e li concatena uno dopo l'altro
     * @return la stringa esadecimale concatenata
     */
    private static String creaStringaEsadecimale(){
        String esadecimale0 = binarioToEsadecimale(h0);
        String esadecimale1 = binarioToEsadecimale(h1);
        String esadecimale2 = binarioToEsadecimale(h2);
        String esadecimale3 = binarioToEsadecimale(h3);
        String esadecimale4 = binarioToEsadecimale(h4);

        fasi += "<br>hex0: " + esadecimale0;
        fasi += "<br>hex1: " + esadecimale1;
        fasi += "<br>hex2: " + esadecimale2;
        fasi += "<br>hex3: " + esadecimale3;
        fasi += "<br>hex4: " + esadecimale4;

        return esadecimale0 + esadecimale1 + esadecimale2 + esadecimale3 + esadecimale4;
    }

    /**
     * converte una stringa binaria di lunghezza 32 in esadecimale
     * @param testoBinario la stringa binaria
     * @return la stringa esadecimale
     */
    private static String binarioToEsadecimale(String testoBinario){
        String esadecimale = "";
        String sottoStringaBinaria;
        for(int i = 0; i < testoBinario.length(); i+= 4){
            sottoStringaBinaria = testoBinario.substring(i, i + 4);
            switch (sottoStringaBinaria){
                case "0000":{
                    esadecimale += '0';
                    break;
                }
                case "0001":{
                    esadecimale += '1';
                    break;
                }
                case "0010":{
                    esadecimale += '2';
                    break;
                }
                case "0011":{
                    esadecimale += '3';
                    break;
                }
                case "0100":{
                    esadecimale += '4';
                    break;
                }
                case "0101":{
                    esadecimale += '5';
                    break;
                }
                case "0110":{
                    esadecimale += '6';
                    break;
                }
                case "0111":{
                    esadecimale += '7';
                    break;
                }
                case "1000":{
                    esadecimale += '8';
                    break;
                }
                case "1001":{
                    esadecimale += '9';
                    break;
                }
                case "1010":{
                    esadecimale += 'a';
                    break;
                }
                case "1011":{
                    esadecimale += 'b';
                    break;
                }
                case "1100":{
                    esadecimale += 'c';
                    break;
                }
                case "1101":{
                    esadecimale += 'd';
                    break;
                }
                case "1110":{
                    esadecimale += 'e';
                    break;
                }
                case "1111":{
                    esadecimale += 'f';
                    break;
                }
            }
        }
        return esadecimale;
    }
}
