package com.homedepot.hr.hr.retailstaffing.model;

import java.io.FileInputStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.homedepot.hr.hr.retailstaffing.dao.DriversLicenseEncryptionDAO;
import com.homedepot.hr.hr.retailstaffing.enumerations.ApplicationObject;
import com.homedepot.hr.hr.retailstaffing.enumerations.InputField;
import com.homedepot.hr.hr.retailstaffing.exceptions.NoRowsFoundException;
import com.homedepot.hr.hr.retailstaffing.exceptions.ValidationException;
import com.homedepot.hr.hr.retailstaffing.interfaces.Constants;
import com.homedepot.hr.hr.retailstaffing.utils.ValidationUtils;
import com.homedepot.ta.aa.dao.exceptions.QueryException;
import com.rsa.kmc.HeaderFormat;
import com.rsa.kmc.KMConfig;
import com.rsa.kmc.KMContext;
import com.rsa.kmc.KMException;
import com.rsa.kmc.KMSimple;

public class EncryptionManager implements Constants
{
	// class name used to identify the RSA key
	//Added it below, getting from the hr_org_parm table
	//private static final String KEY_CLASS = "RetailStaffing-Test";	
	private static String KEY_CLASS = null;	
	
	/** logger instance */
	private static final Logger mLogger = Logger.getLogger(EncryptionManager.class);
	
	/** encryption server configuration object */
	private static KMConfig mServerConfig = null;
		
	// static block to load the encryption configuration
	static
	{
		long startTime = 0;
		
		//Get encryption configuration file path and name from hr_org_parm table 
		String configFileName = null;
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug("==================================================");
			mLogger.debug(String.format("Getting encryption configuration file name from HR_ORG_PARM"));			
		}
		
		try {
			configFileName = DriversLicenseEncryptionDAO.getEncryptionConfigFileName();
			if (configFileName == null || configFileName.equals("")) {
				mLogger.fatal(String.format("Encryption configuration file name not found."));
				throw new NoRowsFoundException(ApplicationObject.ENCRYPT_CONFIG, String.format("Encryption configuration file name not found in HR_ORG_PARM"));
			}
		} catch (QueryException qe) {
			mLogger.fatal(String.format("A query exception occurred getting encryption configuration file name %1$s", configFileName), qe);
		}
		
