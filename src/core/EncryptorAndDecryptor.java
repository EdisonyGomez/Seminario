
package core;

import encriptadores.EncryptorAndDecryptorProgress;
import encriptadores.ExceptionDialog;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;

/**
 *
 * @author Arlene , Yesid
 */
public class EncryptorAndDecryptor extends SwingWorker <Boolean,Boolean>
{
    File[] listOfFilesAndFolders;
    String encryptOrDecrypt;
    String key;
    FileEncryptorAndDecryptor fileEncryptorAndDecryptor;
    int totalNumberOfFiles;
    long totalSizeOfAllFiles;
    boolean completedTask;
    JProgressBar progressBar;
    JTextArea progressOfFilesTextField;
    JLabel progressPercentLabel;
    JButton oKButton;
    EncryptorAndDecryptorProgress progressFrame;
    
    public EncryptorAndDecryptor(File[] listOfFilesAndFolders, String encryptOrDecrypt, String key)
    {
        this.listOfFilesAndFolders = listOfFilesAndFolders;
        this.encryptOrDecrypt = encryptOrDecrypt;
        this.key=key;
        progressFrame= new EncryptorAndDecryptorProgress(encryptOrDecrypt);
        progressFrame.setVisible(true);
        fileEncryptorAndDecryptor = new FileEncryptorAndDecryptor();
        progressBar = progressFrame.getProgressBar();
        progressOfFilesTextField = progressFrame.getProgressOfFilesTextField();
        progressPercentLabel = progressFrame.getProgressPercentLabel();
        oKButton=progressFrame.getoKButton();
        setTotalSizeAndNumberOfAllFiles();
    }
    
    @Override
    protected Boolean doInBackground() 
    {
        try
        {
            if(encryptOrDecrypt.equalsIgnoreCase("encrypt"))
            {
                encrypt();
            }
            else if(encryptOrDecrypt.equalsIgnoreCase("decrypt"))
            {
                decrypt();            
            }
        }
        catch (Exception e)
        {
              new ExceptionDialog("Error del sistema!", "Ocurrio algo inesperado", e).setVisible(true);         
        }
        finally
        {
            return true;
        }
    }
    protected void done()
    {
        try
        {
        
        progressFrame.setCompletedTask(true);
        Toolkit.getDefaultToolkit().beep();
        oKButton.setVisible(true);
        oKButton.setEnabled(true);
        oKButton.setText("OK");
                    
        }
        catch (Exception e)
        {
            new ExceptionDialog("Error del Sistema!", "Ocurrio algo inesperado", e).setVisible(true);
        }
    }
    
    
    private void encrypt()
    {
        for(File file:listOfFilesAndFolders)
        {
            encrypt(file);
        }
        progressBar.setValue(progressBar.getMaximum());
        progressPercentLabel.setText("100%");
    }
    //ALGORITMO DES
    private void encrypt(File file)
    {
        if(!file.isDirectory() && file.exists())
        {
            progressOfFilesTextField.append("Encriptando "+file.getAbsolutePath()+"\n");
            fileEncryptorAndDecryptor.encrypt(file, key, progressBar, progressPercentLabel, totalSizeOfAllFiles, progressOfFilesTextField);
            progressOfFilesTextField.append("Hecho!\n\n");
        }
        else if(file.isDirectory() && file.exists())
        {
            File[] filesInTheDirectory=file.listFiles();
            progressOfFilesTextField.append("\n~~~~~~~~~~~~~~~~~~~~   Dentro "+file.getAbsolutePath()+"   ~~~~~~~~~~~~~~~~~~~~\n");
            for(File eachFileInTheDirectory:filesInTheDirectory)
            {
                encrypt(eachFileInTheDirectory);
            }
            progressOfFilesTextField.append("~~~~~~~~~~~~~~~~~~~~   de "+file.getAbsolutePath()+"   ~~~~~~~~~~~~~~~~~~~~\n\n");
        }
    }
    
    //ALGORITMO RC5
        public void encryptRC5() throws Exception{
        KeyExp ke = new KeyExp();                
        String s[] = ke.compute();
        System.out.println("Enter Input");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("a = ");
        String a = fullfill0(Long.toBinaryString(Long.parseLong(br.readLine(), 16)));
        System.out.print("b = ");
        String b = fullfill0(Long.toBinaryString(Long.parseLong(br.readLine(), 16)));
        a = add(a, fullfill0(s[0]));
        b = add(b, fullfill0(s[1]));
        int tmp = 0;
        for (int i = 1; i <= 12; i++) {                
            tmp = Integer.parseInt(""+Long.parseLong(b,2)%32);             
            a = xor(a, b);           
            a = a.substring(tmp)+a.substring(0,tmp);
            a = add(a, fullfill0(s[2 * i]));          
            tmp = Integer.parseInt(""+Long.parseLong(a,2)%32);
            b = xor(b, a);
            b = b.substring(tmp)+b.substring(0,tmp);
            b = add(b, fullfill0(s[(2 * i)+1]));
            System.out.println(i+" iteration = "+(Long.toHexString(Long.parseLong(a,2)))+(Long.toHexString(Long.parseLong(b,2))));
        }        
        String out = a+b;
        System.out.println("Output = "+(Long.toHexString(Long.parseLong((out.substring(0,32)),2)))+(Long.toHexString(Long.parseLong((out.substring(32)),2))));
    }
    
