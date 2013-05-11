package com.gamisweb.utility;
import java.io.*;
import android.content.res.AssetManager;
import android.database.Cursor;

import java.util.*;




//import javax.swing.*;




public class Io
{
	static int lineCount;

	public static ArrayList<QInfo> getRandQuestions(ArrayList<QInfo> questionSet,int numberOfQuestions){
		ArrayList<QInfo> randomQuestion = new ArrayList<QInfo>();
		QInfo ranQInfo;
		ArrayList<QInfo> tempQuestion = new ArrayList<QInfo>();
		QInfo tempQInfo;
		for(int x=0;x<numberOfQuestions;x++)
		{	
		//	System.out.println("Random Question Round: " + x);
			tempQInfo = new QInfo();
			ranQInfo = new QInfo();
			//randomly gets question from Question Array
			int count = ranLarge(questionSet.size() - 1);
			tempQInfo = questionSet.get(count);
		//	System.out.println("Correct Before: "+ tempQInfo.getCorrect());
		//	System.out.println("tempQInfo:");
		//	tempQInfo.printQuestion();
			questionSet.remove(count);
			
			//retrieves correct answer and generates random order for question answers 
			ranQInfo.setSection(tempQInfo.getSection());
			ranQInfo.setText(tempQInfo.getText());
			for (int i = 0; i < 4; i++)
			{
				int random;
				do
				{
					random = ranSmall();
		//			System.out.println("Random number: " + random);
				}
				while (!ranQInfo.getChoice(random).equalsIgnoreCase(""));
				if(i==tempQInfo.getCorrect()){ 
					ranQInfo.setCorrect(random);
					}
			//	System.out.println("Correct after: "+ ranQInfo.getCorrect());
				ranQInfo.setChoice(random, tempQInfo.getChoice(i));                  
			}
			
			tempQuestion.add(ranQInfo);
		//	System.out.println("ranQInfo:");
		//	ranQInfo.printQuestion();
		}
		for(int x=0;x<tempQuestion.size();x++){
			randomQuestion.add(tempQuestion.get(x));
			
		}
		
		return randomQuestion;
	}	







