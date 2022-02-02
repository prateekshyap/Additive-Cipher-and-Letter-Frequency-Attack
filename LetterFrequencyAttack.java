/*
* Programming Problem - 3.25
* A program to automate the process of letter frequency attack on an additive cipher
*/

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

/*A class which holds alphabet indices (0-26) and their respective frequencies in the given cipher text*/
class Alphabet implements Comparable<Alphabet>
{
	private int alphabet, frequency;
	Alphabet(int a, int f)
	{
		this.alphabet = a;
		this.frequency = f;
	}
	public int getFrequency() { return this.frequency; }
	public int getAlphabet() { return this.alphabet; }
	@Override
	public int compareTo(Alphabet a) { return 0; }
}

class Heap
{
	private Alphabet[] frequencies;
	private char[] letterOrder;
	private int heapSize;
	Heap(int[] frequencies, char[] letterOrder)
	{
		this.frequencies = new Alphabet[frequencies.length];
		for (int i = 0; i < frequencies.length; ++i) //create array of alphabets
			this.frequencies[i] = new Alphabet(i,frequencies[i]);
		this.heapSize = frequencies.length; //heapsize will be initially equal to the total number of unique alphabets present
		this.letterOrder = letterOrder;
		build();
	}
	private int left(int i) { return (2*i)+1; }
	private int right(int i) { return (2*i)+2; }
	private void build() //standard build heap method
	{
		for (int i = (heapSize/2)-1; i >= 0; --i)
			heapify(i);
	}
	private void heapify(int root) //modified heapify method
	{
		int max = root; //store root at max
		int a = 0, b = 0;
		//if left child has greater frequency store in max
		if (left(root) < heapSize && frequencies[left(root)].getFrequency() > frequencies[max].getFrequency()) max = left(root);
		//if frequency of left child is same as that of the max, then select max according to the frequency statistics
		else if (left(root) < heapSize && frequencies[left(root)].getFrequency() == frequencies[max].getFrequency())
		{
			a = frequencies[left(root)].getAlphabet();
			b = frequencies[max].getAlphabet();
			for (int i = 0; i < letterOrder.length; ++i)
			{
				if (letterOrder[i]-'a' == a)
				{
					max = left(root);
					break;
				}
				else if (letterOrder[i]-'a' == b)
					break;
			}
		}
		//if right child has greater frequency store in max
		if (right(root) < heapSize && frequencies[right(root)].getFrequency() > frequencies[max].getFrequency()) max = right(root);
		//if frequency of right child is same as that of max, then select max according to the frequency statistics
		else if (right(root) < heapSize && frequencies[right(root)].getFrequency() == frequencies[max].getFrequency())
		{
			a = frequencies[right(root)].getAlphabet();
			b = frequencies[max].getAlphabet();
			for (int i = 0; i < letterOrder.length; ++i)
			{
				if (letterOrder[i]-'a' == a)
				{
					max = right(root);
					break;
				}
				else if (letterOrder[i]-'a' == b)
					break;
			}
		}
		if (max != root) //if max is not equal to root, swap them and heapify new max
		{
			Alphabet temp = frequencies[max];
			frequencies[max] = frequencies[root];
			frequencies[root] = temp;
			heapify(max);
		}
	}
	public int getMaxFrequencyAlphabet() //standard extract root operation of heap
	{
		Alphabet result = frequencies[0]; //hold the root
		frequencies[0] = frequencies[heapSize-1]; //replace root with the last element
		--heapSize; //reduce heap size
		heapify(0); //heapify root
		return result.getAlphabet(); //return the held value
	}
}