     public String fullfill0(String x) {
        return (get0(32-x.length())+ x);
    }
     
       public String xor(String x, String y) {
        String result = "";
        for (int i = 0; i < x.length(); i++) {
            if (x.charAt(i) == y.charAt(i)) {
                result += "0";
            } else {
                result += "1";
            }

        }
        return result;
    }
       
     public String add(String x, String y) {
        String result = "";
        boolean carry = false;
        for (int i = x.length()- 1; i >= 0; i--) {                      
            if((x.charAt(i) == y.charAt(i) && carry == false)|| (x.charAt(i) != y.charAt(i)  && carry == true)){
                result = "0"+result;                
            }else{
                result = "1"+result;
            }
            if((x.charAt(i) == '1' && y.charAt(i) == '1') || 
                    (x.charAt(i) == '1' && y.charAt(i) == '1' && carry == true) || 
                    (x.charAt(i) !=  y.charAt(i) && carry == true)){carry = true;}
            else{carry = false;}
        }       
        return result;
    }
     //FIN ALGORITMO RC5
    
     
     
     //ALGORITMO AES
      private SecretKeySpec crearClave(String clave) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        byte[] claveEncriptacion = clave.getBytes("UTF-8");
         
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
         
        claveEncriptacion = sha.digest(claveEncriptacion);
        claveEncriptacion = Arrays.copyOf(claveEncriptacion, 16);
         
        SecretKeySpec secretKey = new SecretKeySpec(claveEncriptacion, "AES");
 
        return secretKey;
    }
      
      public String encriptar(String datos, String claveSecreta) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        SecretKeySpec secretKey = this.crearClave(claveSecreta);
         
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");        
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
 
        byte[] datosEncriptar = datos.getBytes("UTF-8");
        byte[] bytesEncriptados = cipher.doFinal(datosEncriptar);
        String encriptado = Base64.getEncoder().encodeToString(bytesEncriptados);
 
        return encriptado;
    }
      
      //FIN ALGORITMO AES
     
     
     
    private void decrypt()
    {
        for(File file:listOfFilesAndFolders)
        {
                decrypt(file);
        }
        progressBar.setValue(progressBar.getMaximum());
        progressPercentLabel.setText("100%");
    }
    private void decrypt(File file)
    {
        if(!file.isDirectory() && file.exists() && file.getName().substring(file.getName().length()-4, file.getName().length()).equalsIgnoreCase(".enc"))
        {
            progressOfFilesTextField.append("Desemcriptando "+file.getAbsolutePath()+"\n");
            fileEncryptorAndDecryptor.decrypt(file, key, progressBar, progressPercentLabel, totalSizeOfAllFiles, progressOfFilesTextField);
            progressOfFilesTextField.append("Hecho!\n\n");
        }
        else if(file.isDirectory() && file.exists())
        {
            File[] filesInTheDirectory=file.listFiles();
            progressOfFilesTextField.append("\n~~~~~~~~~~~~~~~~~~~~   Dentro "+file.getAbsolutePath()+"   ~~~~~~~~~~~~~~~~~~~~\n");
            for(File eachFileInTheDirectory:filesInTheDirectory)
            {
                
                decrypt(eachFileInTheDirectory);
            }
            progressOfFilesTextField.append("~~~~~~~~~~~~~~~~~~~~   De "+file.getAbsolutePath()+"   ~~~~~~~~~~~~~~~~~~~~\n\n");
        }
    }
    
    
    private void setTotalSizeAndNumberOfAllFiles()
    {
        for(File file:listOfFilesAndFolders)
        {
            setTotalSizeAndNumberOfAllFiles(file);
        }
    }
    
    private void setTotalSizeAndNumberOfAllFiles(File file)
    {
        if(!file.isDirectory() && file.exists())
        {
            totalNumberOfFiles++;
            totalSizeOfAllFiles+=file.length();
        }
        else if(file.isDirectory() && file.exists())
        {
            File[] filesInTheDirectory=file.listFiles();
            
            for(File eachFileInTheDirectory:filesInTheDirectory)
            {
                setTotalSizeAndNumberOfAllFiles(eachFileInTheDirectory);
            }
        }
    }

    private String get0(int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
     
    
    
}