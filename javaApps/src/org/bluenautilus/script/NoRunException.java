package org.bluenautilus.script;

/**
 * Created by bstevens on 12/29/13.
 */
public class NoRunException extends Exception {

	public NoRunException(){
	   super("Detected a NO_RUN command from script");
	}
}
