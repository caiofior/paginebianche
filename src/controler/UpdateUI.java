/*
 * Updates the windows
 */
package controler;
/**
 *
 * @author claudiofior
 */
public abstract class UpdateUI implements Runnable{
	protected Controler controler;
	/**
	 * Sets the controller
	 * @param controler 
	 */
	public void set_task(Controler controler){
		this.controler = controler;
	}
	/**
	 * Actions to be taken
	 */
	public void run() {
		
	}
}
