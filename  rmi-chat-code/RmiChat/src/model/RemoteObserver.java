package model;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface para determinar as funções obrigatórias dos clientes RMI.
 * @author Luciano
 * @since 10/01/2013
 * @version 1.0
 */
public interface RemoteObserver extends Remote 
{
	/**
	 * Envia a mensagem para o servidor. 
	 * @since 10/01/2013
	 * @param observable O servidor que monitora as modificações nos clientes.
	 * @param updateMsg Mensagem enviada ao servidor.
	 * @throws RemoteException Exceção caso aconteça algum erro de comunicação.
	 */
    void update(Object observable, Object updateMsg) throws RemoteException;
}