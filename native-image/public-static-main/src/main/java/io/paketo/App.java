package io.paketo;

import io.paketo.CosExample;

public class App 
{
    public static void main( String[] args )
    {
	CosExample cos = new CosExample();
        System.out.println(System.getenv("STR_KEY") +  "Hello!" );
	System.out.println(cos.test());
    }
}
