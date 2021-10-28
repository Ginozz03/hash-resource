package service.md2;

public class MD2 {

    private String fasi;
    private String initialMessage;

    // tabella sostituzione
    private final int[] s = {
            41, 46, 67, 201, 162, 216, 124, 1, 61, 54, 84, 161, 236, 240, 6, 19,
            98, 167, 5, 243, 192, 199, 115, 140, 152, 147, 43, 217, 188, 76, 130, 202,
            30, 155, 87, 60, 253, 212, 224, 22, 103, 66, 111, 24, 138, 23, 229, 18,
            190, 78, 196, 214, 218, 158, 222, 73, 160, 251, 245, 142, 187, 47, 238, 122,
            169, 104, 121, 145, 21, 178, 7, 63, 148, 194, 16, 137, 11, 34, 95, 33,
            128, 127, 93, 154, 90, 144, 50, 39, 53, 62, 204, 231, 191, 247, 151, 3,
            255, 25, 48, 179, 72, 165, 181, 209, 215, 94, 146, 42, 172, 86, 170, 198,
            79, 184, 56, 210, 150, 164, 125, 182, 118, 252, 107, 226, 156, 116, 4, 241,
            69, 157, 112, 89, 100, 113, 135, 32, 134, 91, 207, 101, 230, 45, 168, 2,
            27, 96, 37, 173, 174, 176, 185, 246, 28, 70, 97, 105, 52, 64, 126, 15,
            85, 71, 163, 35, 221, 81, 175, 58, 195, 92, 249, 206, 186, 197, 234, 38,
            44, 83, 13, 110, 133, 40, 132, 9, 211, 223, 205, 244, 65, 129, 77, 82,
            106, 220, 55, 200, 108, 193, 171, 250, 36, 225, 123, 8, 12, 189, 177, 74,
            120, 136, 149, 139, 227, 99, 232, 109, 233, 203, 213, 254, 59, 0, 29, 57,
            242, 239, 183, 14, 102, 88, 208, 228, 166, 119, 114, 248, 235, 117, 75, 10,
            49, 68, 80, 180, 143, 237, 31, 26, 219, 153, 141, 51, 159, 17, 131, 20
    };

    private int[][] checksumMatrix = new int[5][16];
    private int[] checksum = new int[16];
    private int[] digest = new int[48];
    private String[][] msgCheck = new String[6][16];
    private String[] msgFinal = new String[96];

    public MD2(String initialMessage) {

        if (initialMessage.length() > 80) {
            throw new IllegalStateException("Messaggio troppo lungo!");
        }

        fasi = "";

        this.initialMessage = initialMessage;
    }

    public String encrypt() {
        String[][] matrix = new String[5][16];

//        System.out.println("Matrice a interi ASCII");

        chrToInt(matrix);
        String[][] matrixCopied = copyMatrix(matrix);
        msgCheck = chrToInt(msgCheck);

        fasi += "<b>Da matrice a interi ASCII</b><br>";

        printMatrix(matrix);

        System.out.println("Matrice a binario");

        intToBin(matrix);

        fasi += "<b>Da matrice a binario</b><br>";

        printMatrix(matrix);

        System.out.println("Matrice xor");

        xorSurrogate(matrix);

        fasi += "<b>Creazione matrice xor</b><br>";

        printMatrix(matrix);

        System.out.println("Matrix checksum -> ");

        calculateChecksum(matrixCopied);

        fasi += "<b>Creazione matrice checksum</b><br>";

        printMatrix(checksumMatrix);

        System.out.println("Checksum finale -> ");

        fasi += "<b>Checksum finale</b><br>";

        printMatrix(checksum);

        System.out.println("Final message ->");

        calculateFinalMessage(checksum);

        fasi += "<b>Messaggio finale</b><br>";

        printMatrix(msgCheck);

        transformMessageFinal(msgCheck);

        calculateDigest(msgCheck);

        System.out.println("Digest ->");

        fasi += "<b>Digest</b><br>";

        printMatrix(digest);

        String hash = calculateHash(digest);

        return hash;
    }

    /**
     * Calcola hash
     * @param digest Digest
     * @return Hash esadecimale
     */
    private String calculateHash(int[] digest) {

        String val = "";

        for (int i = 0; i < 16; i++) {
            val += Integer.toHexString(digest[i]);
        }

        return val;
    }

    /**
     * Messaggio finale con digest
     * @param msgCheck Messaggio finale
     */
    private void transformMessageFinal(String[][] msgCheck) {
        int counter = 0;

        for (int i = 0; i < msgCheck.length; i++) {
            for (int j = 0; j < msgCheck[i].length; j++) {

                msgFinal[counter] = msgCheck[i][j];

                counter++;
            }
        }
    }

    /**
     * Calcola Digest
     * @param msgCheck Messaggio input
     */
    private void calculateDigest(String[][] msgCheck) {
        for (int i = 0; i < msgCheck.length; i++) {
            for (int j = 0; j < msgCheck[i].length; j++) {
                digest[msgCheck[i].length+j] = Integer.parseInt(msgFinal[i*msgCheck[i].length+j]);
                digest[2*msgCheck[i].length+j] = (digest[msgCheck[i].length+j] ^ digest[j]);
            }
        }

        int checktmp = 0;

        for (int i = 0; i < 18; i++) {
            for (int j = 0; j < 48; j++) {
                checktmp = digest[j] ^ s[checktmp];
                digest[j] = checktmp;
                checktmp = (checktmp+j) % 256;
            }
        }


    }

    /**
     * Messaggio finale
     * @param checksum Messaggio finale
     */
    private void calculateFinalMessage(int[] checksum) {

        for (int i = 0; i < checksum.length; i++) {
            msgCheck[5][i] = String.valueOf(checksum[i]);
        }
    }

