/**   
* @Title: NormalFileReader.java 
* @Package org.brilliance.file 
* @Description: TODO
* @author Pie.Li   
* @date 2013-12-23 下午12:03:15 
* @version V1.0   
*/
package org.brilliance.file;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;

/**
 * @author Pie.Li
 *
 */
public class NormalFileReader {

	/**
	 * Logger
	 */
	private static final Logger logger =  Logger.getLogger(NormalFileReader.class);

	private String path;
	private FileCallBackInterface callback;
	
	public NormalFileReader(String path, FileCallBackInterface callback) {
		this.path = path;
		this.callback = callback;
	}
	
	/**
	 * 
	 */
	public void iterate() {
		
		FileInputStream inputStream = null;
		InputStreamReader reader = null;
		BufferedReader bufferedReader = null;
		try {
			inputStream = new FileInputStream(path);
			reader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(reader);	
			Method method = callback.getClass().getMethod("process", new Class[]{String.class});	
			
			String tempStringLine = null; //bufferedReader.readLine();
			
			while((tempStringLine = bufferedReader.readLine()) != null){
				method.invoke(callback, tempStringLine);
			}
		} catch (Exception e){
			logger.error(e.getMessage(), e);
		} finally {
			if(bufferedReader != null){
				try {
					bufferedReader.close();
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}	
			}
			if(reader != null){
				try {
					reader.close();
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
			if(inputStream != null){
				try {
					inputStream.close();
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
		
	}
	
}
