/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algoritmo.rsa;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author Kassio San
 */
public class AlgoritmoRSA {

    public static void main(String[] args) {
        //entrada da palavra
        String alphabet = "@abcdefghijklmnopqrstuvwxyz" ;
        
        String word = new Scanner(System.in).next();
        char[] charWord = new char[word.length()]; 
        charWord = word.toCharArray();
        BigInteger[] wordNumbers = new BigInteger[word.length()];
        
        for(int i= 0; i<word.length(); i++){        
//            System.out.println("posição da letra: "+charWord[i]+" = "+alphabet.indexOf(charWord[i]));
            wordNumbers[i] = BigInteger.valueOf(alphabet.indexOf(charWord[i]));
        }
        //transforma palavra em sequencia de números
        for (int i = 0; i < wordNumbers.length; i++) {
            System.out.print(wordNumbers[i]);
        }
        System.out.println("");
                         
        //Escolher dois números primos p1 e p2 (De 15 a 40);
        int p1, p2 ;
        p1 = 17 ;
        p2 = 19 ;
        //Multiplicar os dois números primos. n = (p1 * p2);
        BigInteger n = BigInteger.valueOf(p1*p2) ;
        
        //Achar o φ (fi de Euler). φ(n) = (p1 - 1) * (p2 - 1);
        long fiEuler = (p1-1)*(p2-1); 
        System.out.println("FI de Euler: "+fiEuler);
        //Encontrar através dos números co-primos selecionado 
        //entre 1 e o φ (fi de Euler), tal que 1 < e < φ;
        ArrayList<Integer> mdcFiEuler = new ArrayList();
        for(int i= 1; i<fiEuler+1; i++){
            if(fiEuler%i==0){
                mdcFiEuler.add(i);
            }
        }
//        System.out.print("mdc fiEuler: ");
//        for(int i= 0; i<mdcFiEuler.size(); i++){
//            System.out.print(mdcFiEuler.get(i)+"-");
//        }
//        System.out.println("");
        ArrayList<Integer> coPrimos = new ArrayList();
        //Co-primos são os numnero que o maximo divisor comum é 'e' entre 1<e<fiDeEuler;
        ArrayList<Integer> mdcNumber = new ArrayList();
        for(int i= 2; i<fiEuler; i++){           
            mdcNumber = toDoMdc(i);
            int cont = 0 ;
            for(int j= 0; j<mdcNumber.size(); j++){
//                System.out.println("mdcNumber: "+mdcNumber.get(j));;
                if(mdcFiEuler.contains(mdcNumber.get(j))){
                    cont++;
                }
            }
            if(cont==1){
                coPrimos.add(mdcNumber.get(1));
//                System.out.println("Co-Primo: "+mdcNumber.get(1));;
            }

            
        } //for  
        //CRIPTOGRAFAR
        //toChoiceNumber(coPrimos)
        BigInteger numberChoiced = BigInteger.valueOf(toChoiceNumber(coPrimos));
        System.out.println("Numero co-primo escolhido: "+numberChoiced);
        ArrayList<BigInteger> wordEncrypted = new ArrayList();
        System.out.print("Palavra Criptograda: ");
        for(int i= 0; i<wordNumbers.length; i++){;
            wordEncrypted.add(toEncrypt(wordNumbers[i], numberChoiced, n));
            System.out.print(wordEncrypted.get(i)+" ");
        }
        
        //GERAR CHAVE PRIVADA
        Long numberChoicedLong = numberChoiced.longValue();
        BigInteger privateKey = BigInteger.valueOf(euclides(numberChoicedLong,fiEuler,1));
        System.out.println("\nChave Privada: "+privateKey);
        
        //DESCRIPTOGRAFAR
        ArrayList<BigInteger> descryptedFinal = new ArrayList();
        System.out.print("Numeros DESCRIPTOGRAFADOS: ");
        for(int i= 0; i<wordEncrypted.size(); i++){
            descryptedFinal.add(descrypt(wordEncrypted.get(i), privateKey, n));
            System.out.print(descryptedFinal.get(i)+" ");
        }
        
        //Tranformar em palavra
        char[] newCharWord = new char[word.length()]; 
        for(int i= 0; i<descryptedFinal.size(); i++){
            int posLetter = (int)descryptedFinal.get(i).longValue();
            newCharWord[i] = alphabet.charAt(posLetter);
        }
        System.out.print("\nPalavra Descriptografada: ");
        for(int i= 0; i<newCharWord.length; i++){
            System.out.print(newCharWord[i]);
        }
        System.out.println("\n");
        
    }//main
   
    public static ArrayList<Integer> toDoMdc(int number){
        ArrayList<Integer> mdcNumber = new ArrayList();
        for(int i= 1; i<number+1; i++){
            if(number%i==0){
                mdcNumber.add(i);
            }
        }
        return mdcNumber ;
    };//todoMDc
    
    public static int toChoiceNumber(ArrayList<Integer> coPrimos){
        Random gerador = new Random();
        
        int numberChoiced = coPrimos.get(gerador.nextInt(coPrimos.size()));
        return numberChoiced ;
    }//toCohiceNumber
    
    public static BigInteger toEncrypt(BigInteger wordNumber, BigInteger numberChoiced, BigInteger n){
        //c = m^e%n  formula para encriptar
        BigInteger c =  wordNumber.modPow( numberChoiced , n);
        return c ;
    }//toEncrypt  
    
    
    //CHAVE PRIVADA    
    private static long mod(long a, long b) {
        long r = a % b;
        
        if ((r < 0) && (b > 0)) {
            return (b + r);
        }
        
        if ((r > 0) && (b < 0)) {
            return (b + r);
        }
        
        return r;
    }
    
    private static long euclides(long a, long b, long c) {
        long r;
        
        r = mod(b, a);
        
        if (r == 0) {
            return (mod((c / a), (b / a)));
        }
        
        return ((euclides(r, a, -c) * b + c) / (mod(a, b)));
    }
    
    //DESCRIPTOGRAFAR 
    //descrypt(wordEncrypted.get(i), privateKey, n)
    public static BigInteger descrypt(BigInteger c,BigInteger d, BigInteger n){
        //m=(c)^d%n

        BigInteger m = c.modPow(d, n) ;

        return m ;
    }
    
    
}//
