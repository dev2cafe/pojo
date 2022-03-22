package io.paketo;

import io.paketo.CosExample;
import java.io.*;

public class App 
{
    public static void main( String[] args )
    {
	CosExample cos = new CosExample();
        System.out.println(System.getenv("STR_KEY") +  "Hello!" );
	System.out.println(cos.test());

	System.out.println("Enter into Sleep");
 
        try {
            Thread.sleep(600 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
 
        System.out.println("Exit from Sleep");

	String s = null;

        try {
		Process p = Runtime.getRuntime().exec("ping -c 4 8.8.8.8");
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
		while((s=stdInput.readLine()) != null) {
			System.out.println(s);
		}
		System.exit(0);
	}catch(IOException e){
		e.printStackTrace();
		System.exit(-1);
	}




    }
}
