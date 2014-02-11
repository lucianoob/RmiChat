package model;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface para determinar as fun��es obrigat�rias dos clientes RMI.
 * @author Luciano
 * @since 10/01/2013
 * @version 1.0
 */
public interface RemoteObserver extends Remote 
{
	/**
	 * Envia a mensagem para o servidor. 
	 * @since 10/01/2013
	 * @param observable O servidor que monitora as modifica��es nos clientes.
	 * @param updateMsg Mensagem enviada ao servidor.
	 * @throws RemoteException Exce��o caso aconte�a algum erro de comunica��o.
	 */
    void update(Object observable, Object updateMsg) throws RemoteException;
}