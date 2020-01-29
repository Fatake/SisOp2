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

      int i,n=63,contador=0 ;//64 paginas
      Random num= new Random();
      int X;
      boolean pagefault,eliminada=false;
      Page  pagina []= new Page[63];
      
      for(i=0;i<63;i++){
      pagina[i].R=0;
      pagina[i].M=0;
      }
      
      X=num.nextInt(64);
      pagina[X].R=1;
      pagina[X].M=1;
      
      for(i=0;i<n;i++){
         pagina[i].R=0;
         pagina[i].M=0;
      }
            for(i=0;i<n;i++){
         if(X==i){
         contador++;
         }
      }
            if(contador != n){
            pagefault=true;
            for (i=0;i<n;i++){
            while(eliminada != true){
                if( pagina[i].R==0 && pagina[i].M==0){
                    pagina[i]=delete;
                    eliminada=true;
                }
                if( pagina[i].R==0 && pagina[i].M==0){
                    pagina[i]=delete;
                    eliminada=true;
                }
                
            }
            }
            }
      
    }
    
}