class LetterFrequencyAttack
{
	public static void main(String[] args) throws IOException
	{
		BufferedReader cmdReader, fileReader;
		BufferedWriter writer;
		int choice, k, diff = 0, maxFrequency, i = 0, j = 0, newChar = 0;
		String plainText = "", cipherText = "", inputFileName = "", nextLine = "";
		StringBuffer temp = new StringBuffer("");
		int[] frequencies;
		char[] letterOrder = {'e','t','a','o','i','n','s','r','h','d','l','u','c','m','f','y','w','g','p','b','v','k','x','q','j','z'}; //stores the letters in the order of their frequency statistics

		cmdReader = new BufferedReader(new InputStreamReader(System.in));

		System.out.println("----------------------------------------");
		System.out.println("       Letter Frequency Attack");
		System.out.println("----------------------------------------");
		System.out.println();
		System.out.println("Enter 0 for CMD input/1 for File input-");
		
		choice = Integer.parseInt(cmdReader.readLine());

		if (choice == 0) //read from cmd
		{
			System.out.println("Enter the cipher text-");
			cipherText = cmdReader.readLine();
		}
		else if (choice == 1) //read from file
		{
			System.out.println("Enter the file name-");
			inputFileName = cmdReader.readLine();
			fileReader = new BufferedReader(new FileReader(inputFileName));

			while ((nextLine = fileReader.readLine()) != null)
			{
				temp.append(nextLine);
				temp.append('\n');
			}
			cipherText = temp.toString();
			fileReader.close();
		}

		//store the frequencies
		frequencies = new int[26];
		for (i = 0; i < cipherText.length(); ++i)
		{
			if (cipherText.charAt(i) >= 65 && cipherText.charAt(i) <= 90)
				++frequencies[cipherText.charAt(i)-'A'];
			else if (cipherText.charAt(i) >= 97 && cipherText.charAt(i) <= 122)
				++frequencies[cipherText.charAt(i)-'a'];
		}

		System.out.println("How many top possible plaintexts do you want to see? (1 to 25)");
		k = Integer.parseInt(cmdReader.readLine());

		writer = new BufferedWriter(new FileWriter("LetterFrequencyAttackOutput.txt"));
		writer.write("Cipher Text-\n");
		writer.write(cipherText);
		writer.newLine();
		Heap heap = new Heap(frequencies, letterOrder); //create the heap
		for (i = 0; i < k; ++i)
		{
			maxFrequency = heap.getMaxFrequencyAlphabet(); //extract the alphabet with the maximum frequency
			diff = (letterOrder[i]-'a')-maxFrequency; //get the difference from the statistical maximum frequency alphabet
			temp = new StringBuffer("");
			for (j = 0; j < cipherText.length(); ++j) //for each character in the cipher text
			{
				if (cipherText.charAt(j) >= 'a' && cipherText.charAt(j) <= 'z') //add the difference to lower case letters
				{
					newChar = (cipherText.charAt(j)+diff)%122; //%122 for staying within the alphabets range
					if (diff > 0) newChar = newChar+(newChar < 97 ? 97 : 0); //adding 97 to confirm the range
					else if (diff < 0) newChar = newChar < 97 ? 123-(97-newChar) : newChar;
					temp.append((char)newChar);
				}
				else if (cipherText.charAt(j) >= 'A' && cipherText.charAt(j) <= 'Z') //add the difference to upper case letters
				{
					newChar = (cipherText.charAt(j)+diff)%90; //%90 for staying within the alphabets range
					if (diff > 0) newChar = newChar+(newChar < 65 ? 65 : 0); //adding 65 to confirm the range
					else if (diff < 0) newChar = newChar < 65 ? 91-(65-newChar) : newChar;
					temp.append((char)newChar);
				}
				else
					temp.append(cipherText.charAt(j)); //attach other characters as it is
			}

			//print the plain texts
			plainText = temp.toString();
			System.out.println("----------------------------------"+(i+1)+"----------------------------------");
			System.out.println(plainText);
			System.out.println();

			writer.write("----------------------------------"+(i+1)+"----------------------------------");
			writer.write(plainText);
			writer.newLine();
		}

		cmdReader.close();
		writer.close();
	}
}