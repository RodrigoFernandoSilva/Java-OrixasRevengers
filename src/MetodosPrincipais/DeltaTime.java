/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MetodosPrincipais;

/**
 *
 * @author Rodrigo Fernando da Silva
 */
public class DeltaTime extends Thread{
    public static float deltaTime = 0;
    public static long dormirThread = 16;
    public static int dormir;
    
    //Qual é o meu fps maixmo que eu quero no jogo
    public static int fps = 0;
    public void run() {
        int tempFps = 0;
        
        long segundos = 0, segundosPassado = 0;
        
        long ultimoTempo = System.nanoTime(); //Retorna quanto nanos segundos o seu computador esta sendo executado
        long tempoAgora;
        long diferencaTempo;
        
        final int TARGET_FPS = 60;
        //O computador chama o tick(), então ele aguarda um tempo até chamar ele novamente,
        //esse é o calculo que é feito para saber quando tempo demora para chamar ele noamente.
        //Varia de acordo com o FPS setado la em cima, quando menos, maior o tempo.
        final long OPTIMAL_TIME = 1000000000 / TARGET_FPS; //1 segundo é igual a 1000000000 nanossegundos
        //O final acima transforma o nanossegundos em segundos
        
        segundosPassado = System.currentTimeMillis();
        
        double i;
        while(Executando.executando){
            tempoAgora = System.nanoTime();
            diferencaTempo = tempoAgora - ultimoTempo;
            ultimoTempo  = tempoAgora;
            dormirThread = (ultimoTempo-System.nanoTime() + OPTIMAL_TIME)/1000000;
            deltaTime = ((float)diferencaTempo) / 1000000000;
            
            i = (0.016 - deltaTime);
            i = 16 - (16 * i);
            i = (int) i;
            
            dormir = (int) i;
            
            if(dormirThread <= 0){
                dormirThread = 1;
            }
            try{
                sleep(dormirThread);
            } catch(InterruptedException ex){
                //Deu ruim!!!
            }
            
            
            //segundos += System.currentTimeMillis() / 1000000000000L;
            tempFps++;
            
            if(System.currentTimeMillis() - segundosPassado > 1000){
                segundosPassado = System.currentTimeMillis();
                segundos ++;
            }
            
           // System.out.println(segundos);
            
            /*
            if(segundos >= 60){
                fps = tempFps;
                tempFps = 0;
                segundos = 0;
            }*/
            
        }
    }
}
