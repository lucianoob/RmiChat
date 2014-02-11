package model;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Observable;
import java.util.Observer;

/**
 * Classe que gera o servidor RMI.
 * @author Luciano
 * @since 15/01/2013
 * @version 1.0
 */
public class RmiServer extends Observable implements RmiService 
{
	/**
	 * N�mero de clienets conectados ao servidor RMI.
	 */
	public int clients = 0;
	/**
	 * Mensagem trocada entre a comunica��o.
	 */
	public String message = "";
	
	/**
	 * Observador para detectar o envio de mensagens dos clientes.
	 * @author Luciano
	 * @since 15/01/2013
	 * @version 1.0
	 */
    private class WrappedObserver implements Observer, Serializable 
    {
    	/**
    	 * Refer�ncia �nica desta classe.
    	 */
        private static final long serialVersionUID = 1L;
        /**
         * Cliente remoto que ser� observado.
         */
        private RemoteObserver ro = null;
        
        /**
         * Construtor que inicializa o cliente remoto. 
         * @param ro
         */
        public WrappedObserver(RemoteObserver ro) 
        {
            this.ro = ro;
        }
        
        /**
         * Atualiza o envio de mensagens aos clientes.
         * @since 15/01/2013
         * @param o O servidor que monitora as modifica��es nos clientes.
         * @param arg Mensagem enviada ao servidor.
         */
        @Override 
        public void update(Observable o, Object arg) 
        {
            try 
            {
                ro.update(o.toString(), arg);
            } catch (RemoteException e) 
            {
                System.out.println("Remote Removido ("+o+"):" + this);
                o.deleteObserver(this);
                clients--;
            }
        }

    }
    
    /**
     * Adiciona os clientes que ser�o observados.
     * @since 15/01/2013
     * @param o O cliente que ser� monitorado.
     * @throws RemoteException Exce��o caso aconte�a algum erro de comunica��o.
     */
    @Override 
    public void addObserver(RemoteObserver o) throws RemoteException 
    {
        WrappedObserver mo = new WrappedObserver(o);
        addObserver(mo);
        System.out.println("Remote Adicionado:" + mo);
        clients++;
    }
    
    /**
	 * Envia uma mensagem aos clientes.
	 * @since 15/01/2013
	 * @param x Mensagem a ser enviada.
	 * @throws RemoteException Exce��o caso aconte�a algum erro de comunica��o.
	 */
    @Override 
    public void receiveMessage(String x) throws RemoteException
    {
    	System.out.println(x);
    	message = x;
    }
    
    /**
     * Envia a mensagem ao cliente RMI.
     * @since 15/01/2013
     * @param text Texto da mensagem.
     */
    public void sendMessage(String text)
    {
    	message = text;
    	setChanged();
        notifyObservers(message);
    }
}