	public static Vector getQuestions(String qFile)
	{
		Vector qset = new Vector(1, 1);
		Vector outSet = new Vector(1, 1);
		QInfo qTemp = new QInfo();
		QInfo ansTemp = new QInfo();
		String temp = "";
		String line = "";
		BufferedReader file = null;

		try
		{
			file = new BufferedReader(new FileReader(qFile));

			while (temp != null)
			{
				qTemp = new QInfo();
				temp = file.readLine();
				int x = 0;
				while ( (temp != null) && !temp.equalsIgnoreCase(""))
				{
					if (x == 0)
					{
						qTemp.setSection(temp.substring(temp.indexOf("(") + 1,
								temp.indexOf(")")));
						qTemp.setText(qTemp.getText() + " " +
								temp.substring(temp.indexOf(")") + 2));
					}
					else
						qTemp.setText(qTemp.getText() + " " + temp);

					temp = file.readLine();

					x++;
				}

				for (int i = 0; i < 4; i++)
				{
					temp = file.readLine();
					if ( (temp != null) && !temp.equalsIgnoreCase(""))
					{
						int random;
						do
						{
							random = ranSmall();
						}
						while (!qTemp.getChoice(random).equalsIgnoreCase(""));

						if (temp.charAt(temp.length() - 1) == '*')
						{

							qTemp.setChoice(random, temp.substring(3, temp.length() - 1));
							qTemp.setCorrect(random);

						}
						else
							qTemp.setChoice(random, temp.substring(3));
					}
				}

				qset.addElement(qTemp);
				file.readLine();

			}
			file.close();

		}

		catch (FileNotFoundException e)
		{
			System.exit(0);
		}
		catch (IOException e)
		{
			System.out.println("Error reading from file " + qFile);
		}

		while (qset.size() > 1)
		{
			int count = ranLarge(qset.size() - 1);
			qTemp = (QInfo)qset.elementAt(count);
			outSet.addElement(qTemp);
			qset.removeElementAt(count);
		}
		qTemp = (QInfo)qset.elementAt(0);
		outSet.addElement(qTemp);
		qset.removeElementAt(0);
		return outSet;
	}
	/*
  public static ArrayList<QInfo> getdatRandomQuestions(Cursor cursor)
  {
	ArrayList<QInfo> qSet = new ArrayList<QInfo>();
    ArrayList<QInfo> outSet = new ArrayList<QInfo>();
    QInfo qTemp = new QInfo();
    QInfo ansTemp = new QInfo();
    String temp = "";
    String line = "";
    DataInputStream file;
    boolean EOF = false;

    try
    {
      file = new DataInputStream(new FileInputStream(qFile));
      while (temp != null && EOF == false)
      {
        qTemp = new QInfo();
        temp = file.readUTF().trim();
        int x = 0;
       while ( (temp != null) && !temp.equalsIgnoreCase(""))
        {
         if (x == 0)
         {
          qTemp.setNumber(Integer.parseInt(temp.substring(0, temp.indexOf("."))));
           qTemp.setSection(temp.substring(temp.indexOf("(") + 1,
                                            temp.indexOf(")")));
            qTemp.setText(temp.substring(temp.indexOf(")") + 1));

          }
          else
            qTemp.setText(qTemp.getText() + " " + temp);

          temp = file.readUTF().trim();

          x++;
        }

        for (int i = 0; i < 4; i++)
        {
          temp = file.readUTF().trim();
          if ( (temp != null) && !temp.equalsIgnoreCase(""))
          {
            int random;
            do
            {
              random = ranSmall();
            }
            while (!qTemp.getChoice(random).equalsIgnoreCase(""));

            if (temp.endsWith("*"))
            {

              qTemp.setChoice(random, temp.substring(3, temp.length() - 1));
              qTemp.setCorrect(random);

            }
            else
              qTemp.setChoice(random, temp.substring(3));
          }
        }

        qset.addElement(qTemp);
        try
        {
          temp = file.readUTF();
        }
        catch (EOFException e)
        {
          EOF = true;
        }

      }
      qset.addElement(qTemp);
      file.close();

    }

    catch (FileNotFoundException e)
    {
      System.out.println(qFile + " file not found");
    }
    catch (IOException e)
    {
      System.out.println("Error reading from file " + qFile + "@ ");
    }

    while (qset.size() > 1)
    {
      int count = ranLarge(qset.size() - 1);
      qTemp = (QInfo)qset.elementAt(count);
      outSet.addElement(qTemp);
      qset.removeElementAt(count);
    }
    qTemp = (QInfo)qset.elementAt(0);
    outSet.addElement(qTemp);
    qset.removeElementAt(0);
    return outSet;

  }
	 */
	public static int ranSmall()
	{
		return (int)Math.round(Math.random() * 10) % 4;
	}

	public static int ranLarge(int mod)
	{
		return (int)Math.round(Math.random() * 1000) % mod;
	}

	public static void readTextWriteDB(String qFile)
	{

		String temp = "";
		String line = "";
		BufferedReader textFile;
		DataOutputStream datFile;
		String outFile = ("dat" + qFile.substring(3, 4) + ".dat");

		try
		{
			textFile = new BufferedReader(new FileReader(qFile));
			datFile = new DataOutputStream(new FileOutputStream(outFile));

			temp = textFile.readLine();
			while (temp != null)
			{

				datFile.writeUTF(temp);
				datFile.flush();
				temp = textFile.readLine();

			}
			datFile.close();
			textFile.close();

		}

		catch (FileNotFoundException e)
		{
			System.exit(0);
		}
		catch (IOException e)
		{
			System.out.println("Error reading from file " + qFile);
		}

	}

