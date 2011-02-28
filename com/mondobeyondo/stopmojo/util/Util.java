/*
 * Created on Oct 23, 2003
 *
 * Copyright (c) 2005 Derone Bryson
 *
 * This program is free software; you can redistribute it and/or modify it under 
 * the terms of the GNU General Public License as published by the Free Software 
 * Foundation; either version 2, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT 
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS 
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more 
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with 
 * this software; see the file COPYING. If not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * As a special exception, Derone Bryson and the StopMojo Project gives 
 * permission for additional uses of the text contained in its release of 
 * StopMojo.
 *
 * The exception is that, Derone Bryson and the the StopMojo Project hereby 
 * grants permission for non-GPL compatible modules (jar files, libraries, 
 * codecs, etc.) to be used and distributed together with StopMojo. This 
 * permission is above and beyond the permissions granted by the GPL license 
 * StopMojo is covered by.
 *
 * This exception does not however invalidate any other reasons why the 
 * executable file might be covered by the GNU General Public License.
 *
 * This exception applies only to the code released by Derone Bryson and/or the
 * StopMojo Project under the name StopMojo. If you copy code from other Free 
 * Software Foundation releases into a copy of StopMojo, as the General Public 
 * License permits, the exception does not apply to the code that you add in 
 * this way. To avoid misleading anyone as to the status of such modified files, 
 * you must delete this exception notice from them.
 *
 * If you write modifications of your own for StopMojo, it is your choice 
 * whether to permit this exception to apply to your modifications. If you do 
 * not wish that, delete this exception notice.  
 */
package com.mondobeyondo.stopmojo.util;

