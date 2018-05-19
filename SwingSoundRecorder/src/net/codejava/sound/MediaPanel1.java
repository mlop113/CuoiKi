package net.codejava.sound;

import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.Timer;
import java.awt.EventQueue;

import javax.swing.JPanel;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;


import javax.media.*;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.windows.Win32FullScreenStrategy;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import uk.co.caprica.vlcj.runtime.x.LibXUtil;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;


import javax.swing.JTable;
import javax.swing.JTextField;

import javax.swing.table.DefaultTableModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.JToolBar;
import javax.swing.JScrollBar;
import javax.swing.JSplitPane;
import javax.swing.Box;


public class MediaPanel1 extends javax.swing.JFrame {
	//private SwingSoundRecorder recoder= new SwingSoundRecorder();
	private SoundRecordingUtil recorder = new SoundRecordingUtil();
	private AudioPlayer player1 = new AudioPlayer();
	 private EmbeddedMediaPlayerComponent mediaplayer_demo;
	    private File file;
	private Thread playbackThread;
	private RecordTimer timer;
	private ImageIcon iconRecord = new ImageIcon(getClass().getResource(
			"/net/codejava/sound/images/Record.gif"));
	private ImageIcon iconStop = new ImageIcon(getClass().getResource(
			"/net/codejava/sound/images/Stop.gif"));
	private ImageIcon iconPlay = new ImageIcon(getClass().getResource(
			"/net/codejava/sound/images/Play.gif"));
	private boolean isRecording = false;
	private boolean isPlaying = false;
	 private boolean band = true;
	private JFrame frame;
	 String[] cols = {"Col 1", "Col2"};
	 	String[][]  data = {{"Time", "Description"},{"1:06", "How are you"},{"1:15","where are you from?"},{"2:20","Tell me about your life!"}};
	 	String[][] data1 = {{"Time", "Title"},{"1:06", "Record_001"}};
	 	String[][] data2 = {{"Name", "Time"},{"assa", "4:09"},{"Hello World", "10:37"},{"Internet","12:09"}};
	    DefaultTableModel model = new DefaultTableModel(data, cols);
	    DefaultTableModel model1 = new DefaultTableModel(data1, cols);
	    DefaultTableModel model2 = new DefaultTableModel(data2, cols);
	    JTable table = new JTable(model);
	    JButton button = new JButton("Add Notes");
	    JTextField text = new JTextField(15);
	     JTable table_1 = new JTable(model1) {
	    	 public boolean isCellEditable(int row, int col) {
	              return false;
	          }
	     };
	     private javax.swing.JToolBar.Separator jSeparator1;
	    private javax.swing.JButton btnPause;
	    private javax.swing.JButton btnPlay;
	    private javax.swing.JSlider sldProgress;
	    private javax.swing.JSlider sldVolumen;
	    private javax.swing.JPanel panel_button;
	    private javax.swing.JButton btnStop;
	    private javax.swing.JPanel jPanel3;
	    private javax.swing.JPanel jPanel4;
	    private javax.swing.JPanel jPanel2;
	    private JTextField text2;
	    JLabel labelRecordTime = new JLabel("Record Time: 00:00:00");
	    JButton buttonPlay = new JButton("Play");
	    JButton buttonRecord = new JButton("Record");
	    private String saveFilePath;
	/**
	 * Launch the application.
	 */
	    void chargerLibrairie(){
            NativeLibrary.addSearchPath(
                   RuntimeUtil.getLibVlcLibraryName(), "lib");
           Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
           LibXUtil.initialise();
       }
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				
				  new MediaPanel1(args);
				  
			}
		});
	}		
	
	       
	public void actionPerformed(ActionEvent event) {
		JButton button = (JButton) event.getSource();
		if (button == buttonRecord) {
			if (!isRecording) {
				startRecording();
			} else {
				stopRecording();
			}

		} else if (button == buttonPlay) {
			if (!isPlaying) {
				playBack();
			} else {
				stopPlaying();
			}
		}
	}
	private void stopRecording() {
		isRecording = false;
		try {
			timer.cancel();
			buttonRecord.setText("Record");
			buttonRecord.setIcon(iconRecord);
			
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

			recorder.stop();

			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

			saveFile();

		} catch (IOException ex) {
			JOptionPane.showMessageDialog(frame, "Error",
					"Error stopping sound recording!",
					JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}
	}

	private void playBack() {
		timer = new RecordTimer(labelRecordTime);
		timer.start();
		isPlaying = true;
		playbackThread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {

					buttonPlay.setText("Stop");
					buttonPlay.setIcon(iconStop);
					buttonRecord.setEnabled(false);

					player1.play(saveFilePath);
					timer.reset();

					buttonPlay.setText("Play");
					buttonRecord.setEnabled(true);
					buttonPlay.setIcon(iconPlay);
					isPlaying = false;

				} catch (UnsupportedAudioFileException ex) {
					ex.printStackTrace();
				} catch (LineUnavailableException ex) {
					ex.printStackTrace();
				} catch (IOException ex) {
					ex.printStackTrace();
				}

			}
		});

		playbackThread.start();
	}
	private void stopPlaying() {
		timer.reset();
		timer.interrupt();
		player1.stop();
		playbackThread.interrupt();
	}
	
	private void saveFile() {
		JFileChooser fileChooser = new JFileChooser();
		FileFilter wavFilter = new FileFilter() {
			@Override
			public String getDescription() {
				return "Sound file (*.WAV)";
			}

			@Override
			public boolean accept(File file) {
				if (file.isDirectory()) {
					return true;
				} else {
					return file.getName().toLowerCase().endsWith(".wav");
				}
			}
		};

		fileChooser.setFileFilter(wavFilter);
		fileChooser.setAcceptAllFileFilterUsed(false);

		int userChoice = fileChooser.showSaveDialog(frame);
		if (userChoice == JFileChooser.APPROVE_OPTION) {
			saveFilePath = fileChooser.getSelectedFile().getAbsolutePath();
			if (!saveFilePath.toLowerCase().endsWith(".wav")) {
				saveFilePath += ".wav";
			}

			File wavFile = new File(saveFilePath);

			try {
				recorder.save(wavFile);

				JOptionPane.showMessageDialog(frame,
						"Saved recorded sound to:\n" + saveFilePath);

				buttonPlay.setEnabled(true);

			} catch (IOException ex) {
				JOptionPane.showMessageDialog(frame, "Error",
						"Error saving to sound file!",
						JOptionPane.ERROR_MESSAGE);
				ex.printStackTrace();
			}
		}
	}

	private void startRecording() {
		Thread recordThread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					isRecording = true;
					buttonRecord.setText("Stop");
					buttonRecord.setIcon(iconStop);
					buttonPlay.setEnabled(false);

					recorder.start();

				} catch (LineUnavailableException ex) {
					JOptionPane.showMessageDialog(frame,
							"Error", "Could not start recording sound!",
							JOptionPane.ERROR_MESSAGE);
					ex.printStackTrace();
				}
			}
		});
		recordThread.start();
		timer = new RecordTimer(labelRecordTime);
		timer.start();
	}

	 static{
	        NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "lib");
	        Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
	    }
	/**
	 * Create the application.
	 */
	
	/**
	 * Initialize the contents of the frame.
	 */
	private  MediaPanel1(String[] args) {

		
		frame = new JFrame()			;	
	    frame.setLocation(800	, 800);
        frame.setSize(820, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        frame.setVisible(true);

        
      
      
       
        JTable table_2 = new JTable( model2)
        		{
        	public boolean isCellEditable(int row, int col) {
                return false;
            }
        		};
     JPanel panel_addnotes = new JPanel();
     panel_addnotes.setBackground(Color.GRAY);
      panel_addnotes.setBounds(427, 71, 347, 137);
      frame.getContentPane().add(panel_addnotes);
      panel_addnotes.setLayout(new BorderLayout());
      table.setEnabled(false);
      table.setBackground(Color.WHITE);
      table.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
      panel_addnotes.add(table, BorderLayout.NORTH);
      text.setBackground(Color.LIGHT_GRAY);
      panel_addnotes.add(text, BorderLayout.WEST);
      frame.getContentPane().add(panel_addnotes);
      
      text2 = new JTextField();
      text2.setToolTipText("add");
      text2.setBackground(Color.LIGHT_GRAY);
      panel_addnotes.add(text2, BorderLayout.CENTER);
      text2.setColumns(10);
      panel_addnotes.add(button, BorderLayout.SOUTH);
      
            button.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    String value1 = text.getText().toString();
                    String value2=text2.getText();
                    
                    model.addRow(new  Object[] {value1,value2});
                }
           });
      
     
         
    	
      JPanel panel_recording = new JPanel();
      panel_recording.setBackground(Color.LIGHT_GRAY);
      panel_recording.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
      panel_recording.setBounds(427, 285, 347, 224);
      frame.getContentPane().add(panel_recording);
      panel_recording.setLayout(new BorderLayout());
    
      
      panel_recording.add(table_1, BorderLayout.NORTH);
      
      JLabel lblNewLabel = new JLabel("My  Record");
      lblNewLabel.setBounds(457, 268, 106, 14);
      frame.getContentPane().add(lblNewLabel);
      jSeparator1 = new javax.swing.JToolBar.Separator();
      JPanel panel_myvideo = new JPanel();
      panel_myvideo.setBackground(Color.LIGHT_GRAY);
      panel_myvideo.setBounds(19, 460, 381, 87);
      frame.getContentPane().add(panel_myvideo);
      panel_myvideo.setLayout(new BorderLayout());
      
      panel_myvideo.add(table_2, BorderLayout.NORTH);
       
      JLabel lblMyListVideo = new JLabel("My List Video");
      lblMyListVideo.setBounds(19, 440, 76, 14);
      frame.getContentPane().add(lblMyListVideo);
      table_2.addMouseListener(new java.awt.event.MouseAdapter()
    		  {
    	  
    	  public void mouseClicked(java.awt.event.MouseEvent e)

    	  {
    		  
    		  int row=table.rowAtPoint(e.getPoint());
    		  int col= table.columnAtPoint(e.getPoint());
    		  if(row ==0||col==1 )
    		  {
    			  mediaplayer_demo.getMediaPlayer().playMedia("asas.mp4");    
                  sldVolumen.setValue(  mediaplayer_demo.getMediaPlayer().getVolume() );
                  sldProgress.setEnabled(true);
    		  }
    	  }
    		  });
      JButton btnNewButton_4 = new JButton("Open");
      btnNewButton_4.addActionListener(new ActionListener() {
      	public void actionPerformed(ActionEvent arg0) {
      		 JFileChooser fileChooser = new JFileChooser();
             FileNameExtensionFilter filter = new FileNameExtensionFilter("Videos", "mp4","flv","webm","3gp","dat");
             fileChooser.addChoosableFileFilter(filter);
             //fileChooser.setCurrentDirectory(new java.io.File("C:\\videos\\tmp\\"));
             if ( fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION ){            
                 file = fileChooser.getSelectedFile();                                     
                 btnPlay.doClick();
             }
      	}
      });
      btnNewButton_4.setBounds(100, 435, 70, 23);
      frame.getContentPane().add(btnNewButton_4);
      
      JPanel panel_button_record = new JPanel();
      panel_button_record.setBounds(427, 512, 347, 35);
      frame.getContentPane().add(panel_button_record);
      panel_button_record.setLayout(null);
      buttonPlay.addActionListener(new ActionListener() {
      	public void actionPerformed(ActionEvent e) {
      		JButton button = (JButton) e.getSource();
    		if (button == buttonRecord) {
    			if (!isRecording) {
    				startRecording();
    			} else {
    				stopRecording();
    			}

    		} else if (button == buttonPlay) {
    			if (!isPlaying) {
    				playBack();
    			} else {
    				stopPlaying();
    			}
    		}
      	}
      });
      
    
      buttonPlay.setBounds(257, 0, 89, 35);
      panel_button_record.add(buttonPlay);
      buttonRecord.addActionListener(new ActionListener() {
      	public void actionPerformed(ActionEvent e) {
      		JButton button = (JButton) e.getSource();
    		if (button == buttonRecord) {
    			if (!isRecording) {
    				startRecording();
    			} else {
    				stopRecording();
    			}

    		} else if (button == buttonPlay) {
    			if (!isPlaying) {
    				playBack();
    			} else {
    				stopPlaying();
    			}
    		}
      	}
      });
      
   
      buttonRecord.setBounds(0, 0, 110, 35);
      panel_button_record.add(buttonRecord);
      
    
      labelRecordTime.setBounds(113, 0, 140, 35);
      panel_button_record.add(labelRecordTime);
      buttonRecord.setFont(new Font("Sans", Font.BOLD, 14));
		buttonRecord.setIcon(iconRecord);
		buttonPlay.setFont(new Font("Sans", Font.BOLD, 14));
		buttonPlay.setIcon(iconPlay);
		buttonPlay.setEnabled(false);
		labelRecordTime.setFont(new Font("Sans", Font.BOLD, 12));
		
		 JPanel jPanel2 = new JPanel();
		jPanel2.setBounds(19, 40, 381, 306);
		
		jPanel2.setLayout(new BorderLayout());
		
        //se añade reproductor 
		
		 
	        frame.getContentPane().add(jPanel2, BorderLayout.CENTER);
	        mediaplayer_demo = new EmbeddedMediaPlayerComponent();
	        //se añade reproductor 
	        jPanel2.add(mediaplayer_demo);        
	        mediaplayer_demo.setSize(jPanel2.getSize());                
	        mediaplayer_demo.setVisible(true);          
	      
	       

		
		panel_button = new javax.swing.JPanel();
		panel_button.setBounds(19, 353, 381, 71);
		frame.getContentPane().add(panel_button);
		panel_button.setLayout(new java.awt.BorderLayout());
		
		 jPanel3 = new javax.swing.JPanel();
		 
		 jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.LINE_AXIS));
		panel_button.add(jPanel3, BorderLayout.NORTH);
		
		sldProgress = new javax.swing.JSlider();
		jPanel3.add(sldProgress);
		sldProgress.setMinimum(0);
        sldProgress.setMaximum(100);
        sldProgress.setValue(0);
		 jPanel4 = new javax.swing.JPanel();
		panel_button.add(jPanel4, BorderLayout.CENTER);
		jPanel4.setLayout(null);
 
		
		
		 btnPlay = new JButton("Play");
		  btnPlay.addActionListener(new ActionListener() {
			  
	            @Override
	            public void actionPerformed(ActionEvent e)
	            {
	                if (file!=null){                    
	                	mediaplayer_demo.getMediaPlayer().playMedia(file.getAbsolutePath());    
	                    sldVolumen.setValue(  mediaplayer_demo.getMediaPlayer().getVolume() );
	                    sldProgress.setEnabled(true);
	                    setTitle( file.getName() + " - VLCJ Player");    
	                }
	            }
	        }); 
		btnPlay.setBounds(3, 5, 65, 23);
		jPanel4.add(btnPlay);
		
		JButton btnPause = new JButton("Pause");
		btnPause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mediaplayer_demo.getMediaPlayer().setPause( mediaplayer_demo.getMediaPlayer().isPlaying()?true:false );           
			}
		});
		btnPause.setBounds(104, 5, 70, 23);
		jPanel4.add(btnPause);
		
		JButton btnStop = new JButton("Stop");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mediaplayer_demo.getMediaPlayer().stop();   
	              sldProgress.setValue(0);
	              sldProgress.setEnabled(true);
	              frame.setTitle("VLCJ Player");
			}
		});
		btnStop.setBounds(209, 5, 68, 23);
		jPanel4.add(btnStop);
		
		JSlider sldVolumen = new JSlider();
		sldVolumen.setBounds(287, 5, 94, 26);
		jPanel4.add(sldVolumen);
		 sldVolumen.setMinimum(0);
	        sldVolumen.setMaximum(100);
	        
	        JToolBar toolBar = new JToolBar();
	        toolBar.setBounds(0, 0, 804, 25);
	        frame.getContentPane().add(toolBar);
	        JButton btnOpenFile = new JButton("Open File");
	        btnOpenFile.addActionListener(new ActionListener() {
	        	public void actionPerformed(ActionEvent e) {
	        		JFileChooser fileChooser = new JFileChooser();
	                FileNameExtensionFilter filter = new FileNameExtensionFilter("Videos", "mp4","flv","webm","3gp","dat");
	                fileChooser.addChoosableFileFilter(filter);
	                //fileChooser.setCurrentDirectory(new java.io.File("C:\\videos\\tmp\\"));
	                if ( fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION ){            
	                    file = fileChooser.getSelectedFile();                                     
	                    btnPlay.doClick();
	                }
	        	}
	        });
	        toolBar.add(btnOpenFile);
	        toolBar.add(jSeparator1);
	        toolBar.setFloatable(false);
	        toolBar.setRollover(true);
	        JButton btnSnapshot = new JButton("Snap Shot");
	        btnSnapshot.addActionListener(new ActionListener() {
	        	public void actionPerformed(ActionEvent e) {
	        		 if(file!=null){
	                     String absolutePath = file.getAbsolutePath();        
	                     String newPath = absolutePath .substring(0, absolutePath .length()-4) + "_" + System.currentTimeMillis() + ".png";                
	                     if( mediaplayer_demo.getMediaPlayer().saveSnapshot(new File(newPath)) )               
	                        JOptionPane.showMessageDialog(null, "Snapshot: " + newPath );  
	                 }
	        	}
	        });
	        toolBar.add(btnSnapshot);
		  
		  
	        sldVolumen.addChangeListener(new ChangeListener(){

	            @Override
	            public void stateChanged(ChangeEvent e) {
	                Object source = e.getSource();                                
	                mediaplayer_demo.getMediaPlayer().setVolume( ((JSlider) source).getValue() );
	            }            
	        });
	        mediaplayer_demo.getMediaPlayer().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
	            
	            @Override
	            public void positionChanged(MediaPlayer mp, float pos)
	            {
	                if(band){
	                    int value = Math.min(100, Math.round(pos * 100.0f));            
	                    sldProgress.setValue(value);                                                    
	                }
	            }
	            
	            @Override
	            public void finished(MediaPlayer mediaPlayer){
	                
	            }
	            
	        });
	        sldProgress.addMouseListener(new MouseListener(){

	            @Override
	            public void mouseClicked(MouseEvent e) {}

	            @Override
	            public void mousePressed(MouseEvent e) {
	                band= false;
	            }

	            @Override
	            public void mouseReleased(MouseEvent e) {
	                band = true;
	            }

	            @Override
	            public void mouseEntered(MouseEvent e) {}

	            @Override
	            public void mouseExited(MouseEvent e) {}
	            
	        });
	        
	        //Control para cambiar a posicion de reproduccion
	        sldProgress.addChangeListener(new ChangeListener(){

	            @Override
	            public synchronized void stateChanged(ChangeEvent e) {
	                if( !band ){
	                    Object source = e.getSource();                                
	                    float np = ((JSlider) source).getValue() / 100f;                    
	                    mediaplayer_demo.getMediaPlayer().setPosition(np);    
	                }
	                
	            }            
	        });
		  
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setLocationRelativeTo(null);
	}
}
