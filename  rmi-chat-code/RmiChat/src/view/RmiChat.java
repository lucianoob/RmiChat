package view;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.Timer;

import model.RmiClient;
import model.RmiServer;
import model.RmiService;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
/**
 * Chat de comunicação como exemplo de Java RMI.
 * @author Luciano
 * @since 05/01/2013
 * @version 1.15 11/02/2014
 */
public class RmiChat 
{
	private JFrame frmRmichat;
	private String appTitle = "RmiChat v1.15";
	private JTextField txtNickname;
	private JTextField txtMensagem;
	private JTextPane txtMensagens;
	private JButton btnIniciar;
	private JButton btnOk;
	private JPanel panel;
	
	private Boolean isServer = false;
	private Boolean isClient = false;
	
	private Registry rmiRegistry;
	private RmiServer rmiServer;
	private String msgServer = "";
	private RmiClient rmiClient;
	private String msgClient = "";
	
	private Timer timerMessage;
	
	private JTextPane txtStatus;
	private JMenuBar menuBar;
	private JMenu mnArquivo;
	private JMenu mnAjuda;
	private JMenuItem mntmSair;
	private JMenuItem mntmSobre;
	private JRadioButtonMenuItem rdbtnmntmServidor;
	private JRadioButtonMenuItem rdbtnmntmCliente;
	private JMenu mnConfigurao;
	private JMenu mnModo;
	private JMenu mnHost;
	private JMenu mnPorta;
	private JTextField txtHost;
	private JTextField txtPorta;

