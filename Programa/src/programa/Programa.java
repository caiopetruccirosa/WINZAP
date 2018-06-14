package programa;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import compactador.Compactador;
import javax.swing.SwingConstants;

public class Programa {

	private JFrame frmWinzap;
	
	protected File arquivo;
	protected Compactador comp;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Programa window = new Programa();
					window.frmWinzap.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Programa() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmWinzap = new JFrame();
		frmWinzap.setResizable(false);
		frmWinzap.setTitle("WINZAP");
		frmWinzap.setBounds(100, 100, 356, 218);
		frmWinzap.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmWinzap.getContentPane().setLayout(null);
		
		JButton btnOpenDialog = new JButton("Escolher arquivo");
		btnOpenDialog.setBounds(90, 54, 168, 38);
		frmWinzap.getContentPane().add(btnOpenDialog);
		
		JLabel lblNomeArquivo = new JLabel("Arquivo: ");
		lblNomeArquivo.setHorizontalAlignment(SwingConstants.CENTER);
		lblNomeArquivo.setBounds(10, 11, 331, 32);
		frmWinzap.getContentPane().add(lblNomeArquivo);
		
		JButton btnDescompactar = new JButton("Descompactar arquivo");
		btnDescompactar.setBounds(182, 103, 159, 76);
		frmWinzap.getContentPane().add(btnDescompactar);
		
		JButton btnCompactar = new JButton("Compactar arquivo");
		btnCompactar.setBounds(10, 103, 159, 76);
		frmWinzap.getContentPane().add(btnCompactar);
		
		btnOpenDialog.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Arquivo texto", "txt"));
				fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Arquivo compactado", "zap"));
				if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					arquivo = fileChooser.getSelectedFile();					
					lblNomeArquivo.setText("Arquivo: " + arquivo.getName());
				}
			}
		});
		
		btnCompactar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					comp = new Compactador(arquivo);
					comp.compactar();
					
					lblNomeArquivo.setText("Arquivo:");
					arquivo = null;
					
					JOptionPane.showMessageDialog(null, "Arquivo compactado com sucesso!", "Arquivo compactado", JOptionPane.PLAIN_MESSAGE);
				} catch (FileNotFoundException e) {
					JOptionPane.showMessageDialog(null, "Escolha um arquivo para compactar!", "Arquivo não escolhido", JOptionPane.WARNING_MESSAGE);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		btnDescompactar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					comp = new Compactador(arquivo);
					comp.descompactar();
					
					lblNomeArquivo.setText("Arquivo:");
					arquivo = null;
					
					JOptionPane.showMessageDialog(null, "Arquivo descompactado com sucesso!", "Arquivo descompactado", JOptionPane.PLAIN_MESSAGE);
				} catch (FileNotFoundException e) {
					JOptionPane.showMessageDialog(null, "Escolha um arquivo para descompactar!", "Arquivo não escolhido", JOptionPane.WARNING_MESSAGE);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, e.getMessage(), null, JOptionPane.WARNING_MESSAGE);
				}
			}
		});
	}
}