    /**
     * Calcola il checksum
     * @param matrix Matrice da elaborare
     */
    private void calculateChecksum(String[][] matrix) {
        for (int i = 0; i < checksumMatrix.length; i++) {
            for (int j = 0; j < checksumMatrix[i].length; j++) {
                if(i == 0){
                    checksumMatrix[i][j] = Integer.parseInt(matrix[i][j]);
                }else{
                    checksumMatrix[i][j] = Integer.parseInt(xor(intToBin(checksumMatrix[i-1][j]), intToBin(Integer.parseInt(matrix[i][j]))), 2);
                }
            }
        }

        for (int i = 0; i < 16; i++) {
            checksum[i] = checksumMatrix[4][i];
        }
    }

    /**
     * Calcola xor con matrice s
     * @param matrix Matrice da calcolare
     * @return Matrice calcolata
     */
    private String[][] xorSurrogate(String[][] matrix) {

        int l = 0;

        int previous = 0;

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if(j == 0 && i == 0){
                    String lBin = String.format("%8s", Integer.toBinaryString(Integer.parseInt(String.valueOf(l))).replaceAll(" ", "0"));
                    if(l == 0){
                        lBin = "00000000";
                    }
                    String xorBin = xor(matrix[i][j], lBin);
                    int indexSub = Integer.parseInt(xorBin, 2);
                    matrix[i][j] = String.valueOf(s[indexSub]);
                    previous = s[indexSub];
                }
                else if( i != 0 && j == 0){
                    String primoFattore = matrix[i][j];
                    String secondoFattore = intToBin(previous);


                    String indexValueEncrypt =  xor(primoFattore, secondoFattore);

                    previous = Integer.parseInt(indexValueEncrypt, 2);

                    matrix[i][j] = String.valueOf(s[Integer.parseInt(indexValueEncrypt, 2)]);
                    previous = Integer.parseInt(matrix[i-1][15]);
                }
                else{
                    // TODO problema andare a capo, i -= 1
                    // TODO separare andare a capo da centro tabella

                    String primoFattore = matrix[i][j];
                    String secondoFattore = intToBin(previous);
                    String indexValueEncrypt =  xor(primoFattore, secondoFattore);

                    previous = Integer.parseInt(indexValueEncrypt, 2);

                    matrix[i][j] = String.valueOf(s[Integer.parseInt(indexValueEncrypt, 2)]);
                    previous = Integer.parseInt(matrix[i][j]);
                }
            }
        }

        return matrix;
    }

    /**
     * Intero a binario
     * @param dec Numero decimale
     * @return Numero binario 8 bit
     */
    private String intToBin(int dec){
        String result= "00000000";
        int i=result.length()-1;
        while(dec!=0)
        {
            char a[]=result.toCharArray();
            a[i--]= String.valueOf(dec%2).charAt(0);
            result=new String(a);
            dec=dec/2;

        }
        return result;
    }

    /**
     * Matrice a binario
     * @param matrix Matrice da trasformare
     * @return Matrice trasformata
     */
    private String[][] intToBin(String[][] matrix) {

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                String currentElement = matrix[i][j];
                StringBuilder stringBuilder = new StringBuilder("");

                stringBuilder.append(
                        String.format("%8s", Integer.toBinaryString(Integer.parseInt(currentElement)))
                                .replaceAll(" ", "0")
                );
                matrix[i][j] = stringBuilder.toString();
            }
        }

        return matrix;
    }

    /**
     * Matrice a intera
     * @param matrix Trasforma la matrice in interi
     * @return Matrice trasformata
     */
    private String[][] chrToInt(String[][] matrix) {

        int counterChar = 0;

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (initialMessage.length() > counterChar) {
                    matrix[i][j] = String.valueOf(Integer.valueOf(initialMessage.charAt(counterChar)));
                } else {
                    matrix[i][j] = "15";
                }
                counterChar++;
            }
        }

        return matrix;
    }

    /**
     * Stampa la matrice
     * @param matrix Matrice da stampare
     */
    private void printMatrix(String[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
//                System.out.print(matrix[i][j] + " ");
                fasi += matrix[i][j] + " ";
            }
            fasi += "<br>";
//            System.out.println();
        }
    }

    /**
     * Stampa la matrice
     * @param matrix Matrice da stampare
     */
    private void printMatrix(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
//                System.out.print(matrix[i][j] + " ");
                fasi += matrix[i][j] + " ";
            }
            fasi += "<br>";
//            System.out.println("");
        }
    }

    /**
     * Stampa l'array
     * @param array Array da stampare
     */
    private void printMatrix(int[] array) {
        for (int i = 0; i < array.length; i++) {
            fasi += array[i] + " ";
//            System.out.print(array[i] + " ");
        }
        fasi += "<br>";
//        System.out.println("");
    }

    /**
     * Effettua l' operazione bit xor tra due stringhe binarie
     * @param s1 La prima stringa
     * @param s2 La seconda stringa
     * @return La stringa risultante dallo xor binario
     */
    private static String xor(String s1, String s2) {
        String xor = "";
        for (int i = 0; i < s1.length(); i++) {
            if ((s1.charAt(i) == '0' && s2.charAt(i) == '1') || (s1.charAt(i) == '1' && s2.charAt(i) == '0')) {
                xor += '1';
            } else {
                xor += '0';
            }
        }
        return xor;
    }

    public String getFasi(){
        return fasi;
    }
    
    /** Copia la matrice per utilizzarla nel checksum
     *
     * */
    private static String[][] copyMatrix(String[][] matrix){
        String[][] copyMatrix = new String[5][16];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
               copyMatrix[i][j] = matrix[i][j];
            }
        }
        return copyMatrix;
    }
}
