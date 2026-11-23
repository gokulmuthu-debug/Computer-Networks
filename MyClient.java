import java.net.*;
import java.rmi.*;
import java.util.*;
import java.io.*;
public class MyClient
{
	public static void main(String[] arg)
	{
		try 	
		{
		Scanner in=new Scanner(System.in);
		System.out.print("Enter the Machine name: ");
		String mname=in.nextLine();
		String sName = "rmi://"+mname+"/RMServer";
		String dummy;
		MyServerIntf asif = (MyServerIntf)Naming.lookup(sName);  // requesting remote objects on 							            // the server
			
		int a=0, b=0, choice=0; 
		do
		{
			System.out.println("The Choices Are:");
			System.out.println("1. Add");
			System.out.println("2. Sub");
			System.out.println("3. Mul");
			System.out.println("4. Div");
			System.out.println("5. Fact");
			System.out.println("6. Exit");
			System.out.print("Enter Your Choice: "); choice=in.nextInt(); dummy=in.nextLine();
			switch(choice)
			{
				case 1:
					System.out.print("Enter a: "); a=in.nextInt(); dummy=in.nextLine();
					System.out.print("Enter b: "); b=in.nextInt(); dummy=in.nextLine();
					System.out.println("Addition: "+asif.add(a,b));
					break;
				case 2:
					System.out.print("Enter a: "); a=in.nextInt(); dummy=in.nextLine();
					System.out.print("Enter b: "); b=in.nextInt(); dummy=in.nextLine();
					System.out.println("Subtraction: "+asif.sub(a,b));
					break;
				case 3:
					System.out.print("Enter a: "); a=in.nextInt(); dummy=in.nextLine();
					System.out.print("Enter b: "); b=in.nextInt(); dummy=in.nextLine();
					System.out.println("Multiplication: "+asif.mul(a,b));
					break;
				case 4:
					System.out.print("Enter a: "); a=in.nextInt(); dummy=in.nextLine();
					System.out.print("Enter b: "); b=in.nextInt(); dummy=in.nextLine();
					while(b==0)
					{
						System.out.print("b cannot be zero... Enter b: "); b=in.nextInt(); dummy=in.nextLine();
					}
					System.out.println("Division: "+asif.div(a,b));
					break;
				case 5:
					System.out.print("Enter a: "); a=in.nextInt(); dummy=in.nextLine();
					System.out.println("Factorial: "+asif.fact(a));
					break;
				case 6:
					System.out.println("Exiting...");
					break;
				default:
					System.out.println("Invalid Choice!!!");
			}
		}while(choice!=6);
		

		}
		catch(Exception e)
		{
			System.out.println("Exception: "+e);
		}
	}
}