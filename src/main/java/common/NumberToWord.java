package common;

import java.text.DecimalFormat;


public class NumberToWord {
	
	 
	String[] SingleTable=new String[11];
	String[] TeenTable=new String[10];
	String[] TensTable=new String[10];
	String[] OtherTable=new String[5];

	
	String[] MVAL=new String[9];
	
	String[] MTWO=new String[10];
	
	private boolean iArabic=false;

    private void StoreWordsInArray()
    {
        //To SingleTable Array Starts
        if(iArabic == false)
        {
            SingleTable[0] = "";
            SingleTable[1] = "One";
            SingleTable[2] = "Two";
            SingleTable[3] = "Three";
            SingleTable[4] = "Four";
            SingleTable[5] = "Five";
            SingleTable[6] = "Six";
            SingleTable[7] = "Seven";
            SingleTable[8] = "Eight";
            SingleTable[9] = "Nine";
            SingleTable[10] = "Ten";
            //To SingleTable Array Ends

            //To TeenTable Array Starts

            TeenTable[1] = "Eleven";
            TeenTable[2] = "Twelve";
            TeenTable[3] = "Thirteen";
            TeenTable[4] = "Fourteen";
            TeenTable[5] = "Fifteen";
            TeenTable[6] = "Sixteen";
            TeenTable[7] = "Seventeen";
            TeenTable[8] = "Eighteen";
            TeenTable[9] = "Nineteen";
            //To TeenTable Array Starts

            //To TensTable Array
            TensTable[1] = "Ten";
            TensTable[2] = "Twenty";
            TensTable[3] = "Thirty";
            TensTable[4] = "Forty";
            TensTable[5] = "Fifty";
            TensTable[6] = "Sixty";
            TensTable[7] = "Seventy";
            TensTable[8] = "Eighty";
            TensTable[9] = "Ninety";
            //To TensTable Array

            //To Othertable Array
            OtherTable[1] = "Hundred Thousand";
            OtherTable[2] = "Million";
            OtherTable[3] = "Billion";
            OtherTable[4] = "Trillion";
        }
        else
        {
            SingleTable[0] = "";
            SingleTable[1] = "One";
            SingleTable[2] = "Two";
            SingleTable[3] = "Three";
            SingleTable[4] = "Four";
            SingleTable[5] = "Five";
            SingleTable[6] = "Six";
            SingleTable[7] = "Seven";
            SingleTable[8] = "Eight";
            SingleTable[9] = "Nine";
            SingleTable[10] = "Ten";
            //'To SingleTable Array Ends

           // 'To TeenTable Array Starts

            TeenTable[1] = "Eleven";
            TeenTable[2] = "Twelve";
            TeenTable[3] = "Thirteen";
            TeenTable[4] = "Fourteen";
            TeenTable[5] = "Fifteen";
            TeenTable[6] = "Sixteen";
            TeenTable[7] = "Seventeen";
            TeenTable[8] = "Eighteen";
            TeenTable[9] = "Nineteen";
            //'To TeenTable Array Starts

            //'To TensTable Array
            TensTable[1] = "Ten";
            TensTable[2] = "Twenty";
            TensTable[3] = "Thirty";
            TensTable[4] = "Fourty";
            TensTable[5] = "Fifty";
            TensTable[6] = "Sixty";
            TensTable[7] = "Seventy";
            TensTable[8] = "Eighty";
            TensTable[9] = "Ninety";
           // 'To TensTable Array

            //'To Othertable Array
            OtherTable[1] = "Hundred Thousand";
            OtherTable[2] = "Million";
            OtherTable[3] = "Billion";
            OtherTable[4] = "Trillion";

        }
    }
    
    

