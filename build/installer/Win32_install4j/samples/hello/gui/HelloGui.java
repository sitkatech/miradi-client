/*
 *   GUI application demonstration for exe4j
 */


import com.install4j.api.launcher.ApplicationLauncher;
import com.install4j.api.launcher.SplashScreen;
import com.install4j.api.launcher.StartupNotification;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Properties;

public class HelloGui extends JFrame {

    private JLabel helloLabel;

    private HelloGui() {

        setSize(600, 400);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screenSize.width - 600) / 2, (screenSize.height - 400) / 2);
        setTitle("Hello World GUI");

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        JMenuItem updateItem = new JMenuItem("Check For Update");
        updateItem.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    checkForUpdate();
                }
            }
        );
        menu.add(updateItem);

        JMenuItem changeNameItem = new JMenuItem("Change Greeting Name (Custom Installer Application)");
        changeNameItem.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    changeName();
                }
            }
        );
        menu.add(changeNameItem);

        menu.addSeparator();

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    System.exit(0);
                }
            }
        );

        menu.add(exitItem);

        menuBar.add(menu);
        setJMenuBar(menuBar);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        helloLabel = new JLabel(getGreetingText());
        helloLabel.setFont(helloLabel.getFont().deriveFont(50f));
        helloLabel.setHorizontalAlignment(SwingConstants.CENTER);
        getContentPane().add(helloLabel, BorderLayout.CENTER);


        Box box = Box.createVerticalBox();
        box.add(new JLabel(" * See the \"File\" menu for examples of auto-updaters and custom installer applications"));
        box.add(new JLabel(" * Quit and pass \"fail\" as an argument to hello_gui to see what happens for a startup failure"));
        box.add(new JLabel(" * On Windows: start hello_gui.exe again to see how startup notification works"));
        getContentPane().add(box, BorderLayout.SOUTH);
    }

    private void checkForUpdate() {
        launchApplication("535", null);
    }

    private void changeName() {
        launchApplication("594", new ApplicationLauncher.Callback() {
            public void exited(int exitValue) {
                helloLabel.setText(getGreetingText());
            }

            public void prepareShutdown() {
                // will not be invoked in this case
            }
        });
    }

    // The ID of the installer application is shown in the install4j IDE on the screens & actions tab
    // when the "Show IDs" toogle button is selected
    private void launchApplication(String applicationId, ApplicationLauncher.Callback callback) {
        try {
            ApplicationLauncher.launchApplication(applicationId, null, false, callback);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showConfirmDialog(this, "Could not launch installer application.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getGreetingText() {

        String greetingName = "world";
        try {
            File propertiesFile = new File(System.getProperty("user.home"), "hello.properties");
            if (propertiesFile.exists()) {
                Properties properties = new Properties();
                FileInputStream inputStream = new FileInputStream(propertiesFile);
                properties.load(inputStream);
                inputStream.close();
                greetingName = properties.getProperty("greetingName");
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return "Hello " + greetingName + "!";
    }

    public static void main(String[] args)
        throws Exception
    {

        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        try {
            SplashScreen.writeMessage("Initializing giant application ...");
            Thread.sleep(1000);
            SplashScreen.writeMessage("Opening complex main window ...");
            Thread.sleep(1000);
        } catch (SplashScreen.ConnectionException ex) {
        }
        if (args.length == 1 && args[0].equals("fail")) {
			throw new RuntimeException("I was asked to fail");
        } else {
            final HelloGui helloGui = new HelloGui();

            // startup notification on Microsoft Windows
            StartupNotification.registerStartupListener(new StartupNotification.Listener() {
                public void startupPerformed(final String parameters) {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            JOptionPane.showMessageDialog(helloGui, "I've been started again, with parameters \"" + parameters +  "\".", "Hello World", JOptionPane.INFORMATION_MESSAGE);
                        }
                    });
                }
            });

            helloGui.setVisible(true);
        }
    }
}