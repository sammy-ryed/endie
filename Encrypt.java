
import java.util.Scanner;

class Converter {

    int toOctal(int n) {
        int x = 1, octalNumber = 0;
        while (n != 0) {
            int rem = n % 8;
            octalNumber += rem * x;
            n /= 8;
            x *= 10;
        }
        return octalNumber;
    }

    int toBinary(int n) {
        int x = 1, binaryNumber = 0;
        while (n != 0) {
            int rem = n % 2;
            binaryNumber += rem * x;
            n /= 2;
            x *= 10;
        }
        return binaryNumber;
    }

    String toHex(int n) {
        return Integer.toString(n, 16);
    }

    int fromBinary(String bin) {
        return Integer.parseInt(bin, 2);
    }

    int fromOctal(String oct) {
        return Integer.parseInt(oct, 8);
    }

    int fromHex(String hex) {
        return Integer.parseInt(hex, 16);
    }
}

class Encode{
    private char message[];
    private int length;
    private String []encoded;
    private Converter converter;

    public Encode(String text) {
        this.length=text.length();
        this.message= text.toCharArray();
        this.encoded=new String[length];
        this.converter  = new Converter();
    }

    String encode(){
        for(int x=0;x<length;x++){
            char ch = message[x];
            int code = ch;
            if(Character.isWhitespace(ch))
                {encoded[x]=Integer.toString(converter.toOctal(code));
                continue;}
            
            if(code<=99){
                encoded[x]=Integer.toString(converter.toBinary(code));
                continue;
            }
            if(code>=100){
                encoded[x]= (converter.toHex(code));
            }
        }

        return String.join(">", encoded);
    }
    
}

class Decrypt{
    private String message[];
    private char  decoded[];
    private Converter converter;
    private int length;

    public Decrypt(String text) {
        this.length=(text.length() - text.replace(">", "").length())+1;
        this.message=text.split(">");
        converter = new Converter();
        decoded = new char[length];
    }


    boolean bicheck(String text){
        return text.matches("[01]+"); 
    }

    String decode(){
        for (int x =0; x<length;x++){
            String ch= message[x];
            if(ch.equalsIgnoreCase("40")){
                decoded[x]=' ';
                continue;
            }

            if(bicheck(ch)){
                decoded[x]=(char)(converter.fromBinary(ch));
            }
            else{
                decoded[x]=(char)converter.fromHex(ch);
            }
    
        } 
        return String.valueOf(decoded);
    }

    

}

public class Encrypt{
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your choice: \n1. Encrypt\n2. Decrypt");
        int condition = sc.nextInt();
        sc.nextLine();

        if (condition==1)
        {
            System.err.println("Enter String to be encrypted:");
            String encode=sc.nextLine();
            Encode encoder = new Encode(encode);
            System.out.println(encoder.encode());
        }

        else if(condition==2){
            System.err.println("Enter String to be decrypted: ");
            String decode=sc.nextLine();
            Decrypt decoder = new Decrypt(decode);
            System.out.println(decoder.decode());
        }
        else{
            System.out.println("Wrong option! Try Again!");
        }


    }
}