import java.io.File;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * @author Derry Bryson
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Util 
{
	private static NumberFormat
		nf = NumberFormat.getInstance();

	/**
	 * Formats a number using {@link DecimalFormat}.
	 * 
   * @param Format
   * @param Num
   * @return
   */    
	public static String formatNumber(String Format, int Num)
	{
		if(nf instanceof DecimalFormat)
			((DecimalFormat)nf).applyPattern(Format);
      
		return nf.format(Num);
	}
  
	/**
	 * Formats a number using {@link DecimalFormat}.
	 * 
	 * @param Format	the format to apply as defined by DecimalFormat
	 * @param Num			the number to be formatted
	 * @return				the formatted number
	 */
	public static String formatNumber(String Format, double Num)
	{
		if(nf instanceof DecimalFormat)
			((DecimalFormat)nf).applyPattern(Format);
      
		return nf.format(Num);
	}
	
	public static String intToHex(int value, int digits)
	{
		String
		  hex = Integer.toHexString(value);
		
		while(hex.length() < digits)
			hex = "0" + hex;
		
		return hex;
	}
	
	public static String leftPad(String str, int width, String c)
	{
		while(str.length() < width)
			str = c + str;
		
		return str;
	}
  
	/**
	 * Returns the integer value of s.
	 * 
	 * @param s		string containing the integer
	 * @return		the value of the integer in s
	 */
	public static int atoi(String s)
	{
		int
			i,
			numMinuses = 0;
      
		s = s.trim();
		for(i = 0; i < s.length() && (s.charAt(i) == '-' || Character.isDigit(s.charAt(i))); i++)
			if(s.charAt(i) == '-')
				numMinuses++;
		if(i != 0 && numMinuses < 2)
			return Integer.parseInt(s.substring(0, i));
    
		return 0;
	}
  
	/**
	 * Returns the double value of s.
	 * 
	 * @param s		the string containing the floating point number
	 * @return		the value of the floating point number in s
	 */
	public static double atof(String s)
	{
		int
			i,
			numMinuses = 0,
			numDots = 0;
      
		s = s.trim();
		for(i = 0; i < s.length() && (s.charAt(i) == '-' || s.charAt(i) == '.' || Character.isDigit(s.charAt(i))); i++)
			if(s.charAt(i) == '-')
				numMinuses++;
			else if(s.charAt(i) == '.')
				numDots++;
        
		if(i != 0 && numMinuses < 2 && numDots < 2)
			return Double.parseDouble(s.substring(0, i));
      
		return 0.0;
	}
	
	/**
	 * Create a new string composed of times copies of rpt.
	 * 
	 * @param rpt			string to repeat
	 * @param times		number of times to repeat rpt
	 * @return				string composed of times copies of rpt
	 */
	public static String Rpt(String rpt, int times)
	{
		StringBuffer
			sb = new StringBuffer();

		int
			i;

		for(i = 0; i < times; i++)
			sb.append(rpt);

		return sb.toString();
	}
	
	/**
	 * Left justify s into a string len wide.
	 * Note:  does not remove whitespace from s before justifying so it really
	 * just pads the end of s to make it len wide.  This behavior, although
	 * wrong, is maintained for backwards compatibility.
	 * 
	 * @param s			string to be left justified
	 * @param len		width to left justify s into 
	 * @return			string with s left justifed
	 */	
	public static String LJ(String s, int len)
	{
		s = s.trim();
		if(s.length() < len)
			s = s + Rpt(" ", len - s.length());
		else s = s.substring(0, len);
    
		return s;
	}

	/**
	 * 
	 * @param s
	 * @param len
	 * @return
	 */
	public static String RJ(String s, int len)
	{
		s = s.trim();
		if(s.length() < len)
			return Rpt(" ", len - s.length()) + s;
		return s.substring(0, len); 
	}

  /**
   * 
   * @param ts
   * @param colon
   * @return
   */
	public static String formatTimestampTime(Timestamp ts, boolean colon)
	{
		String
			tstr;
  	  
		Calendar
			c = Calendar.getInstance();
  	  
		c.setTimeInMillis(ts.getTime());
  	  
		int hours = c.get(Calendar.HOUR_OF_DAY);
		if(hours > 12)
			hours -= 12;
		if(hours == 0)
			hours = 12;
		tstr = formatNumber("00", hours);
		if(colon)
			tstr += ":";
		tstr += formatNumber("00", c.get(Calendar.MINUTE));
		
		if(c.get(Calendar.HOUR_OF_DAY) < 12)
			tstr += "a";
		else
			tstr += "p";
  	
		return tstr;
	}
  
  /**
   * 
   * @param ts
   * @return
   */
	public static String formatTimestampTime(Timestamp ts)
	{
		return formatTimestampTime(ts, true);
	}
	
	/**
	 * 
	 * @param ts
	 * @return
	 */
	public static String getTimestampDate(Timestamp ts)
	{
		Calendar
			c = Calendar.getInstance();
  	  
		c.setTimeInMillis(ts.getTime());
		return formatNumber("00", c.get(Calendar.MONTH) + 1) + "/" +
					 formatNumber("00", c.get(Calendar.DAY_OF_MONTH)) + "/" +
					 formatNumber("00", c.get(Calendar.YEAR) % 100);
	}
  
  /**
   * 
   * @param ts
   * @return
   */
	public static String getDateDate(Date ts)
	{
		Calendar
			c = Calendar.getInstance();
  	  
		c.setTimeInMillis(ts.getTime());
		return formatNumber("00", c.get(Calendar.MONTH) + 1) + "/" +
					 formatNumber("00", c.get(Calendar.DAY_OF_MONTH)) + "/" +
					 formatNumber("00", c.get(Calendar.YEAR) % 100);
	}
  
  /**
   * 
   * @param d
   * @return
   */
	public static String formatDate(Date d)
	{
		Calendar
			c = Calendar.getInstance();
  	  
		c.setTimeInMillis(d.getTime());
		return formatNumber("00", c.get(Calendar.MONTH) + 1) + "/" +
					 formatNumber("00", c.get(Calendar.DAY_OF_MONTH)) + "/" +
					 formatNumber("00", c.get(Calendar.YEAR) % 100);
	}
  
  /**
   * 
   * @param ts
   * @return
   */
	public static String getTimestampTime(Timestamp ts)
	{
		Calendar
			c = Calendar.getInstance();
  	  
		c.setTimeInMillis(ts.getTime());
		return formatNumber("00", c.get(Calendar.HOUR_OF_DAY)) + 
					 formatNumber("00", c.get(Calendar.MINUTE));
	}
	
	/**
	 * 
	 * @param ts
	 * @return
	 */
	public static String getDateTime(Date ts)
	{
		Calendar
			c = Calendar.getInstance();
  	  
		c.setTimeInMillis(ts.getTime());
		return formatNumber("00", c.get(Calendar.HOUR_OF_DAY)) + 
					 formatNumber("00", c.get(Calendar.MINUTE));
	}
	
	/**
	 * 
	 * @param date
	 * @param time
	 * @return
	 */
	public static Timestamp getTimestamp(String date, String time)
	{
		Calendar
			c = dateToCalendar(date);
		  
//System.out.println("time = " + time + ", " + time.substring(0, 2) + ", " + time.substring(2, 4));		  
		c.set(Calendar.HOUR_OF_DAY, atoi(time.substring(0, 2)));
		c.set(Calendar.MINUTE, atoi(time.substring(2, 4)));
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		
		return new Timestamp(c.getTimeInMillis());
	}
	
	/**
	 * 
	 * @param date
	 * @return
	 */
	public static Calendar dateToCalendar(String date)
	{
		int
			y,
			m,
			d;
  	  
		Calendar
			c = Calendar.getInstance();
  	  
		y = atoi(date.substring(6));
		if(y < 49)
			y += 2000;
		else
			y += 1900;
		d = atoi(date.substring(3));
		m = atoi(date) - 1;
  	
  	
		c.set(y, m, d, 0, 0, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c;
	}
  
  /**
   * 
   * @param c
   * @return
   */
	public static int getDayOfWeek(Calendar c)
	{
		switch(c.get(Calendar.DAY_OF_WEEK))
		{
			case Calendar.SUNDAY :
			return 0;

			case Calendar.MONDAY :
			return 1;

			case Calendar.TUESDAY :
			return 2;

			case Calendar.WEDNESDAY :
			return 3;

			case Calendar.THURSDAY :
			return 4;

			case Calendar.FRIDAY :
			return 5;

			case Calendar.SATURDAY :
			return 6;
		}
		return 0;
	}
	
	/**
	 * 
	 * @param s
	 * @return
	 */
	public static boolean Is_Text(String s)
	{
		return s.trim().length() != 0;
	}
	
	/**
	 * 
	 * @param Time
	 * @param Hrs
	 * @return
	 */
	public static String Time_Add(String Time, double Hrs)
	{
		double
			TimeHours;

		TimeHours = atof(Time.substring(0, 1));
		TimeHours += atof(Time.substring(2, 3)) / 60.0;
		TimeHours += Hrs;
		TimeHours += 0.0083; /*Round to nearest minute*/
		while (TimeHours >= 24.0)
			TimeHours -= 24.0;
		  
		NumberFormat 
			nf = NumberFormat.getInstance();
		  
		if(nf instanceof DecimalFormat)
			((DecimalFormat)nf).applyPattern("00");
		  
		return nf.format(Math.floor(TimeHours)) + 
					 nf.format(Math.floor((TimeHours - Math.floor(TimeHours)) * 60));
	}

  /**
   * 
   * @param Tim1
   * @param Tim2
   * @return
   */
	public static double Time_Diff(String Tim1, String Tim2)
	{
		double
			T1,
			T2;

		T1 = atof(Tim1.substring(0, 1)) +
				 (atof(Tim1.substring(2, 3)) / 60.0);
		T2 = atof(Tim2.substring(0, 1)) +
				 (atof(Tim2.substring(2, 3)) / 60.0);
		return(Math.abs(T1 - T2));
	}

  /**
   * 
   * @param Time
   * @param Hrs
   * @return
   */
	public static String Time_Sub(String Time, double Hrs)
	{
		double
			TimeHours;

		TimeHours = atof(Time.substring(0, 1));
		TimeHours += atof(Time.substring(2, 3)) / 60.0;
		TimeHours += Hrs;
		TimeHours -= 0.0083; /*Round to nearest minute*/
		while (TimeHours < 0.0)
			TimeHours += 24.0;
		  
		NumberFormat 
			nf = NumberFormat.getInstance();
		  
		if(nf instanceof DecimalFormat)
			((DecimalFormat)nf).applyPattern("00");
		  
		return nf.format(Math.floor(TimeHours)) + 
				 nf.format(Math.floor((TimeHours - Math.floor(TimeHours)) * 60));
	}

  /**
   * 
   * @param DateVar
   * @return
   */
	public static double Cal_Julian(String DateVar)
	{
		double
			MonthV,            /* Month value */
			YearV,             /* Year value */
			DayV,              /* Day value */
			Yl;                /* Temporary work variables */

		/* Parse out the month, day, and year */
		YearV = atof(DateVar.substring(6));
		DayV = atof(DateVar.substring(3));
		MonthV = atof(DateVar);
		/* Adjust for the 20th century */
		if (YearV < 50.0) /*= 40.0)*/
			YearV += 2000;
		else
			YearV += 1900;

		Yl = YearV + (MonthV - 2.85) / 12;
		return(Math.floor(Math.floor(Math.floor(367.0 * Yl) - Math.floor(Yl) - 0.75 * Math.floor(Yl)
						+ DayV) - 0.75 * 2.0) + 1721115.0);
	}

  /**
   * 
   * @param JDN
   * @return
   */
	public static String Julian_Cal(double JDN)
	/*
		Returns a calendar date string in the standard form MM/DD/YY from the
		Julian real parameter JDN.
	*/
	{ /* Julian_Cal */
		int
			D,               /* Temporary variables */
			Y,
			M;

		double
			N1,
			N2,
			Y1,
			M1;

		N1 = (JDN - 1721119.0) + 2.0;
		Y1 = Math.floor((N1 - 0.2) / 365.25);
		N2 = N1 - Math.floor(365.25 * Y1);
		M1 = Math.floor((N2 - 0.5) / 30.6);
		D  = (int)(N2 - 30.6 * M1 + 0.5);
		if(M1 > 9)
			Y1 += 1.0;
		Y  = (int)Y1;
		if(M1 > 9)
			M1 -= 12;
		M  = (int)(M1 + 3);
		/* Offset for the 20th century */
		if (Y > 1999)
			Y -= 2000;
		else
			Y -= 1900;
		  
		NumberFormat 
			nf = NumberFormat.getInstance();
		  
		if(nf instanceof DecimalFormat)
			((DecimalFormat)nf).applyPattern("00");

		return nf.format(M) + "/" + nf.format(D) + "/" + nf.format(Y);		  
	}

  /**
   * 
   * @param Date
   * @param NumDay
   * @return
   */
	public static String Date_Add(String Date, int NumDay)
	{
		return Julian_Cal(Cal_Julian(Date) + NumDay);
	}

	/*
		Returns in Buffer a date in the form YY/MM/DD from the original string Date
		in the form MM/DD/YY.  This routine is useful for put dates in a format in
		which they can easily be compared using string comparisons.  Undate_Comp
		can be used to convert the date back again.
		Y2K - if the YY is < 50, then the first digit becomes 'A' or 'B' or 'C' or
		'D' (ie A1/05/04 is 2001/05/04  B5/04/05 is 2015/04/05).

		Date may be the same as buffer -
		ie. Date_Comp(TStr, TStr);
	*/
	public static String Date_Comp(String Date)
	{
		if(Is_Text(Date))
			return Date.substring(6, 8) + "/" + Date.substring(0, 6);
		return Date;
	}

  /**
   * 
   * @param CDate
   * @return
   */
	public static String Undate_Comp(String CDate)
	{
		if(Is_Text(CDate))
			return CDate.substring(3, 8) + "/" + CDate.substring(0, 2);
		return CDate;
	}

	public static String Date_Comp4(String Date)
	{
		if(Is_Text(Date) && Date.length() == 10)
			return Date.substring(6, 10) + "/" + Date.substring(0, 5);
		return Date;
	}

	/**
	 * 
	 * @param CDate
	 * @return
	 */
	public static String Undate_Comp4(String CDate)
	{
		if(Is_Text(CDate) && CDate.length() == 10)
			return CDate.substring(5, 10) + "/" + CDate.substring(0, 4);
		return CDate;
	}

  /**
   * 
   * @param Date1
   * @param Date2
   * @return
   */
	public static int Date_Diff(String Date1, String Date2)
	{
		return(int)(Math.floor(Math.abs(Cal_Julian(Date1) - Cal_Julian(Date2))));
	}

  /**
   * 
   * @param Date
   * @param NumDay
   * @return
   */
	public static String Date_Sub(String Date, int NumDay)
	{
		return Julian_Cal(Cal_Julian(Date) - NumDay);
	}

	private static String[]
			DOW = {"sunday", "monday", "tuesday", "wednesday",
						 "thursday", "friday", "saturday"};
		
  /**
   * 
   * @param Date
   * @param DayCase
   * @return
   */		   	 
	public static String Day_Of_Week(String Date, int DayCase)
	/*
			Returns the day of the week in *Buffer for the calendar date passed
			in *Date in the form MM/DD/YY.
			The following formats are selected by DayCase:
			DayCase = 1  -  MON, TUE, WED, THU...
			DayCase = 2  -  Mon, Tue, Wed, Thu...
			DayCase = 3  -  mon, tue, wed, thu...
			DayCase = 4  -  MONDAY, TUESDAY, WEDNESDAY....
			DayCase = 5  -  Monday, Tuesday, Wednesday....
			DayCase = 6  -  monday, tuesday, wednesday....
	*/
	{/* Day_Of_Week */
		StringBuffer
			dow = new StringBuffer(DOW[(int)(Math.floor((Cal_Julian(Date) + 2) -
														 (Math.floor((Cal_Julian(Date) + 2) / 7) * 7)))]);

			/* If DayCase 1, 2, or 3 copy only first 3 characters of day name */
			if ((DayCase == 1) || (DayCase == 2) || (DayCase == 3))
				dow = new StringBuffer(dow.substring(0, 3));
		    
			switch (DayCase)
			{
				case 1:
				case 4:
					/* Uppercase full name */
				return dow.toString().toUpperCase();

				case 2:
				case 5:
					/* Uppercase first letter only */
					dow.setCharAt(0, Character.toUpperCase(dow.charAt(0)));
				return dow.toString();
			} /* switch */
		return "";
	}

	/* Month names constant */
	private static String[]
		MOY  =  {"january", "february", "march", "april",
						 "may", "june", "july", "august",
						 "september", "october", "november", "december"};
		
  /**
   * 
   * @param Date
   * @param MonthCase
   * @return
   */			   
	public static String Month_Of_Year(String Date, int MonthCase)
	/*
			Returns the month of year for the parameter Date (MM/DD/YY)
			in the format specified by the MonthCase:
			MonthCase = 1  -  JAN, FEB, MAR, APR...
			MonthCase = 2  -  Jan, Feb, Mar, Apr...
			MonthCase = 3  -  jan, feb, mar, apr...
			MonthCase = 4  -  JANUARY, FEBRUARY, MARCH, APRIL....
			MonthCase = 5  -  January, February, March, April....
			MonthCase = 6  -  january, february, march, april....
	*/
	{ /* Month_Of_Year */
		int
			MonthNo;

		MonthNo = atoi(Date.substring(0, 2));
		StringBuffer
			moy = new StringBuffer(MOY[MonthNo - 1]);
		  
		if(MonthCase < 4)
			moy = new StringBuffer(moy.substring(0, 3));
	    
		switch (MonthCase)
		{
			case 1:
			case 4:
			return moy.toString().toUpperCase();
			
			case 2:
			case 5:
				moy.setCharAt(0, Character.toUpperCase(moy.charAt(0)));
			return moy.toString();
		} /* switch MonthCase */
		return "";
	}

  /**
   * 
   * @param Date
   * @param MDYCase
   * @return
   */
	public static String M_D_Y(String Date, int MDYCase)
	/*
			Returns the English spelling of the parameter *Date (MM/DD/YY) in the
			format selected by MDYCase:

			MDYCase = 1  -  January 18, 1987
			MDYCase = 2  -  Wednesday, September 18, 1987
			MDYCase = 3  -  Wed, September 18, 1987
			MDYCase = 4  -  Jan 18, 1987
			MDYCase = 5  -  Wed, Jan 18, 1987
			MDYCase = 6  -  18th day of January, 1987
	*/
	{ /* M_D_Y */
		String
			Sufx;

		int
			Month,
			Day,
			Year;

		NumberFormat 
			nf = NumberFormat.getInstance();
		  
		if(nf instanceof DecimalFormat)
			((DecimalFormat)nf).applyPattern("####");

		Month = atoi(Date.substring(0, 2));
		Day = atoi(Date.substring(3, 5));
		Year = atoi(Date.substring(6, 8));
		if(Year < 50)
			Year += 2000;
		else
			Year += 1900;
      
		switch (MDYCase)
		{
			case 1:
			return Month_Of_Year(Date, 5) + " " + nf.format(Day) + " " + nf.format(Year);

			case 2:
			return Day_Of_Week(Date, 5) + " " + Month_Of_Year(Date, 5) + " " + nf.format(Day) + ", " +
						 nf.format(Year);

			case 3:
			return Day_Of_Week(Date, 2) + " " + Month_Of_Year(Date, 5) + " " + nf.format(Day) + ", " +
						 nf.format(Year);

			case 4:
			return Month_Of_Year(Date, 2) + " " + nf.format(Day) + ", "  + nf.format(Year);

			case 5:
			return Day_Of_Week(Date, 2) + ", " + Month_Of_Year(Date, 2) + " " + nf.format(Day) + ", " +
						 nf.format(Year);

			case 6:
				if(Day < 10 || Date.charAt(3) != '1')
				{
					switch(Date.charAt(4))
					{
						case '1':
							Sufx = "st";
						break;

						case '2':
							Sufx = "nd";
						break;

						case '3':
							Sufx = "rd";
						break;

						default :
							Sufx = "th";
						break;
					} /* switch DCh */
				}
				else
					Sufx = "th";
			return nf.format(Day) + Sufx + " day of " + Month_Of_Year(Date, 5) + ", " + nf.format(Year);
		} /* switch MDYCase */
		return "";
	}

  /**
   * Get the current date.
   * 
   * @return the current date in MM/DD/YY format.
   */
	public static String Today()
	{
		Calendar 
			now = Calendar.getInstance();
        
		return formatNumber("00", now.get(Calendar.MONTH) + 1) + "/" + 
					 formatNumber("00", now.get(Calendar.DAY_OF_MONTH)) + "/" +
					 formatNumber("00", now.get(Calendar.YEAR) % 100);         
	}
	
	public static String sqlEsc(String s)
	{
		return s.replaceAll("'", "''").replaceAll("\\\\", "\\\\\\\\");
	}
	
	/**
	 * Determine the number of digits, decimals, and ints in the specified
	 * rtype (Pattern).
	 * 
	 * @param Pattern
	 * @param patSize
	 * @return the total number of characters required by the pattern.
	 */
	public static int Num_Pattern_Size(String Pattern,
																		 PatternSize patSize)
	{
		int
			C;

		/* interpret pattern */
		C = patSize.Digs = patSize.Ints = patSize.Decs = 0;

		if(Character.toUpperCase(Pattern.charAt(0)) == 'F')
		{
			/*Free form pattern*/
			patSize.Ints = atoi(Pattern.substring(1));
			patSize.Decs = patSize.Ints - 1;
			return(patSize.Ints);
		}

		/* count Digits */
		for (; C < Pattern.length() && (Pattern.charAt(C) == 'D' || Pattern.charAt(C) == 'd'); C++)
			;
		patSize.Digs = C;
    
		/* count I's */
		if (C < Pattern.length() && Character.isDigit(Pattern.charAt(C)))
		{
			patSize.Ints = Util.atoi(Pattern.substring(C));
			while(C < Pattern.length() && Character.isDigit(Pattern.charAt(C)))
				C++;
			C++;
		} /* if Pattern */
		else
		{
			if (C < Pattern.length() && (Pattern.charAt(C) == 'I' || Pattern.charAt(C) == 'i' ||
					Pattern.charAt(C) == 'z' || Pattern.charAt(C) == 'Z'))
			{
				C++;
				patSize.Ints = 10;
			} /* if Pattern */
		} /* if Pattern else */
		/* count Decs */
		if (C < Pattern.length() && Pattern.charAt(C) == '.')
		{
			C++;
			patSize.Decs = C;
			for (; C < Pattern.length() && (Pattern.charAt(C) == 'D' || Pattern.charAt(C) == 'd'); C++)
				;
			patSize.Decs = C - (patSize.Decs);
		} /* if Pattern */

		C = (patSize.Digs) + (patSize.Ints);
		C += (patSize.Decs);
		if(patSize.Decs != 0)
			C++;
//System.out.println("patsize = " + C);		
		return(C);
	}
	
	/**
	 * Determine the size in characters of the given RType.
	 * 
	 * @param RType
	 * @return the number of characters required by the RType.
	 */
	public static int RType_Field_Size(String RType)
	/*
	This routine will return the field size of the RType value passed in.
	'A.', 'P' are not supported.  This routine is destructive to RType.
	*/
	{

		switch(Character.toUpperCase(RType.charAt(0)))
		{
			case 'U':
			case 'A':
			return(atoi(RType.substring(1)));

			case 'S': /*sets*/
			if(Character.toUpperCase(RType.charAt(1)) == 'U')
				return(atoi(RType.substring(3)));
			else
				return(atoi(RType.substring(2)));

			case 'C':
			return(8);

			case 'Y':
			return(1);
			
			case 'X' : // YYYY-MM-DD HH:MM:SS.XXX
			return 23;

			default:
			{
				PatternSize
					patSize = new PatternSize();
				return(Num_Pattern_Size(RType, patSize));
			}
		}
	}
	
	private static int[]
		Md = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
	  
	/**
	 * Pad and format a date to MM/DD/YY.
	 * 
	 * @param Date		date string to be formatted
	 * @return				padded and formatted date string
	 */
	public static String Pad_Date(String Date)
	{
		if(!Is_Text(Date))
			return Date;
      
		String
			DayStr = "",
			MonthStr = "",
			YearStr = "";

		int
			Day,
			Year,
			Month;

		Date = Date.trim();

		if(Pos('-', Date) != 0 || Pos('/', Date) != 0)
		{
			StringTokenizer
				st = new StringTokenizer(Date, "-/");
        
			if(st.hasMoreTokens())
				MonthStr = st.nextToken();
			if(st.hasMoreTokens())
				DayStr = st.nextToken();
			else
  			DayStr = Today().substring(3, 5);
			if(st.hasMoreTokens())
				YearStr = st.nextToken();
			else 
  			YearStr = Today().substring(6, 8);
		}
		else
		{
			if(Date.length() > 1)
				MonthStr = Date.substring(0, 2);
			if(Date.length() > 3)
				DayStr = Date.substring(2, 4);
			if(Date.length() > 5)
				YearStr = Date.substring(4, 6);
			else
			  YearStr = Today().substring(6, 8);
		}
    
		Month = atoi(MonthStr);
		Day = atoi(DayStr);
		Year = atoi(YearStr);
    
		return formatNumber("00", Month) + "/" + formatNumber("00", Day) + "/" +
					 formatNumber("00", Year);
	}           
    
	/**
	 * Check that Date is a valid date in the form M/D/Y.
	 * 
	 * @param Date	Date to be checked
	 * @return			true if date is valid, or false if not
	 */
	public static boolean Check_Date(String Date)
	{ /* Check_Date */
		boolean
			BadDate;
  	  
		char
			Dlmtr;
	    
		String
			DayStr = "",
			MonthStr = "",
			YearStr = "";

		int
			Day,
			Year,
			Month;

		/* Parse Month, Day, Year */
		BadDate = false;
		Date = Date.trim();

		if(Pos('-', Date) != 0 || Pos('/', Date) != 0)
		{
			StringTokenizer
				st = new StringTokenizer(Date, "-/");
        
			if(st.hasMoreTokens())
				MonthStr = st.nextToken();
			if(st.hasMoreTokens())
				DayStr = st.nextToken();
			if(st.hasMoreTokens())
				YearStr = st.nextToken();
		}
		else
		{
			if(Date.length() > 1)
				MonthStr = Date.substring(0, 2);
			if(Date.length() > 3)
				DayStr = Date.substring(2, 4);
			if(Date.length() > 5)
				YearStr = Date.substring(4, 6);
		}
    
		/* Convert & Compare for validity */
		Month = atoi(MonthStr);
		BadDate = (BadDate || Month == 0);
		Day = atoi(DayStr);
		BadDate = (BadDate || Day == 0);
		Year = atoi(YearStr);
//		BadDate = (BadDate || Year == 0);
		BadDate = (BadDate || (Month < 1 || Month > 13));
		if(Year % 4 == 0 && Month == 2)
			BadDate = BadDate || Day > Md[Month - 1] + 1;
		else
			BadDate = BadDate || Day > Md[Month - 1];
		BadDate = BadDate || (Year < 0 || Year > 99);

		/* Pad Date */
		return(!BadDate);
	}

	/**
	 * Return the first postition of Ch in St (1 = first position).
	 * 
	 * @param Ch		character to find position of
	 * @param St		string to scan for Ch
	 * @return			offset (1 based) of the first occurence of Ch in St or 0 if
	 *  						not found
	 */
	public static int Pos(char Ch,
												String St)
	{
		int
			i;
		  
		for(i = 0; i < St.length(); i++)
			if(St.charAt(i) == Ch)
				return i + 1;
		    
		return 0;
	}

  /**
   * Validate a number (Num) against RType.
   * 
   * @param RType
   * @param Num
   * @return true if the number is valid, false otherwise.
   */
  public static boolean Validate_Num(String RType, String Num)
  {
  	int
      C;
      
  	double
  	  NumF,
  	  Min,
  	  Max;
  	  
		if(RType.charAt(RType.length() - 1) == ',')
		{
			/*No ranges specified, assume maximum in both directions.*/
			Min = -9999999999.99;
			Max = 9999999999.99;
		}
		else
		{
			/*Range specified, parse and set.*/
			String Range = RType.substring(Pos(',', RType));
			Min = atof(Range);
			Range = Range.substring(Pos(' ', Range));
			Max = atof(Range);
		}
		
		if(Num.length() > 0)
		{
			/* Convert to number */
			NumF = atof(Num);
			/* See if entry is within valid range */
			if(NumF < Min)
			  return false;
			else if(NumF > Max)
			  return false;
			else if(Num_Chars('.', Num) > 1 || Num_Chars('-', Num) > 1 || (Pos('-', Num) > 1))
			  return false;
			  
			PatternSize
			  patSize = new PatternSize();
			  
			Num_Pattern_Size(RType, patSize);
			  
			if(patSize.Ints != 0)
			{
				/*Strip leading 0's*/
				StringBuffer sb = new StringBuffer(Num);
				C = 0;
				if(Num.charAt(C) == '-')
					C++;
				while ((sb.charAt(C) == '0') && (C + 1 < sb.length()) && (sb.charAt(C+1) != '.'))
					sb.deleteCharAt(C);
				if (Num.charAt(0) == '.')
					sb.insert(0, '0');
				Num = sb.toString();
			}

			/*Prevent lockup condition*/
			if ((patSize.Decs == 0) && (Pos('.', Num) != 0))
				Num = Num.substring(0, Pos('.', Num) - 1);
			/* See if done */
			if((patSize.Digs != 0) || (patSize.Decs != 0))
			{
				/* See if the number fits the pattern */
				if (((patSize.Digs != 0) && (Num_Ints(Num) != patSize.Digs)) ||
						((patSize.Decs != 0) && (Num_Decs(Num) > patSize.Decs)))
				  return false;
			} /* if NumF else */
		} /* if Is_Text(Num) */
		
		return true;
  }

  /**
   * Return the number of digits to the right of the decimal point in Num.
   * 
   * @param Num		string containing number to count decimals in
   * @return			number of digits to the right of the decimal point or 0 if Num
   * 							has no decimal point
   */
  public static int Num_Decs(String Num)
  { /* Num_Decs */
	  boolean
		  DecFlag = false;

	  int
		  C,
		  Decs;

	  for (C = Decs = 0; C < Num.length(); C++)
	  {
		  DecFlag = (DecFlag || (Num.charAt(C) == '.'));
		  if(DecFlag && Num.charAt(C) >= '0' && Num.charAt(C) <= '9')
			  Decs++;
	  } /* for */
	  return(Decs);
  }

  /**
   * Return the number of digits before the decimal point in Num (a number).
   *
   * @param Num		string containing number in which to count
   * @return			count of the digits before the decimal point
   */
  public static int Num_Ints(String Num)
  { /* Num_Ints */
	  int
		  Ints,
		  C;

	  for(C = Ints = 0; ((C < Num.length()) && (Num.charAt(C) != '.')); C++)
	  {
		  if(Num.charAt(C) >= '0' && Num.charAt(C) <= '9')
			  Ints++;
	  } /* for C */
	  return(Ints);
  }

  /**
   * Reformat the number RlStr with the specified width and number of decimals.
   * 
   * @param RlStr			string containing number to be reformatted
   * @param FldWdth		width of reformatted string/number
   * @param NumDecs		number of digits after the decimal
   * @return					the reformatted string
   */
  public static String Pattern(String RlStr,
	  													 int FldWdth,
		  												 int NumDecs)
  {
	  NumberFormat 
		  nf = DecimalFormat.getInstance();
        
	  if (nf instanceof DecimalFormat)
		  ((DecimalFormat)nf).applyPattern(Rpt("#", FldWdth - NumDecs - 1) + "." + Rpt("0", NumDecs));
	    
	  return nf.format(atof(RlStr));
  }

	/**
	 * Return the number of occurences of Ch in St.
	 * 
	 * @param Ch	character to count
	 * @param St	string in which to count occurences of Ch
	 * @return		number of times Ch occurs in St
	 */
	public static int Num_Chars(char Ch,
															String St)
	{ /* Num_Chars */
		int
			C,
			NumChs;

		for (C = NumChs = 0; C < St.length(); C++)
		{
			if(St.charAt(C) == Ch)
				NumChs++;
		} /* for C */
		return(NumChs);
	}
  
  /**
   * Format value per rtype.
   * 
   * @param rtype
   * @param value
   * @return
   */
  public static String RType_Format(String rtype, String value)
  {
		switch(Character.toUpperCase(rtype.charAt(0)))
		{
			case 'U':
			case 'A':
			return Util.LJ(value, Util.RType_Field_Size(rtype));
		  
			case 'S':
			break;

			case 'C':
			return Util.Pad_Date(value);

			case 'Y':
			return Util.LJ(value, Util.RType_Field_Size(rtype));

			case '0': case '1': case '2': case '3': case '4': case '5':
			case '6': case '7': case '8': case '9': case 'I': case 'D':
			case 'Z': case 'F':
			{
				PatternSize
				  patsize = new PatternSize();
				  
				Util.Num_Pattern_Size(rtype, patsize);
				
  			if (!value.equals("") && rtype.charAt(0) != 'F')
	  		{
		  		if ((Util.Num_Decs(value) == 0) && (patsize.Decs != 0) && (value.charAt(value.length() - 1) != '.'))
			  		value += ".";
				  while (Util.Num_Decs(value) != patsize.Decs)
					  value += "0";
			  }
			  if (!value.equals("") && Util.Pos('Z', rtype) != 0)
			  {
			  	if(atof(value) < 0)
			  	{
			  		value = value.replace('-', ' ');
					  value = Util.RJ(value, Util.RType_Field_Size(rtype) - 1);
					  value = value.replace(' ', '0');
					  value = "-" + value;
			  	}
			  	else
			  	{
				    value = Util.RJ(value, Util.RType_Field_Size(rtype));
				    value = value.replace(' ', '0');
			  	}
		  	}
			}
			return Util.RJ(value, Util.RType_Field_Size(rtype));
		}
		
		return value;
  }
  
  public static int parseCSVLine(String line, char delim, char quote, char escape, Vector values)
  {
  	values.clear();
  	
  	if(line.length() == 0)
  	  return 0;
  	  
  	StringBuffer
  	  field = new StringBuffer(),
  	  sb = new StringBuffer(line);
  	  
  	boolean
  	  inQuote = false,
  	  sawSlash = false;
  	  
  	int
  	  i,
  	  len = line.length();
  	  
		for(i = 0; i < len; i++)
		{
			if(!sawSlash && sb.charAt(i) == escape)
				sawSlash = true;
			else if(!sawSlash && sb.charAt(i) == quote)
				inQuote = !inQuote;
			else if(!sawSlash && !inQuote && line.charAt(i) == delim)
			{
				values.add(field.toString());
				field = new StringBuffer();
			}
			else if(sawSlash && sb.charAt(i) == 'n')
			{
				sawSlash = false;
				field.append('\n');
			}
			else if(sawSlash && sb.charAt(i) == 'r')
			{
				sawSlash = false;
				field.append('\r');
			}
			else
			{
				sawSlash = false;
				field.append(sb.charAt(i));
			}
		}
    values.add(field.toString());
		
		return values.size();
  }
  
  public static String getFileExtension(File f)
  {
  	String
		  name = f.getName(),
			ext = "";
  	
  	if(name.lastIndexOf('.') != -1)
  		ext = name.substring(name.lastIndexOf('.'));
  	
  	return ext;
  }
}

