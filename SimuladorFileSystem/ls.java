import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public class ls
{
  
  public static String PROGRAM_NAME = "ls" ;
  public static void main( String[] args ) throws Exception
  {
    // initialize the file system simulator kernel
    Kernel.initialize() ;

    // for each path-name given
    for( int i = 0 ; i < args.length ; i ++ )
    {
      String name = args[i] ; 
      int status = 0 ;
      atime(name);
      ctime(name);
      mtime(name);
      // stat the name to get information about the file or directory
      Stat stat = new Stat() ; 
      status = Kernel.stat( name , stat ) ;
      if( status < 0 )
      {
        Kernel.perror( PROGRAM_NAME ) ;
        Kernel.exit( 1 ) ;
      }

      // mask the file type from the mode
      short type = (short)( stat.getMode() & Kernel.S_IFMT ) ;

      // if name is a regular file, print the info
      if( type == Kernel.S_IFREG )
      {
        print( name , stat ) ;
	//atime(name);
      }
   
      // if name is a directory open it and read the contents
      else if( type == Kernel.S_IFDIR )
      {
        // open the directory
        int fd = Kernel.open( name , Kernel.O_RDONLY ) ;
        if( fd < 0 )
        {
          Kernel.perror( PROGRAM_NAME ) ;
          System.err.println( PROGRAM_NAME + 
            ": unable to open \"" + name + "\" for reading" ) ;
          Kernel.exit(1) ;
        }

        // print a heading for this directory
        System.out.println() ;
        System.out.println( name + ":" ) ;

        // create a directory entry structure to hold data as we read
        DirectoryEntry directoryEntry = new DirectoryEntry() ;
        int count = 0 ;

        // while we can read, print the information on each entry
        while( true ) 
        {
          // read an entry; quit loop if error or nothing read
          status = Kernel.readdir( fd , directoryEntry ) ;
          if( status <= 0 )
            break ;

          // get the name from the entry
          String entryName = directoryEntry.getName() ;

          // call stat() to get info about the file
          status = Kernel.stat( name + "/" + entryName , stat ) ;
          if( status < 0 )
          {
            Kernel.perror( PROGRAM_NAME ) ;
            Kernel.exit( 1 ) ;
          }

          // print the entry information
          print( entryName , stat ) ;
          count ++ ;
        }

        // check to see if our last read failed
        if( status < 0 )
        {
          Kernel.perror( "main" ) ;
          System.err.println( "main: unable to read directory entry from /" ) ;
          Kernel.exit(2) ;
        }

        // close the directory
        Kernel.close( fd ) ;

        // print a footing for this directory
        System.out.println( "total files: " + count ) ;
       atime(name);
       ctime(name);
       mtime(name);
      }
    }

    // exit with success if we process all the arguments
    Kernel.exit( 0 ) ;
  }

  /**
   * Print a listing for a particular file.
   * This is a convenience method.
   * @param name the name to print
   * @param stat the stat containing the file's information
   */
  private static void print( String name , Stat stat )
  {
    // a buffer to fill with a line of output
    StringBuffer s = new StringBuffer() ;

    // a temporary string
    String t = null ;

    // append the inode number in a field of 5
    t = Integer.toString( stat.getIno() ) ;
    for( int i = 0 ; i < 5 - t.length() ; i ++ )
      s.append( ' ' ) ;
    s.append( t ) ;
    s.append( ' ' ) ;

    // append the size in a field of 10
    t = Integer.toString( stat.getSize() ) ;
    for( int i = 0 ; i < 10 - t.length() ; i ++ )
      s.append( ' ' ) ;
    s.append( t ) ;
    s.append( ' ' ) ;

    // append the name
    s.append( name ) ;

    // print the buffer
    System.out.println( s.toString() ) ;
  }


private static void atime(String fileName)throws IOException{

        //String fileName = args[0];
        File myfile = new File(fileName);
        Path path = myfile.toPath();
        BasicFileAttributes fatr = Files.readAttributes (path, BasicFileAttributes.class);
        System.out.printf("Last access time: %s%n", fatr.lastAccessTime());        
}

private static void ctime(String fileName)throws IOException {
        
  
        File myfile = new File(fileName);
        Path path = myfile.toPath();
        BasicFileAttributes fatr = Files.readAttributes(path, BasicFileAttributes.class);  
        System.out.printf("File creation time: %s%n", fatr.creationTime());
    }

private static void mtime (String fileName)throws IOException {

        File myfile = new File(fileName);
        Path path = myfile.toPath();
        BasicFileAttributes fatr = Files.readAttributes(path,BasicFileAttributes.class);
        System.out.printf("Last modification time: %s%n", fatr.lastModifiedTime());
    }

}
