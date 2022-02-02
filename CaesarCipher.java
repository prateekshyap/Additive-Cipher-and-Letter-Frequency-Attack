/*
* Programming Problem - 3.23
* A program that can encrypt and decrypt using general Caesar Cipher
*
* C = E(k,P) = (P+k) mod 26
* P = D(k,C) = (C-k) mod 26
* k can range from 1 to 25
*
* Plain text -  meet me after the toga party
* Cipher text - phhw ph diwhu wkh wrjd sduwb
*/

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

class CaesarCipher
{
	public static void main(String[] args)throws IOException
	{
		//////////////////////variables//////////////////////////////////////////////////////////////////////////
		int choice, k = 1;
		String plainText = "", cipherText = "", inputFileName = "", nextLine = "";
		StringBuffer temp = new StringBuffer("");
		BufferedReader cmdReader, fileReader;
		BufferedWriter writer;

		cmdReader = new BufferedReader(new InputStreamReader(System.in));

		System.out.println("----------------------------------------");
		System.out.println("            Caesar Cipher");
		System.out.println("----------------------------------------");
		System.out.println();
		System.out.println("Enter 0 for CMD input/1 for File input-");
		
		choice = Integer.parseInt(cmdReader.readLine());

		if (choice == 0) //command input
		{
			System.out.println();
			System.out.println("Enter k value-");
			System.out.println("(k should range from 1 to 25 inclusive,\nany other number will result in\nrandom generation of valid k value)");
			
			k = Integer.parseInt(cmdReader.readLine());
			k = (k < 1 || k > 25) ? (int)(Math.random()*(25-1+1)+1) : k;
			
			System.out.println();
			System.out.println("Enter message-");
			
			plainText = cmdReader.readLine();
		}
		else if (choice == 1) //file input
		{
			System.out.println();
			System.out.println("Write a file in the below format and enter the file name-");
			System.out.println("23\nI am a student\nI have taken CNS");
			System.out.println();
			System.out.println("(23 is k value and rest part is multiline plaintext)");
			System.out.println("(k should range from 1 to 25 inclusive,\nany other number will result in\nrandom generation of valid k value)");
			
			inputFileName = cmdReader.readLine();
			fileReader = new BufferedReader(new FileReader(inputFileName));
			k = Integer.parseInt(fileReader.readLine());
			k = (k < 1 || k > 25) ? (int)(Math.random()*(25-1+1)+1) : k;
			while ((nextLine = fileReader.readLine()) != null)
			{
				temp.append(nextLine);
				temp.append('\n');
			}
			plainText = temp.toString();
			fileReader.close();
		}

		//encryption
		temp = new StringBuffer("");
		for (int i = 0; i < plainText.length(); ++i)
		{
			if (plainText.charAt(i) >= 'a' && plainText.charAt(i) <= 'z') //encrypt for lower case letters
				temp.append((char)((((plainText.charAt(i)-'a')+k)%26)+'a'));
			else if (plainText.charAt(i) >= 'A' && plainText.charAt(i) <= 'Z') //encrypt for upper case letters
				temp.append((char)((((plainText.charAt(i)-'A')+k)%26)+'A'));
			else temp.append(plainText.charAt(i)); //attach other characters as it is
		}

		//print the cipher text
		cipherText = temp.toString();
		System.out.println();
		System.out.println("k value: "+k);
		System.out.println(cipherText);

		//decryption
		temp = new StringBuffer("");
		for (int i = 0; i < cipherText.length(); ++i)
		{
			if (cipherText.charAt(i) >= 'a' && cipherText.charAt(i) <= 'z') //decrypt for lower case letters
			{
				int tempValue = (((cipherText.charAt(i)-'a')-k)%26);
				if (tempValue < 0) tempValue = 26+tempValue;
				temp.append((char)(tempValue+'a'));
			}
			else if (cipherText.charAt(i) >= 'A' && cipherText.charAt(i) <= 'Z') //decrypt for upper case letters
			{
				int tempValue = (((cipherText.charAt(i)-'A')-k)%26);
				if (tempValue < 0) tempValue = 26+tempValue;
				temp.append((char)(tempValue+'A'));
			}
			else temp.append(cipherText.charAt(i)); //attach other characters as it is
		}

		//print the secrypted plain text
		plainText = temp.toString();
		System.out.println("After decryption-");
		System.out.println(plainText);

		cmdReader.close();

		/*No formatting of the output file is done because this same file is being used as input in letter frequency attack program*/
		writer = new BufferedWriter(new FileWriter("CaesarOutput.txt"));
		writer.write(cipherText);
		writer.close();
	}
}