		//Get encryption Key Class Name
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug("==================================================");
			mLogger.debug(String.format("Getting encryption key_class name from HR_ORG_PARM"));			
		}
		
		try {
			KEY_CLASS = DriversLicenseEncryptionDAO.getEncryptionKeyClass();
			if (KEY_CLASS == null || KEY_CLASS.equals("")) {
				mLogger.fatal(String.format("Encryption Key Class name not found."));
				throw new NoRowsFoundException(ApplicationObject.ENCRYPT_CONFIG, String.format("Encryption Key Class name not found in HR_ORG_PARM"));
			}
		} catch (QueryException qe) {
			mLogger.fatal(String.format("A query exception occurred getting encryption Key Class name %1$s", KEY_CLASS), qe);
		}		
		
		
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Opening encryption configuration file %1$s", configFileName));
		} // end if
		
		FileInputStream fis = null;
		
		try
		{
			fis = new FileInputStream(configFileName);			
			if(mLogger.isDebugEnabled())
			{
				mLogger.debug(String.format("Successfully opened encryption configuration file %1$s", configFileName));
			} // end if
			
			// load the properties from the stream
			Properties encryptionProps = new Properties();
			encryptionProps.load(fis);			
			
			if(mLogger.isDebugEnabled())
			{
				mLogger.debug(String.format("Successfully loaded %1$d properties from encryption configuration file %2$s", encryptionProps.size(), configFileName));
			} // end if
			
			/*
			 * So this may be wrong, but the API leads me to believe that only a single encryption server configuration object
			 * needs to be created to spawn multiple encryption and decryption objects. Since this part of the process takes the
			 * most time (2+ seconds) I decided to do this once instead of every single request. This drastically improved
			 * performance taking encryption and decryption requests down to < 0.2 seconds instead of the 2+ seconds it was taking
			 * when including this step each request.  
			 */
			mServerConfig = new KMConfig(encryptionProps);
			
			if(mLogger.isDebugEnabled())
			{
				long endTime = System.nanoTime();
				
				if(startTime == 0)
				{
					startTime = endTime;
				} // end if(startTime == 0)
				
				mLogger.debug(String.format("Successfully initialized encryption server configuration object in %1$.9f seconds", (((double)endTime - startTime) / NANOS_IN_SECOND)));
				mLogger.debug("==================================================");
			} // end if
		} // end try
		catch(IOException ioe)
		{
			mLogger.fatal(String.format("An exception occurred loading encryption configuration file %1$s", configFileName), ioe);
		} // end catch
		catch(KMException kme)
		{
			mLogger.fatal(String.format("An exception occurred initializing the encryption server configuration object"), kme);
		} // end catch
		finally
		{
			// stream clean-up
			if(fis != null)
			{
				try
				{
					fis.close();
				} // end try
				catch(IOException ignore)
				{
					// not much more we can do, but log it anyway
					mLogger.error(String.format("An exception occurred closing input stream to encryption configuration file %1$s", configFileName));
				} // end catch
			} // end if(fis != null)
		} // end finally
	} // end static block
		
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#finalize()
	 */
	@Override
	protected void finalize() throws Throwable
	{
		/*
		 * overkill here and will likely never be executed, but you never know so let's be diligent and
		 * cleanup after ourselves. 
		 * 
		 * Shutdown the server configuration object if it was initialized
		 */
		if(mServerConfig != null)
		{		
			try
			{
				if(mLogger.isDebugEnabled())
				{
					mLogger.debug("==================================================");
					mLogger.debug("Shutting down encryption server configuration object");
				} // end if

				mServerConfig.shutdown();
				
				if(mLogger.isDebugEnabled())
				{
					mLogger.debug("Encryption server configuration object shutdown successfully");
					mLogger.debug("==================================================");
				} // end if				
			} // end try
			catch(KMException ignore)
			{
				// nothing we can do about it now, but log it anyway
				mLogger.error("An exception occurred shutting down encryption server configuration object", ignore);				
			} // end catch
		} // end if(mServerConfig != null)
	} // end function finalize()	
	
	/**
	 * Encrypt the plain text value provided
	 * 
	 * @param plainText the value to encrypt
	 * @return byte[] containing the encrypted value
	 * @throws ValidationException thrown if the plain text item provided is null or empty
	 * @throws KMException thrown if an exception occurs encrypting the plain text item provided
	 */
	public static byte[] encryptItem(String plainText) throws ValidationException, KMException
	{
		return encryptItems(plainText).get(0);
	} // end function encryptItem()
	
	/**
	 * Encrypt the plain text items provided
	 * 
	 * @param plainText plain text items to encrypt
	 * @return collection of byte[] containing the encrypted values. The encrypted values will be returned
	 * 		   in the same order they were passed in
	 * 
	 * @throws ValidationException thrown if no plain text items were provided or if any of the plain
	 * 									text items provided were null or empty
	 * @throws KMException thrown if an exception occurs encrypting the plain text items provided
	 */
	public static List<byte[]> encryptItems(String... plainText) throws ValidationException, KMException
	{
		long startTime = 0;
		List<byte[]> cipherText = null;
		
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering encryptItems(), %1$d values provided for encryption", plainText.length));
		} // end if

		KMSimple encrypter = null;
		
		try
		{
			// make sure the array provided is not null or empty
			ValidationUtils.validateNotNullOrEmpty(InputField.ITEMS_TO_ENCRYPT, plainText);
			
			// iterate all the plain text items provided
			for(String value : plainText)
			{
				// make sure the values provided are not null or empty
				if(value == null || value.trim().length() == 0)
				{
					ValidationUtils.validateNotNullOrEmpty(InputField.ITEM_TO_ENCRYPT, value);
				} // end if(value == null || value.trim().length() == 0)
			} // end for(String value : plainText)
			
			// initialize the return collection
			cipherText = new ArrayList<byte[]>(plainText.length);
			
			// use the encryption server configuration object to create an api object that will be used to perform encryption 
			encrypter = mServerConfig.newKMContext();
			
			byte[] cipher = null;
			
			// iterate each item provided
			for(String value : plainText)
			{
				if(mLogger.isDebugEnabled())
				{
					mLogger.debug(String.format("Encrypting value %1$s", value));
				} // end if
				
				// encrypt the item and put it in the return collection
				cipher = encrypter.encrypt(KEY_CLASS, value.getBytes(), HeaderFormat.VERSION_2_7);
				cipherText.add(cipher);		

				if(mLogger.isDebugEnabled())
				{
					mLogger.debug(String.format("Successfully encrypted value %1$s, encrypted value is %2$s", value, Arrays.toString(cipher)));
				} // end if
			} // end for(String value : plainText)
		} // end try
		catch(ValidationException ve)
		{
			// log the exception
			mLogger.error(ve.getMessage(), ve);
			// throw the error
			throw ve;
		} // end catch
		catch(KMException kme)
		{
			// log the exception
			mLogger.error("An exception occurred during encryption", kme);
			// throw the error
			throw kme;
		} // end catch
		finally
		{
			// if we initialized an encryption object and it's still open, close it
			if(encrypter != null && ((KMContext)encrypter).isOpen())
			{
				mLogger.debug("Closing encryption object");
				((KMContext)encrypter).close();
				mLogger.debug("Encryption object closed");
			} // end if(encrypter != null && ((KMContext)encrypter).isOpen())			
		} // end finally
		
		if(mLogger.isDebugEnabled())
		{
			long endTime = System.nanoTime();
			
			if(startTime == 0)			
			{
				startTime = endTime;
			} // end if(startTime == 0)
			
			mLogger.debug(String.format("Exiting encryptItems(), %1$d values encrypted in %2$.9f seconds", plainText.length,
				(((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if
		
		return cipherText;
	} // end function encryptItems()

	/**
	 * Decrypt the encrypted value provided	
	 * 
	 * @param cipherText the value to decrypt
	 * @return String containing the decrypted value
	 * 
	 * @throws ValidationException thrown if the encryted value provided is null or empty
	 * @throws KMException thrown if an exception occurs decrypting the encrypted value provided
	 */
	public static String decryptItem(byte[] cipherText) throws ValidationException, KMException
	{
		return decryptItems(cipherText).get(0);
	} // end function decryptItem()

	/**
	 * Decrypt the encrypted values provided
	 * 
	 * @param cipherText encrypted values to decrypt
	 * @return collection of encrypted items to decrypt. the decrypted values will be returned
	 * 			in the same order they were passed in
	 * 
	 * @throws ValidationException thrown if no encrypted items were provided or if any of the encrypted
	 * 									items provided were null or empty
	 * 
	 * @throws KMException thrown if an exception occurs decrypting the encrypted values provided
	 */
	public static List<String> decryptItems(byte[]... cipherText) throws ValidationException, KMException
	{
		long startTime = 0;
		List<String> plainText = null;
		
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering decryptItems(), %1$d values provided for decryption", cipherText.length));
		} // end if

		KMSimple decrypter = null;
		
		try
		{
			// make sure the array provided is not null or empty
			ValidationUtils.validateNotNullOrEmpty(InputField.ITEMS_TO_DECRYPT, cipherText);
			
			// iterate all the cipher text items provided
			for(byte[] value : cipherText)
			{
				// make sure the values provided are not null or empty
				ValidationUtils.validateNotNullOrEmpty(InputField.ITEM_TO_DECRYPT, value);
			} // end for(byte[] value : cipherText)
			
			// initialize the return collection
			plainText = new ArrayList<String>(cipherText.length);
			
			// use the encryption server configuration object to create an api object that will be used to perform decryption
			decrypter = mServerConfig.newKMContext();
			
			String plain = null;
			
			// iterate each item provided
			for(byte[] value : cipherText)
			{
				if(mLogger.isDebugEnabled())
				{
					mLogger.debug(String.format("Decrypting value %1$s", Arrays.toString(value)));
				} // end if
				
				// decrypt the item and put it in the return collection
				plain = new String(decrypter.decrypt(value));
				plainText.add(plain);

				if(mLogger.isDebugEnabled())
				{
					mLogger.debug(String.format("Successfully decrypted value %1$s, plain text value is %2$s", Arrays.toString(value), plain));
				} // end if
			} // end for(byte[] value : cipherText)
		} // end try
		catch(IllegalArgumentException iae)
		{
			// log the exception
			mLogger.error(String.format("Illegal Argument Provided. %1$s", iae.getMessage()), iae);
			// throw the error
			throw iae;
		} // end catch
		catch(KMException kme)
		{
			// log the exception
			mLogger.error("An exception occurred during decryption", kme);
			// throw the error
			throw kme;
		} // end catch
		finally
		{
			// if we initialized a decryption object and it's still open, close it
			if(decrypter != null && ((KMContext)decrypter).isOpen())
			{
				mLogger.debug("Closing decryption object");
				((KMContext)decrypter).close();
				mLogger.debug("Decryption object closed");
			} // end if(decrypter != null && ((KMContext)decrypter).isOpen())			
		} // end finally
		
		if(mLogger.isDebugEnabled())
		{
			long endTime = System.nanoTime();
			
			if(startTime == 0)			
			{
				startTime = endTime;
			} // end if(startTime == 0)
			
			mLogger.debug(String.format("Exiting decryptItems(), %1$d values decrypted in %2$.9f seconds", cipherText.length,
				(((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if
		
		return plainText;
	} // end function decryptItems()
} // end class EncryptionManager