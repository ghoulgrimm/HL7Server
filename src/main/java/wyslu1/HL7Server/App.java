package wyslu1.HL7Server;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);
        System.out.print("Gib was ein: ");
        String eingabe = br.readLine();
        System.out.println("Du hast " + eingabe + " eingegeben.");
    }
}
