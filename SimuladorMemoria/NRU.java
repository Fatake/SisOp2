package nru;
import java.util.Random;
/*
 * ALGORITMO DE REEMPLAZO NRU
 * Este algoritmo se guía con los bits de referencia 
 * y modificacion.
 * Entrada;N
 * Salida: 
 * Inicio---
 * Todas la paginas no estan referenciadas ni modificadas
 * Se elige una pagina de acuerdo a un tiempo X (Utilizar Random)
 * Se accede a la pagina
 * Se modifica a 1 el bit de referencia o el de modificación 
 * El SO apaga todos los bits de referencia para saber cuales han sido ocupados
 * Se vuelve a hacer uso del avantum de tiempo para solicitar una nueva pagina
 * En caso de que la pagina no esté presente provocara un fallo de pagina (page fault)
 * Se aplica el algoritmo NRU utilizandi las 4 categorias
 * Se desaloja una página de la categoría más baja 
 * (una buena opción son las páginas que estan ubicadas en la categoria 0)
 */
public class NRU {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args, Page delete) {
        int X;//Valos Aleatorio que genera el numero entre 0-63
        int n = 63;
        int contador = 0 ;//64 paginas
        Random num = new Random();
        boolean pagefault;
        boolean eliminada = false;
        Page  pagina [] = new Page[63];

        for(int i=0; i < n ; i++){//inicializar todo en 0 
            pagina[i].R=0;//Bit de referencia = 0
            pagina[i].M=0;//Bit de Modificado = 0
        }

        //Mientras no exista un falo de pagina
        do {
            X = num.nextInt(64); //Se selecicona una pagina al azar
            pagina[X].M = 1;// y modificado 
            
            //Fallo de pagina
            if(X > n){//Si el X es > al numero de paginas
                pagefault = true;//bandera se actiVA
                for (int i=0;i<n;i++){//Para cada pagina
                    /*
                        busca una pagina segun el algoritmo
                        que cumpla con la tabla de prioridades de abajo

                        R   M
                        0   0  mas prioridad
                        0   1
                        1   0
                        1   1  menos prioridad
                    */
                    do{//Mientras no se elimine
                        if( pagina[i].R == 0 && pagina[i].M == 0){//Prioridad mas alta
                            pagina[i] = delete;
                            eliminada = true;
                        }
                        if( pagina[i].R == 0 && pagina[i].M == 1){
                            pagina[i] = delete;
                            eliminada = true;
                        }
                        if( pagina[i].R == 1 && pagina[i].M == 0){
                            pagina[i] = delete;
                            eliminada = true;
                        }
                        if( pagina[i].R == 1 && pagina[i].M == 1){//Prioridad mas Baja
                            pagina[i] = delete;
                            eliminada = true;
                        }
                    }while(eliminada != true);
                    if (eliminada)
                        break;
                    
                }
            }
        } while (eliminada);
    }
    
}
