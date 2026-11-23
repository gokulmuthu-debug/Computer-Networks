//function prototype
import java.rmi.*;

public interface MyServerIntf extends Remote		//remote interface
{	int i=0;
	int add(int a, int b) throws RemoteException;
	int sub(int a, int b) throws RemoteException;
	int mul(int a, int b) throws RemoteException;
	int div(int a, int b) throws RemoteException;
	int fact(int a) throws RemoteException;
}