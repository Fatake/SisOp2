import java.util.*;
import java.io.*;


public class mtime{
	
	public static final String PROGRAM_NAME = "mtime" ;
	
	public static void main(String[] argv) throws Exception{
		
		Kernel.initialize();
		
		if(argv.length==0){
			System.err.println( PROGRAM_NAME + ": usage: java " + PROGRAM_NAME + " input-file ------ NO HAY NINGUN ARGUMENTO" ) ;
			Kernel.exit( 1 ) ;
		}
		
		
	for( int i = 0 ; i < argv.length ; i ++ ){
		File name = new File(argv[i]);
		long ms = name.lastModified();
		
		System.out.println(ms);
		
		Date d = new Date(ms);
		System.out.println(d);
		Calendar c = new GregorianCalendar(); 
		c.setTime(d);
		
		String dia = Integer.toString(c.get(Calendar.DATE));
		String mes = Integer.toString(c.get(Calendar.MONTH)+1);
		String annio = Integer.toString(c.get(Calendar.YEAR));
		String hora = Integer.toString(c.get(Calendar.HOUR_OF_DAY));
		String minuto = Integer.toString(c.get(Calendar.MINUTE));
		String segundo = Integer.toString(c.get(Calendar.SECOND));
		
		System.out.println("Ultima fecha de modificacion del archivo: " + argv[i]);
		System.out.println("DIA:" + dia +
						   "  MES:" + mes +
						   "  AÑO:" + annio + 
						   "  HORA:" + hora + 
						   "  MINUTO:" + minuto + 
						   "  SEGUNDO:" + segundo);
    }
		Calendar calendar = Calendar.getInstance();
		Date date = calendar.getTime();
		System.out.println(date);
		
	}
}
