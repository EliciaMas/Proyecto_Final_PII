package SParcial;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.imageio.ImageIO;

public class LoginF {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginF::new);
    }

    public LoginF() {
        // Crear el marco
        JFrame frame = new JFrame("Inicio");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 350);
        frame.setLocationRelativeTo(null);
        
        // Cambiar el icono
        ImageIcon icon = new ImageIcon("C:\\Users\\DELL\\Desktop\\PFPF\\ProFinal\\src\\login.jpg"); // Ajusta la ruta
        frame.setIconImage(icon.getImage());

        // Crear y añadir el panel con imagen de fondo
        BackgroundPanel panel = new BackgroundPanel();
        panel.setLayout(null);

        // Crear etiquetas y cajas de texto
        JLabel iniLabel = new JLabel("Bienvenido");
        iniLabel.setForeground(Color.WHITE);
        iniLabel.setFont(new Font("Elephant", Font.BOLD, 30));
        iniLabel.setBounds(100, 18, 200, 25);
        panel.add(iniLabel);
        
        JLabel usuarioLabel = new JLabel("Usuario:");
        usuarioLabel.setForeground(Color.WHITE);
        usuarioLabel.setFont(new Font("Elephant", Font.BOLD, 17));
        usuarioLabel.setBounds(35, 150, 150, 25);
        panel.add(usuarioLabel);
        
        JLabel contraseñaLabel = new JLabel("Contraseña:");
        contraseñaLabel.setForeground(Color.WHITE);
        contraseñaLabel.setFont(new Font("Elephant", Font.BOLD, 17));
        contraseñaLabel.setBounds(35, 200, 155, 25);
        panel.add(contraseñaLabel);
        
        JTextField usuarioText = new JTextField();
        usuarioText.setBounds(155, 150, 200, 25);
        panel.add(usuarioText);
        
        JPasswordField contraseñaText = new JPasswordField();
        contraseñaText.setBounds(155, 200, 200, 25);
        panel.add(contraseñaText);

        // Crear el botón "Iniciar Sesión"
        JButton iniciarBTN = new JButton("Iniciar Sesión");
        iniciarBTN.setForeground(Color.WHITE);
        iniciarBTN.setBackground(Color.BLUE);
        iniciarBTN.setFont(new Font("Arial", Font.BOLD, 14));
        iniciarBTN.setBounds(155, 270, 130, 25);
        panel.add(iniciarBTN);

        // Crear el botón "Salir"
        JButton salirBTN = new JButton("Salir");
        salirBTN.setForeground(Color.WHITE);
        salirBTN.setBackground(Color.BLUE);
        salirBTN.setFont(new Font("Arial", Font.BOLD, 14));
        salirBTN.setBounds(295, 270, 75, 25);
        panel.add(salirBTN);

        // Acción al hacer clic en "Iniciar Sesión"
        iniciarBTN.addActionListener(e -> {
            String usuario = usuarioText.getText();
            String contraseña = new String(contraseñaText.getPassword());

            if (usuario.equals("elicia") && contraseña.equals("mas")) {
                frame.dispose(); // Cierra la ventana de inicio de sesión
                SwingUtilities.invokeLater(() -> {
                    // Inicia la ventana de la aplicación JavaFX
                    ConexionBD.launch(ConexionBD.class); // Llama a la clase ConexionBD que es una aplicación JavaFX
                });
            } else {
                JOptionPane.showMessageDialog(frame, "Usuario o contraseña incorrectos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Acción al hacer clic en "Salir"
        salirBTN.addActionListener(e -> System.exit(0));

        // Añadir el panel al marco
        frame.add(panel);
        frame.setVisible(true);
    }

    // Clase para el panel con imagen de fondo
    static class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel() {
            try {
                backgroundImage = ImageIO.read(new File("C:\\Users\\DELL\\Desktop\\Progra II\\Vacacion\\Form Vacaciones\\src\\login.jpg")); // Ajusta la ruta
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }
}
