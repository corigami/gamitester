package com.gamisweb.utility;
import java.io.Serializable;
public class QInfo implements Serializable

{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String text;
	private String[] choice = new String[4];
	private int correct=0;
        private int marked=5;
        private int number;
        private String section;
        private int weight=0;



	public QInfo()
	{
        text = "";
		for(int x = 0;x < 4;x++)
                {

                   choice[x] = "";
                }

	}
	public QInfo( String t, String[] a, int m, int n, String s,int w){
		this.text = t;
		this.choice = a;
		this.marked = m;
		this.number = n;
		this.section = s;
		this.weight = w;
	}
	public QInfo copy(QInfo toCopy){
		QInfo newCopy = new QInfo();
		newCopy.text = toCopy.text;
		newCopy.correct = toCopy.correct;
		newCopy.number = toCopy.number;
		newCopy.section = toCopy.section;
		newCopy.weight = toCopy.weight;

        //copies one array to another.
        System.arraycopy(toCopy.choice, 0, newCopy.choice, 0, 4);

		return newCopy;
		
	}

        public void setChoice(int x, String info)
        {
        choice[x]= info;
        }

        public String getChoice(int x)
        {
        return choice[x];
        }

        public void printChoice()
        {
          for(int x = 0;x < 4;x++)
                {
                  System.out.println(choice[x]);
                }
          }
        public void setText(String body)
        {
        text = body;
        }
        public String getText()
        {
          return text;
          }
    	public int getWeight() {
			return weight;
		}
		public void setWeight(int w) {
			weight = w;
		}
        public void printQuestion()
        {

        System.out.println(section);
        System.out.println(text);
        System.out.println(choice[0]);
        System.out.println(choice[1]);
        System.out.println(choice[2]);
        System.out.println(choice[3]);
        System.out.println(correct);
        }
        public void setCorrect(int x)
        {
          correct = x;
          }
        public int getCorrect()
        {
        return correct;
        }
        public int getMarked()
        {
          return marked;
        }

        public void setMarked(int x)
        {
          marked = x;

        }
        public int getNumber()
        {
          return number;
        }

        public void setNumber(int x)
        {
          number = x;

        }
        public void setSection(String sec)
        {
          section = sec;
        }
        public String getSection()
        {
          return section;
        }

       }







