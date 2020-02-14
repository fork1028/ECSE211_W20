package ca.mcgill.ecse211.project;

import lejos.hardware.Button;
import lejos.hardware.Sound;
/**
 * The music class stores all the frequencies and the times for the notes on a music sheet and plays static tunes
 * 
 * @author Xinyue Chen (260830761)
 * @author Zheng Yu Cui (260808525)
 *
 */
public class Music {
    
    //frequencies
        private static final int B3 = 247;
        private static final int C4 = 262;
        private static final int Cs4 = 277;
        private static final int D4 = 294;
        private static final int Ds4 = 311;
        private static final int E4 = 330;
        private static final int F4 = 349;
        private static final int Fs4 = 370;
        private static final int G4 = 392;
        private static final int Gs4 = 415;
        private static final int A4 = 440;
        private static final int As4 = 466;
        private static final int B4 = 494;
        private static final int C5 = 523;
        private static final int Cs5 = 554;
        private static final int D5 = 587;
        private static final int Ds5 = 622;
        private static final int E5 = 659;
        private static final int F5 = 698;
        private static final int Fs5 = 740;
        private static final int G5 = 784;
        private static final int Gs5 = 831;
        private static final int A5 = 880;
        private static final int As5 = 932;
        private static final int B5 = 988;
        
        private static final int D6 = 1175;
        
        //length
        private static final int blanche = 600;
        private static final int noir = 300;
        private static final int demi = 150;
        private static final int demi_p = 225;
        private static final int quart = 75;
        private static final int huit = 46;
        
        private static final int tempo = 100;
        
        /**
         * Plays the 2ksquad youtube channel intro
         */
        public static void twokay(){
          (new Thread() {
            public void run() {
              Sound.playTone(E4, demi);
              Sound.playTone(B4, demi);
              Sound.playTone(E5, demi);
              Sound.playTone(G5, noir);
              Sound.playTone(B5, demi);
              Sound.playTone(A5, demi);
              Sound.playTone(D6, demi);
              
              Sound.playTone(D4, demi);
              Sound.playTone(D5, demi);
              Sound.playTone(Fs5, demi);
              Sound.playTone(G5, demi);
              Sound.playTone(D6, demi);
              Sound.playTone(B5, demi);
              Sound.playTone(A5, demi);
              Sound.playTone(G5, demi);
              
              Sound.playTone(B3, demi);
              Sound.playTone(B4, demi);
              Sound.playTone(D5, demi);
              Sound.playTone(Fs5, demi);
              Sound.playTone(G5, demi);
              Sound.playTone(Fs5, demi);
              Sound.playTone(D5, demi);
              Sound.playTone(E5, demi);
              
              Sound.playTone(C4, demi);
              Sound.playTone(E4, demi);
              Sound.playTone(B4, demi);
              Sound.playTone(E5, demi);
              Sound.playTone(B5, demi);
              Sound.playTone(A5, demi);
              Sound.playTone(G5, demi);
              Sound.playTone(E5, demi);
            }
          }).start();
          
            
        }
}
