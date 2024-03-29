/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * Mainpage.java
 *
 * Created on 17-ott-2011, 9.56.13
 */
package frame;

import java.awt.Image;
import java.awt.Toolkit;

/**
 *
 * @author claudiofior
 */
public class Mainpage extends javax.swing.JFrame {
	private controler.Controler controler;
	/** Creates new form MainFrame */
	public Mainpage(controler.Controler controler) {
		this();
		this.controler = controler;
		this.setIconImage(				
				Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/phone_blink2.png")));
	}
	public controler.Controler getControler () {
		return this.controler;
	}
	/** Creates new form Mainpage */
	public Mainpage() {
		initComponents();
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuBarMain = new javax.swing.JMenuBar();
        jMenuFile = new javax.swing.JMenu();
        jMenuItemOpenFile = new javax.swing.JMenuItem();
        jMenuItemExit = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Pagine Bianche");

        jMenuFile.setText("File");

        jMenuItemOpenFile.setText("Apri file");
        jMenuItemOpenFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemOpenFileActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemOpenFile);

        jMenuItemExit.setText("Chiudi");
        jMenuItemExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemExitActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemExit);

        jMenuBarMain.add(jMenuFile);

        setJMenuBar(jMenuBarMain);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 587, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 319, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

	private void jMenuItemExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemExitActionPerformed
		controler.UpdateUI updateUI = new controler.UpdateUI() {
				public void run() {
				controler.Action <String,Object>  action = new controler.Action <String,Object>();
				action.put("task","exit");
				this.controler.task_checkout(action);
				}
			};
		updateUI.set_task(this.controler);
		javax.swing.SwingUtilities.invokeLater(updateUI);
		

	}//GEN-LAST:event_jMenuItemExitActionPerformed

	private void jMenuItemOpenFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemOpenFileActionPerformed
		controler.UpdateUI updateUI = new controler.UpdateUI() {
				public void run() {
				controler.Action <String,Object>  action = new controler.Action <String,Object> () ;
				action.put("task", "openfile");
				this.controler.task_checkout(action);
				}
			};
		updateUI.set_task(this.controler);
		javax.swing.SwingUtilities.invokeLater(updateUI);
	}//GEN-LAST:event_jMenuItemOpenFileActionPerformed

	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		/* Set the Nimbus look and feel */
		//<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
		 * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
		 */
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(Mainpage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(Mainpage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(Mainpage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(Mainpage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		//</editor-fold>

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {

			public void run() {
				new Mainpage().setVisible(true);
			}
		});
	}
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuBar jMenuBarMain;
    private javax.swing.JMenu jMenuFile;
    private javax.swing.JMenuItem jMenuItemExit;
    private javax.swing.JMenuItem jMenuItemOpenFile;
    // End of variables declaration//GEN-END:variables
}