	/**
	 * Método principal da aplicação.
	 * @param args Array de textos com os algumentos de entrada.
	 */
	public static void main(String[] args) 
	{
		EventQueue.invokeLater(new Runnable() 
		{
			public void run() 
			{
				try 
				{
					RmiChat window = new RmiChat();
					window.frmRmichat.setVisible(true);
				} catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Criando a aplicação.
	 */
	public RmiChat() 
	{
		initialize();
	}

	/**
	 * Iniciando o visual.
	 */
	private void initialize() 
	{
		frmRmichat = new JFrame();
		frmRmichat.setIconImage(Toolkit.getDefaultToolkit().getImage(RmiChat.class.getResource("/assets/rmichat-logo.png")));
		frmRmichat.setTitle(appTitle);
		frmRmichat.getContentPane().setFont(new Font("Tahoma", Font.BOLD, 14));
		
		panel = new JPanel();
		GroupLayout groupLayout = new GroupLayout(frmRmichat.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(panel, GroupLayout.DEFAULT_SIZE, 444, Short.MAX_VALUE)
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(panel, GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE)
		);
		panel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(42dlu;default):grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(129dlu;default):grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(40dlu;default)"),
				FormFactory.RELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(22dlu;default)"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(109dlu;default):grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(24dlu;default)"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,}));
		
		JLabel lblNickname = new JLabel("NickName:");
		lblNickname.setFont(new Font("Tahoma", Font.BOLD, 14));
		panel.add(lblNickname, "2, 2, right, fill");
		
		txtNickname = new JTextField();
		txtNickname.setFont(new Font("Tahoma", Font.BOLD, 14));
		panel.add(txtNickname, "4, 2, fill, fill");
		txtNickname.setColumns(10);
		
		btnIniciar = new JButton("Iniciar");
		btnIniciar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) 
			{
				if(btnIniciar.getText() == "Iniciar")
				{
					if(txtNickname.getText().length() > 0)
					{
						if(rdbtnmntmServidor.isSelected())
						{
							if(startServer())
							{
								frmRmichat.setTitle(appTitle + " - Servidor("+txtHost.getText()+":"+txtPorta.getText()+")");
								btnIniciar.setText("Parar");
								txtNickname.setEnabled(false);
								txtMensagem.setEnabled(true);
								btnOk.setEnabled(true);
								mnConfigurao.setEnabled(false);
								timerMessage.start();
							}
						}
						if(rdbtnmntmCliente.isSelected())
						{
							if(startClient())
							{
								frmRmichat.setTitle(appTitle + " - Cliente("+txtHost.getText()+":"+txtPorta.getText()+")");
								btnIniciar.setText("Parar");
								txtNickname.setEnabled(false);
								txtMensagem.setEnabled(true);
								btnOk.setEnabled(true);
								mnConfigurao.setEnabled(false);
								timerMessage.start();
							}
						}
					} else
					{
						JOptionPane.showMessageDialog(frmRmichat, "Entre com um nickname para iniciar.", "ERRO", JOptionPane.ERROR_MESSAGE);
					}
				} else if(btnIniciar.getText() == "Parar")
				{
					timerMessage.stop();
					if(isServer)
						txtStatus.setText(stopServer());
					isServer = false;
					isClient = false;
					frmRmichat.setTitle(appTitle);
					btnIniciar.setText("Iniciar");
					txtNickname.setEnabled(true);
					txtMensagem.setEnabled(false);
					btnOk.setEnabled(false);
					mnConfigurao.setEnabled(true);
				}
			}
		});
		btnIniciar.setFont(new Font("Tahoma", Font.BOLD, 14));
		panel.add(btnIniciar, "6, 2, default, fill");
		
		txtMensagens = new JTextPane();
		txtMensagens.setFont(new Font("Tahoma", Font.BOLD, 14));
		txtMensagens.setBackground(new Color(240, 248, 255));
		txtMensagens.setEditable(false);
		panel.add(txtMensagens, "2, 4, 5, 1, fill, fill");
		
		JLabel lblTexto = new JLabel("Texto:");
		lblTexto.setFont(new Font("Tahoma", Font.BOLD, 14));
		panel.add(lblTexto, "2, 6, right, fill");
		
		txtMensagem = new JTextField();
		txtMensagem.setFont(new Font("Tahoma", Font.BOLD, 14));
		txtMensagem.setEnabled(false);
		panel.add(txtMensagem, "4, 6, fill, fill");
		txtMensagem.setColumns(10);
		
		btnOk = new JButton("Ok");
		btnOk.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) 
			{
				String txt = txtMensagem.getText();
				txtMensagem.setText("");
				txtMensagem.requestFocusInWindow();
				
				if(isServer)
					rmiServer.sendMessage(txtNickname.getText()+": "+txt);
				if(isClient)
					txtStatus.setText(rmiClient.sendMessage(txtNickname.getText()+": "+txt));
			}
		});
		btnOk.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnOk.setEnabled(false);
		panel.add(btnOk, "6, 6, fill, fill");
		
		txtStatus = new JTextPane();
		txtStatus.setText("Chat desconectado.");
		txtStatus.setFont(new Font("Tahoma", Font.BOLD, 12));
		txtStatus.setEditable(false);
		panel.add(txtStatus, "1, 8, 7, 2, fill, center");
		frmRmichat.getContentPane().setLayout(groupLayout);
		frmRmichat.setFont(new Font("Arial", Font.BOLD, 14));
		frmRmichat.setResizable(false);
		frmRmichat.setBounds(100, 100, 520, 378);
		frmRmichat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		menuBar = new JMenuBar();
		frmRmichat.setJMenuBar(menuBar);
		
		mnArquivo = new JMenu("Arquivo");
		mnArquivo.setMnemonic('A');
		menuBar.add(mnArquivo);
		
		mntmSair = new JMenuItem("Sair");
		mntmSair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				frmRmichat.dispose();
				System.exit(0);
			}
		});
		mntmSair.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
		mnArquivo.add(mntmSair);
		
		ButtonGroup modoGroup = new ButtonGroup();
		
		mnConfigurao = new JMenu("Configura\u00E7\u00E3o");
		menuBar.add(mnConfigurao);
		
		mnModo = new JMenu("Modo");
		mnConfigurao.add(mnModo);
		
		rdbtnmntmServidor = new JRadioButtonMenuItem("Servidor");
		mnModo.add(rdbtnmntmServidor);
		rdbtnmntmServidor.setSelected(true);
		modoGroup.add(rdbtnmntmServidor);
		
		rdbtnmntmCliente = new JRadioButtonMenuItem("Cliente");
		mnModo.add(rdbtnmntmCliente);
		modoGroup.add(rdbtnmntmCliente);
		
		mnHost = new JMenu("Host");
		mnConfigurao.add(mnHost);
		
		txtHost = new JTextField();
		txtHost.setText("127.0.0.1");
		mnHost.add(txtHost);
		txtHost.setColumns(10);
		
		mnPorta = new JMenu("Porta");
		mnConfigurao.add(mnPorta);
		
		txtPorta = new JTextField();
		txtPorta.setText("1098");
		mnPorta.add(txtPorta);
		txtPorta.setColumns(10);
		
		mnAjuda = new JMenu("Ajuda");
		mnAjuda.setMnemonic('j');
		menuBar.add(mnAjuda);
		
		mntmSobre = new JMenuItem("Sobre");
		mntmSobre.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				String text = appTitle+"\n";
				text += "Aplicação: Chat de comunicação de exemplo em Java RMI.\n";
				text += "Criado: 05/01/2013 - Atualizado: 11/02/2014\n";
				text += "Desenvolvedores:\n";
				text += "   Luciano Oliveira Borges (luciano@iautomate.com.br).\n";
				text +="Fornecedor: IAutomate - Integração de Sistemas (www.iautomate.com.br).\n";
				JOptionPane.showMessageDialog(frmRmichat, text, "SOBRE", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		mntmSobre.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK));
		mnAjuda.add(mntmSobre);
		
		timerMessage = new Timer(500, actionTimer);
	}
	/**
	 * Acão de monitoramento do texto enviado pela comunicação.
	 */
	private Action actionTimer = new AbstractAction() 
	{
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			if(isServer)
            {
            	if(rmiServer.message != msgServer)
            	{
            		msgServer = rmiServer.message;
					txtMensagens.setText(txtMensagens.getText()+msgServer+"\n");
            	}
            	if(rmiServer.clients > 0)
            		txtStatus.setText("Cliente conectado.");
            	else
            		txtStatus.setText("Cliente desconectado, servidor ativo aguardando clientes...");
            } else if(isClient)
            {
            	if(rmiClient.message != msgClient)
            	{
            		msgClient = rmiClient.message;
            		txtMensagens.setText(txtMensagens.getText()+msgClient+"\n");
            	}
            }
		}
	};
	/**
	 * Inicia o servidor RMI.
	 * @since 19/01/2013
	 * @return O estado do servidor.
	 */
	private Boolean startServer()
	{
		int port = Integer.parseInt(txtPorta.getText());
		
		try 
		{
			LocateRegistry.createRegistry(port);
		} catch ( Exception e1 ) 
		{
			
		}
		
		if (System.getSecurityManager() == null)
            System.setSecurityManager(new RMISecurityManager());
        try 
        {
        	rmiServer = new RmiServer();
            rmiRegistry = LocateRegistry.getRegistry(port);
            RmiService rmiService = (RmiService) UnicastRemoteObject.exportObject(rmiServer, port);
            rmiRegistry.rebind("RmiService", rmiService);
            isServer = true;
            txtStatus.setText("Servidor ativo, aguardando clientes...");
            return true;
        } catch (ExportException ex) 
        {
        	txtStatus.setText("ERRO: esta porta '"+port+"' já está em uso.");
        	return false;
        }      
        catch (Exception ex) 
        {
        	txtStatus.setText("ERRO: "+ex.getLocalizedMessage());
            return false;
        }
	}
	/**
	 * Finaliza o servidor RMI.
	 * @since 19/01/2013
	 * @return A mensagem de término do servidor RMI.
	 */
	private String stopServer()
	{
		try 
		{
			rmiRegistry.unbind("RmiService");
			return "Chat desconectado.";
		} catch (Exception e) 
		{
			return e.getMessage();
		}
	}
	/**
	 * Inicia o cliente RMI.
	 * @since 19/01/2013
	 * @return O estado da comunicação do cliente com o servidor.
	 */
	private Boolean startClient()
	{
		String server = txtHost.getText();
    	int port = 1098;
    	RmiService remoteService;
    	
        if (System.getSecurityManager() == null)
            System.setSecurityManager(new RMISecurityManager());
        try 
        {
            remoteService = (RmiService) Naming.lookup("rmi://"+server+":"+port+"/RmiService");

            rmiClient = new RmiClient(remoteService);
            remoteService.addObserver(rmiClient);
            isClient = true;
            txtStatus.setText("Conectado ao servidor com sucesso.");
            return true;
        } catch (ConnectException ex) 
        {
        	txtStatus.setText("ERRO: não foi possível conectar a '"+server+":"+port+"' !");
            return false;
        } catch (Exception ex) 
        {
        	txtStatus.setText("ERRO: "+ex.getMessage());
            return false;
        }
	}

}
