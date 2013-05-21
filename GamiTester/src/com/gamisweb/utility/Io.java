package com.gamisweb.utility;

import java.io.*;
import java.util.*;

public class Io {
    static int lineCount;

    public static ArrayList<QInfo> getRandQuestions(ArrayList<QInfo> questionSet, int numberOfQuestions) {
        ArrayList<QInfo> randomQuestion = new ArrayList<QInfo>();
        ArrayList<QInfo> tempQuestion = new ArrayList<QInfo>();
        QInfo ranQInfo;
        QInfo tempQInfo;
        boolean weightCheck;
        int randomTen;

        for (int x = 0; x < numberOfQuestions; x++) {
            tempQInfo = new QInfo();
            ranQInfo = new QInfo();
            weightCheck = false;
            int count = ranLarge(questionSet.size() - 1);            //randomly gets question from Question Array
            while (!weightCheck) {
                randomTen = (int) Math.round(Math.random() * 10) % 10;
                if (questionSet.get(count).getWeight() <= randomTen) {
                    tempQInfo = questionSet.get(count); // sets the temp object to the old object
                    questionSet.remove(count);
                    weightCheck = true;
                }
            }
            tempQInfo = questionSet.get(count); // sets the temp object to the old object
            questionSet.remove(count); // deletes the old object so it can't be randomly selected again

            //retrieves correct number, question text and generates random order for question answers
            ranQInfo.setSection(tempQInfo.getSection());
            ranQInfo.setText(tempQInfo.getText());
            ranQInfo.setDatabaseID(tempQInfo.getDatabaseID());
            ranQInfo.setNumber(x);
            for (int i = 0; i < 4; i++) {
                int random;
                do {
                    random = ranSmall();
                }
                while (!ranQInfo.getChoice(random).equalsIgnoreCase(""));
                if (i == tempQInfo.getCorrect()) {
                    ranQInfo.setCorrect(random);
                }
                ranQInfo.setChoice(random, tempQInfo.getChoice(i));
            }
            tempQuestion.add(ranQInfo);
        }
        for (int x = 0; x < tempQuestion.size(); x++) {
            randomQuestion.add(tempQuestion.get(x));

        }

        return randomQuestion;
    }


    public static int ranSmall() {
        return (int) Math.round(Math.random() * 10) % 4;
    }

    public static int ranLarge(int mod) {
        return (int) Math.round(Math.random() * 1000) % mod;
    }


    public static ArrayList<QInfo> getTextQuestions(BufferedReader textFile) {
        ArrayList<QInfo> qSet = new ArrayList<QInfo>();

        String temp = "";

        int count = 1;
        boolean EOF = false;
        try {
            while (temp != null && EOF == false) {
                QInfo qTemp = new QInfo();
                temp = textFile.readLine();
                //	System.out.println(temp);
                int x = 0;
                //System.out.println(x);
                while ((temp != null) && !temp.equalsIgnoreCase("")) {
                    if (x == 0) {
                        qTemp.setNumber(Integer.parseInt(temp.substring(0, temp.indexOf("."))));
                        //		System.out.println(qTemp.getNumber());
                        qTemp.setSection(temp.substring(temp.indexOf("(") + 1,
                                temp.indexOf(")")));
                        //		System.out.println(qTemp.getSection());
                        qTemp.setText(temp.substring(temp.indexOf(")") + 1));
                    } else
                        qTemp.setText(qTemp.getText() + " " + temp);
                    //	System.out.println(qTemp.getText());

                    temp = textFile.readLine();
                    //	System.out.println(temp);

                    x++;
                }

                for (int i = 0; i < 4; i++) {
                    temp = textFile.readLine();
                    if ((temp != null) && !temp.equalsIgnoreCase("")) {
                        if (temp.endsWith("*")) {
                            qTemp.setCorrect(i);
                            qTemp.setChoice(i, temp.substring(3, temp.lastIndexOf("*")));
                        } else
                            qTemp.setChoice(i, temp.substring(3));
                    }
                }

                qSet.add(qTemp);
                try {
                    temp = textFile.readLine();
                } catch (EOFException e) {
                    EOF = true;
                }
                count++;
                lineCount = count;

            }
            textFile.close();

        } catch (FileNotFoundException e) {
            System.out.println(textFile + " does not exist");
        } catch (IOException e) {
        } catch (StringIndexOutOfBoundsException e) {
            System.out.println("There has been an error reading at Question # " + (lineCount - 1));
        } catch (NumberFormatException e) {
            System.out.println("There has been an error reading at Question # " + (lineCount - 1));
        }
        return qSet;

    }


/*used in old program
    public static void renumber(String qFile)
	{
		BufferedReader in;
		PrintWriter out;
		String temp = "";
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
	*/
}