    public String  GetFigToWord(double Value)
    {
        StringBuffer  stringwordBuff =new StringBuffer("");
        StoreWordsInArray();

        //StringBuffer  stringwordBuff =new StringBuffer("");
        if(iArabic == false)
        {
        	String Decimalstring ="";
        	long NumberArray[];
    		//MEMORY ALLOCATION FOR LONG ARRAY
        	NumberArray = new long[0];
            NumberArray=new long[2];
            String tmpstringValue ="";
            String[] TotalWord=new String[4];
            DecimalFormat df = new DecimalFormat("0.00");
            String formatted = df.format(Value);
            tmpstringValue = formatted;
            Value = Double.parseDouble(tmpstringValue);
            Long IntegerPart;
            Long DecimalPart;
            IntegerPart = (long) Value;
            Decimalstring = tmpstringValue.substring(tmpstringValue.indexOf(".") + 1, tmpstringValue.length());
            DecimalPart =	Long.valueOf(Decimalstring).longValue();
            NumberArray[0] = IntegerPart;
            NumberArray[1] = DecimalPart;
            Integer tmpX,r1,s1,t1 ;
            long r,s,t;
            String Stringword  = "";


            for(tmpX=0 ; tmpX<=1;tmpX++)
            {
               // 'Billion
                if(IntegerPart > 9999999999l)
                {
                    r = (IntegerPart / 1000000000l);
                    if(r > 10 && r < 20)
                    {
                        r = r % 10;
                        Stringword = Stringword + TeenTable[Math.toIntExact(r)] +" Billion ";
                    }
                    else
                    {
                        s = (r / 10);
                        t = r % 10;
                        Stringword = Stringword + " " + TensTable[Math.toIntExact(s)] + " " + SingleTable[Math.toIntExact(t)] + " Billion ";
                    }
                    IntegerPart = IntegerPart % 1000000000;
                }
                if( IntegerPart > 999999999)
                {
                    r = (IntegerPart / 1000000000);
                    Stringword = Stringword + SingleTable[Math.toIntExact(r)] + " Billion ";
                    IntegerPart = IntegerPart % 1000000000;
                }
                //'Million
                if( IntegerPart > 99999999 )
                {
                    r = (IntegerPart / 10000000);
                    if (r > 10 && r < 20)
                    {
                        r = (r / 10);
                        Stringword = Stringword + " " + SingleTable[Math.toIntExact(r)] + " Hundred ";
                        IntegerPart = IntegerPart % 100000000;

                    }else if (r < 100 && r > 20)
                    {
                        r = (r / 10);
                       Stringword = Stringword + SingleTable[Math.toIntExact(r)] + " Hundred ";
                      //  s = (r / 10);
                      //  t = r % 10;
                     //   Stringword = Stringword + " " + TensTable[s] + SingleTable[t] + " Hundred";
                        IntegerPart = IntegerPart % 100000000;
                        if(IntegerPart <= 999999 )
                        {
                            Stringword = Stringword + " Million ";
                        }
                    }
                    else
                    {
                        r = (r / 10);
                        if(r > 10 && r < 20)
                        {
                            r = r % 10;
                            Stringword = Stringword + " " + TeenTable[Math.toIntExact(r)] + " Hundred ";
                        }
                        else
                        {
                            s =(r / 10);
                            t = r % 10;
                            Stringword = Stringword + " " + TensTable[Math.toIntExact(s)] + SingleTable[Math.toIntExact(t)] + " Hundred ";
                        }
                        IntegerPart = IntegerPart % 10000000;
                        if( IntegerPart <= 999999)
                        {
                            Stringword = Stringword + " Million ";
                        }

                    }
                  //  r = (IntegerPart / 10000);
                  //  Stringword = Stringword + SingleTable[r] + " Hundred ";
                  //  IntegerPart = IntegerPart % 100000;
                }
                if(IntegerPart > 9999999)
                {
                    r = (IntegerPart / 1000000);
                    if( r > 10 && r < 20)
                    {
                        r = r % 10;
                        Stringword = Stringword + TeenTable[Math.toIntExact(r)] + " Million ";
                    }
                    else
                    {
                        s = (r / 10);
                        t = r % 10;
                        Stringword = Stringword + " " + TensTable[Math.toIntExact(s)] + " " + SingleTable[Math.toIntExact(t)] + " Million ";
                    }
                        IntegerPart = IntegerPart % 1000000;
                }
                if( IntegerPart > 999999 )
                {
                    r = (IntegerPart / 1000000);
                    Stringword = Stringword + SingleTable[Math.toIntExact(r)] + " Million ";
                    IntegerPart = IntegerPart % 1000000;
                }
               // 'Thousand
                if (IntegerPart > 99999 )
                {
                    r = (IntegerPart / 10000);
                    if(r > 10 && r < 20 )
                    {
                        r = (r / 10);
                        Stringword = Stringword + " " + SingleTable[Math.toIntExact(r)] + " Hundred ";
                        IntegerPart = IntegerPart % 100000;
                    }

                    else if( r < 100 && r > 20 )
                    {
                        r = (r / 10);
                        Stringword = Stringword + SingleTable[Math.toIntExact(r)] + " Hundred ";
                      //  s = (r / 10);
                      //  t = r % 10;
                      //  Stringword = Stringword + " " + TensTable[s] + SingleTable[t] + " Hundred";
                        IntegerPart = IntegerPart % 100000;
                        if( IntegerPart < 1000 )
                        {
                            Stringword = Stringword + " Thousand ";
                        }

                    }
                    else
                    {
                        r = (r / 10);
                        if(r > 10 && r < 20 )
                        {
                            r = r % 10;
                            Stringword = Stringword + " " + TeenTable[Math.toIntExact(r)] + " Hundred ";
                        }
                        else
                        {
                            s = (r / 10);
                            t = r % 10;
                            Stringword = Stringword + " " + TensTable[Math.toIntExact(s)] + SingleTable[Math.toIntExact(t)] + " Hundred ";
                        }
                        IntegerPart = IntegerPart % 100000;
                        if( IntegerPart < 1000 )
                        {
                            Stringword = Stringword + " Thousand ";
                        }
                    }
                   // 'r = Int(IntegerPart / 10000)
                   // 'Stringword = Stringword & SingleTable(r) & " Hundred "
                   // 'IntegerPart = IntegerPart Mod 100000
                }
                if( IntegerPart > 9999)
                {
                    r = (IntegerPart / 1000);
                    if( r > 10 && r < 20 )
                    {
                        r = r % 10;
                        Stringword = Stringword + " " + TeenTable[Math.toIntExact(r)] + " Thousand ";
                    }
                    else
                    {
                    	s = (r / 10);
                        t = r % 10;
                        Stringword = Stringword + " " + TensTable[Math.toIntExact(s)] + " " + SingleTable[Math.toIntExact(t)] + " Thousand ";

                    }
                    IntegerPart = IntegerPart % 1000;
                }
                if(IntegerPart >= 1000)
                {
                    r = (IntegerPart / 1000);
                    Stringword = Stringword + " " + SingleTable[Math.toIntExact(r)] + " Thousand ";
                    IntegerPart = IntegerPart % 1000;
                }
                //'Hundred
                if( IntegerPart >= 100 )
                {
                    r = (IntegerPart / 100);
                    Stringword = Stringword + " " + SingleTable[Math.toIntExact(r)] + " Hundred ";
                    IntegerPart = IntegerPart % 100;
                }
                //'Ten
                if(IntegerPart > 19 && IntegerPart < 100)
                {
                    r = (IntegerPart / 10);
                    Stringword = Stringword + " " + TensTable[Math.toIntExact(r)];
                    IntegerPart = IntegerPart % 10;
                }
                //'Teen
                if( IntegerPart > 10 && IntegerPart < 20 )
                {
                    r =  (IntegerPart % 10);
                    Stringword = Stringword + " " + TeenTable[Math.toIntExact(r)];
                }
                //'One
                if(IntegerPart > 0 && IntegerPart <= 10)
                {
                    Stringword = Stringword + " " + SingleTable[IntegerPart.intValue()];
                }
                IntegerPart = NumberArray[1];
                TotalWord[tmpX] = Stringword;
                Stringword = "";
            }
            if (TotalWord[0].length() > 0 || TotalWord[1].length() > 0 )
            {

                if(TotalWord[0].length() > 0)
                	stringwordBuff.append("Dirhams" + " " + TotalWord[0]+" "); //need to get currency format from table
                	else
                		stringwordBuff.append("");
                	if(TotalWord[0].length() > 0 && TotalWord[1].length() > 0)
                		stringwordBuff.append("And ");
                	else
                		stringwordBuff.append("");

                	if(TotalWord[1].length() > 0)
                		stringwordBuff.append(TotalWord[1] + "" + " Fills " + ""); //need to get currency format from table
                	else
                		stringwordBuff.append("");
                	stringwordBuff.append("Only");
            }
        }
		return stringwordBuff.toString();
       
    }
}
       
