package MyChat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.awt.Font;
import javax.swing.ImageIcon;
import java.awt.Toolkit;


public class Chat extends JFrame implements ActionListener, KeyListener {
	
	private static final long serialVersionUID = 1L;
	private JTextArea texto;
	private JTextField txtMsg;
	private JButton botaoEnviar;
	private JButton botaoSair;
	private JLabel lblHistorico;
	private JLabel lblMsg;
	private JPanel pnlContent;
	private Socket socket;
	private OutputStream ou ;
	private Writer ouw;
	private BufferedWriter bfw;
	private JTextField txtIP;
	private JTextField txtPorta;
	private JTextField txtNome;
	private JLabel lblNewLabel;
	
	public Chat() throws IOException{
		setIconImage(Toolkit.getDefaultToolkit().getImage(Chat.class.getResource("/imagens/chat.png")));
	    JLabel lblMessage = new JLabel("Verificar!");
	    txtIP = new JTextField("127.0.0.1");
	    txtPorta = new JTextField("12345");
	    txtNome = new JTextField("Cliente");
	    Object[] texts = {lblMessage, txtIP, txtPorta, txtNome };
	    JOptionPane.showMessageDialog(null, texts);
	     pnlContent = new JPanel();
	     txtMsg                       = new JTextField(20);
	     txtMsg.setBounds(10, 405, 372, 18);
	     lblHistorico= new JLabel("Histórico de Mensagens");
	     lblHistorico.setForeground(Color.WHITE);
	     lblHistorico.setFont(new Font("Verdana", Font.BOLD, 15));
	     lblHistorico.setBounds(90, 29, 216, 14);
	     lblMsg= new JLabel("Mensagem");
	     lblMsg.setForeground(Color.WHITE);
	     lblMsg.setFont(new Font("Verdana", Font.BOLD, 15));
	     lblMsg.setBounds(154, 362, 100, 32);
	     botaoEnviar= new JButton("Enviar");
	     botaoEnviar.setFont(new Font("Arial", Font.PLAIN, 14));
	     botaoEnviar.setBounds(216, 434, 90, 23);
	     botaoEnviar.setToolTipText("Enviar Mensagem");
	     botaoSair = new JButton("Sair");
	     botaoSair.setFont(new Font("Arial", Font.PLAIN, 14));
	     botaoSair.setBounds(90, 434, 83, 23);
	     botaoSair.setToolTipText("Sair do Chat");
	     botaoEnviar.addActionListener(this);
	     botaoSair.addActionListener(this);
	     botaoEnviar.addKeyListener(this);
	     txtMsg.addKeyListener(this);
	     JScrollPane scroll = new JScrollPane();
	     scroll.setBounds(38, 67, 310, 264);
	     pnlContent.setLayout(null);
	     pnlContent.add(lblHistorico);
	     pnlContent.add(scroll);
	     pnlContent.add(lblMsg);
	     pnlContent.add(txtMsg);
	     pnlContent.add(botaoSair);
	     pnlContent.add(botaoEnviar);
	     pnlContent.setBackground(Color.WHITE);
	     txtMsg.setBorder(BorderFactory.createEtchedBorder(Color.BLACK, Color.BLACK));
	     setTitle("Chat de " + txtNome.getText());
	     setContentPane(pnlContent);
	     texto= new JTextArea(10,20);
	     texto.setBounds(20, 54, 348, 297);
	     pnlContent.add(texto);
	     texto.setEditable(false);
	     texto.setBackground(new Color(240,240,240));
	     texto.setLineWrap(true);
	     texto.setBorder(BorderFactory.createEtchedBorder(Color.BLACK,Color.BLACK));
	     
	     lblNewLabel = new JLabel("");
	     lblNewLabel.setIcon(new ImageIcon(Chat.class.getResource("/imagens/FundoChat.png")));
	     lblNewLabel.setBounds(0, 0, 394, 471);
	     pnlContent.add(lblNewLabel);
	     setLocationRelativeTo(null);
	     setResizable(false);
	     setSize(400,500);
	     setVisible(true);
	     setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	public void conectar() throws IOException{	
		try {
			  socket = new Socket(txtIP.getText(),Integer.parseInt(txtPorta.getText()));
			  ou = socket.getOutputStream();
			  ouw = new OutputStreamWriter(ou);
			  bfw = new BufferedWriter(ouw);
			  bfw.write(txtNome.getText()+"\r\n");
			  bfw.flush();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Erro de conexão com o servidor!", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void enviarMensagem(String msg) throws IOException{
	    if(msg.equalsIgnoreCase("Desconectar")){
	      bfw.write("Desconectado \r\n");
	      texto.append("Desconectado \r\n");
	    }else{
	      bfw.write(msg+"\r\n");
	      texto.append( txtNome.getText() + " : " + txtMsg.getText()+"\r\n");
	    }
	     bfw.flush();
	     txtMsg.setText("");
	}
	
	public void escutar() throws IOException{
		   InputStream in = socket.getInputStream();
		   InputStreamReader inr = new InputStreamReader(in);
		   BufferedReader bfr = new BufferedReader(inr);
		   String msg = "";

		    while(!"Sair".equalsIgnoreCase(msg))

		       if(bfr.ready()){
		         msg = bfr.readLine();
		       if(msg.equals("Sair"))
		         texto.append("Servidor caiu! \r\n");
		        else
		         texto.append(msg+"\r\n");
		       }
		}
	
	  public void sair() throws IOException{
		   enviarMensagem("Desconectado...");
		   bfw.close();
		   ouw.close();
		   ou.close();
		   socket.close();
		}
	  
	  public void actionPerformed(ActionEvent e) {
		  try {
		     if(e.getActionCommand().equals(botaoEnviar.getActionCommand()))
		        enviarMensagem(txtMsg.getText());
		     else
		        if(e.getActionCommand().equals(botaoSair.getActionCommand()))
		        sair();
		     } catch (IOException e1) {
		          // TODO Auto-generated catch block
		          e1.printStackTrace();
		     }
		}
	  
	  public void keyPressed(KeyEvent e) {

		    if(e.getKeyCode() == KeyEvent.VK_ENTER){
		       try {
		          enviarMensagem(txtMsg.getText());
		       } catch (IOException e1) {
		           // TODO Auto-generated catch block
		           e1.printStackTrace();
		       }
		   }
		}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String []args) throws IOException{

		   Chat app = new Chat();
		   app.conectar();
		   app.escutar();
		}
}