	public static String readdat(String file)
	{
		String output = "";
		String temp;
		DataInputStream datFile;
		boolean EOF = false;
		try
		{

			datFile = new DataInputStream(new FileInputStream(file));

			temp = datFile.readUTF();
			while (EOF != true)
			{
				output = output + temp + '\n';
				try
				{
					temp = datFile.readUTF();
				}
				catch (EOFException e)
				{
					EOF = true;
				}
			}
			datFile.close();
		}

		catch (FileNotFoundException e)
		{
			System.exit(0);
		}
		catch (IOException e)
		{
			System.out.println("Error reading from file " + file);

		}

		return output;
	}

	public static ArrayList<QInfo> getTextQuestions(BufferedReader textFile){
		ArrayList<QInfo> qSet = new ArrayList<QInfo>();


		QInfo ansTemp = new QInfo();
		String temp = "";
		String line = "";

		int count =1;
		boolean EOF = false;
		try
		{
			while (temp != null && EOF == false)
			{
				QInfo qTemp = new QInfo();
				temp = textFile.readLine();
				System.out.println(temp);
				int x = 0;
				System.out.println(x);
				while ( (temp != null) && !temp.equalsIgnoreCase(""))
				{
					if (x == 0)
					{
						qTemp.setNumber(Integer.parseInt(temp.substring(0, temp.indexOf("."))));
						System.out.println(qTemp.getNumber());
						qTemp.setSection(temp.substring(temp.indexOf("(") + 1,
								temp.indexOf(")")));
						System.out.println(qTemp.getSection());
						qTemp.setText(temp.substring(temp.indexOf(")") + 1));
					}
					else
						qTemp.setText(qTemp.getText() + " " + temp);
					System.out.println(qTemp.getText());

					temp=textFile.readLine();
					System.out.println(temp);

					x++;
				}

				for (int i = 0; i < 4; i++)
				{
					temp = textFile.readLine();
					if ( (temp != null) && !temp.equalsIgnoreCase(""))
					{
						if (temp.endsWith("*"))
						{
							qTemp.setCorrect(i);
							qTemp.setChoice(i, temp.substring(3, temp.lastIndexOf("*")));
						}
						else
							qTemp.setChoice(i, temp.substring(3));
					}
				}

				qSet.add(qTemp);
				try
				{
					temp = textFile.readLine();
				}
				catch (EOFException e)
				{
					EOF = true;
				}
				count++;
				lineCount = count;

			}
			textFile.close();

		}

		catch (FileNotFoundException e)
		{
			System.out.println(textFile +" does not exist");
		}
		catch (IOException e)
		{
		}
		catch (StringIndexOutOfBoundsException e)
		{
			System.out.println("There has been an error reading at Question # " + (lineCount -1));
		}
		catch(NumberFormatException e)
		{
			System.out.println("There has been an error reading at Question # " + (lineCount -1));
		}
		return qSet;

	}


	public static Vector getdatQuestions(String qFile)
	{
		Vector qset = new Vector(1, 1);
		QInfo qTemp = new QInfo();
		QInfo ansTemp = new QInfo();
		String temp = "";
		String line = "";
		DataInputStream file;
		int count =1;
		boolean EOF = false;
		try
		{
			file = new DataInputStream(new FileInputStream(qFile));
			while (temp != null && EOF == false)
			{
				qTemp = new QInfo();
				temp = file.readUTF().trim();
				int x = 0;
				while ( (temp != null) && !temp.equalsIgnoreCase(""))
				{
					if (x == 0)
					{
						qTemp.setNumber(Integer.parseInt(temp.substring(0, temp.indexOf("."))));
						qTemp.setSection(temp.substring(temp.indexOf("(") + 1,
								temp.indexOf(")")));
						qTemp.setText(temp.substring(temp.indexOf(")") + 1));
					}
					else
						qTemp.setText(qTemp.getText() + " " + temp);

					temp = file.readUTF().trim();

					x++;
				}

				for (int i = 0; i < 4; i++)
				{
					temp = file.readUTF().trim();
					if ( (temp != null) && !temp.equalsIgnoreCase(""))
					{
						if (temp.endsWith("*"))
						{
							qTemp.setCorrect(i);
							qTemp.setChoice(i, temp.substring(3, temp.lastIndexOf("*")));
						}
						else
							qTemp.setChoice(i, temp.substring(3));
					}
				}

				qset.addElement(qTemp);
				try
				{
					temp = file.readUTF().trim();
				}
				catch (EOFException e)
				{
					EOF = true;
				}
				count++;
				lineCount = count;

			}
			file.close();

		}

		catch (FileNotFoundException e)
		{
			System.exit(0);
		}
		catch (IOException e)
		{
		}
		catch (StringIndexOutOfBoundsException e)
		{
			System.out.println("There has been an error reading at Question # " + (lineCount -1));
		}
		catch(NumberFormatException e)
		{
			System.out.println("There has been an error reading at Question # " + (lineCount -1));
		}
		return qset;

	}

