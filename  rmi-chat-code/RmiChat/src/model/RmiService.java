package model;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface para determinar as funções obrigatórias do servidor RMI.
 * @author Luciano
 * @since 10/01/2013
 * @version 1.0
 */
public interface RmiService extends Remote 
{
	/**
	 * Envia uma mensagem aos clientes.
	 * @since 10/01/2013
	 * @param x Mensagem a ser enviada.
	 * @throws RemoteException Exceção caso aconteça algum erro de comunicação.
	 */
	void receiveMessage(String x) throws RemoteException;
	/**
	 * 
	 * @since 10/01/2013
	 * @param o Servidor que observa os clientes.
	 * @throws RemoteException Exceção caso aconteça algum erro de comunicação.
	 */
    void addObserver(RemoteObserver o) throws RemoteException;
}