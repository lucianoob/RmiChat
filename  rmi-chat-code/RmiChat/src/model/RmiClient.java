package model;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Classe que gera os clientes RMI.
 * @author Luciano
 * @since 15/01/2013
 * @version 1.0
 */
public class RmiClient extends UnicastRemoteObject implements RemoteObserver 
{
	/**
	 * Indica a estãncia do servidor RMI.
	 */
	public static RmiService remoteService;
	/**
	 * Mensagem trocada entre a comunicação.
	 */
	public String message = "";
	/**
	 * Referência única desta classe.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Construtor da classe que inicializa a estância do servidor.
	 * @param rmi Estância do servidor RMI.
	 * @throws RemoteException Exceção caso aconteça algum erro de comunicação.
	 */
    public RmiClient(RmiService rmi) throws RemoteException 
    {
        super();
        remoteService = rmi;
    }
    
    /**
     * Atualiza o envio de mensagens ao servidor.
     * @since 15/01/2013
     * @param observable O servidor que monitora as modificações nos clientes.
	 * @param updateMsg Mensagem enviada ao servidor.
     * @throws RemoteException Exceção caso aconteça algum erro de comunicação.
     */
    @Override
    public void update(Object observable, Object updateMsg) throws RemoteException 
    {
        System.out.println(updateMsg);
        message = updateMsg.toString();
    }
    
    /**
     * Envia a mensagem ao servidor RMI.
     * @since 15/01/2013
     * @param text Texto da mensagem.
     * @return O estado do envio da mensagem.
     */
    public String sendMessage(String text)
    {
    	message = text;
    	try 
    	{
			remoteService.receiveMessage(message);
			return "Mensagem enviada ao servidor";
		} catch (Exception e) 
		{
			return e.getMessage();
		}
    }
}