	public static void writeDatQuestions(String qFile, Vector qset)
	{
		QInfo qTemp = new QInfo();
		DataOutputStream file;
		try
		{
			file = new DataOutputStream(new FileOutputStream(qFile));
			for (int i = 0; i < qset.size(); i++)
			{
				qTemp = (QInfo)qset.elementAt(i);
				file.writeUTF( (i + 1) + ". (" + qTemp.getSection() + ")" +
						qTemp.getText().trim());
				System.out.println( (i + 1) + ". (" + qTemp.getSection() + ") " +
						qTemp.getText());
				file.writeUTF("");

				for (int x = 0; x < 4; x++)
				{
					if (qTemp.getCorrect() == x)
						file.writeUTF("A. " + qTemp.getChoice(x) + "*");
					else
						file.writeUTF("A. " + qTemp.getChoice(x));
				}
				if (i != qset.size() - 1)
					file.writeUTF("");
			}
			file.close();

		}
		catch (FileNotFoundException e)
		{
			System.out.print("Blah");
		}
		catch (IOException e)
		{
			System.out.print("Blah2");
		}

	}

	public static void renumber(String qFile)
	{
		BufferedReader in;
		PrintWriter out;
		String temp = "";
		String line = "";
		boolean EOF = false;
		int number;
		try
		{
			in = new BufferedReader(new FileReader(qFile));
			out = new PrintWriter(new FileOutputStream("renumbered.txt"));
			while (temp != null && EOF == false)
			{
				temp = in.readLine().trim();

				int x = 0;
				while ( (temp != null) && !temp.equalsIgnoreCase(""))
				{
					if (x == 0)
					{
						number = Integer.parseInt(temp.substring(0, temp.indexOf(".")));
						number = number + 100;
						out.println(number + temp.substring(temp.indexOf(".")));
					}
					else
						out.println(temp);

					temp = in.readLine().trim();
					x++;

				}
				out.println(temp);
				out.println(in.readLine());
				out.println(in.readLine());
				out.println(in.readLine());
				out.println(in.readLine());
				temp = in.readLine();
				out.println(temp);
			}
			in.close();
			out.close();
		}
		catch (FileNotFoundException e)
		{
			System.exit(0);
		}
		catch (IOException e)
		{
			System.out.println("Error reading from file " + qFile);
		}

	}
	public static int[] getLog(String logFile,int size)
	{
		int log[] = new int[size];
		BufferedReader fileIn;
		//  String temp;
		try
		{
			fileIn = new BufferedReader( new FileReader(logFile));
			for(int index = 0;index<size;index++)
			{
				log[index] = Integer.parseInt(fileIn.readLine().trim());
			}
			fileIn.close();
		}
		catch (FileNotFoundException e)
		{
			return log;
		}
		catch (IOException e)
		{

		}
		return log;
	}
	public static void outputSection(String datfile,String logfile)
	{
		QInfo a = new QInfo();
		Vector qset = new Vector(1, 1);
		qset = getdatQuestions(datfile);
		String textA="";
		String textB="";
		PrintWriter fileOut;
		try
		{
			fileOut = new PrintWriter(new FileOutputStream(logfile));
			for(int index =0;index<qset.size();index++)
			{
				a = (QInfo)qset.elementAt(index);
				textA = a.getSection();
				if(!textA.equalsIgnoreCase(textB))
				{
					fileOut.println(textA + " 0");
					textB = textA;
				}
			}
			fileOut.close();
		}
		catch(FileNotFoundException e)
		{

		}
		catch(IOException e)
		{

		}
	}
	public static void readTextWriteDat(String qFile)
	{

		String temp = "";
		String line = "";
		BufferedReader textFile;
		DataOutputStream datFile;
		String outFile = ("dat" + qFile.substring(3, 4) + ".dat");

		try
		{
			textFile = new BufferedReader(new FileReader(qFile));
			datFile = new DataOutputStream(new FileOutputStream(outFile));

			temp = textFile.readLine();
			while (temp != null)
			{

				datFile.writeUTF(temp);
				datFile.flush();
				temp = textFile.readLine();

			}
			datFile.close();
			textFile.close();

		}

		catch (FileNotFoundException e)
		{
			System.exit(0);
		}
		catch (IOException e)
		{
			System.out.println("Error reading from file " + qFile);
		}
	}
	public static void printToText(String qFile, Vector qset)
	{
		QInfo qTemp = new QInfo();
		DataOutputStream file;
		PrintWriter out;
		try
		{
			out = new PrintWriter(new FileOutputStream(qFile));
			for (int i = 0; i < qset.size(); i++)
			{
				qTemp = (QInfo)qset.elementAt(i);
				out.println( (i + 1) + ". (" + qTemp.getSection() + ")" +
						qTemp.getText().trim());
				System.out.println( (i + 1) + ". (" + qTemp.getSection() + ") " +
						qTemp.getText());
				out.println("");
				char choice = 'a';
				for (int x = 0; x < 4; x++)
				{
					if (qTemp.getCorrect() == x)
						out.println(choice +". " + qTemp.getChoice(x) + "*");
					else
						out.println(choice +". " + qTemp.getChoice(x));
					choice++;
				}
				if (i != qset.size() - 1)
					out.println("");

			}
			out.close();
		}
		catch (FileNotFoundException e)
		{
			System.out.print("Blah");
		}
		catch (IOException e)
		{
			System.out.print("Blah2");
		}


	}

