package logicgates;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class LogicGates extends JFrame implements Commons{

    Controller controller;
    Debug debug;

    public LogicGates(){
        controller = new Controller(this);
        debug = new Debug();

        initUI();
    }

    private void initUI(){
    	JPanel contentPane = new JPanel(new BorderLayout());
    	contentPane = controller;
    	contentPane.setPreferredSize(new Dimension(600, 1200));

    	JScrollPane scrollPane = new JScrollPane(contentPane);
        contentPane.setAutoscrolls(true);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
    	scrollPane.setPreferredSize(new Dimension(600, 1200));

        add(scrollPane);

    	try{
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
    	}
    	catch(Exception e){
            debug.objectOut("Main - Exception - " + e);
    	}

        setJMenuBar(createMenuBar());

        setTitle("Logic Gates");
        setSize(screenWidth, screenHeight);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocation(0, 0);
    }

    public JMenuBar createMenuBar(){
        JMenuBar menuBar = new JMenuBar();

        JMenu file = new JMenu("File");

        ImageIcon exitIcon = new ImageIcon("exit.png");
        JMenuItem exitToMenu = new JMenuItem("Exit To Menu", exitIcon);
        exitToMenu.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent event){
                controller.exitToMenu();
            }
        });

        JMenuItem loadFile = new JMenuItem("Load File");
        loadFile.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent event){
                controller.load();
            }
        });

        JMenuItem saveFile = new JMenuItem("Save File");
        saveFile.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent event){
                controller.save();
            }
        });

		file.add(saveFile);
		file.add(loadFile);
        file.add(exitToMenu);
        menuBar.add(file);

        return menuBar;
    }

    public static void main(String[] args){
        EventQueue.invokeLater(new Runnable(){
           @Override
           public void run(){
               JFrame lg = new LogicGates();
               lg.setVisible(true);
           }
        });
    }

}
