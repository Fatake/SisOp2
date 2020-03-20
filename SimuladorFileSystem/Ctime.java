import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.*;


public class ctime{
	public static final String PROGRAM_NAME = "ctime" ;
	public static void main(String[] argv) throws Exception{
		Kernel.initialize();
		if(argv.length == 0){
			System.err.println( PROGRAM_NAME + ": usage: java " + PROGRAM_NAME + " input-file ------ NO HAY NINGUN ARGUMENTO" ) ;
			Kernel.exit( 1 ) ;
		}
	
        for( int i = 0 ; i < argv.length ; i ++ ){
            try{
                System.out.println("ctime");
                System.out.println(Files.getAttribute(new File(argv[i]).toPath(),"unix:ctime"));
                System.out.println(new Date(((FileTime)Files.getAttribute(new File(argv[i]).toPath(),"unix:ctime")).toMillis()));
            }catch (Exception e){
                e.printStackTrace();
            }

    }
	}
}
