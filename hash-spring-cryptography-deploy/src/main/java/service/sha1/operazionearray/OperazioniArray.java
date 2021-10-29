package service.sha1.operazionearray;

public class OperazioniArray {
    /**
     * divide una stringa in un' aray di blocchi della dimensione specificata
     * @param stringa la stringa di partenza
     * @param dimensioneBlocco la dimensione di ogni blocco
     * @return un'array di blocchi di lunghezza dimensioneBlocco
     */
    public static String[] dividiInGruppi(String stringa, int dimensioneBlocco){
        String[] blocchi = new String[stringa.length() / dimensioneBlocco];
        //inizializzo le stringhe nell' array
        for(int i = 0; i < blocchi.length; i++){
            blocchi[i] = "";
        }

        int j = 0, h = 0;
        for(int i = 0; i < stringa.length(); i++){
            if(j < dimensioneBlocco){
                blocchi[h] += stringa.charAt(i);
            }else {
                j = 0;
                h++;
                blocchi[h] += stringa.charAt(i);
            }
            j++;
        }

        return blocchi;
    }

    /**
     * suddivide l' array di blocchi in parole, ossia ogni blocco viene diviso in gruppi da 32 bit
     * @param blocchi l'array di blocchi di stringhe binarie
     * @return l'array suddiviso in sotto-array di parole da 32 bit di lunghezza
     */
    public static String[][] dividiInParole(String[] blocchi){
        String[][] parole = new String[blocchi.length][blocchi[0].length() / 32];
        for(int i = 0; i < blocchi.length; i++){
            parole[i] = dividiInGruppi(blocchi[i], 32);
        }
        return parole;
    }

    /**
     * inserisce l' elemento all' interno dell'array
     * @param array l'array di partenza
     * @param elemento l'elemento da inserire
     * @return l'array con l'elemento inserito alla fine
     */
    public static String[] push(String[] array, String elemento){
        String[] inserito = new String[array.length];
        int i = 0;
        while (array[i] != null && i < array.length){
            inserito[i] = array[i];
            i++;
        }
        inserito[i] = elemento;
        return inserito;
    }
}