	/* public static Vector getSections(String datFile,String logFile)
  {
    Vector missedTotals = new Vector(1, 1);
    int y=0;
    BufferedReader textFile;
    String temp ="";
 try
    {
      textFile = new BufferedReader(new FileReader(logFile));
      temp = textFile.readLine();
      while (temp != null)
      {
       Section tempSection = new Section();
        tempSection.setSection(temp.substring(0,3));
        tempSection.setMissed(Integer.parseInt(temp.substring(4)));
        missedTotals.addElement(tempSection);
        temp = textFile.readLine();
        System.out.print(tempSection.getSection());
        System.out.println(" " + tempSection.getMissed());
      }
      textFile.close();
    }

    catch (FileNotFoundException e)
    {
      outputSection(logFile, datFile);
    }
    catch (IOException e)
    {
    }

    return missedTotals;
  }
  public static void outputSectionMissed(Vector missed,String setFile)
{
    PrintWriter fileOut;
    try
    {
      fileOut = new PrintWriter(new FileOutputStream(setFile));
      for(int index =0;index<missed.size();index++)
      {
        fileOut.println(((Section)missed.elementAt(index)).getSection()
        + " " + ((Section)missed.elementAt(index)).getMissed());
      }
      fileOut.close();
    }
    catch(FileNotFoundException e)
    {

    }
    catch(IOException e)
    {

    }
  }
